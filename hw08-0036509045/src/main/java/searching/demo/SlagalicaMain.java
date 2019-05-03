package searching.demo;

import searching.algorithms.Node;
import searching.algorithms.SearchUtil;
import searching.slagalica.KonfiguracijaSlagalice;
import searching.slagalica.Slagalica;
import searching.slagalica.gui.SlagalicaViewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Demo program that illustrates puzzle solving.
 */
public class SlagalicaMain {
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Invalid number of arguments.");
            return;
        }

        KonfiguracijaSlagalice startingConfig;
        try {
            startingConfig = new KonfiguracijaSlagalice(convertToIntArray(args[0]));
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument was entered. " +
                    "String should contain only 9 distinct numbers ranging from 0 to 8 (inclusively).");
            return;
        }

        Slagalica slagalica = new Slagalica(startingConfig);
        Node<KonfiguracijaSlagalice> rješenje = SearchUtil.bfsv(slagalica, slagalica, slagalica);
        if (rješenje == null) {
            System.out.println("Nisam uspio pronaći rješenje.");
        } else {
            System.out.println("Imam rješenje. Broj poteza je: " + rješenje.getCost());
            List<KonfiguracijaSlagalice> lista = new ArrayList<>();
            Node<KonfiguracijaSlagalice> trenutni = rješenje;
            while (trenutni != null) {
                lista.add(trenutni.getState());
                trenutni = trenutni.getParent();
            }
            Collections.reverse(lista);
            lista.stream().forEach(k -> {
                System.out.println(k);
                System.out.println();
            });
            SlagalicaViewer.display(rješenje);
        }
    }

    /**
     * Converts a string to integer array.
     * All character should be integers but this will not break if they aren't.
     * In that case values in the returned values will be unknown.
     *
     * @param s String that should be converted.
     * @return Array of integers.
     * @throws NullPointerException If given string is {@code null}.
     */
    private static int[] convertToIntArray(String s) {
        Objects.requireNonNull(s);
        char[] chars = s.toCharArray();
        int[] ints = new int[chars.length];
        for(int i = 0; i < chars.length; i++) {
            ints[i] = chars[i] - '0';
        }
        return ints;
    }
}
