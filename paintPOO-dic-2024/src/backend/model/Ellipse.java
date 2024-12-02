package backend.model;

public class Ellipse extends Figure {

    protected Point centerPoint;
    protected double sMayorAxis, sMinorAxis;

    public Ellipse(Point centerPoint, double sMayorAxis, double sMinorAxis) {
        super(ShadowType.NINGUNA,false);
        this.centerPoint = centerPoint;
        this.sMayorAxis = sMayorAxis;
        this.sMinorAxis = sMinorAxis;
    }

    public Ellipse(ShadowType shadowType, Boolean biselado, Point centerPoint, double sMayorAxis, double sMinorAxis) {
        super(shadowType,biselado);
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
    Ellipse topHalf = new Ellipse(new Point(centerPoint.getX(), centerPoint.getY() + sMinorAxis / 2), sMayorAxis, sMinorAxis / 2);
    Ellipse bottomHalf = new Ellipse(new Point(centerPoint.getX(), centerPoint.getY() - sMinorAxis / 2), sMayorAxis, sMinorAxis / 2);

    return new Figure[]{topHalf, bottomHalf};
}


}
