export interface APIResponse<T> {
  code: number;
  data: T;
  message: string;
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
  displayName: string,
  username: string,
  password: string,
  reCaptchaToken: string,
  email: string
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
  roles: Roles[]
  isDarkMode: boolean,
  phone: string
}

export type ICourse = {
  id: string,
  name: string,
  description: string,
  thumbnail: string,
  status: CourseStatus,
  visibility: CourseVisibilities,
  duration: number,
  enrollmentCount: number,
  lectureCount: number,
  oldPrice: number,
  price: number,
  createdAt: number,
  updatedAt: number,
}

export type ISection = {
  id: string,
  name: string,
  weight: number,
}

export type ILecture = {
  id: string;
  name: string;
  weight: number;
  createdAt: number;
  updatedAt: number;
}

export type IVideo = {
  id: string,
  duration: number,
  status: VideoStatus,
  resource: IResource
}

export type ICourseSection = ICourse & {
  sections: ISection[]
}

export type ICourseSectionLecture = ICourse & {
  sections: ISectionLecture[]
}

export type ICourseSectionLectureVideo = ICourse & {
  sections: ISectionLectureVideo[]
}

export type ISectionLecture = ISection & {
  lectures: ILecture[]
}

export type ISectionLectureVideo = ISection & {
  lectures: ILectureVideo[]
}

export type ILectureVideo = ILecture & {
  video: IVideo
}


export type IResource = {
  id: string,
  size: number,
  type: string,
  visibility: ResourceVisibilities,
  createdAt: number,
}

const Roles = {
  STUDENT: "STUDENT",
  TUTOR: "TUTOR",
  ADMIN: "ADMIN",
} as const;

export const ResourceVisibilities = {
  PUBLIC: "PUBLIC",
  PRIVATE: "PRIVATE",
  PROTECTED: "PROTECTED",
} as const;

export const VideoStatus = {
  PENDING: "PENDING",
  SUCCESS: "SUCCESS",
  FAILED: "FAILED"
} as const;

export const CourseStatus = {
  DRAFT: "DRAFT",
  PUBLISHED: "PUBLISHED",
  ARCHIVED: "ARCHIVED",
} as const;

export const CourseVisibilities = {
  PUBLIC: "PUBLIC",
  PRIVATE: "PRIVATE",
  PROTECTED: "PROTECTED",
} as const;

export type Roles = typeof Roles[keyof typeof Roles];
export type VideoStatus = typeof VideoStatus[keyof typeof VideoStatus];
export type CourseStatus = typeof CourseStatus[keyof typeof CourseStatus];
export type CourseVisibilities = typeof CourseVisibilities[keyof typeof CourseVisibilities];
export type ResourceVisibilities = typeof ResourceVisibilities[keyof typeof ResourceVisibilities];