package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest {

    private ImageService imageService;

    @Mock
    private RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        imageService = new ImageServiceImpl(recipeRepository);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(recipeRepository);
    }

    @Test
    public void saveImageFile() throws Exception {
        final MockMultipartFile multipartFile =
                new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Test Info".getBytes());

        final Recipe recipe = new Recipe();
        recipe.setId(1L);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(eq(1L))).thenReturn(recipeOptional);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImageFile(1L, multipartFile);

        verify(recipeRepository).findById(eq(1L));

        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        final Recipe recipeArgumentCaptorValue = recipeArgumentCaptor.getValue();

        assertEquals(multipartFile.getBytes().length, recipeArgumentCaptorValue.getImage().length);
    }

}