package com.telegrammCategory.repository;

import com.telegrammCategory.model.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStateRepository extends JpaRepository<UserState, Integer> {
    @Query(nativeQuery = true, value = "SELECT level FROM userState WHERE id = :chatId ")
    Integer findLevelById(Long chatId);
}
