package vn.nlu.huypham.app.service.imp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.dto.response.UserMe;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.UserRepo;
import vn.nlu.huypham.app.service.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class UserServiceImp implements UserService {

    final UserRepo userRepo;
    final ModelMapper modelMapper;

    @Override
    public UserMe getMe(String identity) throws AppException {
        User user = userRepo.findByUsernameOrEmail(identity).orElseThrow(() -> Errors.USER_NOT_FOUND);
        return modelMapper.map(user, UserMe.class);
    }
}
