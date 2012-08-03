package security.api;

public interface Info {
	
	Class<?>[] getRelevantClasses();
	
	MethodHandler getMethodHandler();

}
