export interface APIResponse<T> {
  code: number;
  data: T;
  message?: string;
}

export type LoginResponse = APIResponse<string>;

export type LoginBasicRequest = {
  username: string;
  password: string;
}

export type LoginWithGoogleRequest = {
  token: string;
}

export type PreRegisterBasicRequest = {
  displayName?: string,
  username?: string,
  password?: string,
  reCaptchaToken?: string,
  avatar?: string,
  email?: string
}

export type RegisterOTPConfirmRequest = {
  email: string,
  mailOTPId: string,
  otp: string
}

export type IUserData = {
  id: string
  displayName: string
  name: string
  email: string
  avatar: string
  role: string
  isDarkMode: boolean,
  phone: string
}