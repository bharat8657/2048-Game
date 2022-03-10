package game2048.movements;

import game2048.point.Point;

public abstract class CoordinateSetter {
    public abstract void setCoordinate(Point point, int coordinate);
}

class XSetter extends CoordinateSetter {
    @Override
    public void setCoordinate(Point point, int coordinate) {
        point.setX(coordinate);
    }
}

class YSetter extends CoordinateSetter {
    @Override
    public void setCoordinate(Point point, int coordinate) {
        point.setY(coordinate);
    }
}