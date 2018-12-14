package nx.ESE.documents.core;

public enum CourseName {
	
	PRIMERO_A("1A"), SEGUNDO_A("2A"), TERCERO_A("3A"), CUARTO_A("4A"), QUINTO_A("5A"),SEXTO_A("6A"),SEPTIMO_A("7A"),OCTAVO_A("8A"),
	PRIMERO_B("1B"), SEGUNDO_B("2B"), TERCERO_B("3B"), CUARTO_B("4B"), QUINTO_B("5B"),SEXTO_B("6B"),SEPTIMO_B("7B"),OCTAVO_B("8B"),
	PRIMERO_C("1C"), SEGUNDO_C("2C"), TERCERO_C("3C"), CUARTO_C("4C"), QUINTO_C("5C"),SEXTO_C("6C"),SEPTIMO_C("7C"),OCTAVO_C("8C"),
	PRIMERO_D("1D"), SEGUNDO_D("2D"), TERCERO_D("3D"), CUARTO_D("4D"), QUINTO_D("5D"),SEXTO_D("6D"),SEPTIMO_D("7D"),OCTAVO_D("8D"),
	PRIMERO_E("1E"), SEGUNDO_E("2E"), TERCERO_E("3E"), CUARTO_E("4E"), QUINTO_E("5E"),SEXTO_E("6E"),SEPTIMO_E("7E"),OCTAVO_E("8E"),
	PRIMERO_F("1F"), SEGUNDO_F("2F"), TERCERO_F("3F"), CUARTO_F("4F"), QUINTO_F("5F"),SEXTO_F("6F"),SEPTIMO_F("7F"),OCTAVO_F("8F"),
	PRIMERO_G("1G"), SEGUNDO_G("2G"), TERCERO_G("3G"), CUARTO_G("4G"), QUINTO_G("5G"),SEXTO_G("6G"),SEPTIMO_G("7G"),OCTAVO_G("8G"),
	PRIMERO_H("1H"), SEGUNDO_H("2H"), TERCERO_H("3H"), CUARTO_H("4H"), QUINTO_H("5H"),SEXTO_H("6H"),SEPTIMO_H("7H"),OCTAVO_H("8A"),;
	
	private final String cod;

	private CourseName(String cod) {
		this.cod = cod;
	}

	public String getCode() {
		return cod;
	}
	
	
}
