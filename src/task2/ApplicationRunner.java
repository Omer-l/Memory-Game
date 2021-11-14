import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Arrays;

public class ApplicationRunner {

    //GLOBAL VARIABLES
    //booleans
    public static boolean isGameOver = false;
    public static boolean isMatch = false;
    //Array of XsAndWords
    public static String[][] arrayOfXsAndWords;
    //Array of already found cards
    public static String[] arrayOfMatched = new String[64];
    //user pick number.
    public static int pickNum = 0;
    //matches counter
    public static int matches = 0;
 
    public static void main(String[] args) 
    {
        welcomePrompt();
	  }

    //Welcome prompt user.
    public static void welcomePrompt()
    {
        Scanner input = new Scanner(System.in);
        
        //Error handling to see if user number is not a digit, re runs the procedure.
        try
        {
            System.out.print("\n\n-----------------------------------\nWelcome To The Memory Square Game\n-----------------------------------");
            System.out.print("\nEasy................1\n"
                            + "Intermediate........2\n"
                            + "Difficult...........3\n"
                            + "Just give up now....0\n\n"
                            + "Select your preferred level: ");
            int level = input.nextInt();
            //See if valid input, if invalid number, re run, if not number, then error message.
            if(level < 0 || level > 3)
            {
                    errorMessage();
                    //If invalid number, rerun this method
                    welcomePrompt();
            }
            else if(level == 0)
            {
                System.out.print("Thank you for playing.");
                System.exit(0);
            }
            else
            {
                //elements and the file name based on user input difficult
                int elements = elements(level);
                String fileName = findFileName(level);

                //Build Array of objects(nested array) with Words and X's
                String arrayWords[] = arrayWords(fileName, elements);
                String arrayX[] = arrayX(elements);

                ApplicationRunner.arrayOfXsAndWords = setUpXandWords(arrayWords, arrayX, elements);
                
                arrayWords(fileName, elements(level));
                printTurn(arrayOfXsAndWords, elements);
            
                while(isGameOver == false) 
                {
                    //print board
                    printTurn(arrayOfXsAndWords, elements);
                }
            
            
            }
        }
        //If user enters a letter or a weird character
        catch(InputMismatchException ime)
        {
            errorMessage();
            welcomePrompt();
        }
    }
    //Error message in case user puts in weird input
    public static void errorMessage()
    {
        System.out.println("*********INVALID INPUT***********");
    }

    //Find number of elements in array
    public static int elements(int level)
    {
        int elements = 0;
        switch(level)
        {
            case 1: elements = 16; break;
            case 2: elements = 36; break;
            case 3: elements = 64; break;
        }
        return elements;
    }

    //See which file name to find depending on the level
    public static String findFileName(int level)
    {
        if(level == 1)
            return "small";
        else if(level == 2)
            return "medium";
        else
            return "large";
    }

    //Array of X's
    public static String[] arrayX(int elements)
    {
        //Declare array and assign the index to go up total number of words(*2).
        String[] arrayX = new String[elements];
        
        //Make an array of XXXXXX's
        for(int i = 0; i < arrayX.length; i++)
        {
            arrayX[i] = "XXXXXXXXXXX";
        }
        return arrayX;
    }

    //shuffle words method and make an array depending on the fileName and number of words
    public static String[] arrayWords(String fileName, int elements)
    {
        //Make an array size of the number of words(*2)
        String[] arrayWords = new String[elements];
       
        //try to get file, if not catch FileNotFoundException
        try
        {
            //Get difficulty level file
            String dataFile = System.getProperty("user.dir") + File.separator + fileName + ".txt";
            File dataObject = new File(dataFile);

            //Scanner to read file
            Scanner fileRead = new Scanner(dataObject); 

            //loop fill a Temporary array of words, duplicate the words using k iteration that fills the index with the second card
            //j starts from index 0, k(2nd pair) starts from highest index of the array.
            for(int j = 0, k = elements - 1; j < elements / 2 || k > elements / 2 || fileRead.hasNextLine(); j++, k--)
            { 
            //get word.
            String tmpData = fileRead.nextLine();
            //fill word in array j
            arrayWords[j] = tmpData;
            //Store duplicate for pairing in array k
            arrayWords[k] = tmpData;
            }
        
            for(int i = 0; i < arrayWords.length; i++)
            {
                int index = (int)(Math.random() * arrayWords.length);
                String temp = arrayWords[i];
                arrayWords[i] = arrayWords[index];
                arrayWords[index] = temp;
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println("FILE NOT FOUND");
            e.printStackTrace();
        }
        //return array of words, ready to be displayed.
        return arrayWords;
    }
    
    //void method for creating the array of the XXXXX's and the Words
    public static String[][] setUpXandWords(String[] words, String[] Xs, int elements)
    {
        //Create an array of objects (nested array) with number of elements and each nest is an object with 2 properties.
        String[][] XsAndWords = new String[elements][2];

        //for loop to fill array with words and Xs
        for(int i = 0; i < elements; i++)
        {
            //Fill 1st and 2nd property of object in array with X's and word.
            XsAndWords[i][0] = Xs[i];
            XsAndWords[i][1] = words[i];
        } 
        return XsAndWords;
    }

public static int row = 0;
public static int col = 0;
public static int index = 0;
public static int guesses = 0;


    //print board.
    public static void printTurn(String[][] XsAndWords, int elements)
    {
        printBoard(XsAndWords,elements); 
        Scanner input = new Scanner(System.in);

        System.out.println("\nexit -> To exit the game\nrestart -> To restart the game\n\nNumber of guesses: " + guesses);
        System.out.print("\nEnter row and column with space inbetween: ");
        String rowAndCol = input.nextLine();
        int maxInput = maxInput(elements);
        
        if(rowAndCol.contains("exit"))
        {
            System.out.println("\nThank you for playing.");
            System.exit(0);
        }
        else if(rowAndCol.contains("restart"))
        {
            guesses = 0;
            matches = 0;
            pickNum = 0;
            welcomePrompt();
        }

        //if the length of the input is 3 then check if the charAts are digit or have a space inbetween
        if(rowAndCol.length() == 3 && Character.isDigit(rowAndCol.charAt(0)) && Character.isDigit(rowAndCol.charAt(2)) && rowAndCol.charAt(1) == ' ')
        {
            //if the input is a digit and there is a space inbetween input and the string length is 3 THEN process turn
            if(Integer.parseInt(rowAndCol.charAt(0) + "") <= maxInput && Integer.parseInt(rowAndCol.charAt(2) + "") <= maxInput)
            {
                row = Integer.parseInt(rowAndCol.substring(0,1));
                col = Integer.parseInt(rowAndCol.substring(2,3));
                index = col;

                //Maximum col and row variable

                //Depending on the board the user picks, the index of the array may vary on input.
                if(elements == 16)
                {
                    pickNum++;
                    index = col + (row * 4);
                    maxInput = 3;
                }
                else if(elements == 36)
                {
                    index = col + (row * 6);
                    pickNum++;
                    maxInput = 5;
                }
                else if(elements == 64)
                {
                    index = col + (row * 8);
                    pickNum++;
                    maxInput = 7;
                }

                //if it is user's SECOND PICK
                if(pickNum > 1)
                {
                    pickNum = 0;
                    arrayOfXsAndWords[index][0] = arrayOfXsAndWords[index][1];
                    secondPick = arrayOfXsAndWords[index][1];

                    //print board again for user to see results
                    printBoard(arrayOfXsAndWords,elements);

                    //pause code for 1 second so user can see 2nd picked card
                    pause1second();
                    guesses++;

                    isMatch(firstPick,secondPick,index);
                }
                //if AFTER FIRST PICK
                else if(pickNum == 1)
                {
                    arrayOfXsAndWords[index][0] = arrayOfXsAndWords[index][1];
                    previousIndex = index;
                    firstPick = arrayOfXsAndWords[previousIndex][1];
                }
            }
            else
            {
                System.out.println("***************************************\nMUST ENTER DIGITS WITHIN THE BOUNDARIES\n***************************************");
                pause1second();
                pause1second();
            }
        }
            //else if the input is not in digits print error message and prompt user to input coordinates again
        else
        {
            System.out.print("\n******************************************\nMUST ENTER TWO DIGITS WITH SPACE INBETWEEN\n******************************************\n\n");
            pause1second();
            pause1second();
        }
    }

    //method to see what the max input for the game should be
    public static int maxInput(int elements)
    {
        int maxInput = 0;
        if(elements == 16)
        {
            maxInput = 3;
        }
        else if(elements == 36)
        {
            maxInput = 5;
        }
        else if(elements == 64)
        {
            maxInput = 7;
        }
        return maxInput;
    }   
    
    //method to pause game for one second
    public static void pause1second()
    {
        try
        { 
            Thread.sleep(1000);
        } catch(InterruptedException ie)
        {
            System.out.println("INTERRUPTED EXCEPTION");
        }
    }
    
    //Method for comparing cards, seeing if game is over
    public static void isMatch(String firstPick, String secondPick, int index)
    {
        //for loop to check if already matched card
        boolean firstPickAlreadyFound = false;
        boolean secondPickAlreadyFound = false;
        
        for(int i = 0; i < arrayOfMatched.length; i++)
        {
            if(firstPick == arrayOfMatched[i] || secondPick == arrayOfMatched[i])
            {
                //if they were both already found cards, do nothing
                if(firstPick == arrayOfMatched[i] && secondPick == arrayOfMatched[i])
                {
                    secondPickAlreadyFound = false;
                    firstPickAlreadyFound = false;
                    //Count it as matches but do -- to even it out.
                    matches--;
                }
                else if(firstPick == arrayOfMatched[i])
                {
                    firstPickAlreadyFound = true;
                    System.out.println("\n\nFIRST PICK WAS ALREADY FOUND");
                    pause1second();
                }
                else if(secondPick == arrayOfMatched[i])
                {
                    secondPickAlreadyFound = true;
                    System.out.println("\n\nSECOND PICK WAS ALREADY FOUND");
                    pause1second();
                }

            }
        }
        
        if(firstPickAlreadyFound == true && secondPickAlreadyFound == true)
        {
            //do nothing, leave them as is.
        }
        //if first pick was an already found match.
        else if(firstPickAlreadyFound == true)
        {
            arrayOfXsAndWords[index][0] = "XXXXXXXXXXX"; 
        }
        else if(secondPickAlreadyFound == true)
        {
            arrayOfXsAndWords[previousIndex][0] = "XXXXXXXXXXX"; 
        } 
        else if(previousIndex == index)
        {
            boolean pickAlreadyFound = false;
            for(int i = 0; i < arrayOfMatched.length; i++)
            {
                if(firstPick == arrayOfMatched[i])
                    pickAlreadyFound = true;
            }
            System.out.println("\n\n******************************YOU PICKED THE SAME WORD TWICE******************************");
            if(!pickAlreadyFound)
            {
                arrayOfXsAndWords[index][0] = "XXXXXXXXXXX"; 
            }
            else
            {
                matches++;
            }
            pause1second();
        }
        //if cards match
        else if(firstPick == secondPick)
        {
            System.out.println("\n\n******************************WORDS MATCH******************************");
            //sleep thread process so user sees this message
            pause1second();

            //store matched card as the next element.
            arrayOfMatched[matches] = firstPick;
            //counter of matches
            matches++;


            //if its the last match of the game, end game with a GAME COMPLETE message
            if(matches == arrayOfXsAndWords.length / 2)
            {
                //GAME COMPLETE MESSAGE
                System.out.println("\n\n");
                System.out.print("******************************GAME COMPLETE****************************\nYou Completed the game in " + guesses + " Guesses!");
                pause1second();
                guesses = 0;
                matches = 0;
                welcomePrompt();
            }
        }
        //Else if they don't match 
        else
        {  
            //replace displayed part of the XsAndWords array with Xs again
            arrayOfXsAndWords[previousIndex][0] = "XXXXXXXXXXX"; 
            arrayOfXsAndWords[index][0] = "XXXXXXXXXXX"; 
            
            System.out.println("\n\n******************************WORDS DO NOT MATCH******************************");
            pause1second();
        }
    }
    
    //Print board depending on difficulty;
    public static void printBoard(String[][] XsAndWords, int elements)
    {
        //clear screen before printing the next board
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        //EASY BOARD
        if(elements == 16)
        {
            //print column number
            System.out.printf("%15s%15s%15s%15s\n", "0 |", "1 |", "2 |", "3 |");

            //print dashes under column number
            for(int i = 0; i < 60; i++) 
            { 
                System.out.print("-"); 
            }

            //printX's
            int lineNumber = 0;
            for(int i = 0; i < XsAndWords.length; i++)
            {
                if(i % 4 != 0)
                {
                    System.out.printf("%15s", XsAndWords[i][0]);
                }
                //break a line if max elements in line
                else
                {
                    System.out.println();
                    System.out.printf("%-2s%13s", lineNumber, XsAndWords[i][0]);
                    lineNumber++;
                }
            }
            System.out.println();
            //Print dashes at end
            for(int i = 0; i < 60; i++) 
            { 
                System.out.print("-"); 
            }
        }
        //INTERMEDIATE BOARD
        else if(elements == 36)
        {
            //print column number
            System.out.printf("%15s%15s%15s%15s%15s%15s\n", "0 |", "1 |", "2 |", "3 |", "4 |", "5 |");

            //print dashes under column number
            for(int i = 0; i < 90; i++) 
            { 
                System.out.print("-"); 
            }

            //printX's
            int lineNumber = 0;
            for(int i = 0; i < XsAndWords.length; i++)
            {
                if(i % 6 != 0)
                {
                System.out.printf("%15s", XsAndWords[i][0]);
                }
                //break a line if max elements in line
                else
                {
                    System.out.println();
                    System.out.printf("%-2s%13s", lineNumber, XsAndWords[i][0]);
                    lineNumber++;
                }
            }
            System.out.println(); 
            //print dashes at end
            for(int i = 0; i < 90; i++) 
            { 
                System.out.print("-"); 
            }
        }
        //DIFFICULT BOARD
        else if(elements == 64)
        {
            //print column number
            System.out.printf("%15s%15s%15s%15s%15s%15s%15s%15s\n", "0 |", "1 |", "2 |", "3 |", "4 |", "5 |", "6 |", "7 |");

            //print dashes under column number
            for(int i = 0; i < 120; i++) { System.out.print("-"); }

            //printX's
            int lineNumber = 0;
            for(int i = 0; i < XsAndWords.length; i++)
            {
                if(i % 8 != 0)
                {
                System.out.printf("%15s", XsAndWords[i][0]);
                }
                //break a line if max elements in line
                else
                {
                    System.out.println();
                    System.out.printf("%-2s%13s", lineNumber, XsAndWords[i][0]);
                    lineNumber++;
                }
            }
            System.out.println(); 
            //print dashes at end
            for(int i = 0; i < 120; i++) { System.out.print("-"); }
        }
    }

public static String firstPick;
public static String secondPick;
public static int previousIndex = 99;

    
}
