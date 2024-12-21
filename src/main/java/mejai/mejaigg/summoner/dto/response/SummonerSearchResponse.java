package mejai.mejaigg.summoner.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import mejai.mejaigg.summoner.domain.Summoner;

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
		private String summonerName;
		private String tag;
	}
}
