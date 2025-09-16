package kr.mejai.project.domain.summoner.dto.response;

import java.util.List;

import kr.mejai.project.domain.summoner.domain.Summoner;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class SummonerSearchResponse {
	private List<SummonerProfile> profiles;

	public SummonerSearchResponse(List<Summoner> profiles) {
		this.profiles = profiles.stream()
			.map(profile -> new SummonerProfile(profile.getSummonerName(), profile.getTagLine()))
			.toList();
	}

	@Data
	@AllArgsConstructor
	private static class SummonerProfile {
		private String id;
		private String tag;
	}
}
