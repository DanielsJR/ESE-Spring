package nx.ESE.documents.core;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class TrueFalseItem {

	@NotNull
	@Getter
	@Setter
	private String sentence;

	@NotNull
	@Getter
	@Setter
	private boolean answer;

	@Override
	public String toString() {
		return "TrueFalseItem [sentence=" + sentence + ", answer=" + answer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (answer ? 1231 : 1237);
		result = prime * result + ((sentence == null) ? 0 : sentence.hashCode());
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
		TrueFalseItem other = (TrueFalseItem) obj;
		if (answer != other.answer)
			return false;
		if (sentence == null) {
			if (other.sentence != null)
				return false;
		} else if (!sentence.equals(other.sentence))
			return false;
		return true;
	}

}
