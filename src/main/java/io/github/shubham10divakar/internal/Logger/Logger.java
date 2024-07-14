package io.github.shubham10divakar.internal.Logger;

import java.util.logging.Level;

public class Logger {
    private static boolean loggingEnabled = true; // Default to enabled

    private static Level logLevel = Level.INFO; // Default to enabled

    // Getter method to check if logging is enabled
    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    // Setter method to enable or disable logging
    public static void setLoggingEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static Level getLogLevel() {
        return logLevel;
    }

    public static void setLogLevel(Level logLevel) {
        Logger.logLevel = logLevel;
    }

    public static void log(Level level, String logs) {
        System.out.println(level+ " " +logs);
    }
}
