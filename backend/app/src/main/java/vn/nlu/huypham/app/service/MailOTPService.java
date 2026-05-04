package vn.nlu.huypham.app.service;

import java.util.UUID;

import vn.nlu.huypham.app.dto.request.RegisterRequest;
import vn.nlu.huypham.app.entity.MailOTP;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.payload.email.ClickableEmailContent;
import vn.nlu.huypham.app.payload.email.OTPEmailContent;
import vn.nlu.huypham.app.payload.email.TextEmailContent;

public interface MailOTPService
{
	void sendTextMail(
		String to,
		String subject,
		TextEmailContent content);

	void sendClickableMail(
		String to,
		String subject,
		ClickableEmailContent content);

	void sendOTPMail(
		String to,
		String subject,
		OTPEmailContent content);

	MailOTP createRegisterOTP(
		RegisterRequest dto);

	User validateOTP(
		UUID mailOTPId,
		String email,
		String otp);
}
