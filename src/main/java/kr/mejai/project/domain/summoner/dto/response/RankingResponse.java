package kr.mejai.project.domain.summoner.dto.response;

import java.util.List;

import kr.mejai.project.domain.summoner.dto.TopUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingResponse {
	private List<TopUserDTO> ranking;
}
