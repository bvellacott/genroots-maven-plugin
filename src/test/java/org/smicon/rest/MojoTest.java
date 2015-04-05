package org.smicon.rest;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.Test;

public class MojoTest
{

	@Test
	public void noExceptionTest()
	{
		try
		{
			EmberWiringsMojo mojo = new EmberWiringsMojo();
			
			Field field = EmberWiringsMojo.class.getDeclaredField("controllerOutputDirectory");
			field.setAccessible(true);
			field.set(mojo, new File("src/test/java"));
	
			field = EmberWiringsMojo.class.getDeclaredField("emberModelOutputDirectory");
			field.setAccessible(true);
			field.set(mojo, new File("src/test/js/models"));
	
			field = EmberWiringsMojo.class.getDeclaredField("controllerTemplateFileName");
			field.setAccessible(true);
			field.set(mojo, "ControllerTemplate.mustache");
	
			field = EmberWiringsMojo.class.getDeclaredField("emberModelTemplateFileName");
			field.setAccessible(true);
			field.set(mojo, "EmberModel.mustache");
	
			field = EmberWiringsMojo.class.getDeclaredField("controllerClassPostfix");
			field.setAccessible(true);
			field.set(mojo, "Controller");
			
			mojo.execute();
	
			assertTrue(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
