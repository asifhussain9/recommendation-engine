package com.example.recommendationengine.util;

import java.util.Optional;
import java.util.function.Supplier;

public class SafeGetUtil {
    /**
     * Utility method to avoid NullPointerException in case of nested objects.     *     * @param resolver - Supplier which passes nested object
     *     * @return Optional with value if present, or empty if null or causes NullPointerException
     */    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        try {
            T result = resolver.get();
            return Optional.ofNullable(result);
        }
        catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
