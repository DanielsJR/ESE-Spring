package nx.ESE.documents.core;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CorrespondItem {

	@NotNull
	@Getter
	@Setter
	private String item;

	@NotNull
	@Getter
	@Setter
	private String correspond;

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
