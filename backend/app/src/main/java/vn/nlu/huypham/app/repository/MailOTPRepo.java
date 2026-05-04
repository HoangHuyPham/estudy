package vn.nlu.huypham.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nlu.huypham.app.entity.MailOTP;

public interface MailOTPRepo extends JpaRepository<MailOTP, UUID>
{
}
