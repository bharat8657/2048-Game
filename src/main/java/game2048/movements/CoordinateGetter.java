package game2048.movements;

import game2048.point.Point;

public abstract class CoordinateGetter {
    public abstract int getCoordinate(Point point);
}

class XGetter extends CoordinateGetter {
    @Override
    public int getCoordinate (Point point) {
        return point.getX();
    }
}

class YGetter extends CoordinateGetter {
    @Override
    public int getCoordinate (Point point) {
        return point.getY();
    }
}