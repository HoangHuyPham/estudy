package vn.nlu.huypham.app.service;

import java.util.UUID;

import vn.nlu.huypham.app.dto.request.ClickableMailContent;
import vn.nlu.huypham.app.dto.request.OTPMailContent;
import vn.nlu.huypham.app.dto.request.RegisterOTPBasic;
import vn.nlu.huypham.app.dto.request.TextMailContent;
import vn.nlu.huypham.app.entity.User;

public interface MailOTPService {
    void sendTextMail(String to, String subject, TextMailContent content);

    void sendClickableMail(String to, String subject, ClickableMailContent content);
    
    void sendOTPMail(String to, String subject, OTPMailContent content);

    UUID createRegisterOTP(RegisterOTPBasic dto);

    User validateOTP(UUID mailOTPId, String email, String otp);
}
