package game2048.movements;

import game2048.point.Point;

import java.util.Comparator;

public class ComparatorY implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return p1.getY().compareTo(p2.getY());
    }
}
