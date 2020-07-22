package nx.ese.documents;

import java.util.Date;

import javax.validation.constraints.NotNull;

import nx.ese.utils.NX_DateFormatter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Document
public class Preferences {

    @Id
    private String id;

    @DBRef
    @NotNull
    @Setter
    @Getter
    private User user;

    @Setter
    @Getter
    private Theme theme;

    @CreatedBy
    @Getter
    private String createdBy;

    @CreatedDate
    @Getter
    private Date createdDate;

    @LastModifiedBy
    @Getter
    private String lastModifiedUser;

    @LastModifiedDate
    @Getter
    private Date lastModifiedDate;

    public Preferences(User user, Theme theme) {
        super();
        this.user = user;
        this.theme = theme;
    }

    @Override
    public String toString() {
        return "Preferences [id=" + id + ", user=" + user + ", theme=" + theme + ", createdBy=" + createdBy
                + ", createdDate=" + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate="
                + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        Preferences other = (Preferences) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
