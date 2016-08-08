/* File: Run.java
 * Author: Ashish Tibrewal
 * Date created: 20.07.2016
 * Date modified: 20.07.2016
 * Description: Class containing main entry point to the program
 */

import java.io.*;
import java.util.*;

public class Run
{
  /** Main method */
  //@SuppressWarnings("unchecked")    // Suppress unchecked type cast warnings
  public static void main(String[] args)
  {
    System.out.println("\nProgram started.");
    //List<List<String[]>> nhsData = new ArrayList<List<String[]>>(); // List of list of String array to store data parsed from a file - Cannot have arrays with generic types hence using the Java Collection Framework - Each internal list represents a file and each string array represents a line/row in a file 
    String relativePath = "../";
    displayFiles(args, relativePath);   // Display files
    Questions.initialise();             // Initialise questions
    String input, output;
    int prescription_file = 0;          // Argument number for prescription file
    int practices_file = 1;             // Argument number for practices fle
    FileParser parser = new FileParser(); // Instantiate a new file parser object
    while(true)       // Run until user asks to quit
    {
      output = "0";                     // Initialise output to 0
      displayQuestions();               // Display questions
      input = readInput();              // Read user input
      if(input.equals("Quit") || input.equals("quit") || input.equals("q"))
      {
        System.out.println("\nQuitting... Goodbye.\n");
        System.exit(0);     // Exit program without any errors
      }
      // TODO: Only run this if it doesn't exist in the cache, if it does read the value from the cache
      else if(input.matches("[0-9]+"))
      {
        switch(Integer.parseInt(input))
        {
          case 1:
            output = parser.parseFile(relativePath + args[practices_file], input, null);
            System.out.println("\nAnswer: There are " + output + " practices in London.");
            break;

          case 2:
            output = parser.parseFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average cost of all Peppermint Oil prescriptions was " + output + ".");
            break;

          case 3:
            HashMap<String, Practice> practiceData = parser.getPracticeData(relativePath + args[practices_file]);   // Store practice data to be able to access postcode references when parsing and analysing the prescription file
            output = parser.parseFile(relativePath + args[prescription_file], input, practiceData);
            System.out.println("\nAnswer: The 5 postcodes with the highest actual spend (along with each of their total spends) are as follows (listed in descending order):\n" + output);
            break;

          case 4:
            output = parser.parseFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) was " + output + ".");
            break;

          case 5:
            output = parser.parseFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) varied from the national mean by " + output + ".");
            break;

          default:
            output = "-1";       // Shouldn't get here in normal cases
            break;
        }
      }
      else      // General querry case
      {
        int generalQuerryOutput = 0;
        for(String file:args)
        {
          generalQuerryOutput = generalQuerryOutput + Integer.parseInt(parser.parseFile(relativePath + file, input, null));     // Read and parse each file
        }
        output = String.valueOf(generalQuerryOutput);
        System.out.println("\nAnswer: Found " + output + " occurences of \"" + input + "\" in the data files.");
      }
    }
  }

  /* Method to display files names (arguments) passed to the program */
  private static void displayFiles(String[] args, String relativePath)
  {
    int fileNo = 1;
    System.out.println("\n-------- Files --------");
    for(String file:args)
    {
      System.out.println(fileNo + ". " + file);   // Display all arguments/files passed
      fileNo++;
    }
    System.out.println("-----------------------");
  }

  /* Method to display questions supported by the program */
  private static void displayQuestions()
  {
    int questionNo = 1;
    System.out.println("\n------ Questions ------");
    for(String s:Questions.questionsList)
    {
      System.out.println(questionNo + ". " + s);    // Dispaly all questions
      questionNo++;
    }
    System.out.println("-----------------------\n");
  }

  /* Method to read and parse user input */
  private static String readInput()
  {
    BufferedReader brInput = new BufferedReader(new InputStreamReader(System.in));
    String input = "";
    System.out.println("Please choose from one of the following options:");
    System.out.println("a. Type question number and hit the Enter key to retrieve an answer for the question.");
    System.out.println("b. Type a word (or a phrase) to search for in the data files.");
    System.out.println("c. Type \"Quit\" or \"quit\" or \"q\" and hit the Enter key to quit the program.\n");
    while(true)
    {
      System.out.print("Input: ");
      try
      {
        input = brInput.readLine();
        if(input.equals("Quit") || input.equals("quit") || input.equals("q")) return input;
        else
        {
          if(input.matches("[0-9]+"))
          {
            if((Integer.parseInt(input) > 0) && (Integer.parseInt(input) <= Questions.questionsList.size()))
            {
              System.out.println("Question " + input + ": " + Questions.questionsList.get(Integer.parseInt(input) - 1));
              System.out.print("Searching...");
              // TODO For certain questions, a region would be required
              return input;
            }
            else
            {
              System.out.println("Incorrect input value (question number). Please enter a value within range (1 - " + Questions.questionsList.size() + ").");
            }
          }
          else
          {
            System.out.println("Searching for \"" + input + "\"...");
            return input;
          }
        }
      }
      catch(IOException e)
      {
        System.err.println("Caught IOException when trying to read the user input: " + e.getMessage());
      }
      catch(NumberFormatException e)
      {
        System.err.println("Caught NumberFormatException when trying to read the user input: " + e.getMessage() + ". Incorrect input value (question number). Please enter a value within range (1 - " + Questions.questionsList.size() + ").");
      }
    }
  }
}
