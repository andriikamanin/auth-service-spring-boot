import axios from "axios";
import { jwtDecode } from "jwt-decode";

type DecodedToken = {
  exp: number;
};

const api = axios.create({
  baseURL: "http://localhost:8080", // URL вашего Spring Boot бекенда
  withCredentials: false,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  async (config) => {
    let accessToken = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");

    if (accessToken) {
      try {
        const decoded: DecodedToken = jwtDecode(accessToken);
        const now = Date.now() / 1000;

        // Проверка, истёк ли токен (с запасом 30 сек)
        if (decoded.exp < now + 30 && refreshToken) {
          const response = await axios.post("http://localhost:8080/api/auth/refresh", {
            refreshToken,
          });

          if (accessToken && refreshToken) {
            localStorage.setItem("accessToken", accessToken);
            localStorage.setItem("refreshToken", refreshToken);
          }
        }
      } catch (err) {
        console.error("Failed to refresh token", err);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
      }
    }

    if (accessToken && config.headers) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export default api;