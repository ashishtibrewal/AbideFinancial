/* File: Questions.java
 * Author: Ashish Tibrewal
 * Date created: 21.07.2016
 * Date modified: 21.07.2016
 * Description: Class containing questions that the program supports
 */

import java.util.*;

public class Questions
{
  public static ArrayList<String> questionsList, regionsList, citiesList;

  public static void initialise()
  {
    String question, regions, cities;
    questionsList = new ArrayList<String>();
    regionsList = new ArrayList<String>();
    citiesList = new ArrayList<String>();

    // Questions
    question = "How many practices are in London?";
    questionsList.add(question);
    question = "What was the average actual cost of all Peppermint Oil prescriptions?";
    questionsList.add(question);
    question = "Which 5 post codes have the highest actual spend, and how much did each spend in total?";
    questionsList.add(question);
    question = "What was the average price per prescription of Flucloxacillin (excluding Co-Fluampicil)?";
    questionsList.add(question);
    question = "How much did this vary from the national mean?";
    questionsList.add(question);
    // TODO: Look for other types of possible questions and add them to this list (Add atleast 5-7 more questions)
    
    // Regions
    regions = "North, East, South, West, North-east, North-west, South-east, South-westi";
    for(String r:regions.split(","))
    {
      regionsList.add(r);
    }

    // Cities
    cities = "London";
    for(String c:cities.split(","))
    {
      citiesList.add(c);
    }
  }
}
