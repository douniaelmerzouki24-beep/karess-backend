package com.example.karess.utils;

public class NombreOutils {
    // Zdna Onze, Douze, Treize, Quatorze, Quinze, Seize hna
    // 1. Zid had l'kelmat (dix-sept, dix-huit, dix-neuf) bach l'index 17, 18, 19 i-welli kheen
    private static final String[] unites = {
            "", "Un", "Deux", "Trois", "Quatre", "Cinq", "Six", "Sept", "Huit", "Neuf",
            "Dix", "Onze", "Douze", "Treize", "Quatorze", "Quinze", "Seize",
            "Dix Sept", "Dix Huit", "Dix Neuf"
    };

    // 2. T-akked blli dizaines hta hiya m9adda
    private static final String[] dizaines = {
            "", "Dix", "Vingt", "Trente", "Quarante", "Cinquante", "Soixante",
            "Soixante Dix", "Quatre Vingt", "Quatre Vingt Dix"
    };

    public static String convertir(long n) {
        if (n == 0) return "zéro";
        if (n < 0) return "moins " + convertir(-n);

        String nom = "";

        if ((n / 1000000) > 0) {
            nom += convertir(n / 1000000) + " Million" + (n / 1000000 > 1 ? "s " : " ");
            n %= 1000000;
        }
        if ((n / 1000) > 0) {
            if (n / 1000 == 1) nom += "Mille ";
            else nom += convertir(n / 1000) + " Mille ";
            n %= 1000;
        }
        if ((n / 100) > 0) {
            if (n / 100 == 1) nom += "Cent ";
            else nom += unites[(int)(n / 100)] + " Cent ";
            n %= 100;
        }

        if (n > 0) {
            // Hna l'modification : ila kan n sgher mn 17, ghadi i-akhoud l-kelma directe mn unites
            if (n < 17) {
                nom += unites[(int)n];
            } else if (n < 20) {
                nom += "Dix " + unites[(int)(n % 10)];
            } else {
                long d = n / 10;
                long u = n % 10;

                // Gestion dial soixante-dix (70) w quatre-vingt-dix (90) bach i-jiw mzyanin
                if (d == 7 || d == 9) {
                    nom += dizaines[(int)d - 1] + "" + (u == 0 ? "Dix" : (u == 1 ? "et Onze" : unites[(int)u + 10]));
                } else {
                    nom += dizaines[(int)d] + (u > 0 ? (u == 1 ? " et Un" : " " + unites[(int)u]) : "");
                }
            }
        }

        return nom.trim();
    }
}