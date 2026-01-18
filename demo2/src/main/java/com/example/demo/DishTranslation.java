package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "dish_translations")
public class DishTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @Enumerated(EnumType.STRING)
    private LanguageCode language;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    // --- getters/setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Dish getDish() { return dish; }
    public void setDish(Dish dish) { this.dish = dish; }

    public LanguageCode getLanguage() { return language; }
    public void setLanguage(LanguageCode language) { this.language = language; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
