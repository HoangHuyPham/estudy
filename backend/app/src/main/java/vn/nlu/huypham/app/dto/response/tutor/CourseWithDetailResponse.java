package vn.nlu.huypham.app.dto.response.tutor;

import java.util.Set;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.CourseStatus;
import vn.nlu.huypham.app.constant.CourseVisibilities;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;
import vn.nlu.huypham.app.constant.VideoStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseWithDetailResponse
{
	UUID id;
	String name;
	String description;
	String thumbnail;
	CourseStatus status;
	CourseVisibilities visibility;
	int duration;
	int enrollmentCount;
	int lectureCount;
	float oldPrice;
	float price;
	long createdAt;
	long updatedAt;
	Set<Section> sections;

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Section
	{
		UUID id;
		float weight;
		String name;
		Set<Lecture> lectures;
		long createdAt;
		long updatedAt;
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Lecture
	{
		UUID id;
		float weight;
		String name;
		Video video;
		long createdAt;
		long updatedAt;
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Video
	{
		UUID id;
		int duration;
		VideoStatus status;
		Resource resource;
	}

	@Data
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Resource
	{
		UUID id;
		long size;
		ResourceTypes type;
		ResourceVisibilities visibility;
		long createdAt;
	}
}
