package nx.ESE.documents;

import java.util.Arrays;
import java.util.Date;

import javax.validation.constraints.NotNull;

import nx.ESE.utils.NX_DateFormatter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.Setter;

@Document
public class User {

    @Getter
    @Id
    private String id;

    @Indexed(unique = true)
    @NotNull
    @Setter
    @Getter
    private String username;

    @NotNull
    @Getter
    private String password;

    @Setter
    @Getter
    private String firstName;

    @Setter
    @Getter
    private String lastName;

    @Setter
    @Getter
    private String dni;

    @Setter
    @Getter
    private Date birthday;

    @Setter
    @Getter
    private Gender gender;

    @Setter
    @Getter
    private Avatar avatar;

    @Setter
    @Getter
    private String mobile;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    private Commune commune;

    @NotNull
    @Setter
    @Getter
    private Role[] roles;

    @Setter
    @Getter
    private boolean active;

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

    public User() {
        active = true;
    }

    public User(String username, String password, String firstName, String lastName, String dni, Date birthday,
                Gender gender, Avatar avatar, String mobile, String email, String address, Commune commune) {
        this();
        this.username = username;
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.birthday = birthday;
        this.gender = gender;
        this.avatar = avatar;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.commune = commune;
        this.roles = new Role[]{Role.STUDENT};
    }

    public User(String username, String password) {
        this(username, password, null, null, null, null, null, null, null, null, null, null);

    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", dni=" + dni + ", birthday=" + NX_DateFormatter.formatterDate(this.birthday) + ", gender=" + gender
                + ", mobile=" + mobile + ", avatar=" + avatar + ", email=" + email + ", address=" + address
                + ", commune=" + commune + ", roles=" + Arrays.toString(roles) + ", active=" + active + ", createdBy="
                + createdBy + ", createdDate=" + NX_DateFormatter.formatterDate(this.createdDate) + ", lastModifiedBy=" + lastModifiedUser + ", lastModifiedDate="
                + NX_DateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}