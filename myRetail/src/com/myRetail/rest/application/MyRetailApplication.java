package com.myRetail.rest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.myRetail.rest.resource.MyRetailRS;

import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag; 

@SwaggerDefinition(basePath="myRetail/v1",
        info = @Info(
                description = "My Retail API",
                version = "V1",
                title = "My Retail API",
                contact = @Contact(
                   name = "Nithin Gundu", 
                   email = "gundu.nithin@target.com", 
                   url = "http://target.com"
        )),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {
                @Tag(name = "Private", description = "Tag used to denote operations as private")
        }, 
        externalDocs = @ExternalDocs(value = "redsky", url = "http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"))
@ApplicationPath("/v1")
public class MyRetailApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<Class<?>>();
		resources.add(MyRetailRS.class);
		return resources;
	}

}
