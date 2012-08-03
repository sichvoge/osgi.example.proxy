package security.session;

import java.lang.annotation.Annotation;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import security.annotion.AnnotationHandler;

@Component
public class DoSessionValidationAnnotationHandler implements AnnotationHandler {
	
	private SessionValidator validator;
	
	@Reference
	public void setSessionValidation(SessionValidator validator) {
		if(validator != null) {
			this.validator = validator;
		}
	}

	@Override
	public void handle(Annotation annotation) {		
		validator.validate("", "");
	}

	@Override
	public Class<? extends Annotation> getHandledAnnotationType() {
		return DoSessionValidation.class;
	}

}
