package backend.model;

import backend.Layer;

public abstract class Figure {
    private ShadowType shadowType;
    private boolean biselado;

    private Layer layer;

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

    public Layer getLayer() {
        return this.layer;
    }

    public void setShadowType(ShadowType shadowType){
        this.shadowType = shadowType;
    }

    public void setBiselado(boolean biselado){
        this.biselado = biselado;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }


    public abstract void rotate();

    public abstract void flipHorizontal();

    public abstract void flipVertical();

    public abstract Figure duplicate(double offset);

    public abstract Figure[] divide();
}