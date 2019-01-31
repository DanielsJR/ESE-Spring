package nx.ESE.documents.core;


public class IncompleteTextItem {

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


	@Override
	public String toString() {
		return "IncompleteTextItem [sentence=" + sentence + ", answer=" + answer + "]";
	}


}
