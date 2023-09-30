package ru.fenris06.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fenris06.entity.AppPhoto;

public interface AppPhotoRepository extends JpaRepository<AppPhoto, Long> {
}