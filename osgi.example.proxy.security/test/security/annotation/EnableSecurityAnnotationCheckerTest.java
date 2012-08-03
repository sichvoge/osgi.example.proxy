package security.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import security.annotion.EnableSecurityAnnotationChecker;
import security.api.ClassChecker;
import test.resource.EnableSecurityTestClass;
import test.resource.NoRelevantAnnotationTestClass;
import test.resource.NonAnnotatedTestClass;
import test.resource.TwoAnnotationTestClass;

public class EnableSecurityAnnotationCheckerTest {
	
	private ClassChecker checker; 

	@Before
	public void setUp() throws Exception {
		checker = new EnableSecurityAnnotationChecker();
	}

	@Test
	public void testIsProxyRelevantForClass() {
		assertTrue(checker.isClassRelevant(EnableSecurityTestClass.class));
		assertFalse(checker.isClassRelevant(NonAnnotatedTestClass.class));
		assertFalse(checker.isClassRelevant(NoRelevantAnnotationTestClass.class));
		assertTrue(checker.isClassRelevant(TwoAnnotationTestClass.class));
	}
	
	@Test
	public void testBadParameters() {
		assertFalse(checker.isClassRelevant(null));
	}

	@Test
	public void testGetRelevantClasses() {
		Class<?>[] testSet = { EnableSecurityTestClass.class , NoRelevantAnnotationTestClass.class };
		
		
		Class<?>[] actual = checker.getRelevantInfo(testSet).getRelevantClasses();
		
		assertNotNull(actual);
		assertEquals(1, actual.length);
		
		assertEquals(EnableSecurityTestClass.class, actual[0]);
	}
}
