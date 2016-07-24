/* File: Field.java
 * Author: Ashish Tibrewal
 * Date created: 21.07.2016
 * Date modified: 21.07.2016
 * Description: Class that represents a field in the spreadsheet
 */

public class Field
{
  private String fieldType;
  private String fieldValue;

  public void setFieldType(String fieldType)
  {
    this.fieldType = fieldType;
  }

  public String getFieldType()
  {
    return this.fieldType;
  }

  public void setFieldValue(String fieldValue)
  {
    this.fieldValue = fieldValue;
  }

  public String getFieldValue()
  {
    return this.fieldValue;
  }
}
