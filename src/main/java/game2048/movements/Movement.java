package game2048.movements;

import game2048.point.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Movement {

    public static List<Point> eachRowAndColumnProcessing(List<Point> fullGrid,
                                                         int firstCoordinate,
                                                         CoordinateGetter rowGetter,
                                                         CoordinateSetter coordinateSetter,
                                                         Comparator<Point> comparator) {
        //   x0   x1   x2   x3
        //  [2]  [2]  [ ]  [2] y0
        //  [ ]  [4]  [4]  [ ] y1
        //  [ ]  [2]  [ ]  [ ] y2
        //  [2]  [ ]  [3]  [ ] y3

        for (int i = 0; i < 4; i++) {

            int finalI = i;

            List<Point> individualRowOrColumn = fullGrid.stream()
                    .filter(point -> rowGetter.getCoordinate(point) == finalI)
                    .sorted(comparator)
                    .collect(Collectors.toList());
            if (individualRowOrColumn.size() >= 1) {
                if (firstCoordinate == 0) {
                    assignAscendingCoordinates(individualRowOrColumn, coordinateSetter, firstCoordinate);
                } else {
                    assignDescendingCoordinates(individualRowOrColumn, coordinateSetter, firstCoordinate);
                }

                if (individualRowOrColumn.size() == 1) {
                    coordinateSetter.setCoordinate(individualRowOrColumn.get(0), firstCoordinate);
                } else {
                    fullGrid.removeAll(checkingForTheSameDigits(individualRowOrColumn));
                }
            }
        }
        return fullGrid;
    }


    private static synchronized void assignAscendingCoordinates(List<Point> oneRow, CoordinateSetter coordinateSetter, int index) {
        AtomicInteger finalXCoordinate = new AtomicInteger(index);
        oneRow.forEach(point -> {
            coordinateSetter.setCoordinate(point, finalXCoordinate.get());
            finalXCoordinate.getAndIncrement();
        });
    }

    private static synchronized void assignDescendingCoordinates(List<Point> oneRow, CoordinateSetter coordinateSetter, int index) {
        AtomicInteger finalXCoordinate = new AtomicInteger(index);
        oneRow.forEach(point -> {
            coordinateSetter.setCoordinate(point, finalXCoordinate.get());
            finalXCoordinate.getAndDecrement();
        });
    }

    private static synchronized List<Point> checkingForTheSameDigits(List<Point> oneRowOrColumn) {
        int counter = 0;
        List<Point> elementsToRemove = new ArrayList<>();
        do {
            if (oneRowOrColumn.get(counter).getValue().equals(oneRowOrColumn.get(counter + 1).getValue())) {
                slideByOnePosition(oneRowOrColumn, counter);
//                wholeGrid.remove(oneRowOrColumn.get(oneRowOrColumn.size() - 1));
                elementsToRemove.add(oneRowOrColumn.get(oneRowOrColumn.size() - 1));
                oneRowOrColumn.remove(oneRowOrColumn.size() - 1);
            }
            counter++;
        } while (counter <= oneRowOrColumn.size() - 2);
        return elementsToRemove;
    }

    private static synchronized void slideByOnePosition(List<Point> oneRowOrColumn, int firstElement) {

        int checker = firstElement;
        System.out.println(checker);

        oneRowOrColumn.get(checker).setValue(oneRowOrColumn.get(checker).getValue() + oneRowOrColumn.get(checker + 1).getValue());
        checker++;
        while (checker <= oneRowOrColumn.size() - 2) {
            oneRowOrColumn.get(checker).setValue(oneRowOrColumn.get(checker + 1).getValue());
            checker++;
        }
    }

    public static synchronized List<Point> moveLeft(List<Point> allPointsList) {
        return eachRowAndColumnProcessing(allPointsList, 0, new YGetter(), new XSetter(), new DirectComparatorX());
    }

    public static synchronized List<Point> moveRight(List<Point> allPointsList) {
        return eachRowAndColumnProcessing(allPointsList, 3, new YGetter(), new XSetter(), new ReversedComparatorX());
    }

    public static synchronized List<Point> moveUp(List<Point> allPointsList) {
        return eachRowAndColumnProcessing(allPointsList, 0, new XGetter(), new YSetter(), new DirectComparatorY());
    }

    public static synchronized List<Point> moveDown(List<Point> allPointsList) {
        return eachRowAndColumnProcessing(allPointsList, 3, new XGetter(), new YSetter(), new ReversedComparatorY());
    }

}