import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Cart {
	private int id;
	private double total;
	private ArrayList<CartItem> items;
	private ArrayList<DiscountCode> codes;
	private Address address;
	private ArrayList<Date> failures;
	
	public Cart(int id, Address address, ArrayList<CartItem> items, ArrayList<DiscountCode> codes, ArrayList<Date> failures) {
		this.id = id;
		this.address = address;
		this.total = 0;
		this.items = items;
		this.codes = codes;
		this.failures = failures;
		updateTotal();
		updateFailures();
	}
	
	public int getId() {
		return id;
	}
	
	public ArrayList<CartItem> getItems() {
		return items;
	}
	
	public double getTotal() {
		updateTotal();
		return total;
	}
	
	public int getNumItems() {
		int totalItems = 0;
		for (int i = 0; i < items.size(); i++) {
			totalItems += items.get(i).quantity;
		}
		return totalItems;
	}
	
	private void updateTotal() {
		double baseTotal = 0;
		for (int i = 0; i < items.size(); i++) {
			CartItem item = items.get(i);
			baseTotal += (item.price.calculatePrice() * item.quantity);
		}
		
		double totalDiscount = 0;
		for (int j = 0; j < codes.size(); j++) {
			totalDiscount += codes.get(j).percentOff;
		}
		
		if (totalDiscount > 1) {
			totalDiscount = 1;
		}
		
		total = calculateTaxes(baseTotal * (1 - totalDiscount));
	}
	
	private double calculateTaxes(double baseTotal) {
		
		if (address.state.equals("IA")) {
			return baseTotal * 1.07;
		} else if (address.state.equals("NY")) {
			return baseTotal * 1.045;
		}
		
		return baseTotal * 1.02;
	}
	
	public void addItem(int itemId, Price price) {
		items.add(new CartItem(itemId, 1, price));
	}
	
	public Response addCode(DiscountCode discountCode) {
		ArrayList<Integer> missing = compareRequirements(discountCode);
		if (missing.size() == 0) {
			codes.add(discountCode);
			return new Response(true, "Success: The requested code has been applied to the cart", null);
		}
		ArrayList<String> missingStrings = new ArrayList<String>();
		for (int i = 0; i < missing.size(); i++) {
			missingStrings.add(missing.get(i).toString());
		}
		return new Response(false, "Error: The item requirements to apply this discount code are not met", missingStrings);
	}
	
	private ArrayList<Integer> compareRequirements(DiscountCode discountCode) {
		ArrayList<Integer> missing = new ArrayList<Integer>();
		for (int i = 0; i < discountCode.requirements.size(); i++) {
			boolean found = false;
			for (int j = 0; j < items.size(); j++) {
				if (discountCode.requirements.get(i) == items.get(j).id) {
					found = true;
				}
			}
			if (!found) {
				missing.add(discountCode.requirements.get(i));
			}
		}
		return missing;
	}

	public void addFailure() {
		failures.add(new Date(System.currentTimeMillis()));
	}
	
	public int getNumFailures() {
		updateFailures();
		return failures.size();
	}
	
	private void updateFailures() {
		Date compareDate = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
        cal.setTime(compareDate);
        cal.add(Calendar.DATE, -1);
        compareDate = cal.getTime();
        ArrayList<Date> toRemove = new ArrayList<Date>();
		for (int i = 0; i < failures.size(); i++) {
			if (failures.get(i).before(compareDate)) {
				toRemove.add(failures.get(i));
			}
		}
		for (int j = 0; j < toRemove.size(); j++) {
			failures.remove(toRemove.get(j));
		}
	}
	
	public int modifyQuantity(int itemId, int newQuantity) {
		CartItem item = findItem(itemId);
		if (newQuantity == 0) {
			removeItem(item);
		}
		int quantityDifference = newQuantity - item.quantity;
		item.quantity = newQuantity;
		return quantityDifference;
	}
	
	private void removeItem(CartItem item) {
		items.remove(item);
		ArrayList<DiscountCode> toRemove = new ArrayList<DiscountCode>();
		for (int i = 0; i < codes.size(); i++) {
			if (compareRequirements(codes.get(i)).size() != 0) {
				toRemove.add(codes.get(i));
			}
		}
		for (int j = 0; j < toRemove.size(); j++) {
			codes.remove(toRemove.get(j));
		}
	}
	
	public int getItemQuantity(int itemId) {
		for (int i = 0; i < items.size(); i++) { 
			if (items.get(i).id == itemId) return items.get(i).quantity;
		}
		
		return 0;
	}
	
	private CartItem findItem(int itemId) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).id == itemId) {
				return items.get(i);
			}
		}
		
		return null;
	}
	
	protected ArrayList<DiscountCode> returnCodes (){
		return codes;
	}
}
