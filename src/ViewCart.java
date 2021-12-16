import java.util.ArrayList;

public class ViewCart {
	public int numItems;
	public double totalPrice;
	public ArrayList<ViewItem> items;
	
	public ViewCart(int numItems, double totalPrice, ArrayList<ViewItem> items) {
		this.numItems = numItems;
		this.totalPrice = totalPrice;
		this.items = items;
	}
}
