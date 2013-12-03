import java.util.Comparator;

public class scoreMapScoreComparator implements Comparator<scoreMap> {

	@Override
	public int compare(scoreMap score1, scoreMap score2) {
		return (int) score1.getScore().compareTo(score2.getScore());
	}
}
