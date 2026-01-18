package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/planner")
@CrossOrigin(origins = "*")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/preview")
    public PlannerPlanDto preview(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "lang", defaultValue = "KO") String lang
    ) {
        return plannerService.generatePreview(userId, safeLang(lang));
    }

    @PostMapping("/save")
    public WeeklyPlanSummaryDto save(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "lang", defaultValue = "KO") String lang,
            @RequestBody PlannerPlanDto preview
    ) {
        plannerService.saveWeeklyPlan(userId, preview, safeLang(lang));
        return plannerService.getCurrentPlan(userId, safeLang(lang));
    }

    @GetMapping("/current")
    public WeeklyPlanSummaryDto current(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "lang", defaultValue = "KO") String lang
    ) {
        return plannerService.getCurrentPlan(userId, safeLang(lang));
    }

    private LanguageCode safeLang(String lang) {
        if (lang == null) return LanguageCode.KO;
        String v = lang.trim().toUpperCase();
        if (v.equals("KO") || v.equals("KR") || v.equals("KOREAN")) return LanguageCode.KO;
        if (v.equals("RU")) return LanguageCode.RU;
        if (v.equals("EN")) return LanguageCode.EN;
        try {
            return LanguageCode.valueOf(v);
        } catch (Exception e) {
            return LanguageCode.KO;
        }
    }
}
