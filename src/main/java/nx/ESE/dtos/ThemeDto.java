package nx.ESE.dtos;

public class ThemeDto {
	
	private String name;
	
	private boolean isDark;
	
	private String color;
	
	public ThemeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public ThemeDto(String name, boolean isDark, String color) {
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

}
