package backend;

import backend.model.Point;
import frontend.FrontFigures.FrontFigure;
import javafx.scene.paint.Color;
import backend.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {
    private final ChangesStorage changesStorage = new ChangesStorage();

    private final List<FrontFigure> list = new ArrayList<>();

    private FrontFigure copiedFigure = null;


    /**
     * Se agrega la nueva figura en la lista de figuras,
     * y se agrega la operación en el cache de cambios
     * @param figure la figura por agregar
     */
    public void addFigure(FrontFigure figure) {

        list.add(figure);
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                list.add(figure);
            }

            @Override
            void unExecute() {
                list.remove(figure);
            }

            @Override
            public String toString() { return String.format("Dibujar un %s", figure.getFigureName());}
        });
    }

    /**
     * Se borra la figura de la lista de figuras y se
     * agrega la operación en el cache de cambios
     * @param figure la figura a borrar
     */
    public void deleteFigure(FrontFigure figure) {
        list.remove(figure);
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                list.remove(figure);
            }

            @Override
            void unExecute() {
                list.add(figure);
            }

            @Override
            public String toString() { return String.format("Borrar un %s", figure.getFigureName());}
        });
    }

    /**
     * Devuelve true si el eventPoint se encuentra dentro de los límites de
     * la figura, estando estos definidos por el área, no por el borde.
     * @param figure la figura en que se evalua el punto
     * @param eventPoint el punto que se busca ver si esta dentro de la figura
     * @return true si eventPoint esta dentro de figure
     */
    public boolean figureBelongs(FrontFigure figure, Point eventPoint) {
        return figure.containsPoint(eventPoint);
    }

    /**
     * Guarda la operacion de recoloreado de relleno en el cache de cambios, con los colores antiguos y nuevos
     * @param figure la figura a recolorear
     * @param oldColor el color que tenia la figura antes de recolorear
     * @param newColor el color con que se recoloreara la figura
     */
    public void recolorFill(FrontFigure figure, Color oldColor, Color newColor) {
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                figure.setFillColor(newColor);
            }

            @Override
            void unExecute() {
                figure.setFillColor(oldColor);
            }

            @Override
            public String toString() { return String.format("Cambiar el color de relleno de un %s", figure.getFigureName());}
        });
    }

    /** Guarda la operacion de recoloreado de borde en el cache de cambios, con los colores antiguos y nuevos
     * @param figure la figura a recolorear
     * @param oldColor el color que tenia el borde de la figura antes de recolorear
     * @param newColor el color con que se recoloreara el borde de la figura
     */
    public void recolorBorder(FrontFigure figure, Color oldColor, Color newColor) {
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                figure.setLineColor(newColor);
            }

            @Override
            void unExecute() {
                figure.setLineColor(oldColor);
            }

            @Override
            public String toString() { return String.format("Cambiar el color de borde de un %s", figure.getFigureName());}
        });
    }

    public void formatFigure(FrontFigure figure, Color oldLineColor, Color newLineColor, double oldThickness, double newThickness, Color oldFillColor, Color newFillColor) {
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                figure.setLineColor(newLineColor);
                figure.setThicknessBorder(newThickness);
                figure.setFillColor(newFillColor);
            }

            @Override
            void unExecute() {
                figure.setLineColor(oldLineColor);
                figure.setThicknessBorder(oldThickness);
                figure.setFillColor(oldFillColor);
            }

            @Override
            public String toString() { return String.format("Cambiar el formato de un %s", figure.getFigureName());}
        });
    }

    public void copyFigure(FrontFigure figure) {
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                copiedFigure = figure;
            }

            @Override
            void unExecute() {
                copiedFigure = null;
            }

            @Override
            public String toString() { return String.format("Copiar un %s", figure.getFigureName());}
        });
    }

    public void pasteFigure(FrontFigure figure) {
        list.add(figure);
        changesStorage.addChange(new Operation() {
            @Override
            void execute() {
                list.add(figure);
            }

            @Override
            void unExecute() {
                list.remove(figure);
            }

            @Override
            public String toString() { return String.format("Pegar un %s", figure.getFigureName());}
        });
    }


    /**
     * Si corresponde deshacer algo, se deshace la operación
     * y se cambia la próxima operación de deshacer y rehacer
     */
    public void undo() {
        // El check de si puedo deshacer se hace cuando se habilita/deshabilita el boton
        if (!changesStorage.isUndoCacheEmpty()){
            changesStorage.undo().unExecute();
        } else throw new NothingToUndoException();
    }

    /**
     * Si corresponde rehacer algo, se rehace la operación
     * y se cambia la próxima operación de rehacer y deshacer
     */
    public void redo() {
        // El check de si puedo rehacer se hace cuando se habilita/deshabilita el boton
        if (!changesStorage.isRedoCacheEmpty()){
            changesStorage.redo().execute();
        } else throw new NothingToRedoException();
    }

    /**
     * Devuelve un string indicando la operacion que se realizaria en el siguiente deshacer, o un string vacio si no hay operaciones restantes,
     * con la cantidad de acciones por deshacer restantes
     * @return String con descripcion de deshacer
     */
    public String previewUndo() {
        if ( canUndo() ) return String.format("%s\t%d", changesStorage.getTopElementUndoStack().toString(), changesStorage.undoCacheSize());
        return "0";
    }

    /**
     * Devuelve un string indicando la operacion que se realizaria en el siguiente rehacer, o un string vacio si no hay operaciones restantes,
     * con la cantidad de acciones por deshacer restantes
     * @return String con descripcion de rehacer
     */
    public String previewRedo() {
        if ( canRedo() ) return String.format("%d\t%s", changesStorage.redoCacheSize(), changesStorage.getTopElementRedoStack().toString());
        return "0";
    }

    /**
     * Devuelve true si existe alguna operacion para deshacer
     * @return true si se puede deshacer
     */
    public boolean canUndo() {
        return !changesStorage.isUndoCacheEmpty();
    }

    /**
     * Devuelve true si existe alguna operacion para rehacer
     * @return true si se puede rehacer
     */
    public boolean canRedo() {
        return changesStorage.isTemporaryRedoCacheInitialized() && !changesStorage.isRedoCacheEmpty();
    }

    /**
     * Devuelve una lista iterable con una copia de las figuras en el canvas
     * @return una lista iterable de figuras
     */
    public Iterable<FrontFigure> figures() {
        return new ArrayList<>(list);
    }

}
