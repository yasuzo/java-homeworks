package hr.zemris.fer.hw16.repositories;

import hr.zemris.fer.hw16.models.ImageModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link ImageRepository} which uses a file on the disk as data source.
 *
 * @author Jan Capek
 */
public class ImageFileRepository implements ImageRepository {

    private Path dataSource;

    /**
     * Constructs a new repository with given data source.
     *
     * @param dataSource Path to a new file data source.
     * @throws NullPointerException If the data source is {@code null}.
     */
    public ImageFileRepository(Path dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource);
    }

    @Override
    public List<ImageModel> findAll() {
        List<String> lines;
        try {
            lines = Files.readAllLines(dataSource);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }

//        there should be 3 lines for each of the images -> if not throw exception
        if (lines.size() % 3 != 0) {
            throw new RepositoryException("Data source error: Expected 3 lines for every image in the source file.");
        }

//        fill result list and return it
        List<ImageModel> result = new ArrayList<>(lines.size() / 3);
        Iterator<String> it = lines.iterator();
        for (int i = lines.size() / 3; i > 0; i--) {
            String name = it.next().trim();
            String description = it.next().trim();
            String[] tags = it.next().trim().split(", ");
            ImageModel img = new ImageModel(name, description, tags);
            result.add(img);
        }
        return result;
    }
}
