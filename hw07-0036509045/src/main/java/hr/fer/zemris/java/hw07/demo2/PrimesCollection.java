package hr.fer.zemris.java.hw07.demo2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Collection of first n primes.
 *
 * @author Jan Capek
 */
public class PrimesCollection implements Iterable<Integer> {

    private int n;

    /**
     * Constructs a new collections of prime numbers that holds first {@code n} prime numbers.
     *
     * @param n Number of primes in collection.
     * @throws IllegalArgumentException If given {@code n} is less than 1.
     */
    public PrimesCollection(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Number of primes in a collection cannot be less than 1.");
        }
        this.n = n;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new LocalIterator();
    }

    /**
     * Local iterator class that returns prime numbers in the collection.
     *
     * @author Jan Capek
     */
    private class LocalIterator implements Iterator<Integer> {

        private int elementsLeft = PrimesCollection.this.n;
        private int lastReturned;

        @Override
        public boolean hasNext() {
            return elementsLeft > 0;
        }

        @Override
        public Integer next() {
            if (hasNext() == false) {
                throw new NoSuchElementException("No more elements to return.");
            }
            elementsLeft--;
            return lastReturned = nextPrime(lastReturned);
        }

        /**
         * Calculates a next prime number that is larger than number given as an argument.
         *
         * @param number Number that returned prime should be bigger than.
         * @return Prime greater than given number.
         */
        private int nextPrime(int number) {
            if (number < 1) {
                return 2;
            }

            /*
            Calculate next possible number. If given number is even
            next possible is number + 1 else next possible is number + 2
            */
            int nextNumber = number % 2 == 0 ? number + 1 : number + 2;

            while (isPrime(nextNumber) == false) {
                nextNumber += 2;
            }
            return nextNumber;
        }

        /**
         * Checks if given number is prime.
         *
         * @param number Number to check.
         * @return {@code true} if number is a prime, {@code false} otherwise.
         */
        private boolean isPrime(int number) {
            if (number <= 1) {
                return false;
            }
            if (number == 2) {
                return true;
            }
            if (number % 2 == 0) {
                return false;
            }
            int limit = (int) Math.sqrt(number);
            for (int i = 3; i <= limit; i += 2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
    }
}
