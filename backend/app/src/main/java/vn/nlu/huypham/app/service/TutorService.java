package vn.nlu.huypham.app.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import vn.nlu.huypham.app.dto.request.tutor.CoursesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.CoursesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsPatchRequest;
import vn.nlu.huypham.app.entity.Course;
import vn.nlu.huypham.app.entity.Lecture;
import vn.nlu.huypham.app.entity.Section;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface TutorService
{
	List<Course> createCourses(
		List<CoursesCreationRequest.Course> courses,
		User owner);

	List<Section> createSections(
		List<SectionsCreationRequest.Section> sections,
		User owner,
		UUID courseId) throws AppException;

	List<Lecture> createLectures(
		List<LecturesCreationRequest.Lecture> lectures,
		User owner,
		UUID courseId,
		UUID sectionId) throws AppException;

	List<Course> patchCourses(
		List<CoursesPatchRequest.Course> courses,
		User owner) throws AppException;

	List<Section> patchSections(
		List<SectionsPatchRequest.Section> sections,
		User owner) throws AppException;

	List<Lecture> patchLectures(
		List<LecturesPatchRequest.Lecture> lectures,
		User owner) throws AppException;

	void removeCourses(
		List<UUID> courses,
		User owner) throws AppException;

	void removeSections(
		List<UUID> sections,
		User owner) throws AppException;

	void removeLectures(
		List<UUID> lectures,
		User owner) throws AppException;

	Optional<Course> getCourseById(
		UUID id,
		User owner) throws AppException;

	Page<Course> getAllCourses(
		Pageable pageable,
		User owner) throws AppException;

	void addThumbnail(
		MultipartFile file,
		User owner,
		UUID courseId) throws AppException;

	void publishCourse(
		UUID id,
		User owner) throws AppException;

	void removeCourse(
		UUID id,
		User owner) throws AppException;

	void archiveCourse(
		UUID id,
		User owner) throws AppException;

	void changeCourseToDraft(
		UUID id,
		User owner) throws AppException;

	void addVideo(
		MultipartFile file,
		User owner,
		UUID courseId,
		UUID lectureId) throws AppException;
}
