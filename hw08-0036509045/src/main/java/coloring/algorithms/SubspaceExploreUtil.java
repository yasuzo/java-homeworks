package coloring.algorithms;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class containing algorithms for exploring subspace.
 *
 * @author Jan Capek
 */
public class SubspaceExploreUtil {

    /**
     * Breath-first search algorithm for subspace exploring.
     *
     * @param s0         Item supplier.
     * @param process    Item processor.
     * @param succ       Function that returns new items that need to be processed based on current item.
     * @param acceptable Predicate that tests acceptability of an item.
     * @param <S>        Type of items that need to be explored.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public static <S> void bfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ, Predicate<S> acceptable) {
        Objects.requireNonNull(s0);
        Objects.requireNonNull(process);
        Objects.requireNonNull(succ);
        Objects.requireNonNull(acceptable);

        List<S> explorable = new LinkedList<>();
        explorable.add(s0.get());

        while (explorable.isEmpty() == false) {
            S item = explorable.remove(0);
            if (acceptable.test(item)) {
                process.accept(item);
                explorable.addAll(succ.apply(item));
            }
        }
    }

    /**
     * Better breath-first search algorithm for subspace exploring.
     *
     * @param s0         Item supplier.
     * @param process    Item processor.
     * @param succ       Function that returns new items that need to be processed based on current item.
     * @param acceptable Predicate that tests acceptability of an item.
     * @param <S>        Type of items that need to be explored.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public static <S> void bfsv(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ, Predicate<S> acceptable) {
        Objects.requireNonNull(s0);
        Objects.requireNonNull(process);
        Objects.requireNonNull(succ);
        Objects.requireNonNull(acceptable);

        List<S> explorable = new LinkedList<>();
        Set<S> pendingProcessing = new HashSet<>();

        S startingItem = s0.get();
        explorable.add(startingItem);
        pendingProcessing.add(startingItem);

        while (explorable.isEmpty() == false) {
            S item = explorable.remove(0);
            if (acceptable.test(item) == false) {
                continue;
            }
            process.accept(item);
            List<S> newNeighbours = succ.apply(item).stream()
                    .filter(it -> pendingProcessing.contains(it) == false)
                    .collect(Collectors.toList());
            explorable.addAll(newNeighbours);
            pendingProcessing.addAll(newNeighbours);
        }
    }

    /**
     * Depth-first search algorithm for subspace exploring.
     *
     * @param s0         Item supplier.
     * @param process    Item processor.
     * @param succ       Function that returns new items that need to be processed based on current item.
     * @param acceptable Predicate that tests acceptability of an item.
     * @param <S>        Type of items that need to be explored.
     * @throws NullPointerException If any of the arguments are {@code null}.
     */
    public static <S> void dfs(Supplier<S> s0, Consumer<S> process, Function<S, List<S>> succ, Predicate<S> acceptable) {
        Objects.requireNonNull(s0);
        Objects.requireNonNull(process);
        Objects.requireNonNull(succ);
        Objects.requireNonNull(acceptable);

        List<S> explorable = new LinkedList<>();
        explorable.add(s0.get());

        while (explorable.isEmpty() == false) {
            S item = explorable.remove(0);
            if (acceptable.test(item)) {
                process.accept(item);
                explorable.addAll(0, succ.apply(item));
            }
        }
    }
}
