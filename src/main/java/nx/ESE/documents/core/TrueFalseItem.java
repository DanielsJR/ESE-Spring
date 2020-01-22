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
	private boolean answer;

	private boolean correct;

	public boolean getCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public TrueFalseItem(@NotNull String sentence, @NotNull boolean answer) {
		super();
		this.sentence = sentence;
		this.answer = answer;
	}

	public boolean getAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		return "TrueFalseItem [sentence=" + sentence + ", answer=" + answer + ", correct=" + correct + "]";
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
