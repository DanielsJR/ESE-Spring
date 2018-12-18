package nx.ESE.documents.core;

public class IncompleteTextItem {

	private int id;
	private String sentence;
	private String answer;

	public IncompleteTextItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IncompleteTextItem(String sentence, String answer) {
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "IncompleteTextItem [id=" + id + ", sentence=" + sentence + ", answer=" + answer + "]";
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
		IncompleteTextItem other = (IncompleteTextItem) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
