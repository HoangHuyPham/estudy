package vn.nlu.huypham.app.dto.request.tutor;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.CourseStatus;
import vn.nlu.huypham.app.constant.CourseVisibilities;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursesPatchRequest
{
	List<Course> courses;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Course
	{
		UUID id;
		String name;
		String description;
		String thumbnail;
		CourseStatus status;
		CourseVisibilities visibility;
		int duration;
		int studentCount;
		int enrollmentCount;
		float oldPrice;
		float price;
	}
}
