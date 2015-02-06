package org.smicon.rest.data.mustachetemplate.controller.modelwrapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;

import com.google.common.primitives.Primitives;

public class Populators {
	
	public static <T> T parse(String aParam, Class<T> aSimpleType) throws ParseException 
	{
		//TODO Parameterise date parsing
		
		Class<?> type = Primitives.wrap(aSimpleType);

		long val = java.sql.Date.parse(aParam);
		
		if(type == String.class) return (T)aParam;
	    if(type == Boolean.class) return (T) new Boolean(aParam);
	    if(type == Byte.class) return (T) new Byte(aParam);
	    if(type == Character.class) return (T)(Character)aParam.charAt(0);
	    if(type == Double.class) return (T) new Double(aParam);
	    if(type == Float.class) return (T) new Float(aParam);
	    if(type == Integer.class) return (T) new Integer(aParam);
	    if(type == Long.class) return (T) new Long(aParam);
	    if(type == Short.class) return (T) new Short(aParam);
	    if(type == java.util.Date.class) return (T) new java.util.Date(aParam);
	    if(type == java.sql.Date.class) return (T) java.sql.Date.valueOf(aParam);
	    if(type == BigDecimal.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseBigDecimal(true);
	    	return (T) new BigDecimal(aParam);
	    }
	    if(type == BigInteger.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseIntegerOnly(true);
	    	return (T) new BigInteger(aParam);
	    }
	    
		throw new IllegalArgumentException("The type argument passed is not a valid simple type accepted by the EJB specification!");
	}
	
}
