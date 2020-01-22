package nx.ESE.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@Getter
	@Setter
    private int count;

    public void increment() {
        this.count++;
    }
}