package com.example.demo.controller;

import com.example.demo.dto.ShoppingResponseDto;
import com.example.demo.service.ShoppingService;
import com.example.demo.dto.ShoppingToggleRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @GetMapping
    public ShoppingResponseDto get(@RequestParam Long userId,
                                   @RequestParam(defaultValue = "KO") String lang) {
        return shoppingService.getCurrentWeekShopping(userId, lang);
    }

    @PostMapping("/toggle")
    public boolean toggle(@RequestBody ShoppingToggleRequest req) {
        return shoppingService.toggleCheck(req.getWeeklyPlanId(), req.getIngredientId());
    }
}
