package sparta.cloudassignment.exception;

public class UploadException extends RuntimeException {
    public UploadException() {
    }
    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
