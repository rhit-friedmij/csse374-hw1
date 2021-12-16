import java.util.ArrayList;
import java.util.Date;

public class BackendManager {
	private Database db;
	private ArrayList<Cart> carts;
	private ArrayList<DiscountCode> codes;
	private ArrayList<StockItem> stock;
	
	public BackendManager(Database db) {
		this.db = db;
		this.carts = db.loadCarts();
		this.stock = db.loadStock();
		this.codes = db.loadDiscounts();
	}
	
	public ViewCart returnCart(int cartId) {
		Cart toView = findCart(cartId);
		if (toView == null) {
			return null;
		}
		ArrayList<StockItem> items = findItems(toView.getItems());
		ArrayList<ViewItem> viewItems = toViewItems(items);
		
		int totalItems = toView.getNumItems();	
		
		return new ViewCart(totalItems, toView.getTotal(), viewItems);
	}
	

	public Response addItem(int cartId, int itemId) {
		Cart cart = findCart(cartId);
		StockItem toAdd = findItem(itemId);
		if (toAdd.getStock() < 1) {
			return new Response(false, "Error: The requested item is out of stock", null);
		}
		cart.addItem(itemId, toAdd.price);
		toAdd.modifyStock(-1);
		db.writeCart(cart);
		db.writeStock(toAdd);
		return new Response(true, "Success: The requested item has been added to the cart", null);
	}
	
	public Response addDiscount(int cartId, String discountCode) {
		Cart cart = findCart(cartId);
		DiscountCode dCode = findCode(discountCode);
		
		if (cart.getNumFailures() >= 5) {
			return new Response(false, "Error: The user has failed too many attempts in the past day", null);
		} else if (dCode == null) {
			cart.addFailure();
			return new Response(false, "Error: The discount code entered is not a valid code", null);
		} else if (dCode.expirationDate.before(new Date(System.currentTimeMillis()))) {
			cart.addFailure();
			return new Response(false, "Error: The discount code entered has expired", null);
		}
		
		Response toReturn = cart.addCode(dCode);
		if (toReturn.successful) {
			db.writeCart(cart);
		}
		return toReturn;
	}
	
	public Response modifyQuantities(int cartId, ArrayList<Integer> idsToModify, ArrayList<Integer> quantities) {
		Cart cart = findCart(cartId);
		Cart previousState = cart;
		for (int i = 0; i < idsToModify.size(); i++) {
			StockItem item = findItem(idsToModify.get(i));
			if (quantities.get(i) < 0) {
				ArrayList<String> toReturn = new ArrayList<String>();
				toReturn.add(idsToModify.get(i).toString());
				cart = previousState;
				return new Response(false, "Error, the quantity of an item inputted cannot be negative", toReturn);
			} else if ((quantities.get(i) - cart.getItemQuantity(idsToModify.get(i))) > item.getStock()) {
				ArrayList<String> toReturn = new ArrayList<String>();
				toReturn.add("ID of failure: " + idsToModify.get(i).toString() + ", Stock remaining: " + item.getStock()
								+ ", Current Quantity In Cart: " + cart.getItemQuantity(idsToModify.get(i)));
				cart = previousState;
				return new Response(false, "Error: The quantity of an item exceeds the stock remaining (see returnList)", toReturn);
			}
			item.modifyStock(cart.modifyQuantity(idsToModify.get(i), quantities.get(i)));
		}
		for (int j = 0; j < idsToModify.size(); j++) {
			db.writeStock(findItem(idsToModify.get(j)));
		}
		db.writeCart(cart);
		return new Response(true, "Success: The inputted quantities of the specified items have been changed", null);
	}
	
	private Cart findCart(int cartId) {
		int index = 0;
		while (index < carts.size()) {
			if (carts.get(index).getId() == cartId) {
				return carts.get(index);
			}
			index++;
		}
		
		return null;
	}
	
	private ArrayList<StockItem> findItems(ArrayList<CartItem> cartItems) {
		ArrayList<StockItem> items = new ArrayList<StockItem>();
		for (int i = 0; i < cartItems.size(); i++) {
			StockItem item = null;
			int j = 0;
			while (item == null) {
				if (stock.get(j).id == cartItems.get(i).id) {
					items.add(stock.get(j));
					break;
				}
				j++;
			}
		}
		return items;
	}
	
	private StockItem findItem(int itemId) {
		int index = 0;
		while (index < stock.size()) {
			if (stock.get(index).id == itemId) {
				return stock.get(index);
			}
			index++;
		}
		
		return null;
	}
	
	private DiscountCode findCode(String discountCode) {
		int index = 0;
		while (index < codes.size()) {
			if (codes.get(index).code.equals(discountCode)) {
				return codes.get(index);
			}
			index++;
		}
		
		return null;
	}
	
	private ArrayList<ViewItem> toViewItems(ArrayList<StockItem> items) {
		ArrayList<ViewItem> viewItems = new ArrayList<ViewItem>();
		for (int i = 0; i < items.size(); i++) {
			StockItem curItem = items.get(i);
			boolean inStock = false;
			if (curItem.getStock() > 0) {
				inStock = true;
			}
			viewItems.add(new ViewItem(curItem.name, curItem.description, curItem.price.calculatePrice(),
										curItem.picture, inStock));
		}
		return viewItems;
	}
}
