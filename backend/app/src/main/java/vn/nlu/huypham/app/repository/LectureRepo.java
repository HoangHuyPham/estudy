package vn.nlu.huypham.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vn.nlu.huypham.app.entity.Lecture;

public interface LectureRepo extends JpaRepository<Lecture, UUID>
{
	@Query("""
			SELECT l FROM Lecture l
			JOIN FETCH l.section s
			JOIN FETCH s.course c
			WHERE l.id IN :ids AND c.owner.id = :ownerId
			""")
	List<Lecture> findByIdInAndOwnerId(
		List<UUID> ids,
		UUID ownerId);
}
