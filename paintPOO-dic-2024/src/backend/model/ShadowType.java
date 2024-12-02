package backend.model;

public enum ShadowType {
    NINGUNA(false,0.0),SIMPLE(false,10.0),COLOREADA(true, 10.0),INVSIMPLE(false, -10.0),INVCOLOREADA(true, -10.0);

    private final Boolean colored;
    private final Double offset;
    
    ShadowType(Boolean colored, Double offset){
        this.colored = colored;
        this.offset = offset;
    }

    public Boolean getColored(){
        return colored;
    }

    public Double getOffset(){
        return offset;
    }
}
