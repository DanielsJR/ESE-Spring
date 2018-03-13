package nx.ESE.documents;

public enum Role {
	ADMIN, MANGER, TEACHER, STUDENT, PROXY, AUTHENTICATED;

	public String roleName() {
		return "ROLE_" + this.toString();
	}

}
