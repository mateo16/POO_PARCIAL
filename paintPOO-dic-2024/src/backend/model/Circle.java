package backend.model;

public class Circle extends Ellipse {

    public Circle(ShadowType shadowType,Boolean biselado, Point centerPoint, double radius) {
        super(shadowType, biselado, centerPoint, radius, radius);
    }

    public double getRadius(){
        return super.getsMayorAxis();
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]",centerPoint, sMayorAxis);
    }

}
