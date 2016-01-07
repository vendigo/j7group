package com.github.vendigo.j7group.key.ambiguity;

/**
 * Exception which is thrown when few entities have equal keys and
 * {@link KeyAmbiguityPolicy#FAIL_FAST} option is used.
 * @author Dmytro Marchenko
 */
public class KeyAmbiguityException extends RuntimeException {
    public KeyAmbiguityException(String message) {
        super(message);
    }
}
