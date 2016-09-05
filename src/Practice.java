/* File: Practice.java
 * Author: Ashish Tibrewal
 * Date created: 20.07.2016
 * Date modified: 20.07.2016
 * Description: Class containing practice details
 */

public class Practice
{
  private String practiceCode;
  private String practicePostcode;
  private float practiceActualCost;
  private String practiceRegion;

  public Practice(String practiceCode, String practicePostcode, float practiceActualCost)
  {
    this.practiceCode = practiceCode;
    this.practicePostcode = practicePostcode;
    this.practiceActualCost = practiceActualCost;
  }

  public String getPracticeCode()
  {
    return this.practiceCode;
  }

  public String getPracticePostCode()
  {
    return this.practicePostcode;
  }

  public float getPracticeActualCost()
  {
    return this.practiceActualCost;
  }

  public void setPracticeCode(String practiceCode)
  {
    this.practiceCode = practiceCode;
  }

  public void setPracticePostCode(String practicePostcode)
  {
    this.practicePostcode = practicePostcode;
  }

  public void setPracticeActualCost(float practiceActualCost)
  {
    this.practiceActualCost = practiceActualCost;
  }

  public void setPracticeRegion(String region)
  {
    this.practiceRegion = region;
  }
}
