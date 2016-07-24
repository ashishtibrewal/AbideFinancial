/* File: Data.java
 * Author: Ashish Tibrewal
 * Date created: 21.07.2016
 * Date modified: 21.07.2016
 * Description: Class that represents a row in the spreadsheet
 */

public class Data
{
  private Field[] fields;

  public Data(Field[] fields)
  {
    this.fields = fields; 
  }

  public Field[] getFields()
  {
    return this.fields;
  }

  public void setFields(Field field)
  {
    this.fields = fields;
  }
}
