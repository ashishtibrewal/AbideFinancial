/* File: Utility.java
 * Author: Ashish Tibrewal
 * Date created: 21.07.2016
 * Date modified: 21.07.2016
 * Description: Class containing questions that the program supports
 */

import java.util.*;
import java.io.*;

public final class Utility
{
  public static ArrayList<String> questionsList, regionsList, citiesList;

  /* Method to initialise questions */
  public static void initialiseQuestions()
  {
    String question;
    questionsList = new ArrayList<String>();

    // Questions
    questionsList.add("How many practices are in London?");
    questionsList.add("What was the average actual cost of all Peppermint Oil prescriptions?");
    questionsList.add("Which 5 post codes have the highest actual spend, and how much did each spend in total?");
    questionsList.add("What was the average price per prescription of Flucloxacillin (excluding Co-Fluampicil)?");
    questionsList.add("How much did the average price per prescription of Flucloxacillin (excluding Co-Fluampicil) vary from the national mean?");
    // TODO: Look for other types of possible questions and add them to this list (Add atleast 5-7 more questions)
  }
    
  /* Method to initialise regions */
  public static void initialiseRegions(String fileName, FileParser parser)
  {
    // Regions
    regionsList = new ArrayList<String>();
    System.out.print("\nParsing " + fileName + " to extract postcode and region data. This may take a few seconds, please wait...");
    regionsList = parser.readRegionsFile(fileName);
    System.out.println();
    //System.out.println(regionsList);
  }
  
  /* Method to initialise cities */
  public static void initialiseCities()
  {
    // Cities
    citiesList = new ArrayList<String>();
    String cities = "London";
    for(String c:cities.split(","))
    {
      citiesList.add(c);
    }
  }

  /* Method to parse tokens in a file (Note that String.split(String regex) is way too slow when parsing large strings using a complex regular expression) */
  public static String parseLine(String line)
  {
    StringBuilder sBuilder = new StringBuilder(line);
    boolean inQuotes = false;
    for (int currentIndex = 0; currentIndex < line.length(); currentIndex++)
    {
      char currentChar = sBuilder.charAt(currentIndex);
      if( currentChar == '\"')
        inQuotes = !inQuotes;   // Toggle case
      if(currentChar == ',' && inQuotes)
        sBuilder.setCharAt(currentIndex, ';');  // Change comma to semi-colon
    }
    return new String(sBuilder);
  }

  /* Method to display files names (arguments) passed to the program */
  public static void displayFiles(String[] args)
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
  public static void displayQuestions()
  {
    int questionNo = 1;
    System.out.println("\n------ Questions ------");
    for(String s:Utility.questionsList)
    {
      System.out.println(questionNo + ". " + s);    // Dispaly all questions
      questionNo++;
    }
    System.out.println("-----------------------\n");
  }
}
