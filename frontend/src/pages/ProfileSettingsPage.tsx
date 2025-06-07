// src/pages/ProfileSettingsPage.tsx

import React, { useEffect, useState } from "react";
import { userApi } from "../api/axios";

type LoginHistoryEntry = {
  ip: string;
  userAgent: string;
  timestamp: string;
};

const ProfileSettingsPage = () => {
  const [loginHistory, setLoginHistory] = useState<LoginHistoryEntry[]>([]);
  const [showHistory, setShowHistory] = useState(false);
  const [loading, setLoading] = useState(false);

  const fetchLoginHistory = async () => {
    setLoading(true);
    try {
      const response = await userApi.get("/api/users/me/logins");
      setLoginHistory(response.data);
    } catch (error) {
      console.error("Failed to fetch login history", error);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenHistory = () => {
    setShowHistory(true);
    fetchLoginHistory();
  };

  const handleCloseHistory = () => {
    setShowHistory(false);
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white p-6 flex items-center justify-center">
      <div className="max-w-md w-full bg-gray-800 p-8 rounded-xl shadow space-y-6 text-center">
        <h1 className="text-3xl font-bold mb-4">Настройки безопасности</h1>

        <button
          onClick={handleOpenHistory}
          className="w-full py-2 px-4 bg-gray-700 hover:bg-gray-600 rounded-md font-semibold"
        >
          🛡️ История входов
        </button>

        {showHistory && (
          <div className="mt-6 p-4 bg-gray-900 border border-gray-700 rounded-lg text-left">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold">История входов</h2>
              <button
                onClick={handleCloseHistory}
                className="text-sm text-purple-300 hover:underline"
              >
                Закрыть ✖
              </button>
            </div>

            {loading ? (
              <p className="text-gray-400">Загрузка...</p>
            ) : loginHistory.length === 0 ? (
              <p className="text-gray-400">История входов пуста.</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="min-w-full table-auto text-sm">
                  <thead>
                    <tr className="bg-gray-700 text-gray-300">
                      <th className="px-3 py-2">📍 IP</th>
                      <th className="px-3 py-2">🖥️ Устройство</th>
                      <th className="px-3 py-2">🕒 Время</th>
                    </tr>
                  </thead>
                  <tbody>
                    {loginHistory.map((entry, index) => (
                      <tr key={index} className="border-b border-gray-700">
                        <td className="px-3 py-2">{entry.ip}</td>
                        <td className="px-3 py-2">{entry.userAgent}</td>
                        <td className="px-3 py-2">
                          {new Date(entry.timestamp).toLocaleString("ru-RU")}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default ProfileSettingsPage;