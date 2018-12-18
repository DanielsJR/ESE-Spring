package nx.ESE.documents.core;

public class CorrespondItem {

	private int id;
	private String value1;
	private String value2;

	public CorrespondItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CorrespondItem(String value1, String value2) {
		super();
		this.value1 = value1;
		this.value2 = value2;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "CorrespondItem [id=" + id + ", value1=" + value1 + ", value2=" + value2 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorrespondItem other = (CorrespondItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
