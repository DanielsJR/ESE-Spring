package nx.ESE.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {

	@NotNull
	@Getter
	@Setter
	private String name;

	@NotNull
	//@Getter
	//@Setter
	private boolean isDark;

	@NotNull
	@Getter
	@Setter
	private String color;
	
	public boolean getIsDark() {
		return isDark;
	}

	public void setIsDark(boolean isDark) {
		this.isDark = isDark;
	}

	@Override
	public String toString() {
		return "ThemeDto [name=" + name + ", isDark=" + isDark + ", color=" + color + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + (isDark ? 1231 : 1237);
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
		ThemeDto other = (ThemeDto) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (isDark != other.isDark)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
