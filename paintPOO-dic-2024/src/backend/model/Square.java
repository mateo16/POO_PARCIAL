package backend.model;

public class Square extends Rectangle{


    public Square(ShadowType shadowType, Boolean biselado, Point topLeft, double size) {
        super(shadowType, biselado, topLeft,new Point(topLeft.x + size, topLeft.y + size));
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s ]", topLeft, bottomRight);
    }

}
