package vn.edu.hcmuaf.be.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.edu.hcmuaf.be.entity.Course;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    @Query("""
                SELECT c FROM Course c
                WHERE (:minPrice IS NULL OR c.currentPrice >= :minPrice)
                AND (:maxPrice IS NULL OR c.currentPrice <= :maxPrice)
                AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    Page<Course> findByConditions(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("name") String keyword,
            Pageable pageable);
}
