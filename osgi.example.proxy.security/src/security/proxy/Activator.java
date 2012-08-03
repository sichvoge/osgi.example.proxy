package security.proxy;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;
import org.osgi.util.tracker.ServiceTracker;

import security.api.ClassChecker;

public class Activator implements BundleActivator {
	
	private ServiceTracker tracker;
	private ProxyHook securityProxyHook;
	
	@SuppressWarnings("rawtypes")
	private static class ProxyCheckerTracker extends ServiceTracker {

		private ProxyHook hook;
		
		public ProxyCheckerTracker(BundleContext context, ProxyHook proxyHook) {
			super(context, ClassChecker.class.getName(), null);
			hook = proxyHook;
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			@SuppressWarnings("unchecked")
			ClassChecker checker = (ClassChecker)context.getService(reference);
			hook.setClassChecker(checker);
			return hook;
		}
		
		@Override
		public void removedService(ServiceReference reference, Object service) {
			hook.unsetClassChecker(null);
			context.ungetService(reference);
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		securityProxyHook = new ProxyHook(context);
        context.registerService(new String[]{FindHook.class.getName(), EventListenerHook.class.getName()},
        		securityProxyHook, null);
        
        tracker = new ProxyCheckerTracker(context, securityProxyHook);
        tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
		
		System.out.println("stop proxy");
		List<ServiceRegistration<?>> proxiedRegs = securityProxyHook.getAllPrxiedServiceRegistrations();
		
		for(ServiceRegistration<?> reg : proxiedRegs) {
			reg.unregister();
		}
	}

}
