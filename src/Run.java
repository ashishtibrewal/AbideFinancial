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
  public static void main(String[] args) throws Exception
  {
    System.out.println("\nProgram started.");
    List<List<String[]>> nhsData = new ArrayList<List<String[]>>(); // List of list of String array to store data parsed from a file - Cannot have arrays with generic types hence using the Java Collection Frameworki - Each internal list represents a file and each string array represents a line/row in a file 
    String relativePath = "../";
    displayFiles(args, relativePath);   // Display files
    Questions.initialise();             // Initialise questions
    String input = "";
    while(true)
    {
      displayQuestions();               // Display questions
      input = readInput();              // Read user input
      if(input.equals("Quit") || input.equals("quit") || input.equals("q"))
      {
        System.out.println("\nQuitting... Goodbye.\n");
        System.exit(0);
      }
      for(String file:args)
      {
        nhsData.add(readFile(relativePath + file));     // Read and parse each file
      }
    }
  }

  /** Method to read a file */
  private static List<String[]> readFile(String fileName) throws Exception
  {
    BufferedReader brFile = new BufferedReader(new FileReader(fileName));
    String line = "";
    String[] columnHeaderItems = {};
    String[] rowItems = {};
    boolean headerRead = false;
    int numItems = 0;
    //List<Data> fileData = new ArrayList<Data>();
    List<String[]> fileData = new ArrayList<String[]>();
    int counter = 0;
    while((line = brFile.readLine()) != null)
    {
      if(headerRead == false)
      {
        columnHeaderItems = line.split(",");
        numItems = columnHeaderItems.length;
        headerRead = true;
        //System.out.println(line);
      }
      else
      {
        rowItems = line.split(",");
        for (int i = 0; i < numItems; i++)
        {
          if (rowItems[i].contains("London") || rowItems[i].contains("LONDON"))
          {
            counter++;
          }
        }
        //fileData.add(rowItems);
        /*Field[] fields = new Field[numItems];
        for(int i = 0; i < numItems; i++)
        {
          fields[i] = new Field();
          fields[i].setFieldType(columnHeaderItems[i]);
          fields[i].setFieldValue(rowItems[i]);
        }
        fileData.add(new Data(fields));*/
      }
    }
    for(String s:columnHeaderItems)
    {
      //System.out.println(s);
    }
    System.out.println("Counter: " + counter);
    return fileData;
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
    System.out.println("-----------------------\n");
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
        System.err.println("Caught IOException when trying to read the input: " + e.getMessage());
      }
    }
  }
}
