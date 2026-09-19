package demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import api.CircleRenderer;
import api.Drawable;
import api.ExplosionRenderer;
import api.AbstractElement;
import api.Renderer;
import hw3.AttachedElement;
import hw3.FlyingElement;
import hw3.FollowerElement;
import hw3.PlatformElement;
import hw3.VanishingElement;

/**
 * Uses a FlyingElement to jump on a FollowerElement and an AttachedElement
 * associated with a PlatformElement. If they collide, they "explode" using
 * a VanishingElement. 
 * 
 * To control the FlyingElement use left arrow, right arrow, and "a" to jump.
 * 
 * Requires AttachedElement, VanishingElement, FollowerElement, FlyingElement,
 * and PlatformElement to be completed.
 */
public class JumpExample2 extends GameBase
{
  public static void main(String[] args)
  {
    start(JumpExample2.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;

  // Window size, fixed for simplicity
  private static final int WIDTH = 600;
  private static final int HEIGHT = 400;
  
  // Dimensions for objects
  private static final int SIZE = 30;
  
  // Player motion constants
  private static final double PLAYER_JUMP_VELOCITY = -12;
  private static final double GRAVITY = 0.8;
  private static final double PLAYER_SPEED = 4;

  
  /**
   * The player.
   */
  private FlyingElement player;
  
  /**
   * A platform for player to land on.
   */
  private PlatformElement platform;
  
  /**
   * Possible explosion, if we hit the enemy.
   */
  private ArrayList<VanishingElement> explosions = new ArrayList<>();
  
  /**
   * And let's keep score!
   */
  private int score = 0;
  
  public JumpExample2()
  {
    platform = new PlatformElement(100, 300, 400, 20);
    platform.setColor(Color.GREEN);
    
    FollowerElement e = new FollowerElement(SIZE, SIZE, platform.getWidth() - SIZE);
    e.setColor(Color.RED);
    e.setVelocity(2, 0);
    platform.addAssociated(e);
    
    e = new FollowerElement(SIZE, SIZE, 2 * SIZE);
    e.setColor(Color.MAGENTA);
    e.setVelocity(1, 0);
    platform.addAssociated(e);
    
    AttachedElement c = new AttachedElement(SIZE, SIZE, (platform.getWidth() - SIZE) / 2, 100);
    c.setColor(Color.PINK);
    CircleRenderer r = new CircleRenderer(Color.PINK);
    r.setMargin(5);
    c.setRenderer(r);
    platform.addAssociated(c);
    
    player = new FlyingElement(100, 300 - SIZE, SIZE, SIZE + SIZE/2);
    player.setColor(Color.YELLOW);
    player.setGravity(GRAVITY);
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
  public int getScore()
  {
    return score;
  }
  
  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();
    arr.add(player);
    arr.add(platform);
    for (Drawable c : platform.getAssociated())
    {
      arr.add(c);
    }
    arr.addAll(explosions);
    
    return arr;
  }
  
  @Override
  protected void doUpdates()
  {
    player.update();
    platform.update();
    for (AbstractElement e : explosions)
    {
      e.update();
    }
    
    if (player.collides(platform))
    {
      // we landed! no longer flying...
      player.setGrounded(true);
      
      // align bottom of player with top of platform
      player.setPosition(player.getXReal(), platform.getYReal() - player.getHeight());
      
      // restore horizontal velocity, set vertical velocity to zero
      player.setVelocity(player.getDeltaX(), 0);      
    }
    
    for (AbstractElement e : platform.getAssociated())
    {
      if (player.collides(e))
      {
        e.markForDeletion();
        Renderer r = new ExplosionRenderer(40, Color.ORANGE, Color.BLACK);
        VanishingElement f = new VanishingElement(e.getXInt(), e.getYInt(), SIZE, SIZE, 40);
        f.setRenderer(r);
        explosions.add(f);
        score += 10000;
      }
    }
    
    platform.deleteMarkedAssociated();
    
    // delete marked explosions too
    ArrayList<VanishingElement> temp = new ArrayList<>();
    for (VanishingElement f : explosions)
    {
      if (!f.isMarked())
      {
        temp.add(f);
      }
    }
    explosions = temp;
  }
  
  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
     }
    else if (ch == KeyEvent.VK_RIGHT)
    {
       player.setVelocity(PLAYER_SPEED, player.getDeltaY());
    }
    else if (ch == KeyEvent.VK_A)
    {
      // jump
      player.setVelocity(player.getDeltaX(), PLAYER_JUMP_VELOCITY);
      player.setGravity(GRAVITY);
      player.setGrounded(false);
    }
  }
  
  @Override
  protected void doKeyReleased(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      player.setVelocity(0, player.getDeltaY());
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      player.setVelocity(0, player.getDeltaY());       
    }
  }
}
