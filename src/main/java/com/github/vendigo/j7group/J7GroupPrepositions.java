package com.github.vendigo.j7group;

import static com.github.vendigo.j7group.ProxyHelper.interceptAsFirstArgument;
import static com.github.vendigo.j7group.ProxyHelper.interceptAsSecondArgument;

public class J7GroupPrepositions {
    private J7GroupPrepositions() {
    }

    public static <T> T field(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T by(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T from(Class<T> entityClass) {
        return interceptAsFirstArgument(entityClass);
    }

    public static <T> T to(Class<T> entityClass) {
        return interceptAsSecondArgument(entityClass);
    }
}
