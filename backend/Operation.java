package backend;

/**
 * Clase command de logica de redo/undo
 */
public abstract class Operation {
    // Decidimos dejar los métodos excecute y unExcecute con visibilidad package private porque
    // de esta manera se restringe lo más posible la visibildad de los mismos. Además, no hay
    // ninguna clase que extienda a Operation, lo que reafirma esta decisión
    /**
     * Metodo que ejecuta nuevamente la operacion realizada inicialmente
     */
    abstract void execute();

    /**
     * Metodo que ejecuta la operacion contraria a la ejecutada inicialmente
     */
    abstract void unExecute();
}
