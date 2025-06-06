import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "../api/axios";

const LoginCard = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post("/api/auth/login", { email, password });
      const { accessToken, refreshToken } = response.data;

      // Сохраняем токены в localStorage
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      // Перенаправляем на страницу профиля или dashboard
      navigate("/me");
    } catch (error) {
      console.error("Login failed", error);
      alert("Invalid credentials or unverified email");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-900">
      <div className="max-w-md w-full bg-gradient-to-r from-blue-800 to-purple-600 rounded-xl shadow-2xl overflow-hidden p-8 space-y-8 animate-fadeIn">
        <h2 className="text-center text-4xl font-extrabold text-white">
          Welcome
        </h2>
        <p className="text-center text-gray-200">
          Sign in to your account
        </p>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="relative">
            <input
              id="email"
              name="email"
              type="email"
              required
              placeholder="john@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="peer h-10 w-full border-b-2 border-gray-300 text-white bg-transparent placeholder-transparent focus:outline-none focus:border-purple-500"
            />
            <label
              htmlFor="email"
              className="absolute left-0 -top-3.5 text-gray-500 text-sm transition-all 
                peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 
                peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-purple-500 peer-focus:text-sm"
            >
              Email address
            </label>
          </div>

          <div className="relative">
            <input
              id="password"
              name="password"
              type="password"
              required
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="peer h-10 w-full border-b-2 border-gray-300 text-white bg-transparent placeholder-transparent focus:outline-none focus:border-purple-500"
            />
            <label
              htmlFor="password"
              className="absolute left-0 -top-3.5 text-gray-500 text-sm transition-all 
                peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-400 
                peer-placeholder-shown:top-2 peer-focus:-top-3.5 peer-focus:text-purple-500 peer-focus:text-sm"
            >
              Password
            </label>
          </div>

          <div className="flex items-center justify-between">
            <label className="flex items-center text-sm text-gray-200">
              <input
                type="checkbox"
                className="form-checkbox h-4 w-4 text-purple-600 bg-gray-800 border-gray-300 rounded"
              />
              <span className="ml-2">Remember me</span>
            </label>
            <a className="text-sm text-purple-200 hover:underline" href="#">
              Forgot your password?
            </a>
          </div>

          <button
            type="submit"
            className="w-full py-2 px-4 bg-purple-500 hover:bg-purple-700 rounded-md shadow-lg text-white font-semibold transition duration-200"
          >
            Sign In
          </button>
        </form>

        <div className="text-center text-gray-300">
          Don&apos;t have an account?{" "}
          <Link className="text-purple-300 hover:underline" to="/register">
            Sign up
          </Link>
        </div>
      </div>
    </div>
  );
};

export default LoginCard;