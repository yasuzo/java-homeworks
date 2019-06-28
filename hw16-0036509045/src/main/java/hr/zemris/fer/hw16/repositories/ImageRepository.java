package hr.zemris.fer.hw16.repositories;

import hr.zemris.fer.hw16.models.ImageModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Interface exposing methods for fetching {@link ImageModel}.
 *
 * @author Jan Capek
 */
public interface ImageRepository {

    /**
     * @return All images in repository.
     * @throws RepositoryException In case of an error while fetching data.
     */
    List<ImageModel> findAll();

    /**
     * Returns all images that have given tag.
     *
     * @param tag Image tag used to filter images.
     * @return List of images containing given tag.
     * @throws NullPointerException If the tag is {@code null}.
     * @throws RepositoryException In case of an error while fetching data.
     */
    default List<ImageModel> findWithTag(String tag) {
        Objects.requireNonNull(tag);
        return findAll().stream()
                .filter(img -> img.getTags().contains(tag))
                .collect(Collectors.toList());
    }

    /**
     * Returns an image with given name if one exists.
     *
     * @param name Image name.
     * @return Image with given name or {@code null} if no image was found.
     * @throws RepositoryException In case of an error while fetching data.
     */
    default ImageModel findByName(String name) {
        for (ImageModel img : findAll()) {
            if (img.getName().equals(name)) {
                return img;
            }
        }
        return null;
    }

    /**
     * @return All currently available tags.
     * @throws RepositoryException In case of an error while fetching data.
     */
    default Collection<String> getAvailableTags() {
        Set<String> result = new HashSet<>();
        findAll().forEach(img -> result.addAll(img.getTags()));
        return result;
    }
}
