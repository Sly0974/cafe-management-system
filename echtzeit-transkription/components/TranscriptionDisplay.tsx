
import React, { useEffect, useRef } from 'react';

interface TranscriptionDisplayProps {
  history: string[];
  currentLine: string;
}

const TranscriptionDisplay: React.FC<TranscriptionDisplayProps> = ({ history, currentLine }) => {
    const endOfMessagesRef = useRef<HTMLDivElement | null>(null);

    useEffect(() => {
        endOfMessagesRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [history, currentLine]);

  return (
    <div className="flex-grow bg-gray-800/50 border border-gray-700 rounded-xl p-6 shadow-2xl overflow-y-auto min-h-[40vh] text-2xl leading-relaxed text-gray-200">
      {history.map((line, index) => (
        <p key={index}>{line}</p>
      ))}
      {currentLine && (
        <p className="text-cyan-300">
          {currentLine}
          <span className="inline-block w-1 h-6 bg-cyan-300 ml-1 blinking-cursor"></span>
        </p>
      )}
      <div ref={endOfMessagesRef} />
    </div>
  );
};

export default TranscriptionDisplay;
