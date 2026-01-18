package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final HealthProfileRepository healthProfileRepository;

    public ProfileController(HealthProfileRepository healthProfileRepository) {
        this.healthProfileRepository = healthProfileRepository;
    }

    /**
     * 1) 프로필 저장 (키/몸무게/활동량/목표/notes/conditions)
     *    - null 로 들어온 값은 "그 필드는 건드리지 않고" 기존 값 유지
     */
    @PostMapping
    public ResponseEntity<?> saveProfile(@RequestBody ProfileSetupForm form) {

        if (form.getUserId() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "userId가 필요합니다."));
        }

        HealthProfile profile = healthProfileRepository
                .findByUserId(form.getUserId())
                .orElseGet(HealthProfile::new);

        profile.setUserId(form.getUserId());

        // 🔹 값이 들어온 것만 업데이트 (null이면 기존 값 유지)
        if (form.getHeightCm() != null) {
            profile.setHeightCm(form.getHeightCm());
        }
        if (form.getWeightKg() != null) {
            profile.setWeightKg(form.getWeightKg());
        }
        if (form.getActivityLevel() != null) {
            profile.setActivityLevel(form.getActivityLevel());
        }
        if (form.getGoal() != null) {
            profile.setGoal(form.getGoal());
        }
        if (form.getNotes() != null) {
            profile.setNotes(form.getNotes());
        }
        if (form.getConditions() != null) {
            profile.setConditions(form.getConditions());
        }

        healthProfileRepository.save(profile);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "프로필이 저장되었습니다."
        ));
    }

    /**
     * 2) 프로필 조회
     *    GET /api/profile/{userId}
     *    - 여기서 반드시 "conditions" 를 같이 내려줘야 profile.html 에서 체크 가능
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable("userId") Integer userId) {

        return healthProfileRepository.findByUserId(userId)
                .map(profile -> {
                    Map<String, Object> body = new HashMap<>();
                    body.put("exists",        true);
                    body.put("userId",        profile.getUserId());
                    body.put("heightCm",      profile.getHeightCm());
                    body.put("weightKg",      profile.getWeightKg());
                    body.put("activityLevel", profile.getActivityLevel());
                    body.put("goal",          profile.getGoal());
                    body.put("notes",         profile.getNotes());
                    body.put("conditions",    profile.getConditions()); // 🔥 이 줄이 핵심!

                    return ResponseEntity.ok(body);
                })
                .orElseGet(() ->
                        ResponseEntity.ok(Map.of("exists", false))
                );
    }
}
