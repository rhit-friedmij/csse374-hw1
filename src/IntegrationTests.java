

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;

public class IntegrationTests {

	
	// Testing viewing a cart without calculating any discounts
	@Test
	public void testViewNoDiscounts() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Date> fails = new ArrayList<Date>();
		ArrayList<DiscountCode> codes = new ArrayList<DiscountCode>();
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(null);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ViewCart view = bm.returnCart(17);
		
		assertEquals(view.numItems, 3);
		assertTrue(view.items.get(0).name.equals("apple"));
		assertTrue(view.items.get(0).description.equals("crispy"));
		assertTrue(view.items.get(0).picture.equals("url here"));
		assertTrue(view.items.get(0).price == 12.00);
		assertTrue(view.items.get(0).inStock);
		assertTrue((view.totalPrice >= 26.519999)
				&& (view.totalPrice <= 26.520001));
		EasyMock.verify(dbMock);
	}
	
	
	// Testing viewing a cart that has discounts on it
	@Test
	public void testViewDiscounts() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, .2);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", null, .20, new Date());
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date());
		ArrayList<DiscountCode> codes = new ArrayList<DiscountCode>();
		codes.add(code1);
		codes.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ViewCart view = bm.returnCart(17);
		
		assertEquals(view.numItems, 3);
		assertTrue(view.items.get(0).name.equals("apple"));
		assertTrue(view.items.get(0).description.equals("crispy"));
		assertTrue(view.items.get(0).picture.equals("url here"));
		assertTrue((view.items.get(0).price >= 9.599999)
				&& (view.items.get(0).price <= 9.600001));
		assertTrue(view.items.get(0).inStock);
		assertTrue((view.totalPrice >= 15.4060799)
				&& (view.totalPrice <= 15.4060801));
		EasyMock.verify(dbMock);
	}
	
	// Testing adding an item to a cart with 2 items already in it
	@Test
	public void testAddItem() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);

		ArrayList<DiscountCode> codes = new ArrayList<DiscountCode>();
		ArrayList<Date> fails = new ArrayList<Date>();
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(null);
		dbMock.writeCart(cart1);
		dbMock.writeStock(item3b);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		bm.addItem(17, 2);
		
		assertTrue(cart1.getItems().get(2).id == 2);
		assertTrue(cart1.getItems().get(2).quantity == 1);
		assertTrue(cart1.getItems().get(2).price.calculatePrice() == 8.0);
		EasyMock.verify(dbMock);
	}
	
	// Testing adding a discount code to a cart, when the code is not expired, valid,
	// the user hasn't submitted 5 failed attempts in the last 24 hours, and all requirements are met
	@Test
	public void testAddDiscount() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		Response r = bm.addDiscount(17, "BOGO");
		
		assertEquals(r.message, "Success: The requested code has been applied to the cart");
		EasyMock.verify(dbMock);
	}
	
	// Testing adding a discount code to a cart, when the code is expired
	@Test
	public void testAddDiscountExpired() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(20, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		Response r = bm.addDiscount(17, "BOGO");
		
		assertEquals(r.message, "Error: The discount code entered has expired");
		EasyMock.verify(dbMock);
	}
	
	// Testing adding a discount code to a cart, when the code is invalid
	@Test
	public void testAddDiscountInvalid() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		Response r = bm.addDiscount(17, "FREE");
		
		assertEquals(r.message, "Error: The discount code entered is not a valid code");
		EasyMock.verify(dbMock);
	}
	
	// Testing adding a discount code to a cart, when the user has submitted 5 invalid requests in the past day
	@Test
	public void testAddDiscount5Failures() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		
		ArrayList<Date> fails = new ArrayList<Date>();
		Date fail1 = new Date(System.currentTimeMillis());
		Date fail2 = new Date(System.currentTimeMillis());
		Date fail3 = new Date(System.currentTimeMillis());
		Date fail4 = new Date(System.currentTimeMillis());
		Date fail5 = new Date(System.currentTimeMillis());
		fails.add(fail1);
		fails.add(fail2);
		fails.add(fail3);
		fails.add(fail4);
		fails.add(fail5);
		
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		Response r = bm.addDiscount(17, "FREE");
		
		assertEquals(r.message, "Error: The user has failed too many attempts in the past day");
		EasyMock.verify(dbMock);
	}
		
	// Testing adding a discount code to a cart, when the code is invalid
	@Test
	public void testAddDiscountInvalidItems() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		Response r = bm.addDiscount(17, "BOGO");
		
		assertEquals(r.message, "Error: The item requirements to apply this discount code are not met");
		assertEquals(r.returnList.get(0), "2");
		EasyMock.verify(dbMock);
	}

	// Testing increasing quantity of an item
	@Test
	public void testIncreaseQuantity() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeStock(item1b);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(0);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(22);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Success: The inputted quantities of the specified items have been changed");
		assertEquals(24, cart1.getNumItems());
		EasyMock.verify(dbMock);
	}
	
	// Testing decreasing quantity of an item
	@Test
	public void testDecreaseQuantity() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 4, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeStock(item2b);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(1);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(1);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Success: The inputted quantities of the specified items have been changed");
		assertEquals(3, cart1.getNumItems());
		EasyMock.verify(dbMock);
	}
	
	// Testing increasing quantity of one item and decreasing quantity of another
	@Test
	public void testModifyQuantities() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 4, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeStock(item1b);
		dbMock.writeStock(item2b);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(0);
		idsMod.add(1);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(30);
		quants.add(1);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Success: The inputted quantities of the specified items have been changed");
		assertEquals(32, cart1.getNumItems());
		EasyMock.verify(dbMock);
	}
	
	// Testing providing a negative quantity for an item modification
	@Test
	public void testModifyWithNegativeQuantity() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 4, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(0);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(-10);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Error, the quantity of an item inputted cannot be negative");
		assertEquals(r.returnList.get(0), "0");
		EasyMock.verify(dbMock);
	}
	
	// Testing increasing quantity of one item and decreasing quantity of another
		@Test
		public void testIncreaseQuantityHigherThanStock() {
			Database dbMock = EasyMock.createMock(Database.class);
			Price price1 = new Price(12.00, 0);
			Price price2 = new Price(6.00, 0);
			Price price3 = new Price(8.00, 0);
			
			CartItem item1 = new CartItem(0, 1, price1);
			CartItem item2 = new CartItem(1, 4, price2);
			CartItem item3 = new CartItem(2, 1, price3);
			ArrayList<CartItem> items = new ArrayList<CartItem>();
			items.add(item1);
			items.add(item2);
			items.add(item3);

			ArrayList<Integer> req1 = new ArrayList<Integer>();
			req1.add(0);
			req1.add(2);
			ArrayList<Date> fails = new ArrayList<Date>();
			DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
			DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
			ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
			ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
			codes1.add(code1);
			codes1.add(code2);
			codes2.add(code1);
			Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
			ArrayList<Cart> carts = new ArrayList<Cart>();
			carts.add(cart1);
			
			StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
			StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
			StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
			ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
			itemsb.add(item1b);
			itemsb.add(item2b);
			itemsb.add(item3b);
			
			EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
			EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
			EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
			
			EasyMock.replay(dbMock);
			BackendManager bm = new BackendManager(dbMock);
			ArrayList<Integer> idsMod = new ArrayList<Integer>();
			idsMod.add(1);
			ArrayList<Integer> quants = new ArrayList<Integer>();
			quants.add(22);
			Response r = bm.modifyQuantities(17, idsMod, quants);
			
			assertEquals(r.message, "Error: The quantity of an item exceeds the stock remaining (see returnList)");
			assertEquals(r.returnList.get(0), "ID of failure: 1, Stock remaining: 17, Current Quantity In Cart: 4");
			EasyMock.verify(dbMock);
		}
	
	// Testing removing an item
	@Test
	public void testModifyQuantitiesRemoval1() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeStock(item2b);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(1);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(0);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Success: The inputted quantities of the specified items have been changed");
		assertEquals(2, cart1.getNumItems());
		EasyMock.verify(dbMock);
	}
	
	// Testing removing an item which causes a discount code to become invalid
	@Test
	public void testModifyQuantitiesRemoval2() {
		Database dbMock = EasyMock.createMock(Database.class);
		Price price1 = new Price(12.00, 0);
		Price price2 = new Price(6.00, 0);
		Price price3 = new Price(8.00, 0);
		
		CartItem item1 = new CartItem(0, 1, price1);
		CartItem item2 = new CartItem(1, 1, price2);
		CartItem item3 = new CartItem(2, 1, price3);
		ArrayList<CartItem> items = new ArrayList<CartItem>();
		items.add(item1);
		items.add(item2);
		items.add(item3);

		ArrayList<Integer> req1 = new ArrayList<Integer>();
		req1.add(0);
		req1.add(2);
		ArrayList<Date> fails = new ArrayList<Date>();
		DiscountCode code1 = new DiscountCode("BOGO", req1, .20, new Date(130, 1, 1));
		DiscountCode code2 = new DiscountCode("BOGO2", null, .16, new Date(130, 2, 3));
		ArrayList<DiscountCode> codes1 = new ArrayList<DiscountCode>();
		ArrayList<DiscountCode> codes2 = new ArrayList<DiscountCode>();
		codes1.add(code1);
		codes1.add(code2);
		codes2.add(code1);
		Cart cart1 = new Cart(17, new Address("MO", "St. Louis", "10 Null Road"), items, codes2, fails);
		ArrayList<Cart> carts = new ArrayList<Cart>();
		carts.add(cart1);
		
		StockItem item1b = new StockItem(0, "apple", "crispy", "url here", price1, 50);
		StockItem item2b = new StockItem(1, "sauce", "slimy", "url here", price2, 17);
		StockItem item3b = new StockItem(2, "jeans", "flashy", "url here", price3, 3);
		ArrayList<StockItem> itemsb = new ArrayList<StockItem>();
		itemsb.add(item1b);
		itemsb.add(item2b);
		itemsb.add(item3b);
		
		EasyMock.expect(dbMock.loadCarts()).andReturn(carts);
		EasyMock.expect(dbMock.loadStock()).andReturn(itemsb);
		EasyMock.expect(dbMock.loadDiscounts()).andReturn(codes1);
		dbMock.writeStock(item1b);
		dbMock.writeCart(cart1);
		
		EasyMock.replay(dbMock);
		BackendManager bm = new BackendManager(dbMock);
		ArrayList<Integer> idsMod = new ArrayList<Integer>();
		idsMod.add(0);
		ArrayList<Integer> quants = new ArrayList<Integer>();
		quants.add(0);
		Response r = bm.modifyQuantities(17, idsMod, quants);
		
		assertEquals(r.message, "Success: The inputted quantities of the specified items have been changed");
		assertEquals(cart1.getCodes().size(), 0);
		EasyMock.verify(dbMock);
	}
}
