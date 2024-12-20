package mejai.mejaigg.summoner.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RenewalStatusResponse {
	private LocalDateTime lastUpdatedAt;
}
