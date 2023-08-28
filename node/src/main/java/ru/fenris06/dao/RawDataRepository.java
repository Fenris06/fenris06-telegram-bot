package ru.fenris06.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fenris06.entity.RawData;

public interface RawDataRepository extends JpaRepository<RawData, Long> {
}