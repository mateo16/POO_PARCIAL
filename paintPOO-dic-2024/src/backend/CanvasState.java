package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    public CanvasState() {
         layers = new ArrayList<>();
         layers.add(new Layer());
         layers.add(new Layer());
         layers.add(new Layer());
    }

    private ArrayList<Layer> layers;

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

}
