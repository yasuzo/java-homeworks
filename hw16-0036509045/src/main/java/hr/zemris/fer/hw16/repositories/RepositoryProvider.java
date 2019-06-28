package hr.zemris.fer.hw16.repositories;

import java.util.Objects;

/**
 * Provider of repositories.
 *
 * @author Jan Capek
 */
public class RepositoryProvider {

    private static ImageRepository imageRepository;

    /**
     * @return Image repository.
     * @throws IllegalStateException If image repository has not been set.
     */
    public static ImageRepository getImageRepository() {
        if (imageRepository == null) {
            throw new IllegalStateException("Image repository not set.");
        }
        return imageRepository;
    }

    /**
     * Sets an instance of {@link ImageRepository} that will be provided
     * on {@link RepositoryProvider#getImageRepository()} method call.
     *
     * @param imageRepository Image repository.
     * @throws IllegalStateException If repository has already been set.
     */
    public static void setImageRepository(ImageRepository imageRepository) {
        if (RepositoryProvider.imageRepository != null) {
            throw new IllegalStateException("ImageRepository has already been set.");
        }
        RepositoryProvider.imageRepository = Objects.requireNonNull(imageRepository);
    }
}
