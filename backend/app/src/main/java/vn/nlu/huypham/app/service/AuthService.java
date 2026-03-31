package vn.nlu.huypham.app.service;

import java.util.UUID;

import vn.nlu.huypham.app.dto.request.RegisterBasic;
import vn.nlu.huypham.app.dto.request.RegisterOTPBasic;
import vn.nlu.huypham.app.dto.response.ATAndRT;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface AuthService {
    ATAndRT auth(String username, String password) throws AppException;

    ATAndRT auth(String googleIDToken) throws AppException;

    ATAndRT register(RegisterBasic dto, String otp) throws AppException;

    UUID preRegister(RegisterOTPBasic dto) throws AppException;

    void logout(String accessToken, String refreshToken) throws AppException;
}
