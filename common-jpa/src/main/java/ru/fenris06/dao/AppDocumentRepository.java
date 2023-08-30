package ru.fenris06.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fenris06.entity.AppDocument;

public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}