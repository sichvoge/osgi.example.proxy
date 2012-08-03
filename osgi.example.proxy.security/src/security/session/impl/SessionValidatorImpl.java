package security.session.impl;

import aQute.bnd.annotation.component.Component;
import security.session.SessionValidator;

@Component
public class SessionValidatorImpl implements SessionValidator {

	@Override
	public boolean validate(String username, String sessionid) {
		System.out.println("validate session");
		return false;
	}

}
