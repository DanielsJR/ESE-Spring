package nx.ESE.documents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import nx.ESE.utils.Capitalizer;

public enum Commune {
    ALHUÉ, BUIN, CALERA_DE_TANGO, CERRILLOS, CERRO_NAVIA, COLINA, CONCHALÍ, CURACAVÍ, EL_BOSQUE, EL_MONTE, ESTACIÓN_CENTRAL, HUECHURABA, INDEPENDENCIA, ISLA_DE_MAIPO, LA_CISTERNA, LA_FLORIDA, LA_GRANJA, LA_PINTANA, LA_REINA, LAMPA, LAS_CONDES, LO_BARNECHEA, LO_ESPEJO, LO_PRADO, MACUL, MAIPÚ, MARÍA_PINTO, MELIPILLA, ÑUÑOA, PADRE_HURTADO, PAINE, PEDRO_AGUIRRE_CERDA, PEÑAFLOR, PEÑALOLÉN, PIRQUE, PROVIDENCIA, PUDAHUEL, PUENTE_ALTO, QUILICURA, QUINTA_NORMAL, RECOLETA, RENCA, SAN_BERNARDO, SAN_JOAQUÍN, SAN_JOSÉ_DE_MAIPO, SAN_MIGUEL, SAN_PEDRO, SAN_RAMÓN, SANTIAGO, TALAGANTE, TILTIL, VITACURA;

    public static <T extends Enum<T>> T getEnumFromString(Class<T> enumClass, String value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("EnumClass value can't be null.");
        }

        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            if (enumValue.toString().equalsIgnoreCase(value.toUpperCase().replace(" ", "_"))) {
                return (T) enumValue;
            }
        }

        // Construct an error message that indicates all possible values for the
        // enum.
        StringBuilder errorMessage = new StringBuilder();
        boolean bFirstTime = true;
        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            errorMessage.append(bFirstTime ? "" : ", ").append(enumValue);
            bFirstTime = false;
        }
        throw new IllegalArgumentException(value + " is invalid value. Supported values are " + errorMessage);
    }

    @JsonCreator
    public static Commune fromValue(String value) {
        return getEnumFromString(Commune.class, value);
    }

    @JsonValue
    public String toJson() {
        return Capitalizer.capitalizer(name().toLowerCase().replace("_", " "));
    }

}
