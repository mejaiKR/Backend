package mejai.mejaigg.summoner.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mejai.mejaigg.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum SummonerErrorCode implements ErrorCode {
	SUMMONER_NOT_FOUND(HttpStatus.NOT_FOUND, "소환사를 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
