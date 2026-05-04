package vn.nlu.huypham.app.controller.tutor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.dto.request.tutor.SectionsCreationRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsPatchRequest;
import vn.nlu.huypham.app.dto.request.tutor.SectionsRemovalRequest;
import vn.nlu.huypham.app.entity.Section;
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
@RequestMapping("/tutor/sections")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tutor: Sections")
public class SectionController
{
	final ModelMapper modelMapper;

	final TutorService tutorService;

	@PostMapping("bulk-delete")
	public ResponseEntity<?> deleteSections(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody SectionsRemovalRequest request)
	{
		tutorService.removeSections(request.getSections(), userPrincipal.getUser());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Course removed successfully").data(null).build());
	}

	@PostMapping
	public ResponseEntity<?> createSections(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody SectionsCreationRequest request)
	{
		List<Section> result = tutorService.createSections(request.getSections(),
				userPrincipal.getUser(), request.getCourseId());
		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Section(s) added successfully").data(result.size()).build());
	}

	@PatchMapping
	public ResponseEntity<?> patchSections(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@RequestBody SectionsPatchRequest request)
	{
		List<Section> sections = tutorService.patchSections(request.getSections(),
				userPrincipal.getUser());

		return ResponseEntity.ok().body(ApiResponse.builder().code(200)
				.message("Sections updated successfully").data(sections.size()).build());
	}
}
