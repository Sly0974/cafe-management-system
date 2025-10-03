import React, { useState, useRef, useCallback } from 'react';
import { GoogleGenAI, Modality, LiveServerMessage, Blob } from '@google/genai';
import { TranscriptionStatus } from './types';
import StatusIndicator from './components/StatusIndicator';
import TranscriptionDisplay from './components/TranscriptionDisplay';
import ControlButton from './components/ControlButton';
import SaveButton from './components/SaveButton';
import { createBlob, decode, decodeAudioData } from './utils/audioUtils';

// Polyfill for webkitAudioContext to resolve TypeScript error
const AudioContext = window.AudioContext || (window as any).webkitAudioContext;

const App: React.FC = () => {
  const [status, setStatus] = useState<TranscriptionStatus>(TranscriptionStatus.IDLE);
  const [transcriptionHistory, setTranscriptionHistory] = useState<string[]>([]);
  const [currentTranscription, setCurrentTranscription] = useState('');
  const [error, setError] = useState<string | null>(null);

  const sessionPromiseRef = useRef<Promise<any> | null>(null);
  const audioContextRef = useRef<AudioContext | null>(null);
  const scriptProcessorRef = useRef<ScriptProcessorNode | null>(null);
  const streamRef = useRef<MediaStream | null>(null);
  const sourceRef = useRef<MediaStreamAudioSourceNode | null>(null);
  
  const outputAudioContextRef = useRef<AudioContext | null>(null);
  const outputNodeRef = useRef<GainNode | null>(null);
  const sourcesRef = useRef<Set<AudioBufferSourceNode>>(new Set());
  const nextStartTimeRef = useRef<number>(0);

  const currentTranscriptionRef = useRef('');
  const currentInputTranscriptionRef = useRef('');

  const stopTranscription = useCallback(() => {
    if (sessionPromiseRef.current) {
        sessionPromiseRef.current.then(session => session.close());
        sessionPromiseRef.current = null;
    }
    
    if (streamRef.current) {
      streamRef.current.getTracks().forEach(track => track.stop());
      streamRef.current = null;
    }
    
    if (scriptProcessorRef.current) {
      scriptProcessorRef.current.disconnect();
      scriptProcessorRef.current = null;
    }

    if (sourceRef.current) {
        sourceRef.current.disconnect();
        sourceRef.current = null;
    }

    if (audioContextRef.current && audioContextRef.current.state !== 'closed') {
      audioContextRef.current.close();
      audioContextRef.current = null;
    }
    
    sourcesRef.current.forEach(source => source.stop());
    sourcesRef.current.clear();
    if (outputNodeRef.current) {
        outputNodeRef.current.disconnect();
        outputNodeRef.current = null;
    }
    if (outputAudioContextRef.current && outputAudioContextRef.current.state !== 'closed') {
      outputAudioContextRef.current.close();
      outputAudioContextRef.current = null;
    }
    nextStartTimeRef.current = 0;

    setStatus(TranscriptionStatus.IDLE);
    // Don't clear transcription on stop, so it can be saved.
    // Clear internal refs for the next session.
    currentTranscriptionRef.current = '';
    currentInputTranscriptionRef.current = '';
  }, []);

  const startTranscription = useCallback(async () => {
    setStatus(TranscriptionStatus.CONNECTING);
    setError(null);
    currentInputTranscriptionRef.current = '';
    currentTranscriptionRef.current = '';
    // Clear previous transcription on new start
    setTranscriptionHistory([]);
    setCurrentTranscription('');

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      streamRef.current = stream;

      const ai = new GoogleGenAI({ apiKey: process.env.API_KEY as string });
      audioContextRef.current = new AudioContext({ sampleRate: 16000 });
      
      outputAudioContextRef.current = new AudioContext({ sampleRate: 24000 });
      outputNodeRef.current = outputAudioContextRef.current.createGain();
      outputNodeRef.current.connect(outputAudioContextRef.current.destination);

      sessionPromiseRef.current = ai.live.connect({
        model: 'gemini-2.5-flash-native-audio-preview-09-2025',
        callbacks: {
          onopen: () => {
            if (!audioContextRef.current || !streamRef.current) return;
            setStatus(TranscriptionStatus.LISTENING);
            
            sourceRef.current = audioContextRef.current.createMediaStreamSource(streamRef.current);
            scriptProcessorRef.current = audioContextRef.current.createScriptProcessor(4096, 1, 1);

            scriptProcessorRef.current.onaudioprocess = (audioProcessingEvent) => {
              const inputData = audioProcessingEvent.inputBuffer.getChannelData(0);
              const pcmBlob: Blob = createBlob(inputData);
              sessionPromiseRef.current!.then((session) => {
                session.sendRealtimeInput({ media: pcmBlob });
              });
            };
            sourceRef.current.connect(scriptProcessorRef.current);
            scriptProcessorRef.current.connect(audioContextRef.current.destination);
          },
          onmessage: async (message: LiveServerMessage) => {
            const base64EncodedAudioString = message.serverContent?.modelTurn?.parts?.[0]?.inlineData?.data;
            if (base64EncodedAudioString && outputAudioContextRef.current && outputNodeRef.current) {
                const outputAudioContext = outputAudioContextRef.current;
                const outputNode = outputNodeRef.current;
                const sources = sourcesRef.current;

                nextStartTimeRef.current = Math.max(
                    nextStartTimeRef.current,
                    outputAudioContext.currentTime,
                );
                const audioBuffer = await decodeAudioData(
                    decode(base64EncodedAudioString),
                    outputAudioContext,
                    24000,
                    1,
                );
                const source = outputAudioContext.createBufferSource();
                source.buffer = audioBuffer;
                source.connect(outputNode);
                source.addEventListener('ended', () => {
                    sources.delete(source);
                });
                source.start(nextStartTimeRef.current);
                nextStartTimeRef.current = nextStartTimeRef.current + audioBuffer.duration;
                sources.add(source);
            }

            const interrupted = message.serverContent?.interrupted;
            if (interrupted) {
                sourcesRef.current.forEach(source => {
                    source.stop();
                    sourcesRef.current.delete(source);
                });
                nextStartTimeRef.current = 0;
            }

            if (message.serverContent?.inputTranscription) {
              const text = message.serverContent.inputTranscription.text;
              currentInputTranscriptionRef.current += text;
              currentTranscriptionRef.current = currentInputTranscriptionRef.current;
              setCurrentTranscription(currentTranscriptionRef.current);
            }
            if (message.serverContent?.turnComplete) {
                const fullLine = currentInputTranscriptionRef.current.trim();
                if (fullLine) {
                    setTranscriptionHistory(prev => [...prev, fullLine]);
                }
                currentInputTranscriptionRef.current = '';
                currentTranscriptionRef.current = '';
                setCurrentTranscription('');
            }
          },
          onerror: (e: ErrorEvent) => {
            console.error('API Error:', e);
            setError('Ein Fehler ist aufgetreten. Bitte überprüfen Sie Ihre Verbindung und versuchen Sie es erneut.');
            setStatus(TranscriptionStatus.ERROR);
            stopTranscription();
          },
          onclose: (e: CloseEvent) => {
            stopTranscription();
          },
        },
        config: {
          responseModalities: [Modality.AUDIO],
          inputAudioTranscription: {},
        },
      });

    } catch (err) {
      console.error('Initialization error:', err);
      setError('Mikrofonzugriff verweigert oder kein Mikrofon gefunden.');
      setStatus(TranscriptionStatus.ERROR);
      stopTranscription();
    }
  }, [stopTranscription]);

  const handleToggleTranscription = useCallback(() => {
    const isRunning = status === TranscriptionStatus.LISTENING || status === TranscriptionStatus.CONNECTING;
    if (isRunning) {
      stopTranscription();
    } else {
      startTranscription();
    }
  }, [status, startTranscription, stopTranscription]);

  const handleSaveTranscription = useCallback(() => {
    const linesToSave = [...transcriptionHistory];
    if (currentTranscription.trim()) {
        linesToSave.push(currentTranscription.trim());
    }
    const fullText = linesToSave.join('\n');

    if (!fullText) return;

    const blob = new Blob([fullText], { type: 'text/plain;charset=utf-8' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    const date = new Date().toISOString().split('T')[0];
    link.download = `Transkription-${date}.txt`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
  }, [transcriptionHistory, currentTranscription]);

  const isSavable = transcriptionHistory.length > 0 || currentTranscription.trim() !== '';

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-900 p-4 font-sans">
      <div className="w-full max-w-4xl h-full flex flex-col">
        <header className="text-center mb-4">
          <h1 className="text-4xl font-bold text-cyan-400">Echtzeit-Transkription</h1>
          <p className="text-sm text-gray-500 mt-1">Copyright 2025 by Sly</p>
          <p className="text-lg text-gray-400">Gesprochenes wird live in Text umgewandelt</p>
          <StatusIndicator status={status} error={error} />
        </header>
        
        <TranscriptionDisplay 
          history={transcriptionHistory}
          currentLine={currentTranscription}
        />

        <footer className="mt-6 flex justify-center items-center gap-4">
          <ControlButton status={status} onClick={handleToggleTranscription} />
          <SaveButton onClick={handleSaveTranscription} disabled={!isSavable} />
        </footer>
      </div>
    </div>
  );
};

export default App;