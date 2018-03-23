package nx.ESE.documents;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nx.ESE.utils.CustomDateDeserializer;
import nx.ESE.utils.CustomDateSerializer;
import nx.ESE.utils.Encrypting;


public class Token {

    private String value;

  	@JsonSerialize(using = CustomDateSerializer.class)
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
