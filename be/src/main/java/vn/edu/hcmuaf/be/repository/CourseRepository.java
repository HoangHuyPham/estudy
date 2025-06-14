package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Course;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {}
