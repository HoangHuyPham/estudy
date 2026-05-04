package vn.nlu.huypham.app.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.entity.Course;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface StorageService
{
	Resource store(
		MultipartFile file,
		User user,
		ResourceVisibilities visibility,
		ResourceTypes type,
		Course courseRef) throws AppException;

	void validate(
		MultipartFile file,
		List<String> expectedTypes,
		List<String> expectedMimes) throws AppException;

	void validateImage(
		MultipartFile file) throws AppException;

	void validateVideo(
		MultipartFile file) throws AppException;
}
