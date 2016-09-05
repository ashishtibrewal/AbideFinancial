/* File: FileParser.java
 * Author: Ashish Tibrewal
 * Date created: 20.07.2016
 * Date modified: 20.07.2016
 * Description: Class containing main entry point to the program
 */

import java.util.*;
import java.io.*;

public class FileParser
{
  public final int regionsRegionIndex = 0;
  public final int regionsPostCodeIndex = 1;
  /** Method to obtain a HashMap containing practice code and practice object pairs */
  public HashMap<String, Practice> getPracticeData(String fileName)
  {
    // HashMap -> String: Practice code, Practice: Practice object
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
  public String parseFile(String fileName, String input, HashMap<String, Practice> practiceData)
  {
    String output = "";
    try
    {
      BufferedReader brFile = new BufferedReader(new FileReader(fileName));
      String line = "";
      String[] rowItems = {};
      String querry = "";
      boolean headerRead = false;
      int numItems = 0;
      int counter = 0;
      float actualCost = 0f;
      while((line = brFile.readLine()) != null)
      {
        rowItems = line.split(",");
        if(!headerRead)
        {
          numItems = rowItems.length;
          headerRead = true;
        }
        if(input.matches("[0-9]+"))   // Questions
        {
          switch(Integer.parseInt(input))
          {
            case 1:
              querry = "London";
                if(rowItems[Columns.ADDRESS_CITY].toLowerCase().contains(querry.toLowerCase()))
                  counter++;
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
              Practice p = practiceData.get(querry);
              if(p != null)     // Check to make sure that an entry for the "querry" key exists in the hashmap 'practiceData'
              {
                //System.out.print("HM object ref: " + p + ", HM size: " + practiceData.size() + ", Querry: " + querry + ", P object ref: " + practiceData.get(querry) + ", Act cost (before): " + p.getPracticeActualCost() + ", Actual cost (after): ");
                p.setPracticeActualCost(p.getPracticeActualCost() + Float.parseFloat(rowItems[Columns.ACT_COST]));
                //System.out.println(p.getPracticeActualCost());
                //practiceData.put(querry, practice);       // Note we don't need to "put" the updated object back into the HashMap since 'p' contains a reference to the Practice object contained in the HashMap. Modifying (contents in) 'p' modifies the (contents in) the original Practice object contained within the hashmap.
              }
              break;

            case 4:
              querry = "Flucloxacillin";
              String querryNeg = "Co-Fluampicil";
              //TODO: Need to keep track of multiple regions/locations
              if(rowItems[Columns.BNF_NAME].toLowerCase().contains(querry.toLowerCase()) && !rowItems[Columns.BNF_NAME].toLowerCase().contains(querryNeg.toLowerCase()))
              {
                counter++;
              }
              break;

            case 5:
              break;

            default:
              counter = -1;
              break;
          }
        }
        else      // General querry
        {
          querry = input;
          for(int i = 0; i < numItems; i++)
          {
            if(rowItems[i].toLowerCase().contains(querry.toLowerCase()))
              counter++;
          }
        }
      }
      if(input.matches("[0-9]+"))   // Questions
      {
        switch(Integer.parseInt(input))
        {
          case 1:
            output = String.valueOf(Math.round(counter));   // Do nothing extra
            break;

          case 2:
            output = String.valueOf(actualCost/counter);    // Need to calculate the average
            break;

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
            }
            //Find 5 postcodes with the highest actual cost
            int numToFind = 5;
            String postcodeMaxActualCost = "";
            //ArrayList<Map.Entry<String, Float>> postcodeMaxActualCost = new ArrayList<Map.Entry<String, Float>>();
            for(int i = 0; i < numToFind; i++)     // Iterate five times to find 5 max values
            {
              Map.Entry<String, Float> maxEntry = null;
              for(Map.Entry<String, Float> entry : postcodeData.entrySet())
              {
                if(maxEntry == null || (entry.getValue().compareTo(maxEntry.getValue()) > 0))
                {
                  maxEntry = entry;
                }
              }
              postcodeData.remove(maxEntry.getKey()); // Remove max entry from hashmap to be able to get a new maximum in the next iteration
              postcodeMaxActualCost = postcodeMaxActualCost + (i + 1) + ". " + maxEntry.getKey() + ", " + maxEntry.getValue();  // Add max entry to the list
              if (i < (numToFind - 1))
              {
                postcodeMaxActualCost = postcodeMaxActualCost + "\n";  // Add new line character
              }
            }
            output = postcodeMaxActualCost;
            break;

          case 4:
            output = String.valueOf(counter);
            break;

          case 5:
            output = String.valueOf(counter);
            break;

          default:
            output = String.valueOf(counter);
            break;
        }
      }
      else    // General querry case
      {
        output = String.valueOf(Math.round(counter));
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

  /* Method to parse regions file */
  public ArrayList<Object> readRegionsFile(String fileName)
  {
    ArrayList<Object> returnData = new ArrayList<Object>();   // Create a list that stores data that needs to be returned
    try
    {
      ArrayList<String> regions = new ArrayList<String>();
      HashMap<String, String> postCodeRegion = new HashMap<String, String>(); // HashMap -> String: Postcode, String: Region
      BufferedReader brFile = new BufferedReader(new FileReader(fileName));
      String line = "";
      String updatedLine = "";
      String[] rowItems = {};
      LinkedHashSet<String> regionsHashSet = new LinkedHashSet<String>(); // Instantiate a new linkedhashset object with default initial capacity and load factor
      while((line = brFile.readLine()) != null)
      {
        updatedLine = Utility.parseLine(line);        // Parse line using custom parser
        rowItems = updatedLine.split(",");            // Split on commas
        if(rowItems.length > Columns.REGION_REGION && !rowItems[Columns.REGION_REGION].isEmpty())   // Check to make sure that the element (i.e. 26th column entry) exists for the current row in the regions data file and check to make sure that the cell/element isn't empty
        {
          regionsHashSet.add(rowItems[Columns.REGION_REGION].trim());
          postCodeRegion.put(rowItems[Columns.REGION_POSTCODE].trim(), rowItems[Columns.REGION_REGION].trim());
        }
      }
      Iterator<String> itr = regionsHashSet.iterator();
      while(itr.hasNext())
      {
        regions.add(itr.next());  // Add to list
      }
      regions.remove("Region");   // Remove the "Region" element read on the first row of the regions data csv file
      returnData.add(regions);          // Add regions list to return list
      returnData.add(postCodeRegion);   // Add regions-postcode hashmap to list
    }
    catch(FileNotFoundException e)
    {
      System.err.println("Caught FileNotFoundException when trying to read the input file: " + e.getMessage());
    }
    catch(IOException e)
    {
      System.err.println("Caught IOException when trying to read the input file: " + e.getMessage());
    }
    return returnData;
  }
}
