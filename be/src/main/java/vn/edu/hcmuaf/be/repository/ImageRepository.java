package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Image;

import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {}
