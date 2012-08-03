package osgi.example.proxy.command;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.felix.shell.Command;

import osgi.example.proxy.client.Client;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class EchoCommand implements Command {
	
	private List<Client> clients = new ArrayList<Client>();
	
	@Reference(type='*')
	public synchronized void addClient(Client client) {
		System.out.println("added client");
		this.clients.add(client);
	}
	
	public synchronized void removeClient(Client client) {
		System.out.println(clients.get(0).hashCode() + "," + client.hashCode());
		
		System.out.println("contains: " + clients.contains(client));
		
		System.out.println("removed client:" + this.clients.remove(client));
	}

	@Override
	public void execute(String arg0, PrintStream arg1, PrintStream arg2) {
		if(clients == null || clients.size() == 0) {
			System.out.println("no client");
		} else {
			for(Client cl : clients) {
				cl.echo();
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "echo";
	}

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return "echo";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "echo";
	}

}
