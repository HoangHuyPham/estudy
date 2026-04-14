package vn.nlu.huypham.app.constant;

import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.exception.custom.RedisException;

public class Errors {
    public static final AppException USERNAME_OR_PASSWORD_INVALID = new AppException("Username or password is invalid", 1001);
    public static final AppException USERNAME_EXISTED = new AppException("Username is existed", 1002);
    public static final AppException ACCOUNT_NOT_ACTIVATED = new AppException("Account is not activated", 1003);
    public static final AppException EMAIL_EXISTED = new AppException("Email is existed", 1004);
    public static final AppException REFRESH_TOKEN_INVALID = new AppException("Refresh token is invalid", 1005);
    public static final AppException AUTH_FAILED = new AppException("Authenticate failed", 1006);
    public static final AppException PRE_REGISTER_FAILED = new AppException("Pre-register failed", 1007);
    public static final AppException RECAPTCHA_INVALID = new AppException("reCaptcha verification failed", 1008);
    public static final AppException MAIL_OTP_NOT_FOUND = new AppException("Mail OTP not found", 2001);
    public static final AppException MAIL_OTP_INVALID = new AppException("Mail OTP is invalid", 2002);
    public static final AppException MAIL_OTP_ALREADY_USED = new AppException("Mail OTP is already used", 2003);
    public static final AppException MAIL_OTP_EXPIRED = new AppException("Mail OTP is expired", 2004);
    public static final AppException REGISTER_FAILED = new AppException("Register failed", 2005);
    public static final RedisException REDIS_ERROR = new RedisException("Unknown error", 3000);
    public static final AppException USER_NOT_FOUND = new AppException("User not found", 4001);
    public static final AppException STORAGE_FAILURE = new AppException("Failed to store file", 5001);
    public static final AppException INVALID_FILE_TYPE = new AppException("Invalid file type", 5002);
    public static final AppException INVALID_MIME_TYPE = new AppException("Invalid MIME type", 5003);
    public static final AppException FILE_EMPTY = new AppException("File is empty", 5003);
    public static final AppException FILE_READ_ERROR = new AppException("Failed to read file", 5004);
    public static final AppException CONVERSION_FAILURE = new AppException("Failed to convert video", 5005);
    public static final AppException FFMPEG_ERROR = new AppException("Failed to process video", 5006);
    public static final AppException RESOURCE_NOT_FOUND = new AppException("Resource not found", 6001);
    public static final AppException RESOURCE_CAN_NOT_ACCESS = new AppException("You don't have permission to access this resource", 6002);
}
