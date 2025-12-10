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
 * 1. Noms de variables cryptiques (screenHeight, screenWidth, s, f, d, moveHead, hd, etc.)
 * 2. Présence de « magic numbers » partout (20, 40, 120, etc.)
 * 3. Méthode main() monolithique – aucune séparation des responsabilités
 * 4. Pas de classes/objets – tout repose sur des primitives et des tableaux
 * 5. Gestion des directions basée sur des chaînes de caractères (fragile)
 * 6. Affichage mélangé avec la logique du jeu
 * 7. Aucune constante pour les caractères spéciaux
 * 8. Détection de collisions inefficace (boucles imbriquées)
 * 9. Aucune validation des entrées utilisateur
 * 10. Séquences d'échappement du terminal codées en dur
 *
 * AMÉLIORATIONS SUGGÉRÉES :
 * - Créer des classes : Game, Snake, Food, Position, Direction (enum)
 * - Extraire des constantes : SCREEN_HEIGHT, SCREEN_WIDTH, TICK_DELAY
 * - Séparer en méthodes : update(), render(), handleInput(), checkCollisions()
 * - Utiliser des noms explicites : screenHeight au lieu de screenHeight, snake au lieu de s
 * - Envisager une grille 2D pour une détection de collision plus efficace
 * - Ajouter des commentaires
 */
public class BadSnake {

    // Déplace la tête du serpent selon la direction donnée
    public static int[] moveHead(int[] head, String direction) {
        int[] newHead = new int[]{head[0], head[1]};
        if (direction.equals("L")) newHead[1]--;
        else if (direction.equals("R")) newHead[1]++;
        else if (direction.equals("U")) newHead[0]--;
        else if (direction.equals("D")) newHead[0]++;
        return newHead;
    }

    public static void main(String[] args) throws Exception {

        //------Game begin------//
        // Configuration dimensions écran
        int screenHeight = 20;
        int screenWidth = 40;

        //REPLACED WITH A LIST OF POSITION OBJECTS
        // Initialisation serpent avec 3 segments
        List<int[]> s = new ArrayList<>();
        s.add(new int[]{10, 10});
        s.add(new int[]{10, 9});
        s.add(new int[]{10, 8});


        //DONE
        // Génération position aléatoire de la nourriture
        Random r = new Random();
        int[] food = new int[]{r.nextInt(screenHeight - 2) + 1, r.nextInt(screenWidth - 2) + 1};

        String d = "R";
        int sc = 0;

        // Boucle principale du jeu
        while (true) {
            // Lecture entrée clavier si disponible
            if (System.in.available() > 0) {
                char c = (char) System.in.read();
                // Changement direction avec validation anti-retour
                if (c == 'a' && !d.equals("R")) d = "L";
                else if (c == 'd' && !d.equals("L")) d = "R";
                else if (c == 'w' && !d.equals("D")) d = "U";
                else if (c == 's' && !d.equals("U")) d = "D";
            }

            // Calcul nouvelle position de la tête
            int[] hd = moveHead(s.get(0), d);

            // Vérification collision avec les murs
            if (hd[0] <= 0 || hd[0] >= screenHeight - 1 || hd[1] <= 0 || hd[1] >= screenWidth - 1) {
                System.out.println("GAME OVER - SCORE = " + sc);
                return;
            }

            // Vérification collision avec le corps du serpent
            for (int i = 0; i < s.size(); i++) {
                int[] b = s.get(i);
                if (hd[0] == b[0] && hd[1] == b[1]) {
                    System.out.println("GAME OVER - SCORE = " + sc);
                    return;
                }
            }

            // Vérification si nourriture mangée
            if (hd[0] == food[0] && hd[1] == food[1]) {
                sc++;
                // Nouvelle nourriture générée
                food = new int[]{r.nextInt(screenHeight - 2) + 1, r.nextInt(screenWidth - 2) + 1};
            } else {
                // Suppression dernier segment si pas de croissance
                s.remove(s.size() - 1);
            }

            // Ajout nouvelle tête
            s.add(0, hd);

            // Construction affichage
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < screenHeight; i++) {
                for (int j = 0; j < screenWidth; j++) {
                    boolean drawn = false;

                    // Affichage nourriture
                    if (i == food[0] && j == food[1]) {
                        sb.append("*");
                        drawn = true;
                    }

                    // Affichage serpent
                    for (int[] px : s) {
                        if (px[0] == i && px[1] == j) {
                            sb.append("#");
                            drawn = true;
                            break;
                        }
                    }

                    // Affichage murs ou espaces vides
                    if (!drawn) {
                        if (i == 0 || j == 0 || i == screenHeight - 1 || j == screenWidth - 1)
                            sb.append("X");
                        else
                            sb.append(" ");
                    }
                }
                sb.append("\n");
            }

            // Effacement écran et affichage
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println(sb.toString());
            System.out.println("Score: " + sc);

            // Délai entre chaque frame
            Thread.sleep(120);
        }
    }
}