package core;
/**
 * 
 */

/**
 * This class is the standard class for creating the menu for the application.
 * 
 * @author Anwar Kamil
 * @version 1.0
 * @since 6/2/2019
 *
 */
public class Item {
	private String id;
	private String category;
	private String description;
	private double cost;
	
	/**
	 * This is the constructor for the class which initializes the object and its variables.
	 * @param id
	 * @param category
	 * @param description
	 * @param cost
	 */
//	public Item(String id, String category, String description, double cost) {
	public Item(String id, String category, String description, double cost) {	
		this.id = id;
		this.category = category;
		this.description = description;
		this.cost = cost;
	}

	/**
	 * This method is used to fetch the id of the item instance.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * This method is used to fetch the category of the item instance.
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * This method is used to fetch the description of the item instance.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method is used to fetch the cost of the item instance.
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	
	

}
