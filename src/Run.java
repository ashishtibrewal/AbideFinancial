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
    System.out.println("Program started.");
    int fileNo = 1;
    List<List<String[]>> nhsData = new ArrayList<List<String[]>>();
    String relativePath = "../";
    System.out.println("-------- Files --------");
    for(String file:args)
    {
      System.out.println(fileNo + ". " + file);   // Display all arguments/files passed
      fileNo++;
    }
    System.out.println("-----------------------");
    Questions.initialise();
    fileNo = 0;
    for(String file:args)
    {
      nhsData.add(readFile(relativePath + file));     // Read and parse each file
      fileNo++;
    }
  }

  /** Method to read a file */
  private static List<String[]> readFile(String fileName) throws Exception
  {
    BufferedReader bf = new BufferedReader(new FileReader(fileName));
    String line = "";
    String[] columnHeaderItems = {};
    String[] rowItems = {};
    boolean headerRead = false;
    int numItems = 0;
    //List<Data> fileData = new ArrayList<Data>();
    List<String[]> fileData = new ArrayList<String[]>();
    while((line = bf.readLine()) != null)
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
    for(String s:Questions.questionsList)
    {
      System.out.println(s);
    }
    return fileData;
  }
}
