package ru.fenris06.dao;

import ru.fenris06.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @Query("select a " +
            "from AppUser a " +
            "where a.telegramUserId = ?1")
    Optional<AppUser> findByTelegramUserId(Long telegramUserId);
}