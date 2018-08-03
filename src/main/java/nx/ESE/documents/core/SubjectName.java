package nx.ESE.documents.core;

public enum SubjectName {

	MATEMATICAS("mat"),
	LENGUAJE("leng"),
	HISTORIA("hist"),
	ED_FISICA("e_fisc"),
	MUSICA("mus"),
	ARTES_MANUALES("a_manu");

	private final String cod;

	private SubjectName(String cod) {
		this.cod = cod;
	}

	public String getCode() {
		return cod;
	}

}
