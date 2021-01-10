package com.summaryday.framework.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	//映射的数据库对应表名
	public String name();
	//策略：按照数据库表的字段为参照，按照vo中属性为参照
	//当不是TABLE_MODE_COLUMNNAME模式，会将表的字段名称下划线、中划线过滤；当是TABLE_MODE_COLUMNNAME将保留字段名称下划线
	public enum policy{TABLE,VO,TABLE_MODE_COLUMNNAME};
	
	//VO属性字段查询数据装配依据是表字段还是vo属性
	public policy type() default policy.TABLE;
	
	//表名=左半部分固定+变化部分+日期
	//public String appendName()default "";
	//指定日期
	//public enum tableDate{DATE,DATETIME,HMS,NULL};
	//如果表相同，只是表名安装日期增多的情况下，用一个VO文件，指定日期
	//public tableDate dateType() default tableDate.NULL;
}
