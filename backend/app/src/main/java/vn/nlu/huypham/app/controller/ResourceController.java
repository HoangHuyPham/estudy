package vn.nlu.huypham.app.controller;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.entity.Resource;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.ResourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Misc: Resource")
public class ResourceController
{
	final ResourceService resourceService;
	final ModelMapper modelMapper;

	@GetMapping("/serve/{resourceId}")
	public ResponseEntity<?> serve(
		@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable String resourceId)
	{

		UUID userId = (userPrincipal != null) ? userPrincipal.getUser().getId() : null;

		Resource resource = resourceService.canAccess(UUID.fromString(resourceId), userId);

		return ResponseEntity.ok().header("X-Accel-Redirect", resource.getXAccelRedirect())
				.body(null);
	}
}
