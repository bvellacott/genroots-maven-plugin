package org.smicon.rest;


public class testLibs {

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
