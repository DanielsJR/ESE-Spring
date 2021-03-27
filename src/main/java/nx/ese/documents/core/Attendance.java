package nx.ese.documents.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nx.ese.utils.NxDateFormatter;
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
import nx.ese.documents.User;

@NoArgsConstructor
@Document
public class Attendance {

	@Id
	@Getter
	private String id;

	@Getter
	@Setter
	private String subjectId;

	@DBRef(lazy = true)
	@Getter
	@Setter
	private List<User> students;

	@Getter
	@Setter
	private Date date;

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

	@Override
	public String toString() {
		return "Attendance{" +
				"id='" + id + '\'' +
				", subjectId='" + subjectId + '\'' +
				", students=" + students +
				", date=" + date +
				", createdBy='" + createdBy + '\'' +
				", createdDate=" + NxDateFormatter.formatterDate(createdDate) +
				", lastModifiedUser='" + lastModifiedUser + '\'' +
				", lastModifiedDate=" + NxDateFormatter.formatterDate(lastModifiedDate) +
				'}';
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((subjectId == null) ? 0 : subjectId.hashCode());
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
		Attendance other = (Attendance) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (subjectId == null) {
			if (other.subjectId != null)
				return false;
		} else if (!subjectId.equals(other.subjectId))
			return false;
		return true;
	}
	
	
	

}
