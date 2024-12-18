package mejai.mejaigg.global.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.jwt.JwtAuth;
import mejai.mejaigg.app.jwt.JwtService;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

	private static final String TOKEN_INVALID_MESSAGE = "토큰이 유효하지 않습니다.";
	private static final String TOKEN_EXPIRED_MESSAGE = "토큰이 만료되었습니다.";

	private final JwtService jwtService;

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod)handler;

		// 메서드에 @JwtAuth 어노테이션이 있는지 확인
		JwtAuth jwtAuth = handlerMethod.getMethodAnnotation(JwtAuth.class);
		if (jwtAuth == null) {
			return true;
		}

		return extractJwtToken(request, response);
	}

	private boolean extractJwtToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = extractHeader(request);

		if (token == null) {
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, TOKEN_INVALID_MESSAGE);
			return false;
		}

		try {
			Long id = jwtService.extractId(token);
			request.setAttribute("id", id);
			return true;
		} catch (ExpiredJwtException e) {
			// 토큰이 만료되었다면 403
			sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, TOKEN_EXPIRED_MESSAGE);
			return false;
		} catch (Exception e) {
			// 토큰이 유효하지 않다면 401
			sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, TOKEN_INVALID_MESSAGE);
			return false;
		}
	}

	private String extractHeader(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || authHeader.isEmpty()) {
			return null;
		}
		if (!authHeader.startsWith("Bearer ")) {
			return null;
		}

		return authHeader.substring(7);
	}

	private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
		response.setStatus(status);
		response.getWriter().write(message);
	}
}
