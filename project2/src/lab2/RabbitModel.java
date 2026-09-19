package lab2;


/**
 * A RabbitModel is used to simulate the growth
 * of a population of rabbits. 
 */
public class RabbitModel
{
  // TODO - add instance variables as needed
	private int population;
	private int lastYear = 1;
	private int yearBefore = 0;
	
  
  /**
   * Constructs a new RabbitModel.
   */
  public RabbitModel()
  {
    // TODO
	  /* yearBefore = 0;
	  lastYear = 1;
	  population = yearBefore + lastYear; */
	  reset();
  }  
 
  /**
   * Returns the current number of rabbits.
   * @return
   *   current rabbit population
   */
  public int getPopulation()
  {
    // TODO - returns a dummy value so code will compile
    return population;
  }
  
  /**
   * Updates the population to simulate the
   * passing of one year.
   */
  public void simulateYear()
  {
    // TODO
	  population = lastYear + yearBefore;
	  yearBefore = lastYear;
	  lastYear = population;
  }
  
  /**
   * Sets or resets the state of the model to the 
   * initial conditions.
   */;
  public void reset()
  {
    // TODO
	  population = 1;
  }
}
