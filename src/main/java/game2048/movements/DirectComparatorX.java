package game2048.movements;

import game2048.point.Point;

import java.util.Comparator;

class DirectComparatorX implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return p1.getX().compareTo(p2.getX());
    }
}

class ReversedComparatorX implements Comparator<Point> {

    @Override
    public int compare(Point p1, Point p2) {
        return p2.getX().compareTo(p1.getX());
    }
}