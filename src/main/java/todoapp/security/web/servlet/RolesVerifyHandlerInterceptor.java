package todoapp.security.web.servlet;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import todoapp.security.AccessDeniedException;
import todoapp.security.UnauthorizedAccessException;
import todoapp.security.support.RolesAllowedSupport;

/**
 * Role(역할) 기반으로 사용자의 사용 권한을 확인하는 인터셉터 구현체
 *
 * @author springrunner.kr@gmail.com
 */
public class RolesVerifyHandlerInterceptor implements HandlerInterceptor, RolesAllowedSupport {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// request.getUserPrincipal();
		// request.isUserInRole(role)		
		
		RolesAllowed rolesAllowed = getRolesAllowed(handler);
		log.info("RolesAllowed : {}", rolesAllowed);
		
		if (Objects.nonNull(rolesAllowed)) {
			// 1. 로그인이 되어있는가?
    		// UserSession userSession = userSessionRepository.get();
    		if (Objects.isNull(request.getUserPrincipal())) {
    			throw new UnauthorizedAccessException();
    		}
    		
    		// 2. 사용자의 권한은 적절한가?
    		Set<String> matchedRoles = Stream.of(rolesAllowed.value())
						    				 // .filter(userSession::hasRole)
    				  				         .filter(request::isUserInRole)    				  				         
						    				 .collect(Collectors.toSet());
    		if (matchedRoles.isEmpty()) {
    			throw new AccessDeniedException();
    		}
    	}
		
        return true;
    }

}
