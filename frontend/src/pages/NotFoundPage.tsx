import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@nextui-org/react';
import { Home, ArrowLeft, FileQuestion } from 'lucide-react';

const NotFoundPage: React.FC = () => {
  return (
    <div className="min-h-[70vh] flex flex-col items-center justify-center px-4 text-center">
      {/* Icon */}
      <div className="bg-gray-100 dark:bg-gray-800 rounded-full p-8 mb-6">
        <FileQuestion size={64} className="text-gray-400 dark:text-gray-500" />
      </div>

      {/* 404 text */}
      <h1 className="text-8xl font-extrabold text-gray-200 dark:text-gray-700 select-none mb-2">
        404
      </h1>

      {/* Message */}
      <h2 className="text-2xl font-bold text-gray-800 dark:text-gray-100 mb-2">
        Page Not Found
      </h2>
      <p className="text-gray-500 dark:text-gray-400 max-w-md mb-8">
        Looks like this page doesn't exist or was moved. Let's get you back on track.
      </p>

      {/* Actions */}
      <div className="flex gap-3 flex-wrap justify-center">
        <Button
          as={Link}
          to="/"
          color="primary"
          startContent={<Home size={16} />}
        >
          Back to Home
        </Button>
        <Button
          variant="flat"
          startContent={<ArrowLeft size={16} />}
          onClick={() => window.history.back()}
        >
          Go Back
        </Button>
      </div>
    </div>
  );
};

export default NotFoundPage;