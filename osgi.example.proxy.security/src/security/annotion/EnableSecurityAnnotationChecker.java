package security.annotion;

import java.lang.annotation.Annotation;

import security.api.ClassChecker;
import security.api.MethodHandler;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(
		provide={
			ClassChecker.class
		}
)
public class EnableSecurityAnnotationChecker extends
		AbstractAnnotationBasedChecker {
	
	private MethodHandler handler;
	
	@Reference
    public void setMethodHandler(MethodHandler handler) {
		System.out.println("add ref method handler");
    	if(handler != null) {
    		this.handler = handler;
    	}
    }
    
    public void unsetMethodHandler(MethodHandler handler) {
    	this.handler = null;
    }

	@Override
	protected boolean checkAnnotations(Annotation[] annotations) {
		for(Annotation annotation : annotations) {
			if(annotation instanceof EnableSecurity) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected MethodHandler getMethodHandler() {
		return handler;
	}

}
