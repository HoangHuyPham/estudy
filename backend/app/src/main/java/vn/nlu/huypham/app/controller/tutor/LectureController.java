package vn.nlu.huypham.app.controller.tutor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.dto.request.tutor.LectureAddVideoRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.LecturesRemovalRequest;
import vn.nlu.huypham.app.entity.Lecture;
import vn.nlu.huypham.app.payload.ApiResponse;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.TutorService;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tutor/lectures")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tutor: Lectures")
public class LectureController
{
	final ModelMapper modelMapper;

	final TutorService tutorService;

	@PatchMapping
	public ResponseEntity<?> patchLectures(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody LecturesPatchRequest request)
	{
		List<Lecture> result = tutorService.patchLectures(request.getLectures(),
				userPrincipal.getUser());

		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Lecture(s) updated successfully").data(result.size()).build());
	}

	@PostMapping("bulk-delete")
	public ResponseEntity<?> deleteLectures(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody LecturesRemovalRequest request)
	{
		tutorService.removeLectures(request.getLectures(), userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Lecture(s) removed successfully").data(null).build());
	}

	@PostMapping
	public ResponseEntity<?> createLectures(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody LecturesCreationRequest request)
	{
		List<Lecture> result = tutorService.createLectures(request.getLectures(),
				userPrincipal.getUser(), request.getCourseId(), request.getSectionId());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Lecture(s) added successfully").data(result.size()).build());
	}

	@PostMapping(value = "/add-video", consumes = "multipart/form-data")
	public ResponseEntity<?> addVideo(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody LectureAddVideoRequest request,
		@RequestBody MultipartFile file)
	{
		if (file == null || file.isEmpty())
			throw Errors.PARAMETER_IS_NOT_ENOUGH;
		tutorService.addVideo(file, userPrincipal.getUser(), request.getCourseId(), request.getSectionId());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Video attached successfully").data(null).build());
	}
}
