package nx.ese.documents.core;


import java.time.LocalDate;
import java.util.Date;

import nx.ese.utils.NxDateFormatter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@Document
public class Evaluation {

    @Getter
    private String id;

    @Getter
    @Setter
    private EvaluationType type;

    @Getter
    @Setter
    private String title;

    @DBRef
    @Getter
    @Setter
    private Subject subject;

    @DBRef
    @Getter
    @Setter
    private Quiz quiz;

    @Getter
    @Setter
    private LocalDate date;

    @Setter
    private boolean open;

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

    public boolean getOpen() {
        return open;
    }


    @Override
    public String toString() {
        return "Evaluation [id=" + id + ", type=" + type + ", title=" + title + ", subject=" + subject + ", quiz="
                + quiz + ", date=" + this.date + ", open=" + open + ", createdBy=" + createdBy + ", createdDate=" + NxDateFormatter.formatterDate(this.createdDate)
                + ", lastModifiedUser=" + lastModifiedUser + ", lastModifiedDate=" + NxDateFormatter.formatterDate(this.lastModifiedDate) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((quiz == null) ? 0 : quiz.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        Evaluation other = (Evaluation) obj;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (quiz == null) {
            if (other.quiz != null)
                return false;
        } else if (!quiz.equals(other.quiz))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }


}
