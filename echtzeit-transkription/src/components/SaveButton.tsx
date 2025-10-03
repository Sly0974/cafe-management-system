import React from 'react';

interface SaveButtonProps {
  onClick: () => void;
  disabled: boolean;
}

const SaveIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
        <path strokeLinecap="round" strokeLinejoin="round" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
    </svg>
);


const SaveButton: React.FC<SaveButtonProps> = ({ onClick, disabled }) => {
  return (
    <button
      onClick={onClick}
      disabled={disabled}
      className={`flex items-center justify-center px-8 py-4 text-white font-bold rounded-full shadow-lg transition-all duration-300 ease-in-out transform hover:scale-105 focus:outline-none focus:ring-4 focus:ring-opacity-50 bg-cyan-600 hover:bg-cyan-700 focus:ring-cyan-500 ${disabled ? 'opacity-50 cursor-not-allowed' : ''}`}
    >
      <SaveIcon />
      Speichern
    </button>
  );
};

export default SaveButton;