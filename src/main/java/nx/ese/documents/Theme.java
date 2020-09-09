package nx.ese.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class Theme {

    @Setter
    @Getter
    private String name;

    @Setter
    private boolean dark;

    @Setter
    @Getter
    private String primaryColor;

    @Setter
    @Getter
    private String accentColor;

    public boolean getDark() {
        return dark;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "name='" + name + '\'' +
                ", dark=" + dark +
                ", primaryColor='" + primaryColor + '\'' +
                ", accentColor='" + accentColor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Theme theme = (Theme) o;
        return dark == theme.dark &&
                name.equals(theme.name) &&
                primaryColor.equals(theme.primaryColor) &&
                accentColor.equals(theme.accentColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dark, primaryColor, accentColor);
    }
}
