package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredient_translations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"ingredient_id", "language"}))
public class IngredientTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Enumerated(EnumType.STRING)
    private LanguageCode language;

    private String name;

    public Long getId() { return id; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public LanguageCode getLanguage() { return language; }
    public void setLanguage(LanguageCode language) { this.language = language; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
