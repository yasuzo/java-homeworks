package searching.slagalica;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Object holding a state of a puzzle.
 *
 * @author Jan Capek
 */
public class KonfiguracijaSlagalice {
    private int[] conf;

    /**
     * Constructs a new state of the puzzle.
     *
     * @param conf Puzzle configuration.
     * @throws NullPointerException     If given configuration is {@code null}.
     * @throws IllegalArgumentException If given configuration does not contain all necessary numbers.
     *                                  Config should contain all numbers from 0 to 8 (inclusive) and have exactly 9 entries.
     */
    public KonfiguracijaSlagalice(int[] conf) {
        Objects.requireNonNull(conf);
        if (configOk(conf) == false) {
            throw new IllegalArgumentException("Configuration array does not contain all necessary numbers.");
        }
        this.conf = conf;
    }

    /**
     * Checks if all necessary numbers are present in the configuration array and if the config array is of valid size.
     *
     * @param conf Array that should be checked.
     * @return {@code true} if configuration is valid, {@code false} otherwise.
     * @throws NullPointerException If given configuration is {@code null}.
     */
    private boolean configOk(int[] conf) {
        Objects.requireNonNull(conf);
        Set<Integer> requiredNumbersInConf = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
        if (requiredNumbersInConf.size() != conf.length) {
            return false;
        }
        for (int num : conf) {
            if (requiredNumbersInConf.remove(num) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Configuration array of the state.
     */
    public int[] getPolje() {
        return conf;
    }

    /**
     * @return Index of '0' in state configuration.
     */
    public int indexOfSpace() {
        for (int i = 0; i < conf.length; i++) {
            if (conf[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KonfiguracijaSlagalice that = (KonfiguracijaSlagalice) o;
        return Arrays.equals(conf, that.conf);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(conf);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < conf.length; i++) {
            if (conf[i] == 0) {
                sb.append('*');
            } else {
                sb.append(conf[i]);
            }
            sb.append(' ');
            if (i % 3 == 2) {
                sb.append('\n');
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
