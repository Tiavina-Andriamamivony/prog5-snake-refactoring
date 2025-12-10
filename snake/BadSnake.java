package snake;

import java.util.*;
import java.io.IOException;

/**
 * EXERCICE DE REFACTORING
 *
 * Ce code fonctionne, mais viole de nombreux principes de bonne pratique.
 * Votre mission : le refactorer pour en faire un code propre et maintenable.
 *
 * PROBLÈMES À CORRIGER :
 * 1. Noms de variables cryptiques (sh, sw, s, f, d, mv, hd, etc.)
 * 2. Présence de « magic numbers » partout (20, 40, 120, etc.)
 * 3. Méthode main() monolithique – aucune séparation des responsabilités
 * 4. Pas de classes/objets – tout repose sur des primitives et des tableaux
 * 5. Gestion des directions basée sur des chaînes de caractères (fragile)
 * 6. Affichage mélangé avec la logique du jeu
 * 7. Aucune constante pour les caractères spéciaux
 * 8. Détection de collisions inefficace (boucles imbriquées)
 * 9. Aucune validation des entrées utilisateur
 * 10. Séquences d’échappement du terminal codées en dur
 *
 * AMÉLIORATIONS SUGGÉRÉES :
 * - Créer des classes : Game, Snake, Food, Position, Direction (enum)
 * - Extraire des constantes : SCREEN_HEIGHT, SCREEN_WIDTH, TICK_DELAY
 * - Séparer en méthodes : update(), render(), handleInput(), checkCollisions()
 * - Utiliser des noms explicites : screenHeight au lieu de sh, snake au lieu de s
 * - Envisager une grille 2D pour une détection de collision plus efficace
 * - Ajouter des commentaires
 */
public class BadSnake {
    public static int[] mv(int[] h, String d) {
        int[] k = new int[]{h[0], h[1]};
        if (d.equals("L")) k[1]--;
        else if (d.equals("R")) k[1]++;
        else if (d.equals("U")) k[0]--;
        else if (d.equals("D")) k[0]++;
        return k;
    }

    public static void main(String[] args) throws Exception {
        int sh = 20;
        int sw = 40;

        List<int[]> s = new ArrayList<>();
        s.add(new int[]{10, 10});
        s.add(new int[]{10, 9});
        s.add(new int[]{10, 8});

        Random r = new Random();
        int[] f = new int[]{r.nextInt(sh - 2) + 1, r.nextInt(sw - 2) + 1};  // 'f' = food?

        String d = "R";
        int sc = 0;

        while (true) {
            if (System.in.available() > 0) {
                char c = (char) System.in.read();
                if (c == 'a' && !d.equals("R")) d = "L";
                else if (c == 'd' && !d.equals("L")) d = "R";
                else if (c == 'w' && !d.equals("D")) d = "U";
                else if (c == 's' && !d.equals("U")) d = "D";
            }

            int[] hd = mv(s.get(0), d);
            if (hd[0] <= 0 || hd[0] >= sh - 1 || hd[1] <= 0 || hd[1] >= sw - 1) {
                System.out.println("GAME OVER - SCORE = " + sc);
                return;
            }

           // Vérification de collision en O(n) — on pourrait utiliser un Set pour obtenir du O(1)
            for (int i = 0; i < s.size(); i++) {
                int[] b = s.get(i);
                if (hd[0] == b[0] && hd[1] == b[1]) {
                    System.out.println("GAME OVER - SCORE = " + sc);
                    return;
                }
            }

            if (hd[0] == f[0] && hd[1] == f[1]) {
                sc++;
                f = new int[]{r.nextInt(sh - 2) + 1, r.nextInt(sw - 2) + 1};
            } else {
                s.remove(s.size() - 1);
            }

            s.add(0, hd);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sh; i++) {
                for (int j = 0; j < sw; j++) {
                    boolean drawn = false;

                    if (i == f[0] && j == f[1]) {
                        sb.append("*");
                        drawn = true;
                    }

                    for (int[] px : s) {
                        if (px[0] == i && px[1] == j) {
                            sb.append("#");
                            drawn = true;
                            break;
                        }
                    }

                    if (!drawn) {
                        if (i == 0 || j == 0 || i == sh - 1 || j == sw - 1) 
                            sb.append("X");
                        else 
                            sb.append(" ");
                    }
                }
                sb.append("\n");
            }

            System.out.print("\033[H\033[2J");  
            System.out.flush();
            System.out.println(sb.toString());
            System.out.println("Score: " + sc);

            Thread.sleep(120);
        }
    }
}