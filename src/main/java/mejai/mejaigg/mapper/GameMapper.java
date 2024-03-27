package mejai.mejaigg.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mejai.mejaigg.domain.Game;
import mejai.mejaigg.dto.riot.match.InfoDto;

@Mapper
public interface GameMapper {
	GameMapper INSTANCE = Mappers.getMapper(GameMapper.class);

	@Mapping(source = "matchId", target = "matchId")
	Game toGameEntity(InfoDto infoDto, String matchId);
}
