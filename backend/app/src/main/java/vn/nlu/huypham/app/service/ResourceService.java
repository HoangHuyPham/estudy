package vn.nlu.huypham.app.service;

import java.util.UUID;

import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface ResourceService {
    Resource canAccess(UUID resourceId, UUID userId) throws AppException;
}
