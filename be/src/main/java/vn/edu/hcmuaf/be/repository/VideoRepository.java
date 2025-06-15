package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Video;

import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {}
