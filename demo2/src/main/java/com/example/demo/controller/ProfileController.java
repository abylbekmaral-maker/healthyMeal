package com.example.demo.controller;

import com.example.demo.entity.ProfileSetupForm;
import com.example.demo.entity.HealthProfile;
import com.example.demo.repository.HealthProfileRepository;
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
