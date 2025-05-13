package de.bund.bva.isyfact.task.exception;

import static de.bund.bva.isyfact.util.text.MessageProvider.createMessage;

import java.io.Serial;

public class TaskException extends Exception {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 13L;

    public TaskException(String ausnahmeId, String... parameter) {
        super(createMessage(ausnahmeId, parameter));
    }

    public TaskException(String ausnahmeId, Throwable cause, String... parameter) {
        super(createMessage(ausnahmeId, parameter), cause);
    }
}
