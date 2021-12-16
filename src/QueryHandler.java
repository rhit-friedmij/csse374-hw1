import java.util.ArrayList;

public class QueryHandler {
	BackendManager bm;

	public QueryHandler(BackendManager bm) {
		this.bm = bm;
	}
	
	public ViewCart handleView(int cartId) {
		return null;
	}
	
	public Response handleAdd(int cartId, int itemId) {
		return null;
	}
	
	public Response handleDiscount(int cartId, String discountCode) {
		return null;
	}
	
	public Response handleQuantityModification(int cartId, ArrayList<Integer> idsToModify, ArrayList<Integer> newQuantities) {
		return null;
	}
	
}
