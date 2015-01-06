package org.smicon.rest;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class testLibs {

	public static void main(String[] args) throws Exception {
//		PropertyDescriptor pDesc = new PropertyDescriptor("A", Bean.class);
//		for(Field f : TestClassComposite.class.getDeclaredFields())
//		{
//			f.setAccessible(true);
//		}
        BeanInfo info = Introspector.getBeanInfo( Bean.class, Introspector.USE_ALL_BEANINFO);
 		for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
		{
			System.out.println( pd.getName() );
		}
	}
	
	class Bean {
		public String A;
		public int B;
		
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

}
