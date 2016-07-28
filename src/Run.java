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
  public static void main(String[] args)
  {
    System.out.println("\nProgram started.");
    List<List<String[]>> nhsData = new ArrayList<List<String[]>>(); // List of list of String array to store data parsed from a file - Cannot have arrays with generic types hence using the Java Collection Frameworki - Each internal list represents a file and each string array represents a line/row in a file 
    String relativePath = "../";
    displayFiles(args, relativePath);   // Display files
    Questions.initialise();             // Initialise questions
    String input;
    int output;
    while(true)
    {
      output = 0;
      displayQuestions();               // Display questions
      input = readInput();              // Read user input
      if(input.equals("Quit") || input.equals("quit") || input.equals("q"))
      {
        System.out.println("\nQuitting... Goodbye.\n");
        System.exit(0);
      }
      // TODO: Only run this if it doesn't exist in the cache, if it does read the value from the cache
      for(String file:args)
      {
        output = output + readFile(relativePath + file, input);     // Read and parse each file
      }
      System.out.println("\nAnswer: " + output);
    }
  }

  /** Method to read and process a file */
  // TODO: Maybe add a cache mechanism for the 20 most recent querries
  private static int readFile(String fileName, String input)
  {
    int output = 0;
    try
    {
      BufferedReader brFile = new BufferedReader(new FileReader(fileName));
      String line = "";
      String[] columnHeaderItems = {};
      String[] rowItems = {};
      String querry = "";
      boolean headerRead = false;
      int numItems = 0;
      while((line = brFile.readLine()) != null)
      {
        if(headerRead == false)
        {
          columnHeaderItems = line.split(",");
          numItems = columnHeaderItems.length;
          headerRead = true;
        }
        else
        {
          rowItems = line.split(",");
          if(input.matches("[0-9]+"))   // Questions
          {
            switch(Integer.parseInt(input))
            {
              case 1:
                querry = "London";
                break;

              case 2:
                querry = "Peppermint Oil";
                break;

              case 3:
                break;

              case 4:
                break;

              case 5:
                break;

              default:
                output = -1;
                break;
            }
          }
          else      // General querry
          {
            querry = input;
          }
          for(int i = 0; i < numItems; i++)
          {
            if(rowItems[i].toLowerCase().contains(querry.toLowerCase())) output++;
          }
        }
      }
    }
    catch(FileNotFoundException e)
    {
      System.err.println("Caught FileNotFoundException when trying to read the input file: " + e.getMessage());
    }
    catch(IOException e)
    {
      System.err.println("Caught IOException when trying to read the input file: " + e.getMessage());
    }
    return output;
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
