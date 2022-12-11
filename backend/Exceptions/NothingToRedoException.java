package backend.Exceptions;

public class NothingToRedoException extends RuntimeException {
    private final static String MESSAGE = "No hay nada que rehacer";

    public NothingToRedoException() {
        super(MESSAGE);
    }
}

