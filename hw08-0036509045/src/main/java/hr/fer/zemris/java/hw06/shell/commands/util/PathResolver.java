package hr.fer.zemris.java.hw06.shell.commands.util;

import hr.fer.zemris.java.hw06.shell.Environment;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Class containing static methods for relative path resolving.
 *
 * @author Jan Capek
 */
public class PathResolver {

    /**
     * If given path is absolute, the same path will be returned.
     * If path is relative, it will be resolved to absolute path relative to current working directory of the environment.
     *
     * @param env Environment whose working directory a path should be relative to.
     * @param p Path that needs to be resolved.
     * @return Resolved path.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public static Path resolveRelativePath(Environment env, Path p) {
        Objects.requireNonNull(env);
        Objects.requireNonNull(p);

        if(p.isAbsolute()) {
            return p;
        }

        return Path.of(env.getCurrentDirectory().toString(), p.toString());
    }
}
