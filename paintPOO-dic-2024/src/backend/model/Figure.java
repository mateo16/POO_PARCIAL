package backend.model;

public abstract class Figure {
    private ShadowType shadowType;
    private Boolean biselado;

    public Figure(ShadowType shadowType,Boolean biselado){
        this.shadowType = shadowType;
        this.biselado = biselado;
    }

    public ShadowType getShadowType(){
        return shadowType;
    }

    public Boolean getBiselado(){
        return biselado;
    }

    public abstract void rotate();

    public abstract void flipHorizontal();

    public abstract void flipVertical();

    public abstract Figure duplicate(int offset);

    public abstract Figure[] divide();
}