package mejai.mejaigg.global.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpLoggingFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

		try {
			chain.doFilter(requestWrapper, responseWrapper);
		} finally {
			logRequest(requestWrapper);
			logResponse(requestWrapper, responseWrapper);
			responseWrapper.copyBodyToResponse();
		}
	}

	private void logRequest(ContentCachingRequestWrapper request) throws IOException {
		String requestBody = "";
		if (request.getContentType() != null && request.getContentType().contains("application/json")) {
			byte[] content = request.getContentAsByteArray();
			if (content.length > 0) {
				requestBody = new String(content, StandardCharsets.UTF_8);
			}
		}

		log.info("REQUEST: {} {} - Headers: {}, Body: {}",
			request.getMethod(),
			request.getRequestURI(),
			getHeaders(request),
			requestBody
		);
	}

	private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
		log.info("RESPONSE: {} - Status: {}, Headers: {}",
			request.getRequestURI(),
			response.getStatus(),
			getHeaders(response)
		);
	}

	private String getHeaders(HttpServletRequest request) {
		StringBuilder headerString = new StringBuilder();
		request.getHeaderNames().asIterator().forEachRemaining(headerName ->
			headerString.append(headerName).append(": ").append(request.getHeader(headerName)).append(", "));
		return headerString.toString();
	}

	private String getHeaders(HttpServletResponse response) {
		StringBuilder headerString = new StringBuilder();
		response.getHeaderNames().forEach(headerName ->
			headerString.append(headerName).append(": ").append(response.getHeader(headerName)).append(", "));
		return headerString.toString();
	}
}
