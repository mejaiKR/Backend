package kr.mejai.project.domain.summoner.exception;

import org.springframework.http.HttpStatus;

import kr.mejai.project.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StreakErrorCode implements ErrorCode {
	STREAK_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 달의 스트릭을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
