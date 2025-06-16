package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.Section;

import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {}
