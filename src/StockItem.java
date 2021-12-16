
public class StockItem {
	public int id;
	public String name;
	public String description;
	public String picture;
	public Price price;
	private int stock;
	
	public StockItem(int id, String name, String description, String picture, Price price, int stock) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.picture = picture;
		this.price = price;
		this.stock = stock;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void modifyStock(int change) {
		stock += change;
	}
	
}
