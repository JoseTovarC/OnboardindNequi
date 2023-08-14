package co.com.bancolombia.model.exception;

import java.io.Serializable;


public class DataNotFoundException extends Exception {

    private static final long serialVersionUID = 2524686849100170713L;


    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


    public DataNotFoundException(String message) {
        super(message);
    }



    public DataNotFoundException(Serializable id) {
        super("Entity class not found in persistence layer, id: " + id);
    }
}
