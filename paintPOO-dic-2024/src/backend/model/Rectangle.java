package backend.model;

public class Rectangle extends Figure {

    protected Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public void rotate(){
        double centerX = (topLeft.getX()+bottomRight.getX())/2;
        double centerY = (topLeft.getY()+bottomRight.getY())/2;

        double newTopLeftX = centerX + (topLeft.getY() - centerY);
        double newTopLeftY = centerY - (topLeft.getX() - centerX);
        double newBottomRightX = centerX + (bottomRight.getY() - centerY);
        double newBottomRightY = centerY - (bottomRight.getX() - centerX);

        topLeft = new Point(newTopLeftX, newTopLeftY);
        bottomRight = new Point(newBottomRightX, newBottomRightY);
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
    double midX = (topLeft.getX() + bottomRight.getX()) / 2;  // Split horizontally in the middle
    Rectangle leftHalf = new Rectangle(topLeft, new Point(midX, bottomRight.getY()));
    Rectangle rightHalf = new Rectangle(new Point(midX, topLeft.getY()), bottomRight);

    return new Figure[]{leftHalf, rightHalf};
}


    

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

}
