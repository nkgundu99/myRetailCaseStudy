package com.myRetail.rest.database;

/**
 * Initial Creation - Nithin Gundu
 * This Class consists of methods that interacts with the Data store - 
 * a. Insert products into Data store
 * b. Find the Product by using Product id
 * c. Find all the products in the database
 * d. Update the product price 
 * e. Map the product information
 * 
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.myRetail.rest.exception.MyRetailServiceException;
import com.myRetail.rest.model.Price;
import com.myRetail.rest.model.Product;

public class MyRetailDBImpl {
	DBCollection productCollection = null;
	Product product = null;
	Price price = null;
	List<Product> productList = null;

	public MyRetailDBImpl() {
		productCollection = MyRetailDBProvider.getCollection("products");
	}

	/**
	 * Method to find the product by Product Id
	 * 
	 * @param id
	 * @return product
	 */
	public Product findProductById(Integer id) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("id", id);
		DBCursor cursor = productCollection.find(searchQuery);
		while (cursor.hasNext()) {
			mapProductInformation(cursor);
		}
		return product;
	}

	/**
	 * Method to return all products from Data store
	 * 
	 * @return
	 */
	public List<Product> getAllProducts() {
		//Commented insert products into DB as we need to need insert only once into DB
		// insertProductsIntoDB();
		DBCursor cursor = productCollection.find();
		productList = new ArrayList<Product>();
		while (cursor.hasNext()) {
			mapProductInformation(cursor);
			productList.add(product);
		}
		return productList;
	}

	/**
	 * Method to update the product price, if productId and Product object is
	 * provided
	 * 
	 * @param productId
	 * @param productToUpdate
	 * @return
	 * @throws MyRetailServiceException
	 */
	public boolean updateProductPrice(int productId, Product productToUpdate) throws MyRetailServiceException {
		boolean isUpdated = false;

		if (productId == productToUpdate.getId() && null != productToUpdate.getCurrent_price()
				&& 0.0 != productToUpdate.getCurrent_price().getValue()
				&& !Objects.equals("", productToUpdate.getCurrent_price().getCurrency_code())) {
			Price price = productToUpdate.getCurrent_price();

			BasicDBObject findProduct = new BasicDBObject("id",productId );
			System.err.println("Find Product" + findProduct.get("id").toString());

			BasicDBObject updatedProduct = new BasicDBObject();
			updatedProduct.append("value", price.getValue());
			updatedProduct.append("currency_code", price.getCurrency_code());

			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updatedProduct.append("current_price",
					new BasicDBObject("value", price.getValue()).append("currency_code", price.getCurrency_code())));
			productCollection.update(findProduct, setQuery);
			isUpdated = true;
		} else {
			// assuming no new insertion of new products if product with given
			// id is not found.
			throw new MyRetailServiceException("Product Id is not found in database to update the Price");
		}
		return isUpdated;
	}

	/**
	 * Maps the product information from the DB Cursor
	 * 
	 * @param cursor
	 */
	private void mapProductInformation(DBCursor cursor) {
		product = new Product();
		price = new Price();
		DBObject obj = cursor.next();
		product.setId(obj.get("id") == null ? 0 : Integer.parseInt(obj.get("id").toString()));
		product.setName(obj.get("name") == null ? " " : obj.get("name").toString());
		DBObject object = (BasicDBObject) obj.get("current_price");
		price.setCurrency_code((String) object.get("currency_code"));
		price.setValue((Double) object.get("value"));
		product.setCurrent_price(price);
	}

	/**
	 * Inserts the Products into DB - Only used once
	 * 
	 * @param table
	 */
	public void insertProductsIntoDB() {
		BasicDBObject product = null;
		product = new BasicDBObject("id", 15117729).append("name", "Raising Arizona").append("current_price",
				new BasicDBObject("value", 14.49).append("currency_code", "USD"));
		productCollection.insert(product);

		product = new BasicDBObject("id", 16483589).append("name", "Pulp Fiction").append("current_price",
				new BasicDBObject("value", 15.49).append("currency_code", "USD"));
		productCollection.insert(product);
		product = new BasicDBObject("id", 16696652).append("name", "Fear and Loathing in Las Vegas")
				.append("current_price", new BasicDBObject("value", 16.49).append("currency_code", "USD"));
		productCollection.insert(product);
		product = new BasicDBObject("id", 16752456).append("name", "True Grit").append("current_price",
				new BasicDBObject("value", 17.49).append("currency_code", "USD"));
		productCollection.insert(product);
		product = new BasicDBObject("id", 15643793).append("name", "Fight Club").append("current_price",
				new BasicDBObject("value", 18.49).append("currency_code", "USD"));
		productCollection.insert(product);
		product = new BasicDBObject("id", 13860428).append("name", "The Big Lebowski (Blu-ray) (Widescreen) from DB(Not from External API)")
				.append("current_price", new BasicDBObject("value", 13.49).append("currency_code", "USD"));
		productCollection.insert(product);
	}

}
