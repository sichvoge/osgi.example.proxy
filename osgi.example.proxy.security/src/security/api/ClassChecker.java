package security.api;

public interface ClassChecker {
	
	boolean isClassRelevant(Class<?> clazz);
	
	Info getRelevantInfo(Class<?>[] classesToCheck);

}
