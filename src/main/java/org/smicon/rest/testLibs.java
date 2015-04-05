package org.smicon.rest;

import java.util.function.Function;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.smicon.rest.emberwiring.metas.ModelValidationDataI;


public class testLibs {
	
	enum _enum {
		a,b,c;
	}

	public static void main(String[] args) throws Exception {
//		PropertyDescriptor pDesc = new PropertyDescriptor("A", Bean.class);
//		for(Field f : TestClassComposite.class.getDeclaredFields())
//		{
//			f.setAccessible(true);
//		}
//        BeanInfo info = Introspector.getBeanInfo( Bean.class, Introspector.USE_ALL_BEANINFO);
// 		for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
//		{
//			System.out.println( pd.getName() );
//		}
		Class<?>[] clss = Bean.class.getDeclaredClasses();
		
		for(Class var : clss) {
			System.out.println(var.getSimpleName());
		}
		
		clss = testLibs.class.getDeclaredClasses();
		
		for(Class var : clss) {
			System.out.println(var.getSimpleName());
		}
		
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run()
//			{
//				SwingWorker sw;
//				for(int i = 0; i < 100000; i++) {
//					sw = new SwingWorker() {
//						
//						@Override
//						protected Object doInBackground() throws Exception
//						{
//							System.out.println("_enum is enumeration = " + _enum.class.isEnum());
//							
//							System.out.println(ModelValidationDataI.default_singular_parameter_types);
//							
//							return null;
//						}
//					};
//					
//					sw.execute();
//				}
//			}
//		});
		
		Function<String, String> f1 = new Function<String, String>() {

			@Override
			public String apply(String t)
			{
				return "f1 -> " + t;
			}};
			
		Function f2 = (lambda) -> "f2 -> " + ((Function<String, String>) lambda).apply("Hello!");
		
		System.out.println(f2.apply(f1));
	}
	
	class Bean<Mean, Lean, Sean> {
		public String A;
		public int B;
		public Mean m;
		public Lean l;
		public Sean s;
		
		public String getA() {
			return A;
		}
		public void setA(String a) {
			A = a;
		}
		public int getB() {
			return B;
		}
		public void setB(int b) {
			B = b;
		}
		
	}
	
	class Mean extends Bean {}

	class Lean extends Mean {}

	class Sean extends Lean {}

}
