package kr.mejai.project.domain.summoner.dto.request;

import lombok.Data;

@Data
public class SummonerSearchRequest {
	private String id;
	private int count = 5;
}
