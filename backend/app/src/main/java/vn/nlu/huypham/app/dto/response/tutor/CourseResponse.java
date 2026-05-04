package vn.nlu.huypham.app.dto.response.tutor;

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
public class CourseResponse
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
	long createdAt;
	long updatedAt;
}
