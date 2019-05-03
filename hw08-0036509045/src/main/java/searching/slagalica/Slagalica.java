package searching.slagalica;

import searching.algorithms.Transition;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Instances of this class represent a puzzle.
 *
 * @author Jan Capek
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
        Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

    /**
     * Expected final result/goal.
     */
    private static final int[] goal = {1, 2, 3, 4, 5, 6, 7, 8, 0};

    /**
     * Starting puzzle state.
     */
    private KonfiguracijaSlagalice startingState;

    /**
     * Constructs a new puzzle with given starting state.
     *
     * @param startingState Puzzle's starting state.
     * @throws NullPointerException If given state is {@code null}.
     */
    public Slagalica(KonfiguracijaSlagalice startingState) {
        this.startingState = Objects.requireNonNull(startingState);
    }

    /**
     * @return Puzzle's starting state.
     */
    @Override
    public KonfiguracijaSlagalice get() {
        return startingState;
    }

    /**
     * Returns a list of the possible transitions of a puzzle.
     *
     * @param currentState Current state of the puzzle.
     * @return List of next possible states.
     * @throws NullPointerException If given state is {@code null}.
     */
    @Override
    public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice currentState) {
        Objects.requireNonNull(currentState);

        int zeroIndex = currentState.indexOfSpace();
        int zeroY = zeroIndex / 3;
        int zeroX = zeroIndex % 3;

        int[] nextX = {0, 1, 0, -1};
        int[] nextY = {1, 0, -1, 0};

        List<Transition<KonfiguracijaSlagalice>> list = new ArrayList<>(4);

        for(int i = 0; i < 4; i++) {
            int x = zeroX + nextX[i];
            int y = zeroY + nextY[i];
            int nextZeroIndex = y * 3 + x;
            if(inBounds(x, y)) {
                int[] nextConfig = currentState.getPolje().clone();
                swap(nextConfig, nextZeroIndex, zeroIndex);
                list.add(new Transition<>(new KonfiguracijaSlagalice(nextConfig), 1));
            }
        }
        return list;
    }

    /**
     * Swaps two values in the array.
     *
     * @param array Array whose values need to be swapped.
     * @param ind1 Index of the first element.
     * @param ind2 Index of the second element.
     * @throws ArrayIndexOutOfBoundsException If indexes are not valid.
     * @throws NullPointerException If given array is {@code null}.
     */
    private static void swap(int[] array, int ind1, int ind2) {
        Objects.requireNonNull(array);
        int temp = array[ind1];
        array[ind1] = array[ind2];
        array[ind2] = temp;
    }

    /**
     * Checks if coordinates are in bounds.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return {@code true} if coordinates are in bounds, {@code false} otherwise.
     */
    private static boolean inBounds(int x, int y) {
        if(x < 0 || x >= 3) {
            return false;
        }
        if (y < 0 || y >= 3) {
            return false;
        }
        return true;
    }

    /**
     * Tests if a puzzle is solved.
     *
     * @param state Current puzzle state.
     * @return {@code true} if puzzle is solved, {@code false} otherwise.
     * @throws NullPointerException If given state is {@code null}.
     */
    @Override
    public boolean test(KonfiguracijaSlagalice state) {
        Objects.requireNonNull(state);
        return Arrays.equals(goal, state.getPolje());
    }
}
