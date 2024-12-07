package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Layer {

    static private int LAYERCOUNT = 0;

    private List<Figure> figures = new ArrayList<>();
    private int num;
    private boolean shown;

    public Layer() {
        //se crean cada una con un numero siguiente
        this.num = ++LAYERCOUNT;
        //se crean mostradas como dice el enunciado
        this.shown = true;
    }

    public int getNum() {
        return this.num;
    }
    public boolean getShown() {
        return this.shown;
    }
    public ArrayList<Figure> getFigures() {
        return new ArrayList<>(figures);
    }

    public void setShown(boolean state) {
        this.shown = state;
    }

    public void addFigure(Figure figure) {
        figures.add(figure);
    }

    public void deleteFigure(Figure figure) {
        figures.remove(figure);
    }

    public void setFront(Figure figure) {
        deleteFigure(figure);
        figures.addLast(figure);
    }

    public void setBack(Figure figure) {
        deleteFigure(figure);
        figures.addFirst(figure);
    }

    @Override
    public String toString() {
        return "Capa " + num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Layer layer = (Layer) o;
        return num == layer.num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(figures, num, shown);
    }
}
