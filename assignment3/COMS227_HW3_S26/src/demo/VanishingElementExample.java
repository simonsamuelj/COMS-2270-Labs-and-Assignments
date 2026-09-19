package demo;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import api.Drawable;
import api.ExplosionRenderer;
import api.AbstractElement;
import api.Renderer;
import hw3.VanishingElement;

/**
 * Example of using VanishingElement.
 * 
 * Requires VanishingElement.
 */
public class VanishingElementExample extends GameBase
{
  public static void main(String[] args)
  {
    start(VanishingElementExample.class);
  } 

  // suppress compiler warning
  private static final long serialVersionUID = 1L;


  // Window size, fixed for simplicity
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;

  // Basic dimensions for objects
  private static final int SIZE = 30;

  // Lifetime of explosion object
  private static final int EXPLOSION_COUNT = 40;

  /**
   * List of elements to draw.
   */
  private ArrayList<VanishingElement> explosions;

  /**
   * Renderer to use for all of them.
   */
  Renderer renderer;

  /**
   * Random for generating positions.
   */
  private Random rand;

  /**
   * Constructor creates and starts the timer.
   */
  public VanishingElementExample()
  {
    rand = new Random();
    explosions = new ArrayList<>();
    renderer = new ExplosionRenderer(EXPLOSION_COUNT, Color.ORANGE, Color.BLACK);
  }

  @Override
  public int getGameWidth()
  {
    return WIDTH;
  }

  @Override
  public int getGameHeight()
  {
    return HEIGHT;
  }

  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();
    arr.addAll(explosions);
    return arr;
  }

  @Override
  protected void doUpdates()
  {
    // update everybody
    for (AbstractElement s : explosions)
    {
      s.update();
    }

    // make a random explosion and add it to the list
    int x = rand.nextInt(WIDTH);
    int y = rand.nextInt(HEIGHT);
    VanishingElement e = new VanishingElement(x, y, SIZE, SIZE, EXPLOSION_COUNT);
    e.setRenderer(renderer);
    explosions.add(e);

    // remove all marked elements
    ArrayList<VanishingElement> temp = new ArrayList<>();
    for (VanishingElement s : explosions)
    {
      if (!s.isMarked())
      {
        temp.add(s);
      }
    }
    explosions = temp;
  }
}

