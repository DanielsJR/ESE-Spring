package nx.ese.documents.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CourseName {
	
	PRIMERO_A("1-A"), SEGUNDO_A("2-A"), TERCERO_A("3-A"), CUARTO_A("4-A"), QUINTO_A("5-A"),SEXTO_A("6-A"),SEPTIMO_A("7-A"),OCTAVO_A("8-A"),
	PRIMERO_B("1-B"), SEGUNDO_B("2-B"), TERCERO_B("3-B"), CUARTO_B("4-B"), QUINTO_B("5-B"),SEXTO_B("6-B"),SEPTIMO_B("7-B"),OCTAVO_B("8-B"),
	PRIMERO_C("1-C"), SEGUNDO_C("2-C"), TERCERO_C("3-C"), CUARTO_C("4-C"), QUINTO_C("5-C"),SEXTO_C("6-C"),SEPTIMO_C("7-C"),OCTAVO_C("8-C"),
	PRIMERO_D("1-D"), SEGUNDO_D("2-D"), TERCERO_D("3-D"), CUARTO_D("4-D"), QUINTO_D("5-D"),SEXTO_D("6-D"),SEPTIMO_D("7-D"),OCTAVO_D("8-D"),
	PRIMERO_E("1-E"), SEGUNDO_E("2-E"), TERCERO_E("3-E"), CUARTO_E("4-E"), QUINTO_E("5-E"),SEXTO_E("6-E"),SEPTIMO_E("7-E"),OCTAVO_E("8-E"),
	PRIMERO_F("1-F"), SEGUNDO_F("2-F"), TERCERO_F("3-F"), CUARTO_F("4-F"), QUINTO_F("5-F"),SEXTO_F("6-F"),SEPTIMO_F("7-F"),OCTAVO_F("8-F"),
	PRIMERO_G("1-G"), SEGUNDO_G("2-G"), TERCERO_G("3-G"), CUARTO_G("4-G"), QUINTO_G("5-G"),SEXTO_G("6-G"),SEPTIMO_G("7-G"),OCTAVO_G("8-G"),
	PRIMERO_H("1-H"), SEGUNDO_H("2-H"), TERCERO_H("3-H"), CUARTO_H("4-H"), QUINTO_H("5-H"),SEXTO_H("6-H"),SEPTIMO_H("7-H"),OCTAVO_H("8-A"),;
	
	private final String cod;

	private CourseName(String cod) {
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
			if (enumValue.toString().equalsIgnoreCase(value) || ((CourseName) enumValue).getCode().equalsIgnoreCase(value)) {
				return (T) enumValue;
			}
		}

		// Construct an error message that indicates all possible values for the enum.
		StringBuilder errorMessage = new StringBuilder();
		boolean bFirstTime = true;
		for (Enum<?> enumValue : enumClass.getEnumConstants()) {
			errorMessage.append(bFirstTime ? "" : ", ").append(enumValue);
			bFirstTime = false;
		}
		throw new IllegalArgumentException(value + " is invalid value. Supported values are " + errorMessage);
	}

	@JsonCreator
	public static CourseName fromValue(String value) {
		return getEnumFromString(CourseName.class, value);
	}

	@JsonValue
	public String toJson() {
		return getCode();
	}
	
	
}
