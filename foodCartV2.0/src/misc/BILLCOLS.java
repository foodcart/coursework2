package misc;

// enum for billing table columns
public enum BILLCOLS {
	ID(0), NAME(1), QUANTITY(2), PRICE(3), DISCOUNT(4), TOTAL(5);
	private final int value;

	BILLCOLS(final int v) {
		value = v;
	}

	public int getValue() {
		return value;
	}
}