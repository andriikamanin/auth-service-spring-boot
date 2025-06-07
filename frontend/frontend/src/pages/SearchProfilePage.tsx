import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { searchProfiles } from "../api/user";
import defaultAvatar from "../assets/avatar.jpg";

type UserProfile = {
  id: string;
  nickname: string;
  avatarUrl: string | null;
  publicProfile: boolean;
};

const SearchProfilePage = () => {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<UserProfile[]>([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;
    setLoading(true);
    try {
      const data: UserProfile[] = await searchProfiles(query);
      setResults(data.filter((u) => u.publicProfile));
    } catch (err) {
      console.error("Search failed", err);
      setResults([]);
      alert("User not found");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6 relative">
      <button
        onClick={() => navigate(-1)}
        className="absolute left-6 top-6 text-white text-xl hover:text-purple-400"
      >
        ← Back
      </button>

      <div className="max-w-xl mx-auto pt-12">
        <h1 className="text-3xl font-bold mb-6 text-center">Search Profiles</h1>
        <form onSubmit={handleSearch} className="flex mb-6">
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Enter nickname"
            className="flex-grow p-2 rounded-l bg-gray-700 text-white border border-gray-600"
          />
          <button
            type="submit"
            className="px-4 py-2 bg-purple-600 hover:bg-purple-700 rounded-r"
          >
            Search
          </button>
        </form>

        {loading && <p>Loading...</p>}

        {results.map((user) => (
          <div
            key={user.id}
            className="flex items-center space-x-4 bg-gray-800 p-4 rounded mb-4"
          >
            <img
              src={user.avatarUrl || defaultAvatar}
              alt="avatar"
              className="w-12 h-12 rounded-full"
            />
            <Link
              to={`/u/${user.nickname}`}
              className="text-lg font-semibold hover:underline"
            >
              @{user.nickname}
            </Link>
          </div>
        ))}

        {!loading && results.length === 0 && (
          <p className="text-center text-gray-400">No public profiles found.</p>
        )}
      </div>
    </div>
  );
};

export default SearchProfilePage;