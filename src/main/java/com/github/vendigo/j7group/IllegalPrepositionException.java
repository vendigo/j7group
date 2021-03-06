package com.github.vendigo.j7group;

/**
 * Exceptions is thrown when illegal preposition was used. For instance, field in groupBy construction.
 * @author Dmytro Marchenko
 */
public class IllegalPrepositionException extends RuntimeException {
    public IllegalPrepositionException(String message) {
        super(message);
    }
}
