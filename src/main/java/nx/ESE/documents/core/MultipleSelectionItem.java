package nx.ESE.documents.core;

public class MultipleSelectionItem {

	private String id;
	private String sentence;
	private String a;
	private String b;
	private String c;
	private String d;
	private String answer;
	
	public MultipleSelectionItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MultipleSelectionItem(String sentence, String a, String b, String c, String d, String answer) {
		super();
		this.sentence = sentence;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.answer = answer;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "MultipleSelectionItem [id=" + id + ", sentence=" + sentence + ", a=" + a + ", b=" + b + ", c=" + c
				+ ", d=" + d + ", answer=" + answer + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
	
	
	

}
