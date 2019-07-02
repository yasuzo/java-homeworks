package com.yasuzo.search;

import java.util.*;

/**
 * This class contains vocabulary for searches.
 *
 * @author Jan Capek
 */
public class Vocabulary {

    /**
     * Set of stop words that should be ignored in search.
     */
    private Set<String> stopWords = new HashSet<>();

    /**
     * Map containing word-index pairs. Index represents an i-th coordinate of an n-dimensional search vector.
     */
    private Map<String, Integer> vocabulary = new HashMap<>();



    /**
     * Registers a stop word.
     *
     * @param word Stop word that needs to be registered.
     * @throws NullPointerException If given word is {@code null}.
     */
    public void registerStopWord(String word) {
        Objects.requireNonNull(word);
        stopWords.add(word);
    }

    /**
     * Registers a word as vocabulary.
     *
     * @param word New word.
     * @return Index of the word in a vocabulary vector.
     * @throws NullPointerException If given word is {@code null}.
     */
    public int registerWord(String word) {
        Objects.requireNonNull(word);
        Integer vectorIndex = vocabulary.putIfAbsent(word, vocabulary.size());
        return vectorIndex == null ? vocabulary.size() - 1 : vectorIndex;
    }

    /**
     * Checks if given word is a stop word.
     *
     * @param word Word that needs to be checked.
     * @return {@code true} if given word is stop word, {@code false} otherwise.
     */
    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }

    /**
     * Returns an index of the word in a vocabulary vector.
     *
     * @param word Word which index is wanted.
     * @return Index of the given word in a vocabulary vector or {@code null} if the word is not in vocabulary.
     */
    public Integer getIndexInVector(String word) {
        return vocabulary.get(word);
    }

    /**
     * @return Number of non-stopping words in the vocabulary.
     */
    public int wordCount() {
        return vocabulary.size();
    }
}
