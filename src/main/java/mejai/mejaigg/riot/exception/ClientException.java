package mejai.mejaigg.riot.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
	private final ClientErrorCode clientErrorCode;

	public ClientException(ClientErrorCode clientErrorCode) {
		super(clientErrorCode.getMessage());
		this.clientErrorCode = clientErrorCode;
	}
}
