package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.IngredientRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository,
                                 RecipeRepository recipeRepository, IngredientCommandToIngredient ingredientCommandToIngredient,
                                 IngredientToIngredientCommand ingredientToIngredientCommand, UnitOfMeasureRepository unitOfMeasureRepository) {

        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
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
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand command) {
        log.info("Ingredient command id = {}", command.getId());
        Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());
        if(!recipeOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUnitOfMeasure(unitOfMeasureRepository
                        .findById(command.getUnitOfMeasure().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
                log.info("ingredient update amount = {}", ingredientFound.getAmount());
                log.info("ingredient update description = {}", ingredientFound.getDescription());
            } else {
                //add new Ingredient
                Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                assert ingredient != null;
                ingredient.setRecipe(recipe);
                log.info("ingredient id = {}", ingredient.getId());
                recipe.addIngredient(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream().filter(recipeIngredients ->
                    recipeIngredients.getId().equals(command.getId())).findFirst();

            //to do check for fail
            if (!savedIngredientOptional.isPresent()) {
                savedIngredientOptional = savedRecipe.getIngredients().stream().filter(recipeIngredients ->
                        recipeIngredients.getUnitOfMeasure().getId().equals(command.getUnitOfMeasure().getId())).
                        filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount())).
                        filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription())).findFirst();
            }
            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }

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
