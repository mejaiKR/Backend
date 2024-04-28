package mejai.mejaigg.common.logging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @Component
public class HttpLoggingFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpLoggingFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException,
		ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		// 요청 로깅
		LOGGER.info("[HTTP] Request URI: {} Param : {} Method : {}", request.getRequestURI(),
			request.getQueryString(),
			request.getMethod());
		// 요청에서 특정 헤더 로깅
		// 예: LOGGER.debug("Authorization: {}", request.getHeader("Authorization"));

		//응답 로깅
		filterChain.doFilter(request, response);
		LOGGER.info("[HTTP] Response: {} {}", response.getStatus(), response.getContentType());
	}
}
