package todoapp.web.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import todoapp.security.UserSessionRepository;

@Controller
public class LogoutController {
	
	private UserSessionRepository userSessionRepository;
	
	public LogoutController(UserSessionRepository userSessionRepository) {
		this.userSessionRepository = userSessionRepository;
	}

	@RequestMapping("/logout")
	public View logout() {
		userSessionRepository.clear();
		
		return new RedirectView("/todos");
	}

}
