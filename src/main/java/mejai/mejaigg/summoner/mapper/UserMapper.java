package mejai.mejaigg.summoner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.summoner.entity.User;

@Mapper
public interface UserMapper {
	@Mappings({
		// Assuming we take puuid from AccountDto; it could be from SummonerDto as well
		@Mapping(source = "accountDto.puuid", target = "puuid"),
		@Mapping(source = "accountDto.gameName", target = "summonerName"),
		@Mapping(source = "summonerDto.id", target = "summonerId"),
		@Mapping(source = "summonerDto.summonerLevel", target = "summonerLevel"),
		// Ignoring the collections as before
		@Mapping(target = "rank", ignore = true),
		@Mapping(target = "searchHistory", ignore = true)
	})
	User toUserEntity(AccountDto accountDto, SummonerDto summonerDto);

	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
