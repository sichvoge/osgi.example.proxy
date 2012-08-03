package security.session;

public interface SessionValidator {
	boolean validate(String username, String sessionid);
}
