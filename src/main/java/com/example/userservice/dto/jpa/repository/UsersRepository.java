package com.example.userservice.dto.jpa.repository;

import com.example.userservice.dto.jpa.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, String> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE users_table SET refresh_token = :refreshToken WHERE username = :username", nativeQuery = true)
    void updateByUsername(@Param("refreshToken") String refreshToken, @Param("username") String username);

    UserEntity findUserEntityByUsername(@Param("username") String username);
}
