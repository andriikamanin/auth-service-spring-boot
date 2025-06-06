import axios from "axios";
import { jwtDecode } from "jwt-decode";
import { getAccessToken, getRefreshToken, saveTokens, clearTokens } from "../util/tokenStorage";

type DecodedToken = { exp: number };

const authApi = axios.create({
  baseURL: import.meta.env.VITE_AUTH_API_URL || "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

const userApi = axios.create({
  baseURL: import.meta.env.VITE_USER_API_URL || "http://localhost:8081",
  withCredentials: true,
  
});

// === Interceptor для userApi (автоматическое обновление access токена) ===
userApi.interceptors.request.use(
  async (config) => {
    let accessToken = getAccessToken();
    const refreshToken = getRefreshToken();

    if (accessToken) {
      try {
        const decoded: DecodedToken = jwtDecode(accessToken);
        const now = Date.now() / 1000;

        if (decoded.exp < now + 30 && refreshToken) {
          const response = await authApi.post("/api/auth/refresh", { refreshToken });
          const { accessToken: newAccessToken, refreshToken: newRefreshToken } = response.data;
          saveTokens(newAccessToken, newRefreshToken);
          accessToken = newAccessToken;
        }
      } catch (err) {
        console.error("❌ Failed to refresh token", err);
        clearTokens();
      }
    }

    if (accessToken && config.headers) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export { authApi, userApi };