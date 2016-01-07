package com.github.vendigo.j7group.key.ambiguity;

/**
 * Rule, how to handle situation when few entities would have equal keys.
 * @author Dmytro Marchenko
 */
public enum KeyAmbiguityPolicy {
    /**
     * Keeps only last occurrence. Using by default.
     */
    KEEP_LAST,
    /**
     * Keeps only first occurrence.
     */
    KEEP_FIRST,
    /**
     * Throw {@link KeyAmbiguityException}
     */
    FAIL_FAST
}
