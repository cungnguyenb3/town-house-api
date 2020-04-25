package vn.com.pn.exception;

public class ResourceUnauthorizedException extends RuntimeException{
    public ResourceUnauthorizedException(String message) {
        super(message);
    }
}
