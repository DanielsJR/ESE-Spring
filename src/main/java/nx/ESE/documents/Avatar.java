package nx.ESE.documents;

public class Avatar {

	private String name;
    
	private String type;
	
	private String data;

	public static final String SERVER_AVATAR_PATH = "C:\\ESE\\users\\images\\";

	public Avatar() {
		super();
	}

	public Avatar(String name, String type, String data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Avatar [name=" + name + ", type=" + type + "]";
	}




}
