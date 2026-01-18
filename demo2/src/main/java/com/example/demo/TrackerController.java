package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/api/tracker")
@CrossOrigin(origins = "*")
public class TrackerController {

    private final HealthTrackerRepository healthTrackerRepository;

    public TrackerController(HealthTrackerRepository healthTrackerRepository) {
        this.healthTrackerRepository = healthTrackerRepository;
    }

    // 요청 바디 DTO
    public static class TrackerRequest {
        private Long userId;
        private Integer sleepHours;
        private String mood;
        private Integer waterGlasses;

        public Long getUserId() {
            return userId;
        }
        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Integer getSleepHours() {
            return sleepHours;
        }
        public void setSleepHours(Integer sleepHours) {
            this.sleepHours = sleepHours;
        }

        public String getMood() {
            return mood;
        }
        public void setMood(String mood) {
            this.mood = mood;
        }

        public Integer getWaterGlasses() {
            return waterGlasses;
        }
        public void setWaterGlasses(Integer waterGlasses) {
            this.waterGlasses = waterGlasses;
        }
    }

    // ----------------------------------------------------
    // 1) 오늘 데이터 저장 (insert/update)
    //    POST /api/tracker
    // ----------------------------------------------------
    @PostMapping
    public ResponseEntity<Map<String, Object>> saveToday(@RequestBody TrackerRequest request) {

        Long userId = request.getUserId();
        Integer sleepHours = request.getSleepHours();
        String mood = request.getMood();
        Integer waterGlasses = request.getWaterGlasses();

        if (mood == null || mood.trim().isEmpty()) {
            mood = "ok";
        }

        Map<String, Object> res = new HashMap<>();

        if (userId == null) {
            res.put("success", false);
            res.put("message", "userId 가 필요합니다.");
            return ResponseEntity.badRequest().body(res);
        }

        LocalDate today = LocalDate.now();

        HealthTrackerEntry entry = healthTrackerRepository
                .findByUserIdAndEntryDate(userId, today)
                .orElseGet(() -> {
                    HealthTrackerEntry e = new HealthTrackerEntry();
                    e.setUserId(userId);
                    e.setEntryDate(today);
                    return e;
                });

        entry.setSleepHours(sleepHours);
        entry.setMood(mood);
        entry.setWaterGlasses(waterGlasses);

        healthTrackerRepository.save(entry);

        res.put("success", true);
        res.put("entryId", entry.getId());
        return ResponseEntity.ok(res);
    }

    // ----------------------------------------------------
    // 2) 서버 기준 "오늘" 데이터 조회
    //    GET /api/tracker/today?userId=1
    // ----------------------------------------------------
    @GetMapping("/today")
    public ResponseEntity<Map<String, Object>> getToday(@RequestParam(name="userId") Long userId){
        LocalDate today = LocalDate.now();

        Optional<HealthTrackerEntry> opt =
                healthTrackerRepository.findByUserIdAndEntryDate(userId, today);

        Map<String, Object> res = new HashMap<>();
        if (!opt.isPresent()) {
            res.put("hasEntry", false);
            return ResponseEntity.ok(res);
        }

        res.put("hasEntry", true);
        res.put("entry", opt.get());
        return ResponseEntity.ok(res);
    }

    // ----------------------------------------------------
    // 3) 특정 날짜 데이터 조회
    //    GET /api/tracker/day?userId=1&date=2026-01-07
    // ----------------------------------------------------
    @GetMapping("/day")
    public ResponseEntity<Map<String, Object>> getDay(
            @RequestParam("userId") Long userId,
            @RequestParam("date") String date
    ) {
        LocalDate targetDate = LocalDate.parse(date);

        Optional<HealthTrackerEntry> opt =
                healthTrackerRepository.findByUserIdAndEntryDate(userId, targetDate);

        Map<String, Object> res = new HashMap<>();
        if (!opt.isPresent()) {
            res.put("hasEntry", false);
            return ResponseEntity.ok(res);
        }

        res.put("hasEntry", true);
        res.put("entry", opt.get());
        return ResponseEntity.ok(res);
    }

    // ----------------------------------------------------
    // 4) 한 달 달력 데이터
    //    GET /api/tracker/month?userId=1&year=2026&month=1
    // ----------------------------------------------------
    @GetMapping("/month")
    public ResponseEntity<Map<String, Object>> getMonth(
            @RequestParam("userId") Long userId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();

        List<HealthTrackerEntry> entries =
                healthTrackerRepository.findByUserIdAndEntryDateBetween(userId, start, end);

        Set<LocalDate> datesWithEntry = new HashSet<>();
        for (HealthTrackerEntry e : entries) {
            if (e.getEntryDate() != null) {
                datesWithEntry.add(e.getEntryDate());
            }
        }

        List<Map<String, Object>> days = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("date", d.toString());
            dto.put("hasEntry", datesWithEntry.contains(d));
            days.add(dto);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("days", days);
        return ResponseEntity.ok(res);
    }

    // ----------------------------------------------------
    // 5) 지난 7일 요약 통계
    //    GET /api/tracker/summary/{userId}
    // ----------------------------------------------------
    @GetMapping("/summary/{userId}")
    public ResponseEntity<Map<String, Object>> getSummary(@PathVariable("userId") Long userId) {

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);

        List<HealthTrackerEntry> entries =
                healthTrackerRepository.findByUserIdAndEntryDateBetween(userId, start, end);

        Map<String, Object> res = new HashMap<>();
        if (entries.isEmpty()) {
            res.put("success", true);
            res.put("energyScore", 0);
            res.put("nutritionScore", 0);
            res.put("correlation", "LOW");
            return ResponseEntity.ok(res);
        }

        double totalEnergy = 0;
        double totalNutri = 0;
        int count = 0;

        for (HealthTrackerEntry e : entries) {
            int sleep = (e.getSleepHours() != null) ? e.getSleepHours() : 7;
            int water = (e.getWaterGlasses() != null) ? e.getWaterGlasses() : 10;
            String mood = (e.getMood() != null) ? e.getMood() : "ok";

            double moodFactor;
            if ("best".equals(mood))       moodFactor = 1.0;
            else if ("tired".equals(mood)) moodFactor = 0.6;
            else                           moodFactor = 0.8;

            double sleepScore = Math.max(0, 100 - Math.abs(8 - sleep) * 10);
            double waterScore = Math.min(100, water * 5);

            double energy = (sleepScore * 0.6) + (moodFactor * 100 * 0.4);
            double nutri  = (sleepScore * 0.4) + (waterScore * 0.6);

            totalEnergy += energy;
            totalNutri  += nutri;
            count++;
        }

        int avgEnergy = (int) Math.round(totalEnergy / count);
        int avgNutri  = (int) Math.round(totalNutri / count);

        String corr;
        int diff = Math.abs(avgEnergy - avgNutri);
        if (diff <= 10)      corr = "HIGH";
        else if (diff <= 25) corr = "MID";
        else                 corr = "LOW";

        res.put("success", true);
        res.put("energyScore", avgEnergy);
        res.put("nutritionScore", avgNutri);
        res.put("correlation", corr);

        return ResponseEntity.ok(res);
    }
}
