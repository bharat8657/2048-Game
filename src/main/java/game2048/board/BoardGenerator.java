package game2048.board;

import game2048.point.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardGenerator {
    private static final Random RANDOM = new Random();
    private static final int MAX_SIZE_OF_BOARD = 4;
    private static final int NEW_VALUE = 2;

    public static Board generateNewBoard() {
        Board board = new Board();
        Point point1 = new Point(RANDOM.nextInt(MAX_SIZE_OF_BOARD), RANDOM.nextInt(MAX_SIZE_OF_BOARD), NEW_VALUE);
        Point point2;
        do {
            point2 = new Point(RANDOM.nextInt(MAX_SIZE_OF_BOARD), RANDOM.nextInt(MAX_SIZE_OF_BOARD), NEW_VALUE);

        } while (point1.equals(point2));
        board.setPointList(List.of(point1, point2));
        return board;
    }

    public static Board updateBoard(Board board) {
        List<Point> points = new ArrayList<>(board.getPointList());
        Point newPoint;
        boolean isNewPointEqual;
        do {
            isNewPointEqual = false;
            newPoint = new Point(RANDOM.nextInt(MAX_SIZE_OF_BOARD), RANDOM.nextInt(MAX_SIZE_OF_BOARD), NEW_VALUE);
            for (Point x : points) {
                if (newPoint.equals(x)) {
                    isNewPointEqual = true;
                    break;
                }
            }
        } while (isNewPointEqual);
        points.add(newPoint);
        board.setPointList(points);
        return board;
    }

    public static void printBoard(List<Point> pointList) {
        boolean isFilled;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                isFilled = false;
                for (Point point4 : pointList) {
                    if (point4.getX() == j && point4.getY() == i) {
                        System.out.printf("%3s ", point4.getValue());
                        isFilled = true;
                    }
                }
                if (!isFilled) {
                    System.out.printf("%3s ", ".");
                }
            }
            System.out.println();
        }
    }
}

