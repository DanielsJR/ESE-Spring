package nx.ESE.documents;

import java.util.Date;

import nx.ESE.utils.Encrypting;


public class Token {

    private String value;

    private Date creationDate;

    public Token() {
        this.setValue(new Encrypting().encryptInBase64UrlSafe());
    }

    public String getValue() {
        return value;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setValue(String value) {
        this.value = value;
        this.creationDate = new Date();
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
        return "Token [value=" + value + ", creationDate=" + creationDate.toString() + "]";
    }
}
