package nx.ese.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Theme {

    @Setter
    @Getter
    private String name;

    private boolean isDark;

    @Setter
    @Getter
    private String color;

    public boolean getIsDark() {
        return isDark;
    }

    public void setIsDark(boolean isDark) {
        this.isDark = isDark;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Theme other = (Theme) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Theme [name=" + name + ", isDark=" + isDark + ", color=" + color + "]";
    }


}
