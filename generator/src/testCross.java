import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class testCross {

	public static void main(String[] args) {
		String downClues = Reader1.main();
		
		
		System.out.println( "Puzzle: ");
		Crossword myCrossword = CVDetect.genCrossword("example_imgs/puzzleEZ.jpg");
		System.out.println("");
		ArrayList<String> cluesAcross = new ArrayList<String>();
		cluesAcross.add(downClues.substring(76,90));
		int wordLen = myCrossword.getWordLengthDown(9);
		System.out.println("CLUE:");
		System.out.println( downClues.substring(74,90));
		System.out.println("");
		try {
			Driver.main();
			String questions = "";
			System.out.println("Length of word in puzzle: " + wordLen);
			for(int i=0;i<wordLen;i++)
			{
				questions = questions.concat("?");
			}
			System.out.println(questions);
			System.out.println( SolvePuzzle.response(cluesAcross.get(0), wordLen, questions) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
