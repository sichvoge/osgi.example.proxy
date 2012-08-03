package security.proxy;

import static org.osgi.framework.ServiceEvent.MODIFIED;
import static org.osgi.framework.ServiceEvent.MODIFIED_ENDMATCH;
import static org.osgi.framework.ServiceEvent.REGISTERED;
import static org.osgi.framework.ServiceEvent.UNREGISTERING;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.hooks.service.EventListenerHook;
import org.osgi.framework.hooks.service.FindHook;
import org.osgi.framework.hooks.service.ListenerHook.ListenerInfo;

import security.api.ClassChecker;
import security.api.Info;
import aQute.bnd.annotation.component.Reference;

public class ProxyHook implements FindHook, EventListenerHook {

    private final BundleContext bc;
    
    public static final String PROXY = "proxied";
    
    private ClassChecker checker;
    
    private List<ServiceRegistration<?>> proxyServiceRegistrations;
    
    private List<String> proxiedClassNames;

    public ProxyHook(BundleContext bc) {
        this.bc = bc;
        
        proxyServiceRegistrations = new LinkedList<ServiceRegistration<?>>();
        proxiedClassNames = new LinkedList<String>();
    }
    
    @Reference
    public void setClassChecker(ClassChecker checker) {
    	System.out.println("add ref");
    	if(checker != null) {
    		this.checker = checker;
    	}
    }
    
    public void unsetClassChecker(ClassChecker checker) {
    	this.checker = null;
    }

	@Override
	public void event(ServiceEvent event,
			Map<BundleContext, Collection<ListenerInfo>> listeners) {
		
		if(checker == null) {
			return;
		}
		
		final ServiceReference<?> serviceReference = event.getServiceReference();
		
        if (serviceReference.getProperty(PROXY) == null && serviceReference.getBundle().getBundleContext() != bc) {
            Bundle bundle = serviceReference.getBundle();

            switch (event.getType()) {
                case REGISTERED: {                    
                    String[] interfaces = (String[]) serviceReference.getProperty("objectClass");                
                    
                    Class<?>[] classesToCheck = ProxyUtils.toClass(interfaces, bundle);
                    
                    Info info = checker.getRelevantInfo(classesToCheck);
                    Class<?>[] relevantClasses = info.getRelevantClasses();
                    
                    if(relevantClasses == null || relevantClasses.length == 0) {
                    	return;
                    }
                    
                    System.out.println("relevant class found. proxy: " + bundle.getSymbolicName());
                    String[] propertyKeys = serviceReference.getPropertyKeys();
                	
                    Dictionary<String, Object> properties = ProxyUtils.buildProps(propertyKeys, serviceReference);
                    
                    ServiceRegistration<?> reg = proxyService(bundle,relevantClasses,properties,
                            new ProxyInvocationHandler(bc, serviceReference, info.getMethodHandler()));
                    
                    proxyServiceRegistrations.add(reg);
                    
                    String[] toString = ProxyUtils.toString(relevantClasses);
                    
                    for(String proxiedClasses : toString) {
                    	proxiedClassNames.add(proxiedClasses);
                    }
                    
                    break;
                }
                case UNREGISTERING: {
                    break;
                }
                case MODIFIED: {
                	break;
                }
                case MODIFIED_ENDMATCH: {
                    break;
                }
            }
        }

	}
	
	public List<ServiceRegistration<?>> getAllPrxiedServiceRegistrations() {
		return proxyServiceRegistrations;
	}

	@Override
	public void find(BundleContext context, String name, String filter,
			boolean allServices, Collection<ServiceReference<?>> references) {
		try {
            if (context.getBundle().getBundleId() == 0 || !proxiedClassNames.contains(name)) {
                return;
            }

            Iterator<ServiceReference<?>> iterator = references.iterator();

            while (iterator.hasNext()) {
                ServiceReference<?> sr = (ServiceReference<?>) iterator.next();

                if (sr.getProperty("proxied") == null) {
                    iterator.remove();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

    private static ServiceRegistration<?> proxyService(Bundle bundleSource,
            Class<?>[] interfaces, Dictionary<String, Object> prop, InvocationHandler proxy) {

        prop.put(PROXY, true);
        
        CombinedLoader loader = new CombinedLoader();

        for (Class<?> c : interfaces) {
            loader.addLoader(c);
        }

        Object loggerProxy = Proxy.newProxyInstance(loader, interfaces,proxy);
        
        return bundleSource.getBundleContext().registerService(ProxyUtils.toString(interfaces),loggerProxy, prop);
    }
    
    static class CombinedLoader extends ClassLoader {
        Set<ClassLoader> loaders = new HashSet<ClassLoader>();

        public void addLoader(ClassLoader loader) {
            loaders.add(loader);
        }

        public void addLoader(Class<?> clazz) {
            addLoader(clazz.getClassLoader());
        }

        public Class<?> findClass(String name) throws ClassNotFoundException {
            for (ClassLoader loader : loaders) {
                try {
                    return loader.loadClass(name);
                } catch (ClassNotFoundException cnfe) {
                    // Try next
                }
            }
            throw new ClassNotFoundException(name);
        }

        public URL getResource(String name) {
            for (ClassLoader loader : loaders) {
                URL url = loader.getResource(name);
                if (url != null)
                    return url;
            }
            return null;
        }
    }

}


