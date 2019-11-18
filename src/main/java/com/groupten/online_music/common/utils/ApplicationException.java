package com.groupten.online_music.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApplicationException extends RuntimeException {
    public final int code;

    public ApplicationException() {
        this(0, null, (Throwable) null);
    }

    public ApplicationException(String message) {
        this(0, message, (Throwable) null);
    }

    public ApplicationException(Throwable cause) {
        this(0, null, cause);
    }

    public ApplicationException(String message, Throwable cause) {
        this(0, message, cause);
    }

    public ApplicationException(int code, String message) {
        this(code, message, (Throwable) null);
    }

    public ApplicationException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApplicationException(int code) {
        this.code = code;
    }

    /**
     * Translate the CheckedException to UncheckedException.
     */
    public static RuntimeException unchecked(Throwable ex) {
        if (ex instanceof RuntimeException) {
            return (RuntimeException) ex;
        } else {
            return new RuntimeException(ex);
        }
    }

    /**
     * Translate the ErrorStack to String.
     */
    public static String getStackTraceAsString(Throwable ex) {
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * Get the detail message from local exception and struck.
     */
    public static String getErrorMessageWithNestedException(Throwable ex) {
        Throwable nestedException = ex.getCause();
        return new StringBuilder().append(ex.getMessage()).append(" nested exception is ")
                .append(nestedException.getClass().getName()).append(":").append(nestedException.getMessage())
                .toString();
    }

    /**
     * Get the root cause.
     */
    public static Throwable getRootCause(Throwable ex) {
        Throwable cause;
        while ((cause = ex.getCause()) != null) {
            ex = cause;
        }
        return ex;
    }

    /**
     * Judge the cause by.
     */
    public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = ex;
        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    public int getCode() {
        return code;
    }
}
