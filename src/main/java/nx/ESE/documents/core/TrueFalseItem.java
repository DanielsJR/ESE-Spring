package nx.ESE.documents.core;

public class TrueFalseItem {

	private int id;

	private String sentence;

	private boolean answer;

	public TrueFalseItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrueFalseItem(String sentence, boolean answer) {
		super();
		this.sentence = sentence;
		this.answer = answer;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public boolean isAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TrueFalseItem [id=" + id + ", sentence=" + sentence + ", answer=" + answer + "]";
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
		TrueFalseItem other = (TrueFalseItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
