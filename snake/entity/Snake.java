package snake.entity;

import java.util.List;

public record Snake(List<Position> body) {

    // Déplace la tête du serpent selon la direction donnée
    public static Position moveHead(Position head, Direction direction) {
        return switch (direction) {
            case LEFT -> new Position(head.x(), head.y() - 1);
            case RIGHT -> new Position(head.x(), head.y() + 1);
            case UP -> new Position(head.x() - 1, head.y());
            case DOWN -> new Position(head.x() + 1, head.y());
        };
    }




}
