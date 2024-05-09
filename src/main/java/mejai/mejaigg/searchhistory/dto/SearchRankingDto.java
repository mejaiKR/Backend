package mejai.mejaigg.searchhistory.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchRankingDto {
	@Schema(description = "연도", example = "2022")
	private String year;
	@Schema(description = "달", example = "3")
	private String month;
	private List<TopUserDTO> topRanking;
}
