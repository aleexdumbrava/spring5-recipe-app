package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient,
                                 IngredientToIngredientCommand ingredientToIngredientCommand) {

        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    }

    @Override
    public Set<Ingredient> getIngredients() {
        Set<Ingredient> ingredients = new HashSet<>();

        ingredientRepository.findAll().iterator().forEachRemaining(ingredients :: add);

        return ingredients;
    }

    @Override
    public Ingredient findById(Long id) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);

        return ingredientOptional.orElse(null);

    }

    @Override
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        Ingredient detachedIngredient = ingredientCommandToIngredient.convert(command);

        assert detachedIngredient != null;
        Ingredient savedIngredient = ingredientRepository.save(detachedIngredient);

        return ingredientToIngredientCommand.convert(savedIngredient);
    }

    @Override
    public IngredientCommand findCommandById(Long id) {
        return ingredientToIngredientCommand.convert(findById(id));
    }

    @Override
    public void deleteById(Long idToDelete) {
        ingredientRepository.deleteById(idToDelete);
    }

    @Override
    public Ingredient findByRecipeIdByIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            return null;
        }

        Recipe recipe = recipeOptional.get();

        Ingredient ingredient = null;

        for (Ingredient ingr : recipe.getIngredients()) {
            if (ingr.getId().equals(ingredientId)) {
                ingredient = ingr;
                break;
            }
        }

        return ingredient;
    }
}
