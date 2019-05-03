package searching.algorithms;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Class containing search algorithms.
 *
 * @author Jan Capek
 */
public class SearchUtil {

    /**
     * Breath-first search algorithm for puzzle solving.
     *
     * @param s0   Supplier of a starting state.
     * @param succ Function that will return a list of all possible transitions from current state.
     * @param goal Predicate that will test if a goal has been reached.
     * @param <S>  Type of states this will work with.
     * @return Final node that has reached a goal or {@code null} if a goal couldn't be reached.
     * @throws NullPointerException If any of the arguments are {@code null};
     */
    public static <S> Node<S> bfs(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
        Objects.requireNonNull(s0);
        Objects.requireNonNull(succ);
        Objects.requireNonNull(goal);

        List<Node<S>> explorable = new LinkedList<>();
        explorable.add(new Node<>(null, s0.get(), 0));

        while (explorable.isEmpty() == false) {
            Node<S> node = explorable.remove(0);
            if (goal.test(node.getState())) {
                return node;
            }
            succ.apply(node.getState()).forEach(trans -> {
                explorable.add(new Node<>(node, trans.getState(), node.getCost() + trans.getCost()));
            });
        }
        return null;
    }

    /**
     * Better breath-first search algorithm for puzzle solving. It cannot be stuck in endless loop.
     *
     * @param s0   Supplier of a starting state.
     * @param succ Function that will return a list of all possible transitions from current state.
     * @param goal Predicate that will test if a goal has been reached.
     * @param <S>  Type of states this will work with.
     * @return Final node that has reached a goal or {@code null} if a goal couldn't be reached.
     * @throws NullPointerException If any of the arguments are {@code null};
     */
    public static <S> Node<S> bfsv(Supplier<S> s0, Function<S, List<Transition<S>>> succ, Predicate<S> goal) {
        Objects.requireNonNull(s0);
        Objects.requireNonNull(succ);
        Objects.requireNonNull(goal);

        List<Node<S>> explorable = new LinkedList<>();
        Set<S> pendingProcessing = new HashSet<>();

        S startingState = s0.get();
        explorable.add(new Node<>(null, startingState, 0));
        pendingProcessing.add(startingState);

        while (explorable.isEmpty() == false) {
            Node<S> node = explorable.remove(0);
            if (goal.test(node.getState())) {
                return node;
            }
            succ.apply(node.getState()).stream()
                    .filter(trans -> pendingProcessing.contains(trans.getState()) == false)
                    .forEach(trans -> {
                        explorable.add(new Node<>(node, trans.getState(), node.getCost() + trans.getCost()));
                        pendingProcessing.add(trans.getState());
                    });
        }
        return null;
    }

}
