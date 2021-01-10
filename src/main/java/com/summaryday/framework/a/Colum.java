package com.summaryday.framework.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Colum {

	String columName();

	boolean isNUll() default true;

	public enum ObjTypes{STRING,INT,BOOLEAN,LONG,VARCHAR,INTEGER,Timestamp,Date,DateTime,DOUBLE,FLOAT,BigDecimal};
	
	public ObjTypes type() default ObjTypes.STRING;
}
