import java.util.ArrayList;

public class Response {
	public boolean successful;
	public String message;
	public ArrayList<String> returnList;
	
	public Response(boolean successful, String message, ArrayList<String> returnList) {
		this.successful = successful;
		this.message = message;
		this.returnList = returnList;
	}

}
