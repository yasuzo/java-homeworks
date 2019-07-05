package com.yasuzo.search;

import com.yasuzo.math.NVector;

import java.util.*;

/**
 * Objects of this class are used to create document vectors and offer methods for finding similar documents.
 *
 * @author Jan Capek
 */
public class SearchEngine {

    private Vocabulary vocabulary;

    /**
     * Map of term frequency document vectors. Keys of this map are names/paths to documents.
     */
    private Map<String, NVector<Integer>> tfDocumentVectors;

    /**
     * Map of term frequency-inverse document frequency document vectors. Keys of this map are names/paths to documents.
     */
    private Map<String, NVector<Double>> tfIdfDocumentVectors;

    /**
     * Idf vector used for search.
     */
    private NVector<Double> idfVector;

    /**
     * Vector holding frequencies of all words in all documents.
     */
    private NVector<Integer> allDocsTermFreq;

    /**
     * Flag that indicates if cache is valid.
     */
    private boolean cacheValid;

    /**
     * List of the most recent words used in a query.
     */
    private List<String> queryWords;

    /**
     * Constructs a new document search engine.
     *
     * @param vocabulary Vocabulary used by the engine.
     * @throws NullPointerException If given vocabulary is {@code null}.
     */
    public SearchEngine(Vocabulary vocabulary) {
        this.vocabulary = Objects.requireNonNull(vocabulary);
        allDocsTermFreq = new NVector<>(2000);
        tfDocumentVectors = new HashMap<>();
    }

    /**
     * Returns a list of words in given string.
     *
     * @param content String whose words need to be returned.
     * @return List of words in given string.
     * @throws NullPointerException If given string is {@code null}.
     */
    private static List<String> getWords(String content) {
        content = content.toLowerCase();
        char[] data = content.toCharArray();
        List<String> result = new ArrayList<>(data.length / 6);
        for (int i = 0; i < data.length; i++) {
            if (Character.isAlphabetic(data[i]) == false) {
                continue;
            }
            int start = i;
            while (i < data.length && Character.isAlphabetic(data[i])) {
                i++;
            }
            result.add(String.valueOf(data, start, i - start));
        }
        return result;
    }

    /**
     * Adds a document that needs to be searchable.
     *
     * @param documentKey Key under which document's vocabulary vector will be stored.
     * @param content     Content of the document.
     * @throws NullPointerException If any of the arguments is {@code null}.
     */
    public void addDocument(String documentKey, String content) {
        Objects.requireNonNull(documentKey);
        Objects.requireNonNull(content);

        NVector<Integer> docVector = new NVector<>((int) Math.max(2000, allDocsTermFreq.numberOfDimensions() * 1.5));

        getWords(content).forEach(word -> {
            if (vocabulary.isStopWord(word)) {
                return;
            }
            int index = vocabulary.registerWord(word);
            docVector.setCoordinateAt(index, 0, val -> val + 1);
            allDocsTermFreq.setCoordinateAt(index, 0, val -> val + 1);
        });

        tfDocumentVectors.put(documentKey, docVector);
        cacheValid = false;
    }

    /**
     * Returns a set of similar documents sorted by similarity (desc) and alphabetically (asc).<br>
     * Documents with similarity of 0 will not be returned.
     *
     * @param content Content of the new document.
     * @return Set of similar documents ordered by similarity (descending) and alphabetically (ascending).
     * @throws NullPointerException If content is {@code null}.
     */
    public SortedSet<SearchResult> findSimilar(String content) {
        Objects.requireNonNull(content);

        refreshCache();

        if (tfIdfDocumentVectors.isEmpty()) {
            return new TreeSet<>();
        }

//        vector for given content
        NVector<Number> docVector = new NVector<>(idfVector.numberOfDimensions());

        if (queryWords == null) {
            queryWords = new ArrayList<>(content.length() / 5);
        } else {
            queryWords.clear();
        }

//        init docVector with term frequencies
        getWords(content).forEach(word -> {
            Integer index = vocabulary.getIndexInVector(word);
            if (index == null) {
                return;
            }
            queryWords.add(word);
            docVector.setCoordinateAt(index, 0, val -> val.intValue() + 1);
        });

//        todo: Extract this into a separate method
//        transform docVector into tf-idf vector
        Double zeroValue = 0.0;
        for (int i = docVector.numberOfDimensions() - 1; i >= 0; i--) {
            Double idfValue = idfVector.getCoordinateAt(i, zeroValue);
            docVector.setCoordinateAt(i, 0, number -> number.doubleValue() * idfValue);
        }

//        find results
        SortedSet<SearchResult> results = new TreeSet<>();
        for (Map.Entry<String, NVector<Double>> entry : tfIdfDocumentVectors.entrySet()) {
            double similarity = entry.getValue().cosAngle(docVector);
            if (similarity > 0) {
                results.add(new SearchResult(entry.getKey(), similarity));
            }
        }
        return results;
    }

    /**
     * Refreshes cached data if necessary. If cache is valid, this will do nothing.
     */
    private void refreshCache() {
        if (cacheValid) {
            return;
        }
//        ----------- CONSTRUCTION OF idfVector ------------
        idfVector = new NVector<>(allDocsTermFreq.numberOfDimensions());
        Double zeroValue = 0.0;
        for (int i = allDocsTermFreq.numberOfDimensions() - 1; i >= 0; i--) {
            double idfValue = Math.log((double) tfDocumentVectors.size() / allDocsTermFreq.getCoordinateAt(i, 0).doubleValue());
            idfVector.setCoordinateAt(i, zeroValue, coord -> idfValue);
        }

//        ----------- FILL tfIdfDocumentVectors map ------------
        tfIdfDocumentVectors = new HashMap<>(tfDocumentVectors.size() * 2);
        for (Map.Entry<String, NVector<Integer>> docs : tfDocumentVectors.entrySet()) {
            String key = docs.getKey();
            NVector<Integer> tfVector = docs.getValue();

//            create tfIdfVector for one document
            NVector<Double> tfIdfVector = new NVector<>(tfVector.numberOfDimensions());
            int i = 0;
            for (Integer termFreq : tfVector.getCoordinates()) {
                double idf = idfVector.getCoordinateAt(i, zeroValue);
                tfIdfVector.setCoordinateAt(i, zeroValue, coord -> termFreq * idf);
                i++;
            }
            tfIdfDocumentVectors.put(key, tfIdfVector);
        }
        cacheValid = true;
    }

    /**
     * @return A list of non-stopping words used in the most recent query.
     * @throws IllegalStateException If no query has been performed yet.
     */
    public List<String> getQueryWords() {
        if (queryWords == null) {
            throw new IllegalStateException("No query has been performed yet - there are no query words.");
        }
        return Collections.unmodifiableList(queryWords);
    }

    /**
     * Model of a search result.
     */
    public static class SearchResult implements Comparable<SearchResult> {
        private String documentKey;
        private double similarity;

        /**
         * Constructs a new search result object.
         *
         * @param documentKey Key of the result.
         * @param similarity  Similarity of the result to the searched query.
         */
        private SearchResult(String documentKey, double similarity) {
            this.documentKey = Objects.requireNonNull(documentKey);
            if (similarity < 0) {
                throw new IllegalArgumentException("Similarity cannot be less than 0!");
            }
            this.similarity = similarity;
        }

        /**
         * @return Similarity ratio of the result to the search query.
         */
        public double getSimilarity() {
            return similarity;
        }

        /**
         * @return Document key of the search result.
         */
        public String getDocumentKey() {
            return documentKey;
        }

        @Override
        public int compareTo(SearchResult o) {
            if (o == null) {
                return 1;
            }
            int result = Double.compare(o.similarity, this.similarity);
            if (result == 0) {
                result = this.documentKey.compareTo(o.documentKey);
            }
            return result;
        }
    }
}
