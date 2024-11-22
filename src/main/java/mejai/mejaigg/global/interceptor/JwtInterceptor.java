package mejai.mejaigg.global.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.jwt.JwtAuth;
import mejai.mejaigg.app.jwt.JwtProvider;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

	private final JwtProvider jwtProvider;

	@Override
	public boolean preHandle(HttpServletRequest request,
		HttpServletResponse response,
		Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod)handler;

		// 메서드에 @JwtAuth 어노테이션이 있는지 확인
		JwtAuth jwtAuth = handlerMethod.getMethodAnnotation(JwtAuth.class);
		if (jwtAuth == null) {
			return true;
		}

		String authHeader = request.getHeader("Authorization");
		if (isJwtToken(request, authHeader)) {
			return true;
		}

		// 인증 실패 시 응답
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write("Unauthorized");
		return false;
	}

	private boolean isJwtToken(HttpServletRequest request, String authHeader) {
		if (authHeader == null) {
			return false;
		}
		if (!authHeader.startsWith("Bearer ")) {
			return false;
		}

		String token = authHeader.substring(7);
		if (!jwtProvider.isValidateToken(token)) {
			return false;
		}

		Long id = jwtProvider.extractId(token);
		request.setAttribute("id", id);
		return true;
	}
}
