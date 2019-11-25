package todoapp.web.user;

import javax.annotation.security.RolesAllowed;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import todoapp.core.user.domain.ProfilePictureStorage;
import todoapp.security.UserSession;

@Controller
public class UserController {
	
	private ProfilePictureStorage profilePictureStorage;
	
	public UserController(ProfilePictureStorage profilePictureStorage) {
		this.profilePictureStorage = profilePictureStorage;
	}

	@RolesAllowed({ UserSession.ROLE_USER })
	@GetMapping("/user/profile-picture")
	public @ResponseBody Resource profilePicture(UserSession userSession) {		
		return profilePictureStorage.load(userSession.getUser().getProfilePicture().getUri());
	}

}
