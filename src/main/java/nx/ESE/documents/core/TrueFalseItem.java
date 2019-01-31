package nx.ESE.documents.core;



public class TrueFalseItem {

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


	@Override
	public String toString() {
		return "TrueFalseItem [sentence=" + sentence + ", answer=" + answer + "]";
	}


}
