package fecaf.eduardo.gerenciador.handlers;

public class UnauthorizedExceptionHandler extends RuntimeException {
    public UnauthorizedExceptionHandler(String message) {
        super(message);
    }
}
