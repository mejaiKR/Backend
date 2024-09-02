package mejai.mejaigg.global.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.riot.exception.ClientErrorCode;
import mejai.mejaigg.riot.exception.ClientException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RestApiException.class)
	public ResponseEntity<Object> handleCustomException(RestApiException e) {
		ErrorCode errorCode = e.getErrorCode();
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
		log.warn("handleIllegalArgument", e);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	@ExceptionHandler(ClientException.class)
	public ResponseEntity<Object> handleClientException(ClientException e) {
		log.warn("handleClientException", e);
		ErrorCode errorCode = e.getClientErrorCode();
		return handleExceptionInternal(errorCode, e.getMessage());
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<Object> handleFeignException(FeignException e) {
		log.warn("handleFeignException", e);
		// HTTP 상태 코드 추출
		HttpStatusCode statusCode = HttpStatusCode.valueOf(e.status());
		// 응답 본문 추출
		String responseBody = e.contentUTF8();
		// 기본 오류 메시지
		String defaultMessage = "라이엇 API 호출 중 오류가 발생했습니다.";
		// 오류 메시지 결정
		String message = responseBody != null && !responseBody.isEmpty() ? responseBody : defaultMessage;

		// ErrorCode를 매핑하거나 기본 값 설정
		ErrorCode errorCode = mapErrorCode(statusCode);
		return ResponseEntity.status(statusCode)
			.body(makeErrorResponse(errorCode, message));

	}

	// FeignException의 상태 코드에 따라 ErrorCode를 결정하는 메서드
	private ErrorCode mapErrorCode(HttpStatusCode statusCode) {
		switch (statusCode.value()) {
			case 400:
				return ClientErrorCode.INVALID_PARAMETER;
			case 404:
				return ClientErrorCode.SUMMONER_NOT_FOUND;
			case 429:
				return ClientErrorCode.TOO_MANY_REQUESTS;
			case 500:
				return ClientErrorCode.INTERNAL_SERVER_ERROR;
			// 필요한 다른 상태 코드 처리
			default:
				return ClientErrorCode.UNKNOWN_ERROR;
		}
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException e,
		HttpHeaders headers,
		HttpStatusCode HttpStatusCode,
		WebRequest request) {
		log.warn("handleIllegalArgument", e);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(e, errorCode);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllException(Exception ex) {
		log.warn("handleAllException", ex);
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(errorCode);
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode, message));
	}

	private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(message)
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(e, errorCode));
	}

	private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
		List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ErrorResponse.ValidationError::of)
			.collect(Collectors.toList());

		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.errors(validationErrorList)
			.build();
	}
}
