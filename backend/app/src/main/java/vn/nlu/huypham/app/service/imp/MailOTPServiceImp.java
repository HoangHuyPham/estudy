package vn.nlu.huypham.app.service.imp;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.config.AppConfig;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.OTPTypes;
import vn.nlu.huypham.app.dto.request.ClickableMailContent;
import vn.nlu.huypham.app.dto.request.OTPMailContent;
import vn.nlu.huypham.app.dto.request.RegisterOTPBasic;
import vn.nlu.huypham.app.dto.request.TextMailContent;
import vn.nlu.huypham.app.entity.MailOTP;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.repository.MailOTPRepo;
import vn.nlu.huypham.app.service.MailOTPService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class MailOTPServiceImp implements MailOTPService {

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;
    final AppConfig appConfig;
    final JavaMailSender mailSender;
    final Configuration freemarkerConfig;

    final MailOTPRepo mailOTPRepo;

    @Override
    @Async("mailExecutor")
    public void sendTextMail(String to, String subject, TextMailContent content) {
        try {
            Template template = freemarkerConfig.getTemplate("text-mail-template.ftlh");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                    Map.of(
                            "title", subject,
                            "name", content.getName(),
                            "content", content.getContent()));
            sendHtmlMail(to, subject, html);
        } catch (Exception e) {
            log.error("Error sending text email: {}", e.getMessage());
        }
    }

    @Override
    @Async("mailExecutor")
    public void sendClickableMail(String to, String subject, ClickableMailContent content) {
        try {
            Template template = freemarkerConfig.getTemplate("clickable-mail-template.ftlh");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                    Map.of(
                            "title", subject,
                            "content", content.getContent(),
                            "name", content.getName(),
                            "buttonText", content.getButtonText(),
                            "buttonUrl", content.getButtonUrl()));
            sendHtmlMail(to, subject, html);
        } catch (Exception e) {
            log.error("Error sending clickable email: {}", e.getMessage());
        }
    }

    @Override
    @Async("mailExecutor")
    public void sendOTPMail(String to, String subject, OTPMailContent content) {
        try {
            Template template = freemarkerConfig.getTemplate("otp-mail-template.ftlh");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                    Map.of(
                            "content", content.getContent(),
                            "title", subject,
                            "name", content.getName(),
                            "otp", content.getOtp()));
            sendHtmlMail(to, subject, html);
        } catch (Exception e) {
            log.error("Error sending OTP email: {}", e.getMessage());
        }
    }

    private void sendHtmlMail(String to, String subject, String html) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Override
    public MailOTP createRegisterOTP(RegisterOTPBasic dto) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("username", dto.getUsername());
        payload.put("email", dto.getEmail());
        payload.put("password", passwordEncoder.encode(dto.getPassword()));
        payload.put("displayName", dto.getDisplayName());
        payload.put("avatar", dto.getAvatar());

        MailOTP mailOTP = MailOTP.builder()
                .isUsed(false)
                .otp("%06d".formatted((int) (Math.random() * 999999)))
                .payload(payload)
                .type(OTPTypes.REGISTER_ACCOUNT)
                .expiredAt(Instant.now().plusSeconds(appConfig.getMail().getOtpExp()).toEpochMilli())
                .build();
        mailOTPRepo.save(mailOTP);

        return mailOTP;

    }

    @Override
    public User validateOTP(UUID mailOTPId, String email, String otp) {
        MailOTP mailOTP = mailOTPRepo.findById(mailOTPId).orElseThrow(() -> Errors.MAIL_OTP_NOT_FOUND);

        if (mailOTP.isUsed()) {
            throw Errors.MAIL_OTP_ALREADY_USED;
        }
        if (!mailOTP.getOtp().equals(otp)) {
            throw Errors.MAIL_OTP_INVALID;
        }
        if (System.currentTimeMillis() > mailOTP.getExpiredAt()) {
            throw Errors.MAIL_OTP_EXPIRED;
        }

        User user = User.builder()
                .displayName((String) mailOTP.getPayload().get("displayName"))
                .username((String) mailOTP.getPayload().get("username"))
                .email((String) mailOTP.getPayload().get("email"))
                .password((String) mailOTP.getPayload().get("password"))
                .avatar((String) mailOTP.getPayload().get("avatar"))
                .isEnabled(true)
                .isAccountNonLocked(true)
                .build();

        if (!user.getEmail().equals(email)) {
            throw Errors.MAIL_OTP_INVALID;
        }

        mailOTP.setUsed(true);
        mailOTPRepo.save(mailOTP);

        return user;
    }
}