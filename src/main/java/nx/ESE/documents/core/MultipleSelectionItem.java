package nx.ESE.documents.core;

public class MultipleSelectionItem {

	private String sentence;
	
	private String alternativeA;
	
	private String alternativeB;
	
	private String alternativeC;
	
	private String alternativeD;
	
	private String answer;

	public MultipleSelectionItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public MultipleSelectionItem(String sentence, String alternativeA, String alternativeB, String alternativeC,
			String alternativeD, String answer) {
		super();
		this.sentence = sentence;
		this.alternativeA = alternativeA;
		this.alternativeB = alternativeB;
		this.alternativeC = alternativeC;
		this.alternativeD = alternativeD;
		this.answer = answer;
	}



	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getAlternativeA() {
		return alternativeA;
	}

	public void setAlternativeA(String alternativeA) {
		this.alternativeA = alternativeA;
	}

	public String getAlternativeB() {
		return alternativeB;
	}

	public void setAlternativeB(String alternativeB) {
		this.alternativeB = alternativeB;
	}

	public String getAlternativeC() {
		return alternativeC;
	}

	public void setAlternativeC(String alternativeC) {
		this.alternativeC = alternativeC;
	}

	public String getAlternativeD() {
		return alternativeD;
	}

	public void setAlternativeD(String alternativeD) {
		this.alternativeD = alternativeD;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}



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
