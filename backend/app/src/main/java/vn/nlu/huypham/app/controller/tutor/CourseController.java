package vn.nlu.huypham.app.controller.tutor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.dto.request.tutor.CoursesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.CoursesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.CoursesRemovalRequest;
import vn.nlu.huypham.app.dto.response.tutor.CourseResponse;
import vn.nlu.huypham.app.dto.response.tutor.CourseWithDetailResponse;
import vn.nlu.huypham.app.entity.Course;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.payload.ApiResponsePageable;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.TutorService;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tutor/courses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tutor: Courses")
public class CourseController
{
	final ModelMapper modelMapper;

	final TutorService tutorService;

	@GetMapping
	public ResponseEntity<?> getAll(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int limit)
	{
		Page<Course> courses = tutorService.getAllCourses(PageRequest.of(page, limit),
				userPrincipal.getUser());
		return ResponseEntity.ok()
				.body(ApiResponsePageable.builder().code(200).message("Get courses successfully")
						.data(courses.getContent().stream()
								.map(course -> modelMapper.map(course, CourseResponse.class))
								.collect(java.util.stream.Collectors.toList()))
						.page(page).limit(limit).totalItems(courses.getTotalElements())
						.totalPages(courses.getTotalPages()).build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable UUID id)
	{
		if (userPrincipal.getAuthorities().stream().anyMatch((
			a) -> a.getAuthority().equals("ROLE_" + Roles.ADMIN)))
		{
			return ResponseEntity.ok()
					.body(ApiResponse.builder().code(200).message("Get course successfully")
							.data(modelMapper.map(
									tutorService.getCourseById(id, userPrincipal.getUser())
											.orElseThrow(() -> Errors.ENTITY_NOT_FOUND),
									CourseWithDetailResponse.class))
							.build());
		}

		return ResponseEntity.ok()
				.body(ApiResponse.builder().code(200).message("Get course successfully")
						.data(modelMapper.map(
								tutorService.getCourseById(id, userPrincipal.getUser())
										.orElseThrow(() -> Errors.ENTITY_NOT_FOUND),
								CourseWithDetailResponse.class))
						.build());
	}

	@PostMapping
	public ResponseEntity<?> createCourse(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody CoursesCreationRequest request)
	{
		List<Course> result = tutorService.createCourses(request.getCourses(),
				userPrincipal.getUser());

		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course(s) created successfully").data(result.size()).build());
	}

	@PatchMapping
	public ResponseEntity<?> patchCourse(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody CoursesPatchRequest request)
	{
		List<Course> result = tutorService.patchCourses(request.getCourses(),
				userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course(s) updated successfully").data(result.size()).build());
	}

	@PostMapping("bulk-delete")
	public ResponseEntity<?> deleteCourse(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody CoursesRemovalRequest request)
	{
		tutorService.removeCourses(request.getCourses(), userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course(s) removed successfully").data(null).build());
	}

	@PostMapping("/{id}/draft")
	public ResponseEntity<?> draft(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable UUID id)
	{
		tutorService.changeCourseToDraft(id, userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course changed to draft successfully").data(null).build());
	}

	@PostMapping(value = "/{id}/addThumbnail", consumes = "multipart/form-data")
	public ResponseEntity<?> addThumbnail(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable UUID id,
		@RequestParam("file") MultipartFile file)
	{
		tutorService.addThumbnail(file, userPrincipal.getUser(), id);
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Thumbnail added successfully").data(null).build());
	}

	@PostMapping("/{id}/publish")
	public ResponseEntity<?> publish(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable UUID id)
	{
		tutorService.publishCourse(id, userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course published successfully").data(null).build());
	}

	@PostMapping("/{id}/archive")
	public ResponseEntity<?> archive(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable UUID id)
	{
		tutorService.archiveCourse(id, userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course archived successfully").data(null).build());
	}
}
