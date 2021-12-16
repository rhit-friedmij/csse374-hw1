import java.util.ArrayList;
import java.util.Date;

public class DiscountCode {
	public String code;
	public ArrayList<Integer> requirements;
	public double percentOff;
	public Date expirationDate;
	
	public DiscountCode(String code, ArrayList<Integer> requirements, double percentOff, Date expirationDate) {
		this.code = code;
		this.requirements = requirements;
		this.percentOff = percentOff;
		this.expirationDate = expirationDate;
	}
	
	public ArrayList<Integer> checkRequirements(ArrayList<Integer> itemIds) {
		return null;
	}
}
