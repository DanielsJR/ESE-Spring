package nx.ese.documents.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EvaluationType {

    PRUEBA("Prueba"),
    EXAMEN("Exámen"),
    DISERTACION("Disertación"),
    TRABAJO_GRUPAL("Trabajo Grupal"),
    OTRO("Otro");

    private final String cod;

    private EvaluationType(String cod) {
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
                    || ((EvaluationType) enumValue).getCode().equalsIgnoreCase(value)) {
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
    public static EvaluationType fromValue(String value) {
        return getEnumFromString(EvaluationType.class, value);
    }

    @JsonValue
    public String toJson() {
        return getCode();
    }

}
