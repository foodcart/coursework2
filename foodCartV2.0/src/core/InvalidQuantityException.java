package core;

public class InvalidQuantityException extends Exception {
	 static final long serialVersionUID = -3387516993124229948L;
	public InvalidQuantityException() {
		super("Quantity is less than 1.");
	}
}
