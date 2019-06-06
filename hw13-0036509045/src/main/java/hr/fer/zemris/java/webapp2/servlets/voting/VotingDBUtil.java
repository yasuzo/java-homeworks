package hr.fer.zemris.java.webapp2.servlets.voting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for reading band data.
 *
 * @author Jan Capek
 */
public class VotingDBUtil {

    /**
     * Reads band data and returns it.
     *
     * @param bandInfo Path to band info file.
     * @param votesInfo Path to information about band's votes.
     * @return Band data.
     * @throws IOException If data could not be read.
     * @throws NullPointerException If path to band information is {@code null}.
     */
    public static Map<Integer, BandData> getBandData(Path bandInfo, Path votesInfo) throws IOException {
        Objects.requireNonNull(bandInfo);

//        read band info
        Map<Integer, BandData> bands = parseBands(Files.readAllLines(bandInfo));

//        check if file containing votes exists & if it is readable
        if(votesInfo == null || Files.isReadable(votesInfo) == false) {
            return bands;
        }

//        read number of votes
        for(String voteData : Files.readAllLines(votesInfo)) {
            String[] votes = voteData.split("\t");
            int id = Integer.parseInt(votes[0]);
            int voteNumber = Integer.parseInt(votes[1]);
            bands.get(id).setVotes(voteNumber);
        }
        return bands;
    }

    /**
     * Reads band data and returns it sorted by number of votes in descending order.
     *
     * @param bandInfo Path to band info file.
     * @param votesInfo Path to information about band's votes.
     * @return Sorted band data.
     * @throws IOException If data could not be read.
     * @throws NullPointerException If path to band information is {@code null}.
     */
    public static List<BandData> getSortedBandData(Path bandInfo, Path votesInfo) throws IOException {
        Map<Integer, VotingDBUtil.BandData> bands = VotingDBUtil.getBandData(bandInfo, votesInfo);
        return bands.values().stream()
                .sorted(Comparator.comparingInt(VotingDBUtil.BandData::getVotes).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Writes band votes to a file on given path. If file already exists, it will be overwritten.
     *
     * @param voteInfoFile Path to write destination.
     * @param bandData Band data.
     * @throws IOException If data could not be written.
     * @throws NullPointerException If any of the parameters are {@code null} or any of the data in a map is {@code null}.
     */
    public static void saveBandData(Path voteInfoFile, Map<Integer, BandData> bandData) throws IOException {
        Objects.requireNonNull(voteInfoFile);
        Objects.requireNonNull(bandData);

        BufferedWriter writer = Files.newBufferedWriter(voteInfoFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        for(BandData data : bandData.values()) {
            writer.append(String.format("%d\t%d\n", data.id, data.votes));
        }
        writer.close();
    }

    /**
     * Parses a list of lines each representing information about one band in format "Id\tName\tVideoLink".
     *
     * @param bandInfo List of lines each representing information about one band.
     * @return Parsed map of band data, keys are bands' ids.
     * @throws NullPointerException If given list is {@code null};
     * @throws RuntimeException If list is invalid.
     */
    private static Map<Integer, BandData> parseBands(List<String> bandInfo) {
        Objects.requireNonNull(bandInfo);
        Map<Integer, BandData> bands = new HashMap<>();
        for(String line : bandInfo) {
            String[] data = line.split("\t");
            BandData band = new BandData(Integer.parseInt(data[0]), data[1], data[2]);
            bands.put(band.id, band);
        }
        return bands;
    }

    /**
     * Model of data about a single band.
     */
    public static class BandData{
        private int id;
        private String name;
        private String videoLink;
        private int votes;

        /**
         * Constructs a new band data object.
         *
         * @param id Band's id.
         * @param name Band's name.
         * @param videoLink Link to one of band's videos.
         * @param votes Number of votes a band has.
         * @throws NullPointerException If given name or video link is {@code null}.
         * @throws IllegalArgumentException If number of votes is less than 0.
         */
        private BandData(int id, String name, String videoLink, int votes) {
            this.id = id;
            this.name = Objects.requireNonNull(name);
            this.videoLink = Objects.requireNonNull(videoLink);
            setVotes(votes);
        }

        /**
         * Constructs a new band data object.
         *
         * @param id Band's id.
         * @param name Band's name.
         * @param videoLink Link to one of band's videos.
         * @throws NullPointerException If given name or video link is {@code null}.
         */
        private BandData(int id, String name, String videoLink) {
            this.id = id;
            this.name = Objects.requireNonNull(name);
            this.videoLink = Objects.requireNonNull(videoLink);
        }

        /**
         * @return Band's id.
         */
        public int getId() {
            return id;
        }

        /**
         * @return Band's name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return Link to one of band's videos.
         */
        public String getVideoLink() {
            return videoLink;
        }

        /**
         * @return Number of votes the band has.
         */
        public int getVotes() {
            return votes;
        }

        /**
         * Sets votes to given number.
         *
         * @param votes New number of votes for the band.
         * @throws IllegalArgumentException If number is less than 0.
         */
        public void setVotes(int votes) {
            if(votes < 0) {
                throw new IllegalArgumentException("Number of votes cannot be less than 0.");
            }
            this.votes = votes;
        }
    }
}
