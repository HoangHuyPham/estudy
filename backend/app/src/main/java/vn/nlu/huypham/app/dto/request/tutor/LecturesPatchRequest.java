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
public class LecturesPatchRequest
{
	List<Lecture> lectures;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Lecture
	{
		UUID id;
		float weight;
		String name;
	}
}
