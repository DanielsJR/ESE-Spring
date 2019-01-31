package nx.ESE.documents.core;



public class CorrespondItem {

	private String item;
	
	private String correspond;


	public CorrespondItem() {
		super();
		// TODO Auto-generated constructor stub
	}


	public CorrespondItem(String item, String correspond) {
		super();
		this.item = item;
		this.correspond = correspond;
	}


	public String getItem() {
		return item;
	}


	public void setItem(String item) {
		this.item = item;
	}


	public String getCorrespond() {
		return correspond;
	}


	public void setCorrespond(String correspond) {
		this.correspond = correspond;
	}


	@Override
	public String toString() {
		return "CorrespondItem [item=" + item + ", correspond=" + correspond + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correspond == null) ? 0 : correspond.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
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
		if (correspond == null) {
			if (other.correspond != null)
				return false;
		} else if (!correspond.equals(other.correspond))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		return true;
	}



}
