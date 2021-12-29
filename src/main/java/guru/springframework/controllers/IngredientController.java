package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable String recipeId, Model model) {
        log.debug("recipe id = {}", recipeId);
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredient/list";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String viewIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        log.debug("recipe id = {}", recipeId);
        log.debug("ingredient id = {}", ingredientId);

        model.addAttribute("ingredient", ingredientService.findByRecipeIdByIngredientId
                (Long.valueOf(recipeId), Long.valueOf(ingredientId)));

        return "recipe/ingredient/show";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
        log.debug("recipe id = {}", recipeId);
        log.debug("ingredient id to update = {}", ingredientId);

        model.addAttribute("ingredient", ingredientService.findByRecipeIdByIngredientId(Long.valueOf(recipeId), Long.valueOf(ingredientId)));
        model.addAttribute("uomList", unitOfMeasureService.listAllUom());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping
    @RequestMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable String recipeId, Model model) {
        log.info("recipe id = {}", recipeId);
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

        IngredientCommand ingredientCommand = new IngredientCommand();

        ingredientCommand.setRecipeId(Long.valueOf(recipeId));

        model.addAttribute("ingredient", ingredientCommand);
        //init uom
        ingredientCommand.setUnitOfMeasure(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllUom());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping
    @RequestMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

        log.info("I'm in the saveOrUpdate method in IngredientController");

        IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(command);

        return "redirect:/recipe/" + savedIngredient.getRecipeId() + "/ingredient/" + savedIngredient.getId() + "/show";

    }

}
