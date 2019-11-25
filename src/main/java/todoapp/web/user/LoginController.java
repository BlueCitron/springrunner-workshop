package todoapp.web.user;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import todoapp.core.user.application.UserJoinder;
import todoapp.core.user.application.UserPasswordVerifier;
import todoapp.core.user.domain.User;
import todoapp.core.user.domain.UserEntityNotFoundException;
import todoapp.core.user.domain.UserPasswordNotMatchedException;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private UserPasswordVerifier verifier;
	private UserJoinder joinder;
	private UserSessionRepository userSessionRepository;
	
	public LoginController(UserPasswordVerifier verifier, UserJoinder joinder, UserSessionRepository userSessionRepository) {
		this.verifier = verifier;
		this.joinder = joinder;
		this.userSessionRepository = userSessionRepository;
	}

	@GetMapping
	public void loginForm() {
		
	}
	
	@PostMapping
	public String loginProcess( @Valid LoginCommand command
			                  , BindingResult bindingResult
			                  , Model model) {
		log.info("username: {}, password: {}", command.getUsername(), command.getPassword());
		// String username = request.getParameter("username");
		// String password = request.getParameter("password");
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("message", "사용자 입력 값이 올바르지 않습니다.");
			model.addAttribute("bindingResult", bindingResult);
			return "login";
		}
		
		User logindUser;
		try {
			// 사용자 비밀번호 확인
			logindUser = verifier.verify(command.getUsername(), command.getPassword());
		} catch (UserEntityNotFoundException ignore) {
			// 등록된 사용자가 없으면, 신규 사용자 등록
			logindUser = joinder.join(command.getUsername(), command.getPassword());
		}
		userSessionRepository.set(new UserSession(logindUser));
		
		return "redirect:/todos";
	}

	@ExceptionHandler(UserPasswordNotMatchedException.class)
	public String handleUserPasswordNotMatchedException(Model model) {
		model.addAttribute("message", "사용자 입력이 올바르지 않습니다.");
		return "login";
	}

	static class LoginCommand {
		
		@Size(min = 4, max = 20)
		private String username;
		private String password;
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
	}

}
