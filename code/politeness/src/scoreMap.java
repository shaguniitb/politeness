import java.util.Comparator;


public class scoreMap {
	private String id;
	public String request;
	private Float norm_score;
	
	public int numGratitude = 0;
	public int numDeference = 0;
	public int numGreeting = 0;
	public int inPosLexicon = 0;
	public int inNegLexicon = 0;
	
	public int numApologize = 0;
	public int pleaseCount = 0;
	public int pleaseStartCount = 0;
	public int numIndirect = 0;
	
	public int numDirectQuestion = 0;
	public int numDirectStart = 0;
	public int numCounterFactual = 0;
	public int numIndicative = 0;
	
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
