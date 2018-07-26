package nx.ESE.documents;

public class Theme {

	private String name;
	
	private boolean isDark;
	
	private String color;
	
	public Theme() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Theme(String name, boolean isDark, String color) {
		super();
		this.name = name;
		this.isDark = isDark;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsDark() {
		return isDark;
	}

	public void setIsDark(boolean isDark) {
		this.isDark = isDark;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Theme other = (Theme) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Theme [name=" + name + ", isDark=" + isDark + ", color=" + color + "]";
	}

	


}
