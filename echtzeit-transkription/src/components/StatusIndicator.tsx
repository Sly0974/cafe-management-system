import React from 'react';
import { TranscriptionStatus } from '../types';

interface StatusIndicatorProps {
  status: TranscriptionStatus;
  error: string | null;
}

const StatusIndicator: React.FC<StatusIndicatorProps> = ({ status, error }) => {
  const getStatusInfo = () => {
    switch (status) {
      case TranscriptionStatus.LISTENING:
        return { text: 'Hört zu...', color: 'bg-green-500' };
      case TranscriptionStatus.CONNECTING:
        return { text: 'Verbinden...', color: 'bg-yellow-500' };
      case TranscriptionStatus.ERROR:
        return { text: 'Fehler', color: 'bg-red-500' };
      case TranscriptionStatus.IDLE:
      default:
        return { text: 'Gestoppt', color: 'bg-gray-500' };
    }
  };

  const { text, color } = getStatusInfo();

  return (
    <div className="mt-4 flex flex-col items-center justify-center p-2 rounded-lg">
      <div className="flex items-center space-x-2">
        <span className={`w-3 h-3 rounded-full ${color}`}></span>
        <span className="text-gray-300">{text}</span>
      </div>
      {status === TranscriptionStatus.ERROR && error && (
        <p className="text-red-400 mt-2 text-sm">{error}</p>
      )}
    </div>
  );
};

export default StatusIndicator;