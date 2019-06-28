package hr.zemris.fer.hw16.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model of an image.
 *
 * @author Jan Capek
 */
public class ImageModel {
    private String name;
    private String description;
    private List<String> tags;

    /**
     * Constructs a new ImageModel with given attributes.
     *
     * @param name Name of the image (including an extension).
     * @param description Description of the image.
     * @param tags Array of tags for the image. Array can be {@code null}.
     * @throws NullPointerException If either name or any of the tags is {@code null}.
     */
    public ImageModel(String name, String description, String... tags){
        this.name = Objects.requireNonNull(name);
        this.description = description;

//        fill tags list
        if (tags == null) {
            return;
        }
        this.tags = new ArrayList<>(tags.length);
        for (String tag : tags) {
            Objects.requireNonNull(tag); // throw if any of the tags is null
            this.tags.add(tag);
        }
    }

    /**
     * @return Image name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Image description. Might be {@code null} if there is no description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return List of image tags.
     */
    public List<String> getTags() {
        return tags;
    }
}
