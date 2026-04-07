import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import axios, { type InternalAxiosRequestConfig } from "axios";
import type { APIResponse, IUserData } from "@/interface";
import * as jose from "jose";
import { Endpoints } from "@/constant";

export const cn = (...inputs: ClassValue[]) => {
  return twMerge(clsx(inputs))
}

export const axiosClient = axios.create({
  baseURL: 'http://localhost:5555',
  headers: {
    'Content-Type': 'application/json',
  },
});

let refreshingTokenPromise: Promise<string> | null = null;

const refreshToken = async (): Promise<string> => {
  if (refreshingTokenPromise) {
    return refreshingTokenPromise;
  }

  refreshingTokenPromise = axiosClient
    .post<APIResponse<string>>(Endpoints.auth.refreshToken, {}, { withCredentials: true })
    .then((res) => {
      const newAccessToken = res.data?.data;
      if (!newAccessToken) {
        throw new Error("Refresh token response không có access token");
      }

      localStorage.setItem("accessToken", newAccessToken);
      return newAccessToken;
    })
    .finally(() => {
      refreshingTokenPromise = null;
    });

  return refreshingTokenPromise;
};

axiosClient.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});


axiosClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config as (InternalAxiosRequestConfig & { _retry?: boolean }) | undefined;
    if (!originalRequest) {
      return Promise.reject(error);
    }

    const isRefreshRequest = originalRequest.url?.includes(Endpoints.auth.refreshToken);

    if (error.response?.status === 401 && !originalRequest._retry && !isRefreshRequest) {
      originalRequest._retry = true;

      try {
        const newAccessToken = await refreshToken();
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return axiosClient(originalRequest);
      } catch (refreshError) {
        localStorage.removeItem("accessToken");
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export const isExpiredAT = (token: string): boolean => {
  try {
    const payload = jose.decodeJwt(token);
    const exp = payload.exp;
    if (!exp) return true;
    const currentTime = Math.floor(Date.now() / 1000);
    return currentTime >= exp;
  } catch (error) {
    console.error("Lỗi khi giải mã JWT:", error);
    return true;
  }
}

export const parseAT = (token: string): IUserData => {
  const payload = jose.decodeJwt<IUserData>(token);
  return {
    id: payload.sub!,
    displayName: payload.displayName,
    name: payload.name,
    email: payload.email,
    avatar: payload.avatar,
    role: payload.role,
    isDarkMode: payload.isDarkMode,
    phone: payload.phone
  };
}