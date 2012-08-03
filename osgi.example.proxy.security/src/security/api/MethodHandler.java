package security.api;

import java.lang.reflect.Method;

public interface MethodHandler {
	
	boolean handle(Method method);

}
