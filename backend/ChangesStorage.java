package backend;

import java.util.ArrayDeque;
import java.util.Deque;

public class ChangesStorage {
    private final Deque<Operation> undoCache = new ArrayDeque<>();
    private Deque<Operation> temporaryRedoCache = new ArrayDeque<>();

    /**
     * Agrega un elemento al cache de operaciones, en caso de que hayan operaciones dispoibles para
     * rehacer, las elimina.
     * @param operation La operacion a guardar
     */
    public void addChange(Operation operation) {
        undoCache.push(operation);
        // "Dropeo" el stack de redo anterior
        //temporaryRedoCache = null;
    }

    /**
     * Devuelve, pero no elimina, la ultima operacion hecha.
     * @return La ultima operacion realizada
     */
    public Operation undo() {
        // La creacion del cache temporal se realiza al hacer algun undo
        if ( !isTemporaryRedoCacheInitialized() ) {
            temporaryRedoCache = new ArrayDeque<>();
        }
        Operation retOp = undoCache.peek();
        moveTopElement(undoCache, temporaryRedoCache);
        return retOp;
    }

    /**
     * Devuelve, pero no elimina, la ultima operacion deshecha, o null si no se deshizo alguna operacion.
     * @return Una instancia de operacion, o null si no hay operaciones deshechas antes del ultimo cambio
     */
    public Operation redo() {
        if ( !isTemporaryRedoCacheInitialized() ) {
            return null;
        }
        Operation retOp = temporaryRedoCache.peek();
        moveTopElement(temporaryRedoCache, undoCache);
        return retOp;
    }

    // Funcion auxiliar generica para mover el ultimo elemento insertado en fromStack a toStack
    private <A> void moveTopElement(Deque<A> fromStack, Deque<A> toStack) {
        toStack.push(fromStack.pop());
    }

    /**
     * Devuelve true si el cache de operaciones para rehacer esta creado.
     * @return true si el cache de operaciones para rehacer esta disponible
     */
    protected boolean isTemporaryRedoCacheInitialized() {
        return this.temporaryRedoCache != null;
    }

    /**
     * Devuelve true si el cache de operaciones para rehacer esta creado y tiene elementos dentro.
     * @return true si el cache de operaciones para rehacer tiene elementos
     */
    protected boolean isRedoCacheEmpty() {
        return temporaryRedoCache.isEmpty();
    }

    /**
     * Devuelve true si el cache de operaciones para deshacer tiene elementos dentro.
     * @return true si el cache de operaciones para deshacer tiene elementos dentro.
     */
    protected boolean isUndoCacheEmpty() {
        return undoCache.isEmpty();
    }

    /**
     * Devuelve, pero no elimina, la ultima operacion hecha
     * @return la ultima operacion realizada
     */
    protected Operation getTopElementUndoStack() {
        return undoCache.peek();
    }

    /**
     * Devuelve, pero no elimina, la ultima operacion deshecha
     * @return la ultima operacion deshecha
     */
    protected Operation getTopElementRedoStack() {
        return temporaryRedoCache.peek();
    }

    /**
     * Devuelve la cantidad de operaciones disponibles para deshacer
     * @return la cantidad de operaciones para deshacer
     */
    public int undoCacheSize() { return undoCache.size(); }

    /**
     * Devuelve la cantidad de operaciones disponibles para rehacer
     * @return la cantidad de operaciones para rehacer
     */
    public int redoCacheSize() { return isTemporaryRedoCacheInitialized() ? temporaryRedoCache.size() : 0; }
}
