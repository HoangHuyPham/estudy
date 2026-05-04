export const Endpoints = {
    AUTH: {
        LOGIN: "/auth/login/basic",
        LOGIN_WITH_GOOGLE: "/auth/login/google",
        PREREGISTER: "/auth/pre-register",
        REGISTER: "/auth/register",
        LOGOUT: "/auth/logout",
        REFRESH_TOKEN: "/auth/refresh-token",
    },
    TUTOR: {
        COURSES: "/tutor/courses",
        SECTIONS: "/tutor/sections",
        SECTIONS_BULK_DELETE: "/tutor/sections/bulk-delete",
        LECTURES: "/tutor/lectures",
        LECTURES_BULK_DELETE: "/tutor/lectures/bulk-delete",
    },
    SERVE: "/resource/serve",
    BACKEND: "http://localhost:5555",
}