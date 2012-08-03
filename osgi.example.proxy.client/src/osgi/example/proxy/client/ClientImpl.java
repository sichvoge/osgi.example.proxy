package osgi.example.proxy.client;

import aQute.bnd.annotation.component.Component;

@Component
public class ClientImpl implements Client {

	@Override
	public void echo() {
		System.out.println("client impl");
	}

}
