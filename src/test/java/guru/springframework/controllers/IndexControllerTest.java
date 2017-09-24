package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class IndexControllerTest {

    private IndexController indexController;

    @Mock
    private RecipeService recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        indexController = new IndexController(recipeService);
    }

    @Test
    public void getIndexPage() throws Exception {
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        Recipe secondRecipe = new Recipe();
        secondRecipe.setDescription("second recipe");

        recipes.add(secondRecipe);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        final String indexPage = indexController.getIndexPage(model);

        assertEquals("index", indexPage);

        verify(recipeService).getRecipes();

        verify(model).addAttribute(eq("recipes"), argumentCaptor.capture());

        final Set<Recipe> capture = argumentCaptor.getValue();

        assertEquals(2, capture.size());

        verifyNoMoreInteractions(recipeService, model);
    }

}