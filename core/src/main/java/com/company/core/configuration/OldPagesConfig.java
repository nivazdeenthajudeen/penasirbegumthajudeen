package com.company.core.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "OldPagesConfig")
public @interface OldPagesConfig {

	@AttributeDefinition(name= "schedulerName", type = AttributeType.STRING)
	public String schedulerName() default "old page scheduler";
	
	@AttributeDefinition(name= "schedulerExpression", type = AttributeType.STRING)
	public String schedulerExpression() default "0 */1 * ? * *";
	
	@AttributeDefinition(name= "schedulerEnabled", type = AttributeType.STRING)
	public String schedulerEnabled() default "true" ;
	
	
}
