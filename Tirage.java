
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Tirage {
    private int n;
    private int k;
    private int t;


    public Tirage(int n, int k, int t) {
        this.n = n;
        this.k = k;
        this.t = t;

    }

    public List<Integer> effectuerTirage( GestionnaireBillets gestionnaireBillets) {
        System.out.println("Tirage en cours");
        /*
         * chois des k numero ganant
         */
        ArrayList<Integer> numerosgagnants = new ArrayList<Integer>();
        for (int i = 0; i < k; i++) {
            numerosgagnants.add((int) (Math.random() * n + 1));
        }

        return gestionnaireBillets.numerogagnantverife(numerosgagnants);

    }

}
