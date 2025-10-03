
import React from 'react';
import { TranscriptionStatus } from '../types';

interface ControlButtonProps {
  status: TranscriptionStatus;
  onClick: () => void;
}

const MicrophoneIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11a7 7 0 01-7 7m0 0a7 7 0 01-7-7m7 7v4m0 0H8m4 0h4m-4-8a3 3 0 01-3-3V5a3 3 0 116 0v6a3 3 0 01-3 3z" />
    </svg>
);

const StopIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 10h6v4H9z" />
    </svg>
);

const LoadingSpinner = () => (
    <svg className="animate-spin h-5 w-5 mr-3 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
);

const ControlButton: React.FC<ControlButtonProps> = ({ status, onClick }) => {
  const getButtonProps = () => {
    switch (status) {
      case TranscriptionStatus.LISTENING:
        return {
          text: 'Stoppen',
          icon: <StopIcon />,
          style: 'bg-red-600 hover:bg-red-700',
          disabled: false,
        };
      case TranscriptionStatus.CONNECTING:
        return {
          text: 'Verbinden...',
          icon: <LoadingSpinner />,
          style: 'bg-gray-500 cursor-not-allowed',
          disabled: true,
        };
      case TranscriptionStatus.ERROR:
        return {
          text: 'Erneut versuchen',
          icon: <MicrophoneIcon />,
          style: 'bg-yellow-500 hover:bg-yellow-600',
          disabled: false,
        };
      case TranscriptionStatus.IDLE:
      default:
        return {
          text: 'Starten',
          icon: <MicrophoneIcon />,
          style: 'bg-green-600 hover:bg-green-700',
          disabled: false,
        };
    }
  };

  const { text, icon, style, disabled } = getButtonProps();

  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`flex items-center justify-center px-8 py-4 text-white font-bold rounded-full shadow-lg transition-all duration-300 ease-in-out transform hover:scale-105 focus:outline-none focus:ring-4 focus:ring-opacity-50 ${style} ${disabled ? 'opacity-70' : ''}`}
    >
      {icon}
      {text}
    </button>
  );
};

export default ControlButton;
