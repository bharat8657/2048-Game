package game2048.movements;

import game2048.point.Point;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Movement {

    private static List<Point> TEMPORARY_LIST;
    private static final ComparatorX COMPARATOR_X = new ComparatorX();
    private static final ComparatorY COMPARATOR_Y = new ComparatorY();
    private static int firstPlace, secondPlace, thirdPlace, fourthPlace;    // indexes of main places for numbers on board
    private static int threeElementsFirst, threeElementsSecond, threeElementsThird, auxiliaryCoordinateIndex;   // auxiliary variables
    private static int forTwoElementsRowWithoutChangesFirstPlace, forTwoElementsRowWithoutChangesSecondPlace;   // auxiliary variables

    //  x0  x1  x2   x3
    //  [2]  [2]  [ ]  [2] y0
    //  [ ]  [4]  [4]  [ ] y1
    //  [ ]  [2]  [ ]  [ ] y2
    //  [2]  [ ]  [3]  [ ] y3

    public static void eachRowProcessing (CoordinateGetter rowGetter,
                                         CoordinateSetter rowSetter,
                                         Comparator<Point> comparator,
                                         CoordinateGetter coordinateGetter,
                                         CoordinateSetter coordinateSetter) {
        IntStream.range(0, 4).forEach(rowIndex -> {

            List<Point> certainLineOfGameField = TEMPORARY_LIST.stream()
                    .filter(point -> rowGetter.getCoordinate(point) == rowIndex)
                    .sorted(comparator)
                    .collect(Collectors.toList());

            switch (certainLineOfGameField.size()) {
                case 1:
                    oneElementProcessing(certainLineOfGameField.get(0), coordinateGetter, coordinateSetter);
                    certainLineOfGameField.clear();
                    break;
                case 2:
                    twoElementsProcessing(certainLineOfGameField, rowIndex, rowSetter, coordinateSetter);
                    break;
                case 3:
                    threeElementsProcessing(certainLineOfGameField, coordinateSetter);
                    break;
                case 4:
                    fourElementsProcessing(certainLineOfGameField);
                    break;
            }
        });
    }

    public static List<Point> moveLeft(List<Point> allPointsList) {
        auxiliaryMethodLeftUp();
        TEMPORARY_LIST = allPointsList;
        eachRowProcessing(new YGetter(), new YSetter(), COMPARATOR_X, new XGetter(), new XSetter());
        return TEMPORARY_LIST;
    }

    public static List<Point> moveRight(List<Point> allPointsList) {
        auxiliaryMethodRightDown();
        TEMPORARY_LIST = allPointsList;
        eachRowProcessing(new YGetter(), new YSetter(), COMPARATOR_X, new XGetter(), new XSetter());
        return TEMPORARY_LIST;
    }

    public static List<Point> moveUp(List<Point> allPointsList) {
        auxiliaryMethodLeftUp();
        TEMPORARY_LIST = allPointsList;
        eachRowProcessing(new XGetter(), new XSetter(), COMPARATOR_Y, new YGetter(), new YSetter());
        return TEMPORARY_LIST;
    }

    public static List<Point> moveDown(List<Point> allPointsList) {
        auxiliaryMethodRightDown();
        TEMPORARY_LIST = allPointsList;
        eachRowProcessing(new XGetter(), new XSetter(), COMPARATOR_Y, new YGetter(), new YSetter());
        return TEMPORARY_LIST;
    }


    private static void oneElementProcessing (Point point, CoordinateGetter coordinateGetter, CoordinateSetter coordinateSetter) {
        if (coordinateGetter.getCoordinate(point) != firstPlace){
            coordinateSetter.setCoordinate(point, firstPlace);
        }
    }

    private static void twoElementsProcessing (List<Point> twoPointsList, int rowIndex, CoordinateSetter rowSetter, CoordinateSetter coordinateSetter) {

        if (twoPointsList.get(0).getValue().equals(twoPointsList.get(1).getValue())) {
            rowSetter.setCoordinate(twoPointsList.get(0), rowIndex);
            coordinateSetter.setCoordinate(twoPointsList.get(0), firstPlace);
            twoPointsList.get(0).setValue(twoPointsList.get(0).getValue() + twoPointsList.get(1).getValue());
            TEMPORARY_LIST.remove(twoPointsList.get(1));
        } else {
            coordinateSetter.setCoordinate(twoPointsList.get(0), forTwoElementsRowWithoutChangesFirstPlace); //0  2
            coordinateSetter.setCoordinate(twoPointsList.get(1), forTwoElementsRowWithoutChangesSecondPlace); //1  3
        }
    }

    private static void threeElementsProcessing (List<Point> threePointsList, CoordinateSetter rowSetter) {

        threePointsList.forEach(point -> {
            rowSetter.setCoordinate(point, auxiliaryCoordinateIndex);
            auxiliaryCoordinateIndex++;
        });

        if (threePointsList.get(threeElementsFirst).getValue().equals(threePointsList.get(threeElementsSecond).getValue())) {
            threePointsList.get(threeElementsFirst).setValue(threePointsList.get(threeElementsFirst).getValue() + threePointsList.get(threeElementsSecond).getValue());
            threePointsList.get(threeElementsSecond).setValue(threePointsList.get(threeElementsThird).getValue());
            TEMPORARY_LIST.remove(threePointsList.get(threeElementsThird));
        } else if (threePointsList.get(threeElementsSecond).getValue().equals(threePointsList.get(threeElementsThird).getValue())) {
            threePointsList.get(threeElementsSecond).setValue(threePointsList.get(threeElementsSecond).getValue() + threePointsList.get(threeElementsThird).getValue());
            TEMPORARY_LIST.remove(threePointsList.get(threeElementsThird));
        }
        auxiliaryCoordinateIndex -= 3;
    }

    private static void fourElementsProcessing (List<Point> fourElementsList) {

        int manipulator = 0;

        if (fourElementsList.get(firstPlace).getValue().equals(fourElementsList.get(secondPlace).getValue())) {
            fourElementsList.get(firstPlace).setValue(fourElementsList.get(firstPlace).getValue() + fourElementsList.get(secondPlace).getValue());
            fourElementsList.get(secondPlace).setValue(fourElementsList.get(thirdPlace).getValue());
            fourElementsList.get(thirdPlace).setValue(fourElementsList.get(fourthPlace).getValue());
            TEMPORARY_LIST.remove(fourElementsList.get(fourthPlace));
            fourElementsList.remove(fourthPlace);
            manipulator = 1;
        } else if (fourElementsList.get(secondPlace).getValue().equals(fourElementsList.get(thirdPlace).getValue())) {
            fourElementsList.get(secondPlace).setValue(fourElementsList.get(secondPlace).getValue() + fourElementsList.get(thirdPlace).getValue());
            fourElementsList.get(thirdPlace).setValue(fourElementsList.get(fourthPlace).getValue());
            TEMPORARY_LIST.remove(fourElementsList.get(fourthPlace));
            fourElementsList.remove(fourthPlace);
            manipulator = 2;
        } if (manipulator == 1 && fourElementsList.get(threeElementsSecond).getValue().equals(fourElementsList.get(threeElementsThird).getValue())) {
                fourElementsList.get(threeElementsSecond).setValue(fourElementsList.get(threeElementsSecond).getValue() + (fourElementsList.get(threeElementsThird).getValue()));
                TEMPORARY_LIST.remove(fourElementsList.get(threeElementsThird));
        } else if (manipulator == 0 && fourElementsList.get(thirdPlace).getValue().equals(fourElementsList.get(fourthPlace).getValue())) {  // 2 3
            fourElementsList.get(thirdPlace).setValue(fourElementsList.get(thirdPlace).getValue() + (fourElementsList.get(fourthPlace).getValue())); // 0 1
            TEMPORARY_LIST.remove(fourElementsList.get(fourthPlace));
        }
    }


    private static void auxiliaryMethodLeftUp() {
        forTwoElementsRowWithoutChangesFirstPlace = 0;
        forTwoElementsRowWithoutChangesSecondPlace = 1;
        auxiliaryCoordinateIndex = 0;
        threeElementsFirst = 0;
        threeElementsSecond = 1;
        threeElementsThird = 2;
        firstPlace = 0;
        secondPlace = 1;
        thirdPlace = 2;
        fourthPlace = 3;
    }

    private static void auxiliaryMethodRightDown() {
        forTwoElementsRowWithoutChangesFirstPlace = 2;
        forTwoElementsRowWithoutChangesSecondPlace = 3;
        auxiliaryCoordinateIndex = 1;
        threeElementsFirst = 2;
        threeElementsSecond = 1;
        threeElementsThird = 0;
        firstPlace = 3;
        secondPlace = 2;
        thirdPlace = 1;
        fourthPlace = 0;
    }
}