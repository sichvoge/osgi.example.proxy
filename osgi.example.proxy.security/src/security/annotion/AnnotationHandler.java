package security.annotion;

import java.lang.annotation.Annotation;


/**
 * @author Christian Vogel
 *
 * @version 1.0.0
 * @since 1.0
 */
public interface AnnotationHandler {
	
	void handle(Annotation annotation);
	
	Class<? extends Annotation> getHandledAnnotationType();

}
