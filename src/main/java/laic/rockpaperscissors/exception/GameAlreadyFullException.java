package laic.rockpaperscissors.exception;

public class GameAlreadyFullException extends RuntimeException {
    public GameAlreadyFullException(String message) {
        super(message);
    }
}
