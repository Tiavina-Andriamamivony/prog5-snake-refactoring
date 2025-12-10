package snake.entity;

import java.util.Random;

public record Food(Position position) {

    public static Food generate(int screenHeight, int screenWidth) {
        Random r = new Random();
        return new Food(new Position(r.nextInt(screenHeight - 2) + 1, r.nextInt(screenWidth - 2) + 1));
    }
}
