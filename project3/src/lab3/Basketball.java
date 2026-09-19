package lab3;



public class Basketball
{
	
	private double diameter;
	private boolean inflated;
	
  public Basketball(double givenDiameter)
  {
	  diameter = givenDiameter;
	  inflated = false;
  }

  public boolean isDribbleable()
  {
    return inflated;
  }

  public double getDiameter()
  {
    return diameter;
  }

  public double getCircumference()
  {
    return 2 * Math.PI * (diameter / 2) * (diameter /2);
  }

  public void inflate()
  {
	  inflated = true;
  }
}
