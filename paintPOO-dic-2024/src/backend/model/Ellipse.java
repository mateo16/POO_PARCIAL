package backend.model;

public class Ellipse extends Figure {

    protected  Point centerPoint;
    protected  double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]", centerPoint, sMayorAxis, sMinorAxis);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public double getsMayorAxis() {
        return sMayorAxis;
    }

    public double getsMinorAxis() {
        return sMinorAxis;
    }

    @Override
    public void rotate() {
        double aux = sMayorAxis;
        sMayorAxis = sMinorAxis;
        sMinorAxis = aux;
    }

    @Override
    public void flipHorizontal() {
        centerPoint = new Point(-centerPoint.getX(), centerPoint.getY());
    }

    @Override
    public void flipVertical() {
        centerPoint = new Point(centerPoint.getX(), -centerPoint.getY());
    }

    @Override
    public Figure duplicate(int offset) {
        Point newCenter = new Point(centerPoint.getX() + offset, centerPoint.getY() + offset);
        return new Ellipse(newCenter, sMayorAxis, sMinorAxis);  
    }

    @Override
public Figure[] divide() {
    // Calculate the new semi-major and semi-minor axes for the divided ellipses
    double newSemiMajorAxis = sMayorAxis / 2;
    double newSemiMinorAxis = sMinorAxis / 2;

    // Create the left ellipse
    Ellipse leftHalf = new Ellipse(
        new Point(centerPoint.getX() - newSemiMajorAxis / 2, centerPoint.getY()), // Adjust X leftward
        newSemiMajorAxis,
        newSemiMinorAxis
    );

    // Create the right ellipse
    Ellipse rightHalf = new Ellipse(
        new Point(centerPoint.getX() + newSemiMajorAxis / 2, centerPoint.getY()), // Adjust X rightward
        newSemiMajorAxis,
        newSemiMinorAxis
    );

    // Return the two new ellipses
    return new Figure[]{leftHalf, rightHalf};
}




}
