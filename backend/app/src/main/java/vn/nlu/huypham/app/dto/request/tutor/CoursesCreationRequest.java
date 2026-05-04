package vn.nlu.huypham.app.dto.request.tutor;

import java.util.List;

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
public class CoursesCreationRequest
{
	List<Course> courses;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Course
	{
		String name;
		String description;
	}
}
