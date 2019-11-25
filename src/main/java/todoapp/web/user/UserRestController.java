package todoapp.web.user;

import java.net.URI;

import javax.annotation.security.RolesAllowed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import todoapp.core.user.application.ProfilePictureChanger;
import todoapp.core.user.domain.ProfilePicture;
import todoapp.core.user.domain.ProfilePictureStorage;
import todoapp.core.user.domain.User;
import todoapp.security.UserSession;
import todoapp.security.UserSessionRepository;
import todoapp.web.model.UserProfile;

@RestController
public class UserRestController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private ProfilePictureStorage profilePictureStorage;
	private ProfilePictureChanger profilePictureChanger;
	private UserSessionRepository userSessionRepository;
	
	public UserRestController(ProfilePictureStorage profilePictureStorage, ProfilePictureChanger profilePictureChanger,
			UserSessionRepository userSessionRepository) {
		this.profilePictureStorage = profilePictureStorage;
		this.profilePictureChanger = profilePictureChanger;
		this.userSessionRepository = userSessionRepository;
	}

	@RolesAllowed({ UserSession.ROLE_USER })
	@RequestMapping("/api/user/profile")
	public UserProfile userProfile(UserSession userSession) {		
		return new UserProfile(userSession.getUser());
	}
	
	@RolesAllowed({ UserSession.ROLE_USER })
	@PostMapping("/api/user/profile-picture")
	public UserProfile changeProfilePicture(UserSession userSession, MultipartFile profilePicture) {
		log.info("profilePicture: {}", profilePicture);
		
		User logindUser = userSession.getUser();		
		
		URI uri = profilePictureStorage.save(profilePicture.getResource());		
		User savedUser = profilePictureChanger.change(logindUser.getUsername(), new ProfilePicture(uri));
		userSessionRepository.set(new UserSession(savedUser));
		
		return new UserProfile(savedUser);
	}
	
}
