import { Blob } from '@google/genai';

/**
 * Encodes a Uint8Array into a Base64 string.
 * This is a manual implementation to avoid external dependencies.
 */
export function encode(bytes: Uint8Array): string {
  let binary = '';
  const len = bytes.byteLength;
  for (let i = 0; i < len; i++) {
    binary += String.fromCharCode(bytes[i]);
  }
  return btoa(binary);
}

/**
 * Decodes a Base64 string into a Uint8Array.
 * This is a manual implementation to avoid external dependencies.
 */
export function decode(base64: string): Uint8Array {
  const binaryString = atob(base64);
  const len = binaryString.length;
  const bytes = new Uint8Array(len);
  for (let i = 0; i < len; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes;
}


/**
 * Creates a Blob object compatible with the Gemini API from raw audio data.
 * @param data The raw Float32Array audio data from the microphone.
 * @returns A Blob object with base64 encoded PCM data.
 */
export function createBlob(data: Float32Array): Blob {
  const l = data.length;
  // Convert Float32 to Int16 PCM format
  const int16 = new Int16Array(l);
  for (let i = 0; i < l; i++) {
    // FIX: Aligned with Gemini API documentation for audio encoding.
    int16[i] = data[i] * 32768;
  }

  // The supported audio MIME type is 'audio/pcm'.
  return {
    data: encode(new Uint8Array(int16.buffer)),
    mimeType: 'audio/pcm;rate=16000',
  };
}

/**
 * Decodes raw PCM audio data into an AudioBuffer.
 * This is a manual implementation for streaming audio.
 */
export async function decodeAudioData(
  data: Uint8Array,
  ctx: AudioContext,
  sampleRate: number,
  numChannels: number,
): Promise<AudioBuffer> {
  const dataInt16 = new Int16Array(data.buffer);
  const frameCount = dataInt16.length / numChannels;
  const buffer = ctx.createBuffer(numChannels, frameCount, sampleRate);

  for (let channel = 0; channel < numChannels; channel++) {
    const channelData = buffer.getChannelData(channel);
    for (let i = 0; i < frameCount; i++) {
      channelData[i] = dataInt16[i * numChannels + channel] / 32768.0;
    }
  }
  return buffer;
}
