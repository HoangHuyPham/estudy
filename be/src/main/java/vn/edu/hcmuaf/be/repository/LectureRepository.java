package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Lecture;

import java.util.UUID;

public interface LectureRepository extends JpaRepository<Lecture, UUID> {}
