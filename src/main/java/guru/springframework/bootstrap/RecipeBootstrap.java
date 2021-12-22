package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@Transactional
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private RecipeRepository recipeRepository;
    private CategoryRepository categoryRepository;
    private UnitOfMeasureRepository unitOfMeasureRepository;


    public RecipeBootstrap(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("recipes = {}", getRecipes());
        recipeRepository.saveAll(getRecipes());
    }

    private Set<Recipe> getRecipes() {
        Set<Recipe> recipes = new HashSet<>();
        // Unit of measures :

        Optional<UnitOfMeasure> teaSpoonOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        UnitOfMeasure teaSpoonUom = teaSpoonOptional.get();

        Optional<UnitOfMeasure> tableSpoonOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        UnitOfMeasure tableSpoonUom = tableSpoonOptional.get();

        Optional<UnitOfMeasure> cupOptional = unitOfMeasureRepository.findByDescription("Cup");
        UnitOfMeasure cupUom = cupOptional.get();

        Optional<UnitOfMeasure> pinchOptional = unitOfMeasureRepository.findByDescription("Pinch");
        UnitOfMeasure pinchUom = pinchOptional.get();

        Optional<UnitOfMeasure> ounceOptional = unitOfMeasureRepository.findByDescription("Ounce");
        UnitOfMeasure ounceUom = ounceOptional.get();

        Optional<UnitOfMeasure> eachOptional = unitOfMeasureRepository.findByDescription("Each");
        UnitOfMeasure eachUom = eachOptional.get();

        Optional<UnitOfMeasure> dashOptional = unitOfMeasureRepository.findByDescription("Dash");
        UnitOfMeasure dashUom = dashOptional.get();

        Optional<UnitOfMeasure> pintOptional = unitOfMeasureRepository.findByDescription("Pint");
        UnitOfMeasure pintUom = pintOptional.get();


        Recipe perfectGuacamole = new Recipe();
        perfectGuacamole.setDescription("Perfect Guacamole");
        perfectGuacamole.setCookTime(0);
        perfectGuacamole.setPrepTime(10);
        perfectGuacamole.setDifficulty(Difficulty.EASY);
        perfectGuacamole.setServings(3);
        perfectGuacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        perfectGuacamole.setSource("Elise Bauer");
        Notes perfectGuacamoleNotes = new Notes();
        perfectGuacamoleNotes.setRecipeNotes("Be careful handling chilis! If using, it's best to wear food-safe gloves. " +
                "If no gloves are available, wash your hands thoroughly after handling, " +
                "and do not touch your eyes or the area near your eyes for several hours afterwards.");
        perfectGuacamole.setNotes(perfectGuacamoleNotes);
        perfectGuacamole.setDirections("Cut the avocado, Mash the avocado flesh, Add remaining ingredients to taste, Serve immediately");
        Optional<Category> optionalCategoryMexican = categoryRepository.findByDescription("Mexican");
        Category categoryMexican= optionalCategoryMexican.get();
        perfectGuacamole.getCategories().add(categoryMexican);
        categoryMexican.getRecipes().add(perfectGuacamole);
        Ingredient ripeAvocado = new Ingredient("ripe avocados", new BigDecimal(2), perfectGuacamole, eachUom);
        Ingredient salt = new Ingredient("salt", new BigDecimal(1/4), perfectGuacamole, teaSpoonUom);
        Ingredient freshLime = new Ingredient("fresh lime", new BigDecimal(1), perfectGuacamole, tableSpoonUom);
        Ingredient onion = new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(4), perfectGuacamole, tableSpoonUom);
        Ingredient jalapeno = new Ingredient("serrano (or jalapeño) chilis, stems and seeds removed, minced", new BigDecimal(2),
                perfectGuacamole, eachUom);
        Ingredient cilantro = new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2),
                perfectGuacamole, tableSpoonUom);
        Ingredient blackPepper = new Ingredient("freshly ground black pepper", new BigDecimal(1), perfectGuacamole, pinchUom);
        Ingredient ripeTomato = new Ingredient("ripe tomato, chopped (optional)", new BigDecimal(1/2), perfectGuacamole, eachUom);
        Ingredient redRadish = new Ingredient("Red radish or jicama slices for garnish (optional)", new BigDecimal(1),
                perfectGuacamole, eachUom);

        perfectGuacamole.addIngredient(ripeAvocado);
        perfectGuacamole.addIngredient(salt);
        perfectGuacamole.addIngredient(freshLime);
        perfectGuacamole.addIngredient(onion);
        perfectGuacamole.addIngredient(jalapeno);
        perfectGuacamole.addIngredient(cilantro);
        perfectGuacamole.addIngredient(blackPepper);
        perfectGuacamole.addIngredient(ripeTomato);
        perfectGuacamole.addIngredient(redRadish);

        recipes.add(perfectGuacamole);

        Recipe spicyGrilledChicken = new Recipe();
        spicyGrilledChicken.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        spicyGrilledChicken.setSource("Sally Vargas");
        spicyGrilledChicken.setDescription("Spicy Grilled Chicken");
        spicyGrilledChicken.setPrepTime(20);
        spicyGrilledChicken.setCookTime(15);
        spicyGrilledChicken.setServings(4);
        Notes spicyGrilledChickenNotes = new Notes(spicyGrilledChicken, "Look for ancho chile powder with the Mexican ingredients at your grocery store, on buy it online. " +
                "(If you can't find ancho chili powder, you replace the ancho chili, the oregano, " +
                "and the cumin with 2 1/2 tablespoons regular chili powder, though the flavor won't be quite the same.)");
        spicyGrilledChicken.setNotes(spicyGrilledChickenNotes);
        spicyGrilledChicken.setDifficulty(Difficulty.MODERATE);
        Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");
        Category americanCategory = americanCategoryOptional.get();
        spicyGrilledChicken.getCategories().add(americanCategory);
        americanCategory.getRecipes().add(spicyGrilledChicken);
        spicyGrilledChicken.setDirections("Prepare a gas or charcoal grill for medium-high, direct heat, Make the marinade and coat the chicken, " +
                "Grill the chicken, Warm the tortillas, Assemble the tacos");
        Ingredient chiliPowder = new Ingredient("ancho chili powder", new BigDecimal(2), spicyGrilledChicken, tableSpoonUom);
        Ingredient oregano = new Ingredient("dried oregano", new BigDecimal(1), spicyGrilledChicken, teaSpoonUom);
        Ingredient cumin = new Ingredient("dried cumin", new BigDecimal(1), spicyGrilledChicken, teaSpoonUom);
        Ingredient sugar = new Ingredient("sugar", new BigDecimal(1), spicyGrilledChicken, teaSpoonUom);
        Ingredient saltChicken = new Ingredient("salt", new BigDecimal(1/2), spicyGrilledChicken, teaSpoonUom);
        Ingredient garlic = new Ingredient("clove garlic, finely chopped", new BigDecimal(1), spicyGrilledChicken, eachUom);
        Ingredient orangeZest = new Ingredient("finely grated orange zest", new BigDecimal(1), spicyGrilledChicken, tableSpoonUom);
        Ingredient orangeJuice = new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), spicyGrilledChicken, tableSpoonUom);
        Ingredient oliveOil = new Ingredient("olive oil", new BigDecimal(2), spicyGrilledChicken, tableSpoonUom);
        Ingredient chickenThighs = new Ingredient("skinless, boneless chicken thighs (1 1/4 pounds)", new BigDecimal(4), spicyGrilledChicken, eachUom);

        spicyGrilledChicken.addIngredient(chiliPowder);
        spicyGrilledChicken.addIngredient(cumin);
        spicyGrilledChicken.addIngredient(oregano);
        spicyGrilledChicken.addIngredient(sugar);
        spicyGrilledChicken.addIngredient(saltChicken);
        spicyGrilledChicken.addIngredient(garlic);
        spicyGrilledChicken.addIngredient(orangeZest);
        spicyGrilledChicken.addIngredient(orangeJuice);
        spicyGrilledChicken.addIngredient(oliveOil);
        spicyGrilledChicken.addIngredient(chickenThighs);

        recipes.add(spicyGrilledChicken);


        return recipes;

    }
}
