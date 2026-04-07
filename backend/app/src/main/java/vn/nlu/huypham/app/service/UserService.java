package vn.nlu.huypham.app.service;

import vn.nlu.huypham.app.dto.response.UserMe;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface UserService {
    UserMe getMe(String identity) throws AppException;
}
