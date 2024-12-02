package backend.model;

public abstract class Figure {
    private ShadowType shadowType;

    public Figure(ShadowType shadowType){
        this.shadowType = shadowType;
    }

    public ShadowType getShadowType(){
        return shadowType;
    }

    public abstract void rotate();

    public abstract void flipHorizontal();

    public abstract void flipVertical();

    public abstract Figure duplicate(int offset);

    public abstract Figure[] divide();
}