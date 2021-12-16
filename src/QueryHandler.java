import java.util.ArrayList;

public class QueryHandler {
	BackendManager bm;

	public QueryHandler(BackendManager bm) {
		this.bm = bm;
	}
	
	public ViewCart handleView(int cartId) {
		return bm.returnCart(cartId);
	}
	
	public Response handleAdd(int cartId, int itemId) {
		return bm.addItem(cartId, itemId);
	}
	
	public Response handleDiscount(int cartId, String discountCode) {
		return bm.addDiscount(cartId, discountCode);
	}
	
	public Response handleQuantityModification(int cartId, ArrayList<Integer> idsToModify, ArrayList<Integer> newQuantities) {
		return bm.modifyQuantities(cartId, idsToModify, newQuantities);
	}
	
}
