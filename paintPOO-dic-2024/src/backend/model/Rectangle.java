package backend.model;

public class Rectangle extends Figure {

    protected Point topLeft, bottomRight;

    protected double angle = 0;
    private boolean isFlippedHorizontal = false; 
    private boolean isFlippedVertical = false;   
    private Point centerPoint;

    public Rectangle(ShadowType shadowType, Boolean biselado, Point topLeft, Point bottomRight) {
        super(shadowType,biselado);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        updateCenterPoint(); 
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public double getAngle() {
        return angle;
    }

    private void updateCenterPoint() {
        centerPoint = new Point(
            (topLeft.getX() + bottomRight.getX()) / 2,
            (topLeft.getY() + bottomRight.getY()) / 2
        );
    }

    private void updateCorners(double width, double height) {
        topLeft = new Point(centerPoint.getX() - width / 2, centerPoint.getY() - height / 2);
        bottomRight = new Point(centerPoint.getX() + width / 2, centerPoint.getY() + height / 2);
    }

    @Override
    public void flipHorizontal() {
        double width = Math.abs(bottomRight.getX() - topLeft.getX());
        if (!isFlippedHorizontal) {
            centerPoint = new Point(centerPoint.getX() + width, centerPoint.getY());
        } else {
            centerPoint = new Point(centerPoint.getX() - width, centerPoint.getY());
        }
        isFlippedHorizontal = !isFlippedHorizontal;
        updateCorners(width, Math.abs(bottomRight.getY() - topLeft.getY()));
    }

    @Override
    public void flipVertical() {
        double height = Math.abs(bottomRight.getY() - topLeft.getY());
        if (!isFlippedVertical) {
            centerPoint = new Point(centerPoint.getX(), centerPoint.getY() + height);
        } else {
            centerPoint = new Point(centerPoint.getX(), centerPoint.getY() - height);
        }
        isFlippedVertical = !isFlippedVertical;
        updateCorners(Math.abs(bottomRight.getX() - topLeft.getX()), height);
    }

    @Override
    public void rotate() {
        double width = Math.abs(bottomRight.getX() - topLeft.getX());
        double height = Math.abs(bottomRight.getY() - topLeft.getY());
        updateCorners(height, width);
        angle = (angle + 90) % 360;
    }
   

    @Override 
public Figure duplicate(double offset) {
    return new Rectangle(
            this.getShadowType(),
        this.getBiselado(),
        new Point(topLeft.getX() + offset, topLeft.getY() + offset),
        new Point(bottomRight.getX() + offset, bottomRight.getY() + offset)
    );
}


@Override
public Figure[] divide() {
    double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
    double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
    double height = bottomRight.getY() - topLeft.getY(); 

    Rectangle leftHalf = new Rectangle(this.getShadowType(), this.getBiselado(), new Point(topLeft.getX(), topLeft.getY()+ height/4),  new Point(centerX, centerY+ height/4));
    Rectangle rightHalf = new Rectangle(this.getShadowType(), this.getBiselado(), new Point(centerX, centerY - height/4),  new Point(bottomRight.getX(), bottomRight.getY() - height/4));
    
    return new Figure[]{leftHalf, rightHalf};
}


    

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

}
