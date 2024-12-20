package mejai.mejaigg.searchhistory.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingResponse {
	private List<TopUserDTO> ranking;
}
