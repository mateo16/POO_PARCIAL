package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<Figure> list = new ArrayList<>();

    public void addFigure(Figure figure) {
        list.add(figure);
    }

    public void deleteFigure(Figure figure) {
        list.remove(figure);
    }

    public void setFront(Figure figure) {
        deleteFigure(figure);
        list.addLast(figure);
    }

    public void setBack(Figure figure) {
        deleteFigure(figure);
        list.addFirst(figure);
    }

   
    public Iterable<Figure> figures() {
        return new ArrayList<>(list);
    }

}
