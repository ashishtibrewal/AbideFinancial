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
  public static final int prescription_file = 0;    // Argument number for prescription file
  public static final int practices_file = 1;       // Argument number for practices file
  public static final int regions_file = 2;         // Argument number for regions file
  public static final String relativePath = "../";  // Relative path for files

  /** Main method */
  //@SuppressWarnings("unchecked")    // Suppress unchecked type cast warnings
  public static void main(String[] args)
  {
    System.out.println("\nProgram started.");
    SimpleCache cache = SimpleCache.getInstance(); // Obtain reference to the cache
    //List<List<String[]>> nhsData = new ArrayList<List<String[]>>(); // List of list of String array to store data parsed from a file - Cannot have arrays with generic types hence using the Java Collection Framework - Each internal list represents a file and each string array represents a line/row in a file 
    String input, output;
    FileParser parser = new FileParser(); // Instantiate a new file parser object
    UserInputParser inputParser = new UserInputParser(); // Instantiate a new user input parser object
    Utility.displayFiles(args);           // Display files
    Utility.initialiseQuestions();        // Initialise questions
    Utility.initialiseRegions(relativePath + args[regions_file], parser);  // Initialise regions
    Utility.initialiseCities();           // Initialise cities
    while(true)                           // Run until user asks to quit
    {
      output = "0";                       // Initialise output to 0
      Utility.displayQuestions();         // Display questions
      input = inputParser.readInput();    // Read user input
      if(input.equals("Quit") || input.equals("quit") || input.equals("q") || input.equals("Q"))
      {
        System.out.println("\nQuitting... Goodbye.\n");
        System.exit(0);     // Exit program without any errors
      }
      else if(input.matches("[0-9]+"))
      {
        switch(Integer.parseInt(input)) // Switch on input and only parse files if data ins't already present in the cache
        {
          case 1:
            if((output = (String)cache.get(input)) == null)
            {
              output = parser.parseFile(relativePath + args[practices_file], input, null);
              cache.put(input, output);
            }
            System.out.println("\nAnswer: There are " + output + " practices in London.");
            break;

          case 2:
            if((output = (String)cache.get(input)) == null)
            {
              output = parser.parseFile(relativePath + args[prescription_file], input, null);
              cache.put(input, output);
            }
            System.out.println("\nAnswer: The average cost of all Peppermint Oil prescriptions was " + output + ".");
            break;

          case 3:
            if((output = (String)cache.get(input)) == null)
            {
              // HashMap -> String: Practice code, Practice: Practice object
              HashMap<String, Practice> practiceData = parser.getPracticeData(relativePath + args[practices_file]);   // Store practice data to be able to access postcode references when parsing and analysing the prescription file
              output = parser.parseFile(relativePath + args[prescription_file], input, practiceData);
              cache.put(input, output);
            }
            System.out.println("\nAnswer: The 5 postcodes with the highest actual spend (along with each of their total spends) are as follows (listed in descending order):\n" + output);
            break;

          case 4:
            if((output = (String)cache.get(input)) == null)
            {
              // HashMap -> String: Practice code, Practice: Practice object
              HashMap<String, Practice> practiceData = parser.getPracticeData(relativePath + args[practices_file]);   // Store practice data to be able to access postcode references when parsing and analysing the prescription file
              output = parser.parseFile(relativePath + args[prescription_file], input, practiceData);
              cache.put(input, output);
            }
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) was " + output + ".");
            break;

          case 5:
            if((output = (String)cache.get(input)) == null)
            {
              output = parser.parseFile(relativePath + args[prescription_file], input, null);
              cache.put(input, output);
            }
            System.out.println("\nAnswer: The average price per prescription of Flucloxacillin (excluding Co-Fluampicil) varied from the national mean by " + output + ".");
            break;

          default:
            output = "-1";        // Shouldn't get here in normal cases
            break;
        }
      }
      else                        // General querry case
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
}
