package mejai.mejaigg.riot.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ClientErrorCode {
	SUMMONER_NOT_FOUND(HttpStatus.NOT_FOUND,"소환사를 찾을 수 없습니다."),
	TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS,"요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"라이엇 서버 내부 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	ClientErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
