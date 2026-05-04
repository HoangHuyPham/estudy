package vn.nlu.huypham.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.nlu.huypham.app.entity.Video;

public interface VideoRepo extends JpaRepository<Video, UUID>
{
}
