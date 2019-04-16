package security.util;

import java.util.Objects;

/**
 * Class that enables hex to byte conversion and vice versa.
 *
 * @author Jan Capek
 */
public class Util {

    /**
     * Converts hex representation of bytes to byte array.
     * Each byte is made of two characters.
     *
     * @param s String that represents hex number.
     * @return Array of bytes held in given string.
     * @throws NullPointerException     If given string is {@code null}.
     * @throws IllegalArgumentException If string holds chars that are not in the hex alphabet.
     */
    public static byte[] hextobyte(String s) {
        char[] chars = Objects.requireNonNull(s).toLowerCase().toCharArray();
        byte[] result = new byte[(chars.length + 1) / 2];

        int powerOfBase = 0;
        int currentByteIndex = result.length - 1;
        for (int i = chars.length - 1; i >= 0; i--) {
            result[currentByteIndex] += decodeHexChar(chars[i]) * Math.pow(16, powerOfBase);
            powerOfBase = (powerOfBase + 1) % 2;
            currentByteIndex = powerOfBase == 0 ? currentByteIndex - 1 : currentByteIndex;
        }
        return result;
    }

    /**
     * Decodes a char that represents hex number to byte.
     *
     * @param c Char representation of hex number.
     * @return Hex number.
     * @throws IllegalArgumentException If given character does not exist in the hex alphabet.
     */
    private static byte decodeHexChar(char c) {
        if (Character.isDigit(c)) {
            return (byte) (c - '0');
        }
//        TODO: Maybe this way of checking is not the best idea.
        if ("abcdef".indexOf(c) == -1) {
            throw new IllegalArgumentException("Unrecognized character.");
        }
        return (byte) (c - 'a' + 10);
    }

    /**
     * Converts byte array to it's String representation.
     * Each byte will be represented with two characters.
     *
     * @param arr Array of bytes that will be converted to String.
     * @return String representation of byte array.
     * @throws NullPointerException If given array is {@code null.
     */
    public static String bytetohex(byte[] arr) {
        Objects.requireNonNull(arr);

        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(encodeByte(b));
        }

        return sb.toString();
    }

    /**
     * Encodes a byte to two character string.
     *
     * @param b Byte that needs to be encoded.
     * @return String representation of given byte.
     */
    private static String encodeByte(byte b) {
        byte second = (byte)(b & 0xF);
        byte first = (byte)(b >>> 4 & 0xF);

        char sec = second >= 10 ? (char) ('a' + second - 10) : (char) (second + '0');
        char fir = first >= 10 ? (char) ('a' + first - 10) : (char) (first + '0');
        return String.format("%c%c", fir, sec);

    }
}
