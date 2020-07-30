package nx.ese.utils;

import lombok.Getter;
import lombok.Setter;
import nx.ese.documents.Avatar;
import nx.ese.documents.Commune;
import nx.ese.documents.Gender;
import nx.ese.documents.Role;
import nx.ese.dtos.UserDto;

import java.util.Arrays;
import java.util.Date;

public class UserTestDto {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String dni;

    @Getter
    @Setter
    private Date birthday;

    @Getter
    @Setter
    private Gender gender;

    @Getter
    @Setter
    private Avatar avatar;

    @Getter
    @Setter
    private String mobile;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String address;

    @Getter
    @Setter
    private Commune commune;

    @Getter
    @Setter
    private Role[] roles;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String createdBy;

    @Getter
    @Setter
    private Date createdDate;

    @Getter
    @Setter
    private String lastModifiedUser;

    @Getter
    @Setter
    private Date lastModifiedDate;


    public UserTestDto(UserDto user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.dni = user.getDni();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        this.avatar = user.getAvatar();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.commune = user.getCommune();
        this.roles = user.getRoles();
        this.active = user.isActive();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedUser = user.getLastModifiedUser();
        this.lastModifiedDate = user.getLastModifiedDate();
    }

    @Override
    public String toString() {

        return "UserDto [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
                + ", lastName=" + lastName + ", dni=" + dni + ", birthday=" + NxDateFormatter.formatterDate(this.birthday) + ", gender=" + gender
                + ", mobile=" + mobile + ", avatar=" + avatar + ", email=" + email + ", address=" + address
                + ", commune=" + commune + ", roles=" + Arrays.toString(roles) + ", active=" + active + ", createdBy="
                + createdBy + ", createdDate=" + NxDateFormatter.formatterDate(this.createdDate) + ", lastModifiedBy=" + lastModifiedUser + ", lastModifiedDate="
                + NxDateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }


}
