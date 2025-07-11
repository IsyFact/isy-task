package de.bund.bva.isyfact.task.exception;

import static de.bund.bva.isyfact.util.text.MessageProvider.createMessage;

import java.io.Serial;

public class TaskRuntimeException extends RuntimeException {

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = -3L;

    private final String ausnahmeId;
    private final String fehlertext;

    public TaskRuntimeException(String ausnahmeId, String... parameter) {
        super(createMessage(ausnahmeId, parameter));
        this.ausnahmeId = ausnahmeId;
        this.fehlertext = createMessage(ausnahmeId, parameter);
    }

    // Getter für Ausnahme-ID
    public String getAusnahmeId() {
        return ausnahmeId;
    }

    // Getter für Fehlertext
    public String getFehlertext() {
        return fehlertext;
    }

}
