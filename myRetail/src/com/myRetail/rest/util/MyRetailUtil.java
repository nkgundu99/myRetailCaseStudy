package com.myRetail.rest.util;

import org.json.JSONObject;

import com.myRetail.rest.exception.MyRetailServiceException;
import com.myRetail.rest.model.Product;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class MyRetailUtil 
{
	
	/**
	 * This method is calls the Product information hosted at Red Sky
	 * @param product
	 * @throws MyRetailServiceException 
	 */
	public String[] getProductNameFromRedSky(Product product) throws MyRetailServiceException 
	{
		String[] productRedSky = new String[2];
		Client client = Client.create();
		WebResource webResource = client.resource(
				"http://redsky.target.com/v2/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics");

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		if (response.getStatus() != 200) 
		{
			throw new MyRetailServiceException("Exception while invoking Red Sky with status code "+ response.getStatus());
		}
		
		String output = response.getEntity(String.class);
		System.out.println(output);
		JSONObject obj = new JSONObject(output);
		JSONObject jsonProduct = obj.getJSONObject("product");
		String productId = jsonProduct.getJSONObject("available_to_promise_network").getString("product_id");
		String productName = jsonProduct.getJSONObject("item").getJSONObject("product_description").getString("title");

		//get the Product name and Id from the Resd sky and store in String array
		if (productId != null && productName != null) {
			productRedSky[0] = productId;
			productRedSky[1] = productName;
		}
		return productRedSky;
	}
}
