package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // 회원가입 중복 체크용
    Optional<User> findByEmail(String email);

    // 로그인용 (이메일 + 비밀번호)
    Optional<User> findByEmailAndPassword(String email, String password);

	Optional<User> findById(Integer userId);
	Optional<User> findTopByOrderByIdAsc();
	}


