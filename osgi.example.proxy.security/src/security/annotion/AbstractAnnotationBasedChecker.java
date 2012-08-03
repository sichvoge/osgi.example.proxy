package security.annotion;

import java.lang.annotation.Annotation;

import security.api.ClassChecker;
import security.api.Info;
import security.api.MethodHandler;

public abstract class AbstractAnnotationBasedChecker implements ClassChecker {

	@Override
	public boolean isClassRelevant(Class<?> clazz) {
		try {
			return checkAnnotations(clazz.getAnnotations());
		} catch(Exception ex) {
			return false;
		}
	}
	
	@Override
	public Info getRelevantInfo(Class<?>[] classesToCheck) {
		Class<?>[] buffer = new Class<?>[classesToCheck.length];
		int relevantClassesFound = 0;
		
		for(Class<?> clazzToCheck : classesToCheck) {
			if(isClassRelevant(clazzToCheck)) {
				buffer[relevantClassesFound] = clazzToCheck;
				relevantClassesFound++;
			}
		}
		
		Class<?>[] relevantClasses = new Class<?>[relevantClassesFound];		
		System.arraycopy(buffer, 0, relevantClasses, 0, relevantClassesFound);
		
		MethodHandler handler = getMethodHandler();
		
		return new AnnotationClassInfo(relevantClasses, handler);
	}
	
	protected abstract boolean checkAnnotations(Annotation[] annotations);
	
	protected abstract MethodHandler getMethodHandler();

}
