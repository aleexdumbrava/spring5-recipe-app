package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImp implements ImageService {

    private final RecipeRepository recipeRepository;

    public ImageServiceImp(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImageFile(Long id, MultipartFile file) {

        log.debug("Inside save image file");

        Recipe recipe = recipeRepository.findById(id).get();
        int i = 0;
        try {
            Byte[] byteObjects = new Byte[file.getBytes().length];

            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            recipe.setImage(byteObjects);

            recipeRepository.save(recipe);

        } catch (IOException e) {

            log.error("Error occurred at save image file", e);
            e.printStackTrace();
        }



    }
}
