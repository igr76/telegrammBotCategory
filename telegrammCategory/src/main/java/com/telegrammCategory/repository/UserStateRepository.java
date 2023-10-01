package com.telegrammCategory.repository;

import com.telegrammCategory.model.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public class UserStateRepository {
    public UserState findById(long chatId) {
        return null;
    }//extends JpaRepository<UserState, Long> {
//    @Query(nativeQuery = true, value = "SELECT level FROM userState WHERE id = :chatId ")
//    Integer findLevelById(Long chatId);
}
