/* File: UserInputParser.java
 * Author: Ashish Tibrewal
 * Date created: 20.07.2016
 * Date modified: 20.07.2016
 * Description: Class used to parse user input
 */

import java.io.*;

public class UserInputParser
{
  /* Method to read and parse user input */
  public static String readInput()
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
            if((Integer.parseInt(input) > 0) && (Integer.parseInt(input) <= Utility.questionsList.size()))
            {
              System.out.println("Question " + input + ": " + Utility.questionsList.get(Integer.parseInt(input) - 1));
              System.out.print("Searching...");
              // TODO For certain questions, a region would be required
              return input;
            }
            else
            {
              System.out.println("Incorrect input value (question number). Please enter a value within range (1 - " + Utility.questionsList.size() + ").");
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
        System.err.println("Caught NumberFormatException when trying to read the user input: " + e.getMessage() + ". Incorrect input value (question number). Please enter a value within range (1 - " + Utility.questionsList.size() + ").");
      }
    }
  }
}
