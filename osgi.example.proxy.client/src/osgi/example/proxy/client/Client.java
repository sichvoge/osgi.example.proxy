package osgi.example.proxy.client;

import security.annotion.EnableSecurity;
import security.session.DoSessionValidation;

@EnableSecurity
public interface Client {
	@DoSessionValidation
	public void echo();
}
