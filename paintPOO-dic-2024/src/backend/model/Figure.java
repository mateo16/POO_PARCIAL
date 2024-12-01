package backend.model;

public abstract class Figure {

    

    public abstract void rotate();

    public abstract void flipHorizontal();

    public abstract void flipVertical();

    public abstract Figure duplicate(int offset);

    public abstract Figure[] divide();
}
