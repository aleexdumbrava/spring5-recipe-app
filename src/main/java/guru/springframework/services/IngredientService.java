package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;

import java.util.Set;

public interface IngredientService {

    Set<Ingredient> getIngredients();
    Ingredient findById(Long id);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    IngredientCommand findCommandById(Long id);
    void deleteById(Long idToDelete);
    Ingredient findByRecipeIdByIngredientId(Long recipeId, Long ingredientId);
}
