package kr.co.hanbit.mastering.springmvc4.error;

import java.io.Serializable;

/**
 * Created by Jeon on 2017-04-02.
 */
public class EntityNotFoundException extends Exception {

    private static final long serialVersionUID = 4996699486242848682L;
    public  EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
