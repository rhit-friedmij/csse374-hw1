
public class Price {
	public double basePrice;
	public double discount;
	
	public Price(double basePrice, double discount) {
		this.basePrice = basePrice;
		this.discount = discount;
	}
	
	public double calculatePrice() {
		return basePrice * (1 - discount);
	}
	
	
}
