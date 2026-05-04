package vn.nlu.huypham.app.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vn.nlu.huypham.app.entity.Course;

public interface CourseRepo extends JpaRepository<Course, UUID>
{
	@Query("""
			    SELECT COUNT(l.id)
			    FROM Course c
			    JOIN c.sections s
			    JOIN s.lectures l
			    WHERE c.id = :courseId
			""")
	int countLecturesInCourse(
		UUID courseId);

	List<Course> findByIdInAndOwnerId(
		List<UUID> courseIds,
		UUID ownerId);

	Optional<Course> findByIdAndOwnerId(
		UUID id,
		UUID ownerId);

	Page<Course> findAllByOwnerId(
		Pageable pageable,
		UUID ownerId);
}
