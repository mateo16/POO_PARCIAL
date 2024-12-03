package backend.model;

public class Rectangle extends Figure {

    protected Point topLeft, bottomRight;

    protected double angle = 0;

    public Rectangle(Point topLeft, Point bottomRight) {
        super(ShadowType.NINGUNA,false);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Rectangle(ShadowType shadowType, Boolean biselado, Point topLeft, Point bottomRight) {
        super(shadowType,biselado);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
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

    @Override
    public void rotate(){
        double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
        double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
    
        // Calculate the width and height of the rectangle
        double width = Math.abs(bottomRight.getX() - topLeft.getX());
        double height = Math.abs(bottomRight.getY() - topLeft.getY());
    
        // Swap the width and height for rotation
        double newTopLeftX = centerX - height / 2;
        double newTopLeftY = centerY - width / 2;
        double newBottomRightX = centerX + height / 2;
        double newBottomRightY = centerY + width / 2;
    
        // Update rectangle points
        topLeft = new Point(newTopLeftX, newTopLeftY);
        bottomRight = new Point(newBottomRightX, newBottomRightY);

        angle += 90;
        if(angle == 360) {
            angle = 0;
        }
    }

    @Override
    public void flipHorizontal(){
        double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
        topLeft = new Point(2 * centerX - topLeft.getX(), topLeft.getY());
        bottomRight = new Point(2 * centerX - bottomRight.getX(), bottomRight.getY());
    }

    @Override
public void flipVertical() {
    double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
    topLeft = new Point(topLeft.getX(), 2 * centerY - topLeft.getY());
    bottomRight = new Point(bottomRight.getX(), 2 * centerY - bottomRight.getY());
}

    

    @Override 
public Figure duplicate(int offset) {
    return new Rectangle(
        new Point(topLeft.getX() + offset, topLeft.getY() + offset),
        new Point(bottomRight.getX() + offset, bottomRight.getY() + offset)
    );
}


@Override
public Figure[] divide() {
    double centerX = (topLeft.getX() + bottomRight.getX()) / 2;
    double centerY = (topLeft.getY() + bottomRight.getY()) / 2;
    double height = bottomRight.getY() - topLeft.getY(); 

    
    Rectangle leftHalf = new Rectangle(new Point(topLeft.getX(), topLeft.getY()+ height/4),  new Point(centerX, centerY+ height/4));
    Rectangle rightHalf = new Rectangle(new Point(centerX, centerY - height/4),  new Point(bottomRight.getX(), bottomRight.getY() - height/4));
    
    return new Figure[]{leftHalf, rightHalf};
}


    

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

}
