package vn.nlu.huypham.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nlu.huypham.app.entity.Enrollment;
import vn.nlu.huypham.app.entity.embeded_id.EnrollmentId;

public interface EnrollmentRepo extends JpaRepository<Enrollment, EnrollmentId>
{
	int countByCourseId(
		UUID courseId);
}
