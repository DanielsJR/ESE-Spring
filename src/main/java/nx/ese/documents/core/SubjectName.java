package nx.ese.documents.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubjectName {

    MATEMATICAS("Matemáticas"),
    LENGUAJE("Lenguaje"),
    HISTORIA("Historia"),
    ED_FISICA("Educación Física"),
    MUSICA("Música"),
    ARTES_MANUALES("Artes Manuales"),
    INGLES("Ingles");

    private final String cod;

    private SubjectName(String cod) {
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
            if (enumValue.toString().equalsIgnoreCase(value) || ((SubjectName) enumValue).getCode().equalsIgnoreCase(value)) {
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
    public static SubjectName fromValue(String value) {
        return getEnumFromString(SubjectName.class, value);
    }

    @JsonValue
    public String toJson() {
        return getCode();
    }


}
