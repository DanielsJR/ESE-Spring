package nx.ese.dtos;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nx.ese.documents.Theme;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {

    @NotNull
    @Getter
    @Setter
    private String name;

    @Setter
    private boolean dark;

    @NotNull
    @Getter
    @Setter
    private String primaryColor;

    @NotNull
    @Getter
    @Setter
    private String accentColor;

    public ThemeDto(Theme theme) {
        this.name = theme.getName();
        this.dark = theme.getDark();
        this.primaryColor = theme.getPrimaryColor();
        this.accentColor = theme.getAccentColor();
    }

    public boolean getDark() {
        return dark;
    }

    @Override
    public String toString() {
        return "ThemeDto{" +
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
        ThemeDto themeDto = (ThemeDto) o;
        return dark == themeDto.dark &&
                name.equals(themeDto.name) &&
                primaryColor.equals(themeDto.primaryColor) &&
                accentColor.equals(themeDto.accentColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, dark, primaryColor, accentColor);
    }


}
