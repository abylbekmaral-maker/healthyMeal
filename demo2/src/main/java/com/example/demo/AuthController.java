package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final HealthProfileRepository healthProfileRepository;

    public AuthController(UserRepository userRepository,
                          HealthProfileRepository healthProfileRepository) {
        this.userRepository = userRepository;
        this.healthProfileRepository = healthProfileRepository;
    }

    // 1) 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String email = body.get("email");
        String password = body.get("password");

        if (name == null || name.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이름, 이메일, 비밀번호를 모두 입력해 주세요."
            ));
        }

        // 이미 있는 이메일인지 확인
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "이미 가입된 이메일입니다."
                    ));
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);   // 실제 서비스에서는 암호화 필요

        User saved = userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "회원가입이 완료되었습니다.",
                "userId", saved.getId(),
                "name", saved.getName()
        ));
    }

    // 2) 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "이메일과 비밀번호를 모두 입력해 주세요."
            ));
        }

        var userOpt = userRepository.findByEmail(email);

        // 이메일 없음
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "등록되지 않은 이메일입니다."
            ));
        }

        User user = userOpt.get();

        // 비밀번호 불일치
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "비밀번호가 올바르지 않습니다."
            ));
        }

        // 프로필 존재 여부
        boolean hasProfile = healthProfileRepository
                .findByUserId(user.getId())
                .isPresent();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "userId", user.getId(),
                "name", user.getName(),
                "hasProfile", hasProfile
        ));
    }

    // 3) 비밀번호 변경
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        Integer userId = Integer.valueOf(body.get("userId"));
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (currentPassword == null || newPassword == null
                || currentPassword.isBlank() || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "현재 비밀번호와 새 비밀번호를 모두 입력해 주세요."
            ));
        }

        return userRepository.findById(userId)
                .map(user -> {
                    if (!user.getPassword().equals(currentPassword)) {
                        return ResponseEntity.badRequest().body(Map.of(
                                "success", false,
                                "message", "현재 비밀번호가 올바르지 않습니다."
                        ));
                    }

                    user.setPassword(newPassword);
                    userRepository.save(user);

                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "비밀번호가 변경되었습니다."
                    ));
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "사용자를 찾을 수 없습니다."
                )));
    }
}
