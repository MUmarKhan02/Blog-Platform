import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from '@nextui-org/react';
import { motion } from 'framer-motion';
import { PenTool } from 'lucide-react';
import { useTheme } from './ThemeContext';

const HeroSection: React.FC = () => {
  const { theme } = useTheme();

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.2,
        delayChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.8,
        ease: 'easeOut',
      },
    },
  };

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={containerVariants}
      className={`mb-12 rounded-lg overflow-hidden ${
        theme === 'dark'
          ? 'bg-gradient-to-r from-gray-800 to-gray-900'
          : 'bg-gradient-to-br from-blue-100 via-indigo-50 to-purple-100'
      }`}
    >
      <div className="px-6 md:px-12 py-16 md:py-20 text-center">
        {/* Heading */}
        <motion.h1
          variants={itemVariants}
          className={`text-4xl md:text-5xl font-bold mb-4 ${
            theme === 'dark' ? 'text-white' : 'text-gray-900'
          }`}
        >
          Welcome to Khan's Blog Platform
        </motion.h1>

        {/* Subtitle */}
        <motion.p
          variants={itemVariants}
          className={`text-lg md:text-xl mb-8 max-w-2xl mx-auto ${
            theme === 'dark' ? 'text-gray-300' : 'text-gray-600'
          }`}
        >
          Share your thoughts, ideas, and stories with the world. Write, publish, and connect with readers on our platform.
        </motion.p>

        {/* Button */}
        <motion.div variants={itemVariants}>
          <Button
            as={Link}
            to="/posts/new"
            color="primary"
            size="lg"
            className="font-semibold"
            startContent={<PenTool size={20} />}
          >
            Start Writing
          </Button>
        </motion.div>
      </div>
    </motion.div>
  );
};

export default HeroSection;

