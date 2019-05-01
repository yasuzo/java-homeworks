package hr.fer.zemris.java.hw06.shell.commands.massrename_util;

import java.util.Objects;

/**
 * Functional interface for building a file name with regex.
 *
 * @author Jan Capek
 */
public interface NameBuilder {

    /**
     * Fills a string buffer with a result of execution.
     *
     * @param result Filtered result that holds a file.
     * @param sb String builder that should be filled.
     * @throws NullPointerException If any of the arguments are {@code null}.
     * @throws RuntimeException If other implementations throw an exception.
     */
    void execute(FilterResult result, StringBuilder sb);

    /**
     * Returns a composed name builder.
     *
     * @param other Name builder whose {@link NameBuilder#execute(FilterResult, StringBuilder)}
     *              method will be called after it is called on this.
     * @return Composed name builder.
     * @throws NullPointerException If other is {@code null}.
     */
    default NameBuilder then(NameBuilder other) {
        Objects.requireNonNull(other);
        return (result, sb) -> {
            this.execute(result, sb);
            other.execute(result, sb);
        };
    }
}
