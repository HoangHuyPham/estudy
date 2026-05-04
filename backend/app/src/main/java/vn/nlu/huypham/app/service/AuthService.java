package vn.nlu.huypham.app.service;

import java.util.UUID;

import vn.nlu.huypham.app.dto.request.RegisterConfirmRequest;
import vn.nlu.huypham.app.dto.request.RegisterRequest;
import vn.nlu.huypham.app.dto.response.TokenPairResponse;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface AuthService
{
	TokenPairResponse auth(
		String username,
		String password) throws AppException;

	TokenPairResponse auth(
		String googleIDToken) throws AppException;

	TokenPairResponse register(
		RegisterConfirmRequest dto,
		String otp) throws AppException;

	UUID preRegister(
		RegisterRequest dto) throws AppException;

	void logout(
		String accessToken,
		String refreshToken) throws AppException;
}
