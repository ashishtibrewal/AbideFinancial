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
  @SuppressWarnings("unchecked")    // Suppress unchecked type cast warnings
  public static void main(String[] args)
  {
    System.out.println("\nProgram started.");
    List<List<String[]>> nhsData = new ArrayList<List<String[]>>(); // List of list of String array to store data parsed from a file - Cannot have arrays with generic types hence using the Java Collection Frameworki - Each internal list represents a file and each string array represents a line/row in a file 
    String relativePath = "../";
    displayFiles(args, relativePath);   // Display files
    Questions.initialise();             // Initialise questions
    String input;
    Object output;
    int prescription_file = 0;
    int practices_file = 1;
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
      else if(input.matches("[0-9]+"))
      {
        switch(Integer.parseInt(input))
        {
          case 1:
            output = (Float)readFile(relativePath + args[practices_file], input, null);
            System.out.println("\nAnswer: There are " + Math.round(((Float)output).floatValue()) + " practices in London.");
            break;

          case 2:
            output = (Float)readFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average cost of all Peppermint Oil prescriptions was " + ((Float)output).floatValue() + ".");
            break;

          case 3:
            HashMap<String, Practice> practiceData = getPracticeData(relativePath + args[practices_file]);   // Store practice data to be able to access postcode references when parsing and analysing the prescription file
            output = (ArrayList<Map.Entry<String, Float>>)readFile(relativePath + args[prescription_file], input, practiceData);
            System.out.println("\nAnswer: The 5 postcodes with the highest actual spend (along with each of their total spends) are as follows (listed in descending order):");
            int i = 1; 
            for(Map.Entry<String, Float> entry : (ArrayList<Map.Entry<String, Float>>)output)
            {
              System.out.println(i + ". " + entry.getKey() + ", " + entry.getValue());
              i++;
            }
            break;

          case 4:
            output = (Float)readFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) was " + ((Float)output).floatValue() + ".");
            break;

          case 5:
            output = (Float)readFile(relativePath + args[prescription_file], input, null);
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) varied from the national mean by " + ((Float)output).floatValue() + ".");
            break;

          default:
            output = new Float(-1);       // Shouldn't get here in normal cases
            break;
        }
        /*for(String file:args)
        {
          output = output + readFile(relativePath + file, input);     // Read and parse each file
          System.out.println(output);
        }*/
      }
      else // General querry case
      {
        int generalQuerryOutput = 0;
        for(String file:args)
        {
          generalQuerryOutput = generalQuerryOutput + ((Integer)readFile(relativePath + file, input, null)).intValue();     // Read and parse each file
          output = (Integer)generalQuerryOutput;
        }
        System.out.println("\nAnswer: Found " + output + " occurences of \"" + input + "\" in the data files.");
      }
    }
  }

  private static HashMap<String, Practice> getPracticeData(String fileName)
  {
    HashMap<String, Practice> practiceData = new HashMap<String, Practice>();
    try
    {
      BufferedReader brFile = new BufferedReader(new FileReader(fileName));
      String line = "";
      String[] rowItems = {};
      while((line = brFile.readLine()) != null)
      {
        rowItems = line.split(",");
        practiceData.put(rowItems[Columns.PRACTICE_CODE].trim(), new Practice(rowItems[Columns.PRACTICE_CODE].trim(), rowItems[Columns.ADDRESS_POSTCODE].trim(), 0f));
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
    return practiceData;
  }


  /** Method to read and process a file */
  // TODO: Maybe add a cache mechanism for the 20 most recent querries
  private static Object readFile(String fileName, String input, HashMap<String, Practice> practiceData)
  {
    float output = 0f;
    try
    {
      BufferedReader brFile = new BufferedReader(new FileReader(fileName));
      String line = "";
      String[] columnHeaderItems = {};
      String[] rowItems = {};
      String querry = "";
      boolean headerRead = false;
      int numItems = 0;
      int counter = 0;
      float actualCost = 0f;
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
                for(int i = 0; i < numItems; i++)
                {
                  if(rowItems[i].toLowerCase().contains(querry.toLowerCase())) output++;
                }
                break;

              case 2:
                querry = "Peppermint Oil";
                if(rowItems[Columns.BNF_NAME].toLowerCase().contains(querry.toLowerCase()))
                {
                  actualCost = actualCost + Float.parseFloat(rowItems[Columns.ACT_COST]);
                  counter++;
                }
                break;

              case 3:
                querry = rowItems[Columns.PRACTICE].trim();
                Practice practice = practiceData.get(querry);
                if(practice != null)
                {
                  //System.out.println(practice + "," + practiceData.size() + "," + querry);
                  practice.setPracticeActualCost(practice.getPracticeActualCost() + Float.parseFloat(rowItems[Columns.ACT_COST]));
                  practiceData.put(querry, practice);
                }
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
            for(int i = 0; i < numItems; i++)
            {
              if(rowItems[i].toLowerCase().contains(querry.toLowerCase())) output++;
            }
          }
        }
      }
      if(input.matches("[0-9]+"))   // Questions
      {
        switch(Integer.parseInt(input))
        {
          // Do nothing
          case 1:
            return new Float(output);

          // Need to calculate average for question 2
          case 2:
            output = actualCost/counter;
            return new Float(output);

          case 3:
            HashMap<String, Float> postcodeData = new HashMap<String, Float>();
            for(Map.Entry<String, Practice> entry : practiceData.entrySet())
            {
              String postcode = entry.getValue().getPracticePostCode();
              float postcodeActualCost = entry.getValue().getPracticeActualCost();
              if(postcodeData.get(postcode) == null)    // New entry
              {
                postcodeData.put(postcode, new Float(postcodeActualCost));
              }
              else      // Update entry
              {
                postcodeData.put(postcode, new Float(postcodeData.get(postcode) + postcodeActualCost));
              }
              //System.out.println(postcodeData.get(postcode));
            }
            //Find 5 postcodes with the highest actual cost
            ArrayList<Map.Entry<String, Float>> postcodeMaxActualCost = new ArrayList<Map.Entry<String, Float>>();
            for(int i = 0; i < 5; i++)     // Iterate five times to find 5 max values
            {
              Map.Entry<String, Float> maxEntry = null;
              for(Map.Entry<String, Float> entry : postcodeData.entrySet())
              {
                if(maxEntry == null || (entry.getValue().compareTo(maxEntry.getValue()) > 0))
                {
                  maxEntry = entry;
                }
              }
              postcodeMaxActualCost.add(maxEntry);    // Add max entry to list
              postcodeData.remove(maxEntry.getKey()); // Remove max entry from hashmap to be able to get a new maximum in the next iteration
            }
            return postcodeMaxActualCost;

          case 4:
            return new Float(output);

          case 5:
            return new Float(output);

          default:
            output = -1;
            return new Float(output);
        }
      }
      else return new Integer((int)output);  // General querry case
    }
    catch(FileNotFoundException e)
    {
      System.err.println("Caught FileNotFoundException when trying to read the input file: " + e.getMessage());
    }
    catch(IOException e)
    {
      System.err.println("Caught IOException when trying to read the input file: " + e.getMessage());
    }
    return null;    // Shouldn't get here in normal cases
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
