package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    // ✅ 오늘 식단
    @GetMapping("/today")
    public HomeTodayDto today(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "lang", defaultValue = "KO") String lang
    ) {
        LanguageCode language = LanguageCode.valueOf(lang.toUpperCase());
        return homeService.getToday(userId, language);
    }

    // ✅ 특정 날짜 식단
    @GetMapping("/by-date")
    public HomeTodayDto byDate(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "date") String date,
            @RequestParam(name = "lang", defaultValue = "KO") String lang
    ) {
        LocalDate d = LocalDate.parse(date);
        LanguageCode language = LanguageCode.valueOf(lang.toUpperCase());
        return homeService.getByDate(userId, d, language);
    }
}
