package security.annotion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import security.api.MethodHandler;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class AnnotationBasedMethodHandler implements MethodHandler {
	
	private Map<Class<? extends Annotation>,AnnotationHandler> handlerList;
	
	public AnnotationBasedMethodHandler() {
		handlerList = new HashMap<Class<? extends Annotation>,AnnotationHandler>();
	}
	
	@Reference(type='*')
	public void addAnnotationHandler(AnnotationHandler handler) {
		if(handler != null) {
			handlerList.put(handler.getHandledAnnotationType(), handler);
		}
	}
	
	public void removeAnnotationHandler(AnnotationHandler handler) {
		handlerList.remove(handler);
	}

	@Override
	public boolean handle(Method method) {
		Annotation[] annotations = method.getDeclaredAnnotations();
		
		boolean handled = false;
		
		for(Annotation annotation : annotations) {
			AnnotationHandler handler = handlerList.get(annotation.annotationType());
			
			if(handler != null) {
				handled = true;
				handler.handle(annotation);
			}
		}
		
		return handled;
	}

}
