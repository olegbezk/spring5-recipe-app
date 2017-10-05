package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private final RecipeRepository recipeRepository;

    @Autowired
    public IngredientServiceImpl(
            final IngredientToIngredientCommand ingredientToIngredientCommand,
            final RecipeRepository recipeRepository
    ) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(final Long recipeId, final Long ingredientId) {
        final Optional<Recipe> byId = recipeRepository.findById(recipeId);

        if(!byId.isPresent()) {
            //todo impl error handling
            log.error("Recipe with id:{} not found.", recipeId);
        }

        final Optional<IngredientCommand> ingredientCommand = byId.get().getIngredients().stream()
                .filter(i -> i.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if(!ingredientCommand.isPresent()){
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        return ingredientCommand.get();
    }
}
