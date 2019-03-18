package misc;

public enum TABCOLS {
	COUNTER(0), DESC(1), QUAN(2), PRICE(3), ID(4);
	private final int value;

	TABCOLS(final int v) {
		value = v;
	}

	public int getValue() {
		return value;
	}
}