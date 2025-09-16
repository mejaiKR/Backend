package kr.mejai.project.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	String name();

	HttpStatus getHttpStatus();

	String getMessage();
}
