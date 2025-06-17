import axios from "axios"

const Endpoint = {
    LOGIN_URL: 'api/Auth/Login',
    PRODUCT_URL: 'api/Product',
    CATEGORY_URL: 'api/Category',
    CART_URL: 'api/Cart',
    PROFILE_URL: 'api/Profile',
    UPLOAD_URL: 'api/Image/Upload',
    CHECK_EMAIL_URL: 'api/Auth/CheckEmail',
    REGISTER_URL: 'api/Auth/Register',
    CHANGE_PASSWORD_URL: 'api/Auth/ChangePassword',
    CHECK_VOUCHER_URL: 'api/Voucher/CheckVoucher',
    VOUCHER_URL: 'api/Voucher',
    ORDER_URL: 'api/Order',
    ORDER_BUYER_URL: 'api/Order/ByBuyer',
    USER_URL: 'api/User',
    ROLE_URL: 'api/Role',
    ADMIN_URL: 'api/Admin',
    CART_MAN_URL: 'api/Admin/Carts',
    COURSE_MAN_URL: 'api/Admin/Course',
    ROLE_MAN_URL: 'api/Admin/Role',
    USER_MAN_URL: 'api/Admin/User',
}

class AppRequest {
    private static _instance: AppRequest;
    private constructor() { }

    static getInstance() {
        if (!this._instance) {
            const axiosInstance = axios.create({
                baseURL: "http://localhost:8080/"
            })
            axiosInstance.interceptors.request.use(
                config=>{
                    config.headers.Authorization = localStorage.getItem("jwt")
                    return config
                },
                err=>{
                    return Promise.reject(err)
                }
            )
            this._instance = axiosInstance;
        }
        return this._instance;
    }
}


export {
    Endpoint, AppRequest
}