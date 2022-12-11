package backend.Exceptions;

public class NothingToUndoException extends RuntimeException {
    private final static String MESSAGE = "No hay nada que deshacer";

    public NothingToUndoException() {
        super(MESSAGE);
    }
}

