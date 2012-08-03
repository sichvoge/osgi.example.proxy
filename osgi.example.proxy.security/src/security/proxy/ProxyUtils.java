package security.proxy;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

public class ProxyUtils {
	
	public static String[] toString(Class<?>[] interfaces) {
        String[] names = new String[interfaces.length];
        
        for(int i = 0; i < interfaces.length; i++) {
        	names[i] = interfaces[i].getName();
        }
        
        return names;
    }
	
	public static Class<?>[] toClass(String[] interfaces, Bundle bl) {
        Class<?>[] names = new Class<?>[interfaces.length];
        
        for(int i = 0; i < interfaces.length; i++) {
        	try {
                names[i] = bl.loadClass(interfaces[i]);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        
        return names;
    }
	
	public static Dictionary<String, Object> buildProps(String[] propertyKeys, ServiceReference<?> reference) {
		Dictionary<String, Object> properties = new Hashtable<String,Object>();
        
		for (String string : propertyKeys) {
            properties.put(string, reference.getProperty(string));
        }
		
        return properties;
    }

}
