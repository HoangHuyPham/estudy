package vn.nlu.huypham.app.dto.request.tutor;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SectionsCreationRequest
{
	UUID courseId;
	List<Section> sections;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Section
	{
		float weight;
		String name;
		List<Lecture> lectures;
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Lecture
	{
		String name;
		float weight;
	}
}
