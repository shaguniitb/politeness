import java.util.Comparator;


public class scoreMap {
	private String id;
	private String request;
	private Float norm_score;
	
	public String getId(){
		return id;
	}
	
	public scoreMap(String request_id, String request_string, Float norm_score){
		this.id = request_id;
		this.request = request_string;
		this.norm_score = norm_score;			
	}
	
	public Float getScore(){
		return norm_score;
	}

}
