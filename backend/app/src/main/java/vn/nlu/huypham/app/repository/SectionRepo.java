package vn.nlu.huypham.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import vn.nlu.huypham.app.entity.Section;

public interface SectionRepo extends JpaRepository<Section, UUID>
{
	@Query("""
			SELECT s FROM Section s
			JOIN FETCH s.course c
			WHERE s.id IN :ids AND c.owner.id = :ownerId
			""")
	List<Section> findByIdInAndOwnerId(
		List<UUID> ids,
		UUID ownerId);
}
