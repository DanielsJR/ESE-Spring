package nx.ESE.documents.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum QuizLevel {

    PRIMERO_BASICO("Primero Básico"),
    SEGUNDO_BASICO("Segundo Básico"),
    TERCERO_BASICO("Tercero Básico"),
    CUARTO_BASICO("Cuarto Básico"),
    QUINTO_BASICO("Quinto Básico"),
    SEXTO_BASICO("Sexto Básico"),
    SEPTIMO_BASICO("Septimo Básico"),
    OCTAVO_BASICO("Octavo Básico");

    private final String cod;

    private QuizLevel(String cod) {
        this.cod = cod;
    }

    public String getCode() {
        return cod;
    }

    public static <T extends Enum<T>> T getEnumFromString(Class<T> enumClass, String value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("EnumClass value can't be null.");
        }

        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            if (enumValue.toString().equalsIgnoreCase(value)
                    || ((QuizLevel) enumValue).getCode().equalsIgnoreCase(value)) {
                return (T) enumValue;
            }
        }


        StringBuilder errorMessage = new StringBuilder();
        boolean bFirstTime = true;
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            errorMessage.append(bFirstTime ? "" : ", ").append(enumValue);
            bFirstTime = false;
        }
        throw new IllegalArgumentException(value + " is invalid value. Supported values are " + errorMessage);
    }

    @JsonCreator
    public static QuizLevel fromValue(String value) {
        return getEnumFromString(QuizLevel.class, value);
    }

    @JsonValue
    public String toJson() {
        return getCode();
    }

}
