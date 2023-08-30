package ru.fenris06.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fenris06.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}