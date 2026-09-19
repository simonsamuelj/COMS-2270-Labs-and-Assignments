package lab2;

/**
 * A RabbitModel is used to simulate the growth
 * of a population of rabbits. 
 */
public class RabbitModel3
{
  // TODO - add instance variables as needed
	private int population;
	
  
  /**
   * Constructs a new RabbitModel.
   */
  public RabbitModel3()
  {
    // TODO
	  population = 2;
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
	  population++;
	  population = population %5;
  }
  
  /**
   * Sets or resets the state of the model to the 
   * initial conditions.
   */
  public void reset()
  {
    // TODO
	  population = 2;
  }
}
