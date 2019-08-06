package nx.ESE.documents.core;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class MultipleSelectionItem {

	@NotNull
	@Getter
	@Setter
	private String sentence;

	@NotNull
	@Getter
	@Setter
	private String alternativeA;

	@NotNull
	@Getter
	@Setter
	private String alternativeB;

	@NotNull
	@Getter
	@Setter
	private String alternativeC;

	@NotNull
	@Getter
	@Setter
	private String alternativeD;

	@NotNull
	@Getter
	@Setter
	private String answer;

	@Override
	public String toString() {
		return "MultipleSelectionItem [sentence=" + sentence + ", alternativeA=" + alternativeA + ", alternativeB="
				+ alternativeB + ", alternativeC=" + alternativeC + ", alternativeD=" + alternativeD + ", answer="
				+ answer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alternativeA == null) ? 0 : alternativeA.hashCode());
		result = prime * result + ((alternativeB == null) ? 0 : alternativeB.hashCode());
		result = prime * result + ((alternativeC == null) ? 0 : alternativeC.hashCode());
		result = prime * result + ((alternativeD == null) ? 0 : alternativeD.hashCode());
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
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
		MultipleSelectionItem other = (MultipleSelectionItem) obj;
		if (alternativeA == null) {
			if (other.alternativeA != null)
				return false;
		} else if (!alternativeA.equals(other.alternativeA))
			return false;
		if (alternativeB == null) {
			if (other.alternativeB != null)
				return false;
		} else if (!alternativeB.equals(other.alternativeB))
			return false;
		if (alternativeC == null) {
			if (other.alternativeC != null)
				return false;
		} else if (!alternativeC.equals(other.alternativeC))
			return false;
		if (alternativeD == null) {
			if (other.alternativeD != null)
				return false;
		} else if (!alternativeD.equals(other.alternativeD))
			return false;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (sentence == null) {
			if (other.sentence != null)
				return false;
		} else if (!sentence.equals(other.sentence))
			return false;
		return true;
	}

}
