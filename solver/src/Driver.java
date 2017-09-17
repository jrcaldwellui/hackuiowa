import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Driver {
    public static void main (String args[]) throws IOException {
        //File from https://github.com/donohoe/nyt-crossword/blob/master/clues.txt
        //Dict from https://raw.githubusercontent.com/adambom/dictionary/master/dictionary.txt
        //Thes from https://justenglish.me/2014/04/18/synonyms-for-the-96-most-commonly-used-words-in-english/
        //mobyThes from http://www.gutenberg.org/files/3202/
        URL path = Driver.class.getResource("allClues.txt");
        FileReader file =  new FileReader(path.getFile());
        BufferedReader buffer = new BufferedReader(file);
       //BufferedWriter writer = new BufferedWriter(new FileWriter("allThes.txt"));

        String tempLine;
        String clue = "";
        String answer = "";
        int tempVal;
        boolean foundAnswer=false;
        boolean foundClue = false;
        HashMap<Integer,Clue> clues = new HashMap<>();
        int tempHashVal;
        Clue tempClue;
        boolean blank = false;

        //allClues.txt
        while ((tempLine = buffer.readLine())!=null)
        {
            foundAnswer=false;
            foundClue=false;
            answer="";
            clue="";
           // tempLine = buffer.readLine();
            for(int j=0; j<tempLine.length(); j++)
            {
                tempVal = (int)tempLine.charAt(j);
                if(tempVal==40) //(
                {
                    foundClue=true;
                }
                else if(tempVal==41) //)
                {
                    foundClue=false;
                }
                else if(foundClue)
                {
                    clue=clue+tempLine.charAt(j);
                }

                if(tempVal==91) //[
                {
                    foundAnswer=true;
                }
                else if(tempVal==93) //]
                {
                    foundAnswer=false;
                }
                else if(foundAnswer)
                {
                    answer=answer+tempLine.charAt(j);
                }

            }
            clue=clue.toLowerCase();
            clue=clue.trim();
            answer=answer.toLowerCase();
            answer=answer.trim();
             //adds clue and answer to hash
            tempClue = new Clue(clue);
            tempClue.addAnswer(answer);
            if (clues.containsKey(clue.hashCode()))
            {
                clues.get(clue.hashCode()).addAnswer(answer);
            }
            else {
                clues.put(clue.hashCode(), tempClue);
            }
        }

        //read in common thesaurus
       // path = Driver.class.getResource("commonThes.txt");
        path = Driver.class.getResource("mobyThes.txt");
        file =  new FileReader(path.getFile());
        buffer = new BufferedReader(file);
        HashMap<Integer,Word> words = new HashMap<>();
        boolean onWord=false;
        String word="";
        ArrayList<String> wordsOnLine = new ArrayList<>();
        int count=0;


        //allThes.txt
        while ((tempLine = buffer.readLine())!=null)
        //for(int i=0; i<5; i++)
        {
            //tempLine=buffer.readLine();
            tempLine=tempLine.toLowerCase();
            tempLine=tempLine.trim();
            word="";
            onWord=false;
            //tempLine=buffer.readLine();
            tempLine=tempLine.toLowerCase();
            tempLine=tempLine.trim();
            for(int j=0; j<tempLine.length(); j++)
            {
                if(((int)tempLine.charAt(j)>122 ||(int)tempLine.charAt(j)<97) &&(
                        (int)tempLine.charAt(j)!=32 && tempLine.charAt(j)!='-'))
                {
                    if(onWord)
                    {
                        word=word.trim();
                        //word=Word.makeOneWord(word);
                        wordsOnLine.add(word);
                    }
                    onWord=false;
                    word="";
                }
                else
                {
                    onWord=true;
                }
                if(onWord && (int)tempLine.charAt(j)!=32 && tempLine.charAt(j)!='-')
                {
                    word=word+tempLine.charAt(j);
                }
            }
            word=word.trim();
            //word=Word.makeOneWord(word);
            wordsOnLine.add(word);
           //for(int j=0; j<wordsOnLine.size(); j++)
            //{
              //  if(!words.containsKey(wordsOnLine.get(j).hashCode()))
                //{
                    words.put(wordsOnLine.get(0).hashCode(), new Word(wordsOnLine.get(0), wordsOnLine));
                //}
                //System.out.println(wordsOnLine.get(j));
            //}
            //count++;
            wordsOnLine.clear();
        }
        //System.out.println(count);
        /*
        String tesads= "radical";
       System.out.println(words.containsKey(tesads.hashCode()));
       for(int test=0; test<words.get(tesads.hashCode()).getSynonyms().size(); test++)
       {
           System.out.print(", " + words.get(tesads.hashCode()).getSynonyms().get(test));
       }
       */


        Scanner input = new Scanner(System.in);
        String clueStr = "";
        int len;
        String like;
        while(!clueStr.equals("-1")) {
            System.out.println("Enter the clue:");
            clueStr = input.nextLine();
            clueStr = clueStr.trim();
            clueStr = clueStr.toLowerCase();
            //System.out.println(tempStr);
            ArrayList<String> output = FindAnswer.getAnswer(clues,words, clueStr);
            if(words.containsKey(clueStr.hashCode()))
            {
                for(int i=0; i<words.get(clueStr.hashCode()).getSynonyms().size(); i++)
                {
                    if(!output.contains(words.get(clueStr.hashCode()).getSynonyms().get(i)))
                    {
                        output.add(words.get(clueStr.hashCode()).getSynonyms().get(i));
                    }
                }
            }
            if (output != null) {
                for (int i = 0; i < output.size(); i++) {
                    System.out.println(output.get(i));
                }

                System.out.println("Enter the len:");
                len = input.nextInt();
                output = FindAnswer.narrowAnswerLen(output,len);
                if (output != null) {
                    for (int i = 0; i < output.size(); i++) {
                        System.out.println(output.get(i));
                    }
                } else {
                    System.out.println("Invalid clue");
                }
                System.out.println("Enter the blanks:");
                like=input.next();
                output = FindAnswer.narrowAnswerKnown(output,like);
                if (output != null) {
                    for (int i = 0; i < output.size(); i++) {
                        System.out.println(output.get(i));
                    }
                } else {
                    System.out.println("Invalid clue");
                }
            }
            else {
                System.out.println("Invalid clue");
            }
            input.nextLine();
        }
        /*
        //for crossClues1.txt
        while ((tempLine = buffer.readLine())!=null) {
            foundAnswer = false;
            blank = false;
            answer = "";
            clue = "";
            //tempLine = buffer.readLine();
            // System.out.println(tempLine);
            for (int j = 0; j < tempLine.length(); j++) {
                tempVal = (int) tempLine.charAt(j);
                if (tempVal <= 90 && tempVal >= 65 && j != 0) {
                    tempVal = (int) tempLine.charAt(j - 1);
                    //System.out.print(" " + tempVal);
                    if ((tempVal <= 90 && tempVal >= 65) || tempVal == 9) {
                        foundAnswer = true;
                        answer = answer + tempLine.charAt(j);
                    } else {
                        clue = clue + tempLine.charAt(j);
                    }
                } else if (!foundAnswer) {
                    tempVal = (int) tempLine.charAt(j);
                    if (tempVal == 95 && !blank) {
                        blank = true;
                        clue = clue + "_";
                    } else if (tempVal != 95) {
                        clue = clue + tempLine.charAt(j);
                    }
                }
            }
            clue = clue.toLowerCase();
            clue = clue.trim();
            answer = answer.toLowerCase();
            writer.write("(" + clue + ") ");
            writer.write("[" + answer + "]");
            writer.newLine();
        }
        */
            //tempHashVal=clue.hashCode();

            /* adds clue and answer to hash
            tempClue = new Clue(clue);
            tempClue.addAnswer(answer);
            clues.put(clue.hashCode(),tempClue);
            */
           // System.out.println(clue);
            //System.out.println(answer);
            //System.out.println(buffer.readLine());


      //  while (buffer.readLine()!=null)
        //{

        //}
    }
}
