package game2048.movements;

import game2048.point.Point;

import java.util.Comparator;

public class ComparatorX implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return p1.getX().compareTo(p2.getX());
    }
}
