package vn.nlu.huypham.app.service.imp;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.CourseStatus;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.constant.VideoStatus;
import vn.nlu.huypham.app.dto.request.tutor.CoursesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.CoursesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsCreationRequest;
import vn.nlu.huypham.app.entity.Course;
import vn.nlu.huypham.app.entity.Lecture;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.entity.Section;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.entity.Video;
import vn.nlu.huypham.app.event.ResourceDeleteEvent;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.repository.CourseRepo;
import vn.nlu.huypham.app.repository.EnrollmentRepo;
import vn.nlu.huypham.app.repository.LectureRepo;
import vn.nlu.huypham.app.repository.ResourceRepo;
import vn.nlu.huypham.app.repository.SectionRepo;
import vn.nlu.huypham.app.repository.VideoRepo;
import vn.nlu.huypham.app.service.FFmpegService;
import vn.nlu.huypham.app.service.StorageService;
import vn.nlu.huypham.app.service.TutorService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class TutorServiceImp implements TutorService
{
	final ModelMapper modelMapper;
	final EnrollmentRepo enrollmentRepo;
	final CourseRepo courseRepo;
	final SectionRepo sectionRepo;
	final LectureRepo lectureRepo;
	final VideoRepo videoRepo;
	final ResourceRepo resourceRepo;

	final StorageService storageService;
	final FFmpegService ffmpegService;

	final ApplicationEventPublisher eventPublisher;

	@Override
	public Page<Course> getAllCourses(
		Pageable pageable,
		User owner)
	{
		return courseRepo.findAllByOwnerId(pageable, owner.getId());
	}

	@Override
	public Optional<Course> getCourseById(
		UUID id,
		User owner)
	{
		return courseRepo.findByIdAndOwnerId(id, owner.getId());
	}

	@Override
	public void publishCourse(
		UUID id,
		User owner) throws AppException
	{
		Course course = courseRepo.findById(id).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
			throw Errors.RESOURCE_CAN_NOT_ACCESS;

		// Status check
		if (course.getStatus() != CourseStatus.DRAFT)
			throw Errors.CAN_NOT_DO_ACTION;

		// Lecture count check
		int lectureCount = courseRepo.countLecturesInCourse(id);
		if (lectureCount == 0)
			throw Errors.NO_LECTURE_COUNT;

		course.setStatus(CourseStatus.PUBLISHED);
		course.setLectureCount(lectureCount);
		courseRepo.save(course);
	}

	@Override
	@Transactional
	public void removeCourse(
		UUID id,
		User owner) throws AppException
	{
		Course course = courseRepo.findById(id).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// enrollment check
		int enrolledCourses = enrollmentRepo.countByCourseId(id);
		if (enrolledCourses > 0)
			throw Errors.CAN_NOT_DO_ACTION;

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
			throw Errors.RESOURCE_CAN_NOT_ACCESS;

		// List of paths to delete after commit
		List<Path> paths = course.getResources().stream()
				.map(r -> Path.of(r.getDiskPath()).getParent()).toList();

		courseRepo.delete(course);
		eventPublisher.publishEvent(new ResourceDeleteEvent(paths));
	}

	@Override
	public void archiveCourse(
		UUID id,
		User owner) throws AppException
	{
		Course course = courseRepo.findById(id).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
			throw Errors.RESOURCE_CAN_NOT_ACCESS;

		// Status check
		if (course.getStatus() != CourseStatus.PUBLISHED)
			throw Errors.CAN_NOT_DO_ACTION;

		// Lecture count check
		int lectureCount = courseRepo.countLecturesInCourse(id);
		if (lectureCount == 0)
			throw Errors.NO_LECTURE_COUNT;

		// enrollment check
		int enrolledCourses = enrollmentRepo.countByCourseId(id);
		if (enrolledCourses > 0)
			throw Errors.CAN_NOT_DO_ACTION;

		course.setStatus(CourseStatus.ARCHIVED);
		course.setLectureCount(lectureCount);
		course.setEnrollmentCount(enrolledCourses);
		courseRepo.save(course);
	}

	@Override
	public void changeCourseToDraft(
		UUID id,
		User owner) throws AppException
	{
		Course course = courseRepo.findById(id).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
			throw Errors.RESOURCE_CAN_NOT_ACCESS;

		// Status check
		if (course.getStatus() != CourseStatus.PUBLISHED)
			throw Errors.CAN_NOT_DO_ACTION;

		// Lecture count check
		int lectureCount = courseRepo.countLecturesInCourse(id);
		if (lectureCount == 0)
			throw Errors.NO_LECTURE_COUNT;

		// enrollment check
		int enrolledCourses = enrollmentRepo.countByCourseId(id);
		if (enrolledCourses > 0)
			throw Errors.CAN_NOT_DO_ACTION;

		course.setStatus(CourseStatus.DRAFT);
		course.setLectureCount(lectureCount);
		course.setEnrollmentCount(enrolledCourses);
		courseRepo.save(course);
	}

	@Override
	@Transactional
	public void addVideo(
		MultipartFile file,
		User owner,
		UUID courseId,
		UUID lectureId) throws AppException
	{
		Course course = courseRepo.findById(courseId).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);
		Lecture lecture = lectureRepo.findById(lectureId)
				.orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);
		Section section = sectionRepo.findById(lecture.getSection().getId())
				.orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Lecture - section - course check
		if (!section.getCourse().getId().equals(courseId)
				|| !lecture.getSection().getId().equals(section.getId()))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check
		if (course.getStatus() != CourseStatus.DRAFT)
		{
			throw Errors.CAN_NOT_DO_ACTION;
		}

		// Validate file type
		storageService.validateVideo(file);

		// Store
		Resource video = storageService.store(file, course.getOwner(), ResourceVisibilities.PRIVATE,
				ResourceTypes.VIDEO, courseRepo.getReferenceById(courseId));
		Video newVideo = Video.builder().resource(video)
				.duration(ffmpegService.getDurationInSeconds(Path.of(video.getDiskPath())))
				.status(VideoStatus.SUCCESS).lecture(lecture).build();
		videoRepo.save(newVideo);
		lecture.setVideo(newVideo);
	}

	@Override
	@Transactional
	public void addThumbnail(
		MultipartFile file,
		User owner,
		UUID courseId) throws AppException
	{
		Course course = courseRepo.findById(courseId).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);
		Resource oldThumbnail = course.getThumbnail() == null ? null
				: resourceRepo.findById(UUID.fromString(course.getThumbnail())).orElse(null);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check
		if (course.getStatus() != CourseStatus.DRAFT)
		{
			throw Errors.CAN_NOT_DO_ACTION;
		}
		// Validate file type
		storageService.validateImage(file);

		// Delete old thumbnail if exists
		if (oldThumbnail != null)
		{
			resourceRepo.delete(oldThumbnail);
			eventPublisher.publishEvent(new ResourceDeleteEvent(
					List.of(Path.of(oldThumbnail.getDiskPath()).getParent())));
		}

		// Store new thumbnail
		Resource thumbnail = storageService.store(file, course.getOwner(),
				ResourceVisibilities.PUBLIC, ResourceTypes.IMAGE,
				courseRepo.getReferenceById(courseId));
		course.setThumbnail(thumbnail.getId().toString());
		courseRepo.save(course);
	}

	@Override
	public List<Course> createCourses(
		List<CoursesCreationRequest.Course> courses,
		User owner)
	{
		return courseRepo.saveAll(courses.stream().map(course ->
		{
			Course c = modelMapper.map(course, Course.class);
			c.setOwner(owner);
			return c;
		}).toList());
	}

	@Override
	public List<Course> patchCourses(
		List<CoursesPatchRequest.Course> courses,
		User owner) throws AppException
	{
		// Mapping UUID to course patch request
		List<UUID> courseIds = courses.stream().map(c -> c.getId()).toList();
		List<Course> ownedCourses = courseRepo.findByIdInAndOwnerId(courseIds, owner.getId());
		if (ownedCourses.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}
		Map<UUID, CoursesPatchRequest.Course> courseMap = courses.stream()
				.collect(Collectors.toMap(CoursesPatchRequest.Course::getId, c -> c));

		// Status check - draft + patch
		ownedCourses = ownedCourses.stream()
				.filter(owned -> owned.getStatus().equals(CourseStatus.DRAFT)).map(owned ->
				{
					CoursesPatchRequest.Course c = courseMap.get(owned.getId());
					modelMapper.map(c, owned);
					return owned;
				}).toList();

		return courseRepo.saveAll(ownedCourses);
	}

	@Override
	public void removeCourses(
		List<UUID> courseIds,
		User owner) throws AppException
	{
		// Owned check for all courses
		List<Course> ownedCourses = courseRepo.findByIdInAndOwnerId(courseIds, owner.getId());
		if (ownedCourses.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check - draft
		ownedCourses = ownedCourses.stream().filter(c -> c.getStatus().equals(CourseStatus.DRAFT))
				.toList();

		courseRepo.deleteAllInBatch(ownedCourses);
	}

	@Override
	@Transactional
	public List<Section> createSections(
		List<SectionsCreationRequest.Section> sections,
		User owner,
		UUID courseId) throws AppException
	{
		Course course = courseRepo.findById(courseId).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check
		if (course.getStatus() != CourseStatus.DRAFT)
			throw Errors.CAN_NOT_DO_ACTION;

		List<Section> newSections = sections.stream().map(s ->
		{
			Section section = modelMapper.map(s, Section.class);
			section.setCourse(course);

			if (s.getLectures().size() > 0)
			{
				lectureRepo.saveAll(s.getLectures().stream().map(l ->
				{
					Lecture lecture = modelMapper.map(l, Lecture.class);
					lecture.setSection(section);
					return lecture;
				}).toList());
			}

			return section;
		}).toList();

		return sectionRepo.saveAll(newSections);
	}

	@Override
	public List<Section> patchSections(
		List<SectionsPatchRequest.Section> sections,
		User owner) throws AppException
	{
		// Mapping UUID to section patch request
		List<UUID> sectionIds = sections.stream().map(s -> s.getId()).toList();
		List<Section> ownedSections = sectionRepo.findByIdInAndOwnerId(sectionIds, owner.getId());
		if (ownedSections.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}
		Map<UUID, SectionsPatchRequest.Section> sectionMap = sections.stream()
				.collect(Collectors.toMap(SectionsPatchRequest.Section::getId, s -> s));

		// Status check - draft + patch
		ownedSections = ownedSections.stream()
				.filter(owned -> owned.getCourse().getStatus().equals(CourseStatus.DRAFT))
				.map(owned ->
				{
					SectionsPatchRequest.Section s = sectionMap.get(owned.getId());
					modelMapper.map(s, owned);
					return owned;
				}).toList();
		return sectionRepo.saveAll(ownedSections);

	}

	@Override
	public void removeSections(
		List<UUID> sectionIds,
		User owner) throws AppException
	{
		// Owned check for all sections
		List<Section> ownedSections = sectionRepo.findByIdInAndOwnerId(sectionIds, owner.getId());
		if (ownedSections.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		ownedSections = ownedSections.stream()
				.filter(owned -> owned.getCourse().getStatus().equals(CourseStatus.DRAFT)).toList();
		sectionRepo.deleteAllInBatch(ownedSections);
	}

	@Override
	public List<Lecture> createLectures(
		List<LecturesCreationRequest.Lecture> lectures,
		User owner,
		UUID courseId,
		UUID sectionId) throws AppException
	{
		Course course = courseRepo.findById(courseId).orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);
		Section section = sectionRepo.findById(sectionId)
				.orElseThrow(() -> Errors.CAN_NOT_DO_ACTION);

		// Owned check
		if (!course.getOwner().getId().equals(owner.getId()))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Section - course check
		if (!section.getCourse().getId().equals(courseId))
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check
		if (course.getStatus() != CourseStatus.DRAFT)
			throw Errors.CAN_NOT_DO_ACTION;

		List<Lecture> newLectures = lectures.stream().map(l ->
		{
			Lecture lecture = modelMapper.map(l, Lecture.class);
			lecture.setSection(section);
			return lecture;
		}).toList();
		return lectureRepo.saveAll(newLectures);
	}

	@Override
	public List<Lecture> patchLectures(
		List<LecturesPatchRequest.Lecture> lectures,
		User owner) throws AppException
	{
		// Owned check for all lectures
		List<UUID> lectureIds = lectures.stream().map(l -> l.getId()).toList();
		List<Lecture> ownedLectures = lectureRepo.findByIdInAndOwnerId(lectureIds, owner.getId());
		if (ownedLectures.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}
		Map<UUID, LecturesPatchRequest.Lecture> lectureMap = lectures.stream()
				.collect(Collectors.toMap(l -> l.getId(), l -> l));

		ownedLectures = ownedLectures.stream()
				// Status check - draft + patch
				.filter(owned -> owned.getSection().getCourse().getStatus()
						.equals(CourseStatus.DRAFT))
				.map(owned ->
				{
					LecturesPatchRequest.Lecture l = lectureMap.get(owned.getId());
					modelMapper.map(l, owned);
					return owned;
				}).toList();
		return lectureRepo.saveAll(ownedLectures);
	}

	@Override
	public void removeLectures(
		List<UUID> lectureIds,
		User owner) throws AppException
	{
		// Owned check for all lectures
		List<Lecture> ownedLectures = lectureRepo.findByIdInAndOwnerId(lectureIds, owner.getId());
		if (ownedLectures.size() <= 0)
		{
			throw Errors.RESOURCE_CAN_NOT_ACCESS;
		}

		// Status check - draft
		ownedLectures = ownedLectures.stream().filter(
				owned -> owned.getSection().getCourse().getStatus().equals(CourseStatus.DRAFT))
				.toList();
		lectureRepo.deleteAllInBatch(ownedLectures);
	}
}
