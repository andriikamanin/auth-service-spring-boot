// src/utils/tokenStorage.ts

export const getAccessToken = (): string | null =>
  localStorage.getItem("accessToken") || sessionStorage.getItem("accessToken");

export const getRefreshToken = (): string | null =>
  localStorage.getItem("refreshToken") || sessionStorage.getItem("refreshToken");

export const clearTokens = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  sessionStorage.removeItem("accessToken");
  sessionStorage.removeItem("refreshToken");
};