package snake.entity;

import java.util.List;

import static snake.entity.Snake.moveHead;

public record Game (Food food, Snake snake, int score, int screenHeight, int screenWidth) {

    public static Game initialize(int screenHeight, int screenWidth) {
        var initialSnake = new Snake(
            List.of(
                new Position(screenHeight / 2, screenWidth / 2),
                new Position(screenHeight / 2, screenWidth / 2 - 1),
                new Position(screenHeight / 2, screenWidth / 2 - 2)
            )
        );
        var initialFood = Food.generate(screenHeight, screenWidth);
        return new Game(initialFood, initialSnake, 0, screenHeight, screenWidth);
    }

    //Tryin to harcode the refactor and clean it after
    public void start() throws Exception {
        Direction direction = Direction.RIGHT;
        // Logic to start the game
        while (true) {
            // Lecture entrée clavier si disponible
            if (System.in.available() > 0) {
                char c = (char) System.in.read();
                // Changement direction avec validation anti-retour
                if (c == 'a' && !direction.equals(Direction.RIGHT)) direction = Direction.LEFT;
                else if (c == 'd' && !direction.equals(Direction.LEFT)) direction = Direction.RIGHT;
                else if (c == 'w' && !direction.equals(Direction.DOWN)) direction = Direction.UP;
                else if (c == 's' && !direction.equals(Direction.UP)) direction = Direction.DOWN;
            }

            // Calcul nouvelle position de la tête
            Position hd = moveHead(snake.body().get(0), direction);

            // Vérification collision avec les murs
            if (hd.x() <= 0 || hd.x() >= screenHeight - 1 || hd.y() <= 0 || hd.y() >= screenWidth - 1) {
                System.out.println("GAME OVER - SCORE = " + score);
                return;
            }

            // Vérification collision avec le corps du serpent
            for (Position b : snake.body()) {
                if (hd.equals(b)) {
                    System.out.println("GAME OVER - SCORE = " + score);
                    return;
                }
            }

            // Vérification si nourriture mangée
            if (hd.equals(food.position())) {

                score++;
                food = Food.generate(screenHeight, screenWidth);

            } else {
                // Suppression dernier segment si pas de croissance
                snake = new Snake(snake.body().subList(0, snake.body().size() - 1));
            }

            // Ajout nouvelle tête
            s.add(0, hd);

            // Construction affichage
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < screenHeight; i++) {
                for (int j = 0; j < screenWidth; j++) {
                    boolean drawn = false;

                    // Affichage nourriture
                    if (food.position().equals(new Position(i, j))) {
                        sb.append("*");
                        drawn = true;
                    }

                    // Affichage serpent
                    for (Position px : snake.body()) {
                        if (px.equals(new Position(i, j))) {
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
            System.out.println("Score: " + score);

            // Délai entre chaque frame
            Thread.sleep(120);
        }
    }


}
