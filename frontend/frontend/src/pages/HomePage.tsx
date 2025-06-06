// src/pages/HomePage.tsx

import React from "react";
import { Link } from "react-router-dom";

const HomePage = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900 text-white px-4">
      <div className="max-w-md text-center space-y-6">
        <h1 className="text-4xl font-bold">👋 Welcome!</h1>
        <p className="text-lg">Choose where to go:</p>
        <div className="space-y-4">
          <Link
            to="/me"
            className="block py-2 px-6 bg-blue-600 hover:bg-blue-700 rounded-md font-semibold"
          >
            Go to Profile
          </Link>
          <Link
            to="/search"
            className="block py-2 px-6 bg-purple-600 hover:bg-purple-700 rounded-md font-semibold"
          >
            Search Profile
          </Link>
        </div>
      </div>
    </div>
  );
};

export default HomePage;