package com.myRetail.rest.resource;

/**
 * Initial Creation - Nithin Gundu
 * This is My Retail Resource Class that serves as the Products service
 */
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.myRetail.rest.database.MyRetailDBImpl;
import com.myRetail.rest.exception.MyRetailServiceException;
import com.myRetail.rest.model.Product;
import com.myRetail.rest.util.MyRetailUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/products")
@Api(value = "Products")

public class MyRetailRS {

	MyRetailDBImpl retailDbImpl = new MyRetailDBImpl();

	@Context
	UriInfo uriInfo;

	@ApiOperation(value = "Retrieve all products")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Your Products", responseContainer = "List", response = Product.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Product.class) })

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getAllProducts() {
		List<Product> productList = retailDbImpl.getAllProducts();
		GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(productList) {
		};
		return Response.ok(entity).build();
	}

	@ApiOperation(value = "Retrieve the products based on Product Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Your Products", response = Product.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = String.class),
			@ApiResponse(code = 404, message = "Product Id is not found in Data Store", response = String.class),
			@ApiResponse(code = 400, message = "Exception while invoking Red Sky Service", response = String.class) })

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getProductById(
			@ApiParam(value = "Filter the products based on product id", required = true) @PathParam("id") String id) {
		if (Integer.parseInt(id) < 0) {
			// 204
			return Response.noContent().build();
		}
		Product foundProduct = retailDbImpl.findProductById(new Integer(id));

		if (null != foundProduct) {
			MyRetailUtil myRetailUtil = new MyRetailUtil();
			String[] productRedSky;
			try {
				// Currently the external API host only one Product object hence
				// returning the String[] with id and Product Name, else return
				// the List of product objects
				productRedSky = myRetailUtil.getProductNameFromRedSky(foundProduct);

				// If the Id is available in the external API- Here it is 13860428
				if (Integer.parseInt(productRedSky[0]) == new Integer(id)) {
					foundProduct.setName(productRedSky[1]);
				}
				return Response.status(200).entity(foundProduct).type(MediaType.APPLICATION_JSON).build();
			} catch (MyRetailServiceException e) {
				return Response.status(400).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
			}
		} else {
			return Response.status(404).entity("Product Id is not found in Data Store").type(MediaType.TEXT_PLAIN)
					.build();
		}

	}

	@ApiOperation(value = "Update the price of the product given the Product Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Update Success", response = String.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = String.class),
			@ApiResponse(code = 400, message = "Please provide the product price", response = String.class) })

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updatePrice(
			@ApiParam(value = "Update the price of the product based on product id", required = true) @PathParam("id") String id,
			@ApiParam(value = "Update the price of the product object", required = true) Product product) {
		if (null == product.getCurrent_price())
			return Response.status(400).entity("Please provide the product price").build();
		if (Integer.parseInt(id) < 0) {
			// 204
			return Response.noContent().build();
		}
		try {
			if (retailDbImpl.updateProductPrice(Integer.parseInt(id), product)) {
				return Response.status(200).entity("SUCCESS").type(MediaType.TEXT_PLAIN).build();
			}
		} catch (NumberFormatException | MyRetailServiceException e) {
			return Response.status(400).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
		}
		return Response.status(500).entity("Internal Server Error").type(MediaType.TEXT_PLAIN).build();
	}
}
