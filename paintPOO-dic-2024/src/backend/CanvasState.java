package backend;

import backend.model.Figure;
import backend.model.ShadowType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CanvasState {


    //private final List<Figure> list = new ArrayList<>();

    public CanvasState() {
         layers = new ArrayList<>();
         layers.add(new Layer());
         layers.add(new Layer());
         layers.add(new Layer());
    }

    private ArrayList<Layer> layers;

    public void addFigure(Layer layer, Figure figure) {
        layer.addFigure(figure);
    }

    public void deleteFigure(Layer layer, Figure figure) {
        layer.deleteFigure(figure);
    }

    public void setFront(Layer layer, Figure figure) {
        layer.setFront(figure);
    }

    public void setBack(Layer layer, Figure figure) {
        layer.setBack(figure);
    }

   
    public Iterable<Figure> figures(Layer layer) {
        return layer.getFigures();
    }

    public Iterable<Figure> figuresShown() {
        List<Figure> l = new ArrayList<>();
        for(Layer layer : layers) {
            if(layer.getShown()) {
                l.addAll(layer.getFigures());
            }
        }
        return l;
    }

    public void addLayer() {
        layers.add(new Layer());
    }

    public void deleteLayer(Layer layer) {
        layers.remove(layer);
    }

    public ArrayList<Layer> getLayers() {
        return new ArrayList<>(layers);
    }

    public void showLayer(Layer layer) {
        for(Layer l : layers) {
            if(layer == l) {
                l.setShown(true);
            }
        }
    }

    public void hideLayer(Layer layer) {
        for(Layer l : layers) {
            if(layer == l) {
                l.setShown(false);
            }
        }
    }

    public void changeShadow(Figure figure, ShadowType shadow) {
        figure.getLayer().changeShadow(figure, shadow);
    }

    public void changeBiselado(Figure figure, boolean biselado) {
        figure.getLayer().changeBiselado(figure, biselado);
    }

}
