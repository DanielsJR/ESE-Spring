package nx.ESE.documents;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import nx.ESE.utils.Encrypting;


public class Token {

    private String value;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date createdAt;

    public Token() {
        this.setValue(new Encrypting().encryptInBase64UrlSafe());
    }

    public String getValue() {
        return value;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setValue(String value) {
        this.value = value;
        this.createdAt = new Date();
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return value.equals(((Token) obj).value);
    }

    @Override
    public String toString() {
		String date = "null";
		if (createdAt != null) {
			date = new SimpleDateFormat("dd-MMM-yyyy").format(createdAt.getTime());
		}
        return "Token [value=" + value + ", createdAt=" + date + "]";
    }
}
