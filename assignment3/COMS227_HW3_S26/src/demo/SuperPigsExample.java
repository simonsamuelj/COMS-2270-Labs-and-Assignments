package demo;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;

import api.CircleRenderer;
import api.Drawable;
import api.ExplosionRenderer;
import api.AbstractElement;
import api.ImageRenderer;
import api.Renderer;
import hw3.AttachedElement;
import hw3.FlyingElement;
import hw3.FollowerElement;
import hw3.LiftElement;
import hw3.MovingElement;
import hw3.PlatformElement;
import hw3.VanishingElement;

/**
 * Demo of pretty much all the game elements in hw3. The goal is to rescue the
 * princess. Jump on the apples to "eat" them, but don't run into them from the
 * side.
 * 
 * Left and right arrows to control player, 'a' to jump.
 * 
 * The two images need to be present in the src/demo directory, since they are
 * loaded as resources on the classpath, not as files in the working directory.
 * 
 * Requires AttachedElement, LiftElement, VanishingElement, FollowerElement,
 * FlyingElement, MovingElement, and PlatformElementElement.
 * 
 * @author smkautz
 */
public class SuperPigsExample extends GameBase
{
  public static void main(String[] args)
  {
    start(SuperPigsExample.class);
  } 
  
  // suppress compiler warning
  private static final long serialVersionUID = 1L;
  
  // Window size, fixed for simplicity
  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;

  // Basic dimensions to use for objects
  private static final int SIZE = 30;
  private static final int PLAYER_SIZE = 48;

  // names of image files
  private static final String PLAYER_IMAGE = "pig_small_alpha.png";
  private static final String APPLE_IMAGE = "apple_small_alpha.png";

  // Misc constants
  private static final double PLAYER_JUMP_VELOCITY = -12;
  private static final double PLAYER_BASE_VELOCITY = 5; // >= elevator velocities

  private static final double GRAVITY = 0.8;
  private static final double PLAYER_SPEED = 2;
  private static final int EXPLOSION_LIFETIME = 40;
  private static final int ENEMY_COUNT = 1;
  private static final int ENEMY_HIT_SCORE = 1000;
  private static final int CHARM_INTERVAL = 75;
  
  /**
   * The player
   */
  private FlyingElement player;
  
  /**
   * List of platforms
   */
  private ArrayList<PlatformElement>  platforms;
  
  /**
   * List of platforms
   */
  private ArrayList<LiftElement>  elevators;
  
  /**
   * List of explosions
   */
  private ArrayList<VanishingElement> explosions;
  
  /**
   * AttachedElement that may occasionally appear...
   */
  private AttachedElement charm;
  
  /**
   * Track the current platform, so we don't generate new enemies on it.
   */
  private AbstractElement currentPlatformElement;

  /**
   * Count of number of frames right arrow key has been held down, to potentially speed up
   * player.
   */
  private int rightArrow;
  
  /**
   * Count of number of frames left arrow key has been held down, to potentially speed up
   * player.
   */
  private int leftArrow;
  
  /**
   * Stop timer when game is over.
   */
  private boolean over;
  
  /**
   * Generator for random positions.
   */
  private Random rand;

  /**
   * Create this once, since we need it a lot.
   */
  private Renderer enemyRenderer;

  /**
   * Current score for the game.
   */
  private int score;
  
  /**
   * Renderer for player, so we can change player's direction.
   */
  private ImageRenderer renderer;

  /**
   * Renderer for princess, so we can change direction.
   */
  private ImageRenderer princessRenderer;
  
  /**
   * Counter to let a number of frames elapse before adding/removing charm.
   */
  private int charmCounter;
  
  /**
   * Tower for the princess
   */
  private MovingElement tower;
  
  /**
   * The princess!
   */
  private FlyingElement princess;
  
  /**
   * Initial speed of princess and tower.
   */
  private double princessSpeed = -0.5;
  
  /**
   * When we get a charm, pause the tower for a couple of seconds.
   */
  private boolean princessPaused = false;
  
  /**
   * Construct everything;
   */
  public SuperPigsExample()
  {
    rand = new Random();
    
    // create all the elements
    setupLevel();
  }
  
  private void setupLevel()
  {
    over = false;

    // load images
    Image playerImage = null;
    java.net.URL url = SuperPigsExample.class.getResource(PLAYER_IMAGE);
    if (url != null)
    {
      playerImage = new ImageIcon(url).getImage();
    }

    Image enemyImage = null;
    url = SuperPigsExample.class.getResource(APPLE_IMAGE);
    if (url != null)
    {
      enemyImage = new ImageIcon(url).getImage();
    }
    renderer = new ImageRenderer(playerImage, Color.PINK);
    princessRenderer = new ImageRenderer(playerImage, Color.PINK);
    
    // platforms
    platforms = new ArrayList<>();
    PlatformElement p0 = new PlatformElement(100 - SIZE * 2, 340, SIZE * 2, SIZE * 2);
    p0.setColor(Color.GREEN);
    platforms.add(p0);
    PlatformElement p1 = new PlatformElement(100, 400, SIZE * 5, SIZE);
    p1.setColor(Color.GREEN);
    platforms.add(p1);
    PlatformElement p2 = new PlatformElement(200, 480, SIZE * 5, SIZE);
    p2.setColor(Color.GREEN);
    platforms.add(p2);
    p2.setBounds(200, 500);
    p2.setVelocity(2, 0);  // moving
    PlatformElement p3 = new PlatformElement(500, 400, SIZE * 5, SIZE);
    p3.setColor(Color.GREEN);
    platforms.add(p3);    
    
    PlatformElement p5 = new PlatformElement(370, 280, SIZE * 4, SIZE / 2);
    platforms.add(p5);
     
    // elevators
    elevators = new ArrayList<>();
    
    LiftElement e0 = new LiftElement(200, 180, SIZE * 4, SIZE);
    elevators.add(e0);
    e0.setBounds(150, 350);
    e0.setVelocity(0, 3);  // moving
//    Platform e0 = new Platform(200, 350 , SIZE * 4, SIZE);
//    platforms.add(e0);
//    //e0.setBounds(150, 350);
//    //e0.setVelocity(0, 3);  // moving

    LiftElement e1 = new LiftElement(680, 200, SIZE * 2, SIZE);
    elevators.add(e1);
    //e1.setBounds(200, 350);
    //e1.setVelocity(0, 4);  // moving

    LiftElement e2 = new LiftElement(580, 100, SIZE * 2, SIZE);
    //System.out.println("xxxx " + e0.getChildren());
    elevators.add(e2);
    //e2.setBounds(150, 250);
    //e2.setVelocity(0, 5);  // moving

    // player
    player = new FlyingElement(100, 100, PLAYER_SIZE, PLAYER_SIZE);
    player.setRenderer(renderer);
    player.setPosition(p0.getXInt() + (p0.getWidth() - PLAYER_SIZE) / 2, p0.getYInt()  - PLAYER_SIZE);
    player.setVelocity(0, PLAYER_BASE_VELOCITY);
    currentPlatformElement = p0;

    // tower and princess
    tower = new MovingElement(700, 80, SIZE * 2, SIZE);
    tower.setVelocity(princessSpeed, 0);
    tower.setColor(Color.PINK);
    int princessSize =  (int) (PLAYER_SIZE * .8);
    princess =  new FlyingElement(100, 100, princessSize, princessSize);
    princess.setRenderer(princessRenderer);
    princess.setVelocity(princessSpeed, 0);
    princess.setPosition(tower.getXInt() + (tower.getWidth() - princessSize) / 2, tower.getYInt() - princessSize);
    
    // platforms and enemies
    enemyRenderer = new ImageRenderer(enemyImage, Color.RED);
    setUpEnemies();

    // no explosions yet (but there will be...)
    explosions = new ArrayList<>();

  }

  
  @Override
  protected int getGameWidth()
  {
    return WIDTH;
  }
  
  @Override
  protected int getGameHeight()
  {
    return HEIGHT;
  }

  @Override 
  protected int getScore()
  {
    return score;
  }
  
  @Override
  protected boolean isOver()
  {
    return over;
  }
  

  @Override
  protected ArrayList<Drawable> getAllDrawables()
  {
    ArrayList<Drawable> arr = new ArrayList<>();
    arr.add(player);
    arr.add(tower);
    arr.add(princess);
    if (charm != null)
    {
      arr.add(charm);
    }
    arr.addAll(platforms);
    for (PlatformElement p : platforms)
    {
      arr.addAll(p.getAssociated());
    }
    arr.addAll(elevators);
    for (LiftElement p : elevators)
    {
      arr.addAll(p.getAssociated());
    }
    arr.addAll(explosions); // render last
    return arr;
  }

  
  
  
  @Override
  protected void doUpdates()
  {
    player.update();
    tower.update();
    princess.update();
    for (AbstractElement p : platforms)
    {
      p.update();
    }
    for (AbstractElement p : elevators)
    {
      p.update();
    }
    for (AbstractElement s : explosions)
    {
      s.update();
    }

    // if player is on a moving platform, adjust for motion
    double deltaX = 0;
    if (currentPlatformElement instanceof PlatformElement)
    {
      deltaX = ((PlatformElement) currentPlatformElement).getDeltaX();
    }
    else if (currentPlatformElement instanceof LiftElement)
    {
      deltaX = ((LiftElement) currentPlatformElement).getDeltaX();
    }
    if (player.isGrounded() && deltaX != 0)
    {
      player.setPosition(player.getXReal() + deltaX, player.getYReal());
    }

    // adjust player speed if arrow key has been held down
    if (leftArrow > 0) 
    {
      leftArrow += 1;
      if (leftArrow > 5)
      {
        player.setVelocity(-PLAYER_SPEED * 2, player.getDeltaY());
      }
    }
    if (rightArrow > 0) 
    {
      rightArrow += 1;
      if (rightArrow > 5)
      {
        player.setVelocity(PLAYER_SPEED * 2, player.getDeltaY());
      }
    }
    
    for (PlatformElement s : platforms)
    {
      checkCollisionWithPlatformElement(s);
    }
    for (LiftElement s : elevators)
    {
      checkCollisionWithPlatformElement(s);
    }
    
    // if we wandered off a platform, go ballistic
    if (player.isGrounded() && ! isOnPlatformElement())
    {
      // I'm falling!!
      player.setGravity(GRAVITY);
      player.setGrounded(false);      
    }

    // in any case, check for collision with enemy
    ArrayList<FollowerElement> enemies = getAllEnemies();
    for (FollowerElement s : enemies)
    {
      checkCollidesWithEnemy(s);
    }
    
    // also the charm
    if (charm != null)
    {
      if (player.collides(charm))
      {
        charm.markForDeletion();
        VanishingElement f = new VanishingElement(charm.getXInt(), charm.getYInt(), SIZE * 2, SIZE * 2, EXPLOSION_LIFETIME); 
        f.setRenderer(new ExplosionRenderer(EXPLOSION_LIFETIME, Color.PINK, getBackgroundColor()));
        explosions.add(f);
        score += ENEMY_HIT_SCORE * 10;
        charm = null;
        princessPaused = true;
      }
    }

    // also the tower?
    checkCollisionWithPlatformElement(tower);
    if (currentPlatformElement == tower)
    {
      score += ENEMY_HIT_SCORE * 100;
      over = true;
    }
    
    // fall off screen?
    if (player.getYInt() + player.getHeight() > HEIGHT - SIZE)
    {
      over = true;
      renderer.flipVertical(true);
    }

    // princess falls off?
    if (princess.getYInt() + princess.getHeight() > HEIGHT - SIZE)
    {
      over = true;
      princessRenderer.flipVertical(true);
    }    
    
    if (princessPaused && tower.getXInt() > 25)
    {
      tower.setVelocity(0, 0);
      princess.setVelocity(0, 0);      
    }
    else
    {
      // if the tower gets halfway across, speed it up
      if (tower.getXInt() == WIDTH / 2)
      {
        princessSpeed *= 2;
        princessRenderer.flipHorizontal(true);
      }   

      // if the tower gets all the way to left, princess jumps off
      if (tower.getXInt() <= 25 && tower.getXInt() > 0)
      {
        princessRenderer.flipHorizontal(true);
        princess.setVelocity(3, 0);
      }
      else if (tower.getXInt() <= 0 && princess.isGrounded())
      {
        princess.setVelocity(3, 5);
        princess.setGravity(GRAVITY);
        princess.setGrounded(false);
      }
      else if (tower.getXInt() > 25)
      {
        // update motion of tower, maybe
        tower.setVelocity(princessSpeed, 0);
        princess.setVelocity(princessSpeed, 0);
      }
    }
    charmCounter -= 1;
    if (charmCounter <= 0)
    {
      if (charm != null)
      {
        charm.markForDeletion();
        charm = null;
        charmCounter = CHARM_INTERVAL + rand.nextInt(50);
      }
      else
      {
        makeAttachedElement();
        charmCounter = CHARM_INTERVAL + rand.nextInt(50);
        princessPaused = false;
      }
    }
    
    // take care of deletions
    deleteAllMarked();
  }
  
  /**
   *  Set the element's position to a random location on the platform.
   */
  private void setPositionOnPlatformElement(PlatformElement p, FollowerElement s)
  {
    int minX = p.getXInt();
    int maxX = p.getXInt() + p.getWidth() - s.getWidth();
    int bound = maxX - minX;
    bound = Math.max(1, bound);   
    int x = rand.nextInt(bound) + minX;
    int y = p.getYInt() - s.getHeight();
    s.setPosition(x, y);
  }

  /**
   *  Set the element's position to a random location on the platform.
   */
  private void setPositionOnPlatformElement(LiftElement p, FollowerElement s)
  {
    int minX = p.getXInt();
    int maxX = p.getXInt() + p.getWidth() - s.getWidth();
    int bound = maxX - minX;
    bound = Math.max(1, bound);   
    int x = rand.nextInt(bound) + minX;
    int y = p.getYInt() - s.getHeight();
    s.setPosition(x, y);
  }
  
  /**
   * Make sure there is an enemy on each platform other than the one the player is on
   */
  private void setUpEnemies()
  {
    for (PlatformElement p : platforms)
    {
      // don't plop an enemy on top of the player
      if (p != currentPlatformElement)
      {
        if (p.getAssociated().size() < ENEMY_COUNT)
        {
          FollowerElement e = new FollowerElement(SIZE, SIZE, 0);
          e.setRenderer(enemyRenderer);
          setPositionOnPlatformElement(p, e);
          p.addAssociated(e);
          double dx = 1 + rand.nextDouble();
          if (rand.nextInt(2) > 0) dx = -dx;
          e.setVelocity(dx, 0);
        }
      }
    }
    for (LiftElement p : elevators)
    {
      // don't plop an enemy on top of the player
      if (p != currentPlatformElement)
      {
        if (p.getAssociated().size() < ENEMY_COUNT)
        {
          FollowerElement e = new FollowerElement(SIZE, SIZE, 0);
          e.setRenderer(enemyRenderer);
          setPositionOnPlatformElement(p, e);
          p.addAssociated(e);
          double dx = 1 + rand.nextDouble();
          if (rand.nextInt(2) > 0) dx = -dx;
          e.setVelocity(dx, 0);
        }
      }
    }
  }

  private void makeAttachedElement()
  {
    int max = platforms.size() + elevators.size();
    int which = rand.nextInt(max);
    int count = 0;
    CircleRenderer r = new CircleRenderer(Color.PINK);
    r.setMargin(5);
    for (PlatformElement p : platforms)
    {
      if (count == which)
      {
        charm = new AttachedElement(SIZE, SIZE, (p.getWidth() - SIZE)/ 2, 100);
        charm.setRenderer(r);
        p.addAssociated(charm);
        return;
      }
      count += 1;
    }
    for (LiftElement p : elevators)
    {
      if (count == which)
      {
        charm = new AttachedElement(SIZE, SIZE, (p.getWidth() - SIZE)/ 2, 100);
        charm.setRenderer(r);
        p.addAssociated(charm);
        return;
      }
      count += 1;
    }
  }
  
  /**
   * Returns a list of all enemies.
   */
  private ArrayList<FollowerElement> getAllEnemies()
  {
    ArrayList<FollowerElement> arr = new ArrayList<>();
    for (PlatformElement p : platforms)
    {
      for (AbstractElement b : p.getAssociated())
      {
        if (b != charm)
        {
          arr.add((FollowerElement) b);
        }
      }
      
    }
    for (LiftElement p : elevators)
    {
      for (AbstractElement b : p.getAssociated())
      {
        if (b != charm)
        {
          arr.add((FollowerElement) b);
        }
      }
      
    }
    return arr;
  }


  /**
   * Determines whether the player is currently on a platform.
   */
  private boolean isOnPlatformElement()
  {
    double saved = player.getYReal();
    player.setPosition(player.getXReal(), saved + 2);
    for (PlatformElement s : platforms)
    {
      if (player.collides(s))
      {
        player.setPosition(player.getXReal(), saved);
        return true;
      }
    }
    for (LiftElement s : elevators)
    {
      if (player.collides(s))
      {
        player.setPosition(player.getXReal(), saved);
        return true;
      }
    }
    player.setPosition(player.getXReal(), saved);
    return false;
  }

  
  /**
   * Update player's state appropriately in case of collision with a platform.
   * This could be from below, left, right, or above (landing on the platform).
   */
  private void checkCollisionWithPlatformElement(AbstractElement platform)
  {
    
    if (player.collides(platform))
    {
      double deltaY = 0;
      // unusual code gives student's freedom to experiment with different implementations
      if (platform instanceof MovingElement) {
        MovingElement moving = (MovingElement) platform;     
        deltaY = moving.getDeltaY();
      } else if (platform instanceof LiftElement) {
        LiftElement lift = (LiftElement) platform;     
        deltaY = lift.getDeltaY();
      }

      // need to figure out whether we actually landed on the platform,
      // or collided with it in some other way. Strategy is to check
      // whether moving the player up a bit stops it from colliding,
      // where "a bit" is based on player dy plus maximal possible
      // vertical motion of platform.  We can't just use platform dy, because
      // it changes sign at top and bottom of motion, so use absolute
      // value of dy as a bound
      
      boolean landed = false;
      double possibleShift = 0;
      double fudge = .01;  // for possible rounding errors
      if (player.getDeltaY() > 0)
      {
        possibleShift  += player.getDeltaY();
      }
      if (deltaY < 0)
      {
        possibleShift += -deltaY;
      }
      else
      {
        possibleShift += deltaY;
      }

      double savedY = player.getYReal();
      player.setPosition(player.getXReal(), savedY - possibleShift - fudge);
      if (!player.collides(platform) && player.getDeltaY() > 0)
      {
        // ok, we landed
        landed = true;
      }
      if (landed)
      {
        // be on top of it...
        player.setPosition(player.getXReal(), platform.getYReal() - player.getHeight());
        player.setGrounded(true);
        player.setVelocity(player.getDeltaX(), PLAYER_BASE_VELOCITY);
        checkArrowKeys();
        currentPlatformElement = platform;
        
        // this will generate a new enemy on the platform the player left
        setUpEnemies();
      }
      else
      {
        // as we were
        player.setPosition(player.getXReal(), savedY);
        
        // Player is not landing.  If this is a collision from below,
        // then reverse the vertical velocity, and if it's horizontal
        // then set horizontal velocity to zero.  Note that player may
        // be ballistic or not in a horizontal motion.
        
        // temporarily set ballistic false, since we have to adjust dx
        boolean jumping = !player.isGrounded(); 
        player.setGrounded(true); 

        double newX = player.getXReal();
        double newY = player.getYReal();
        double dx = player.getDeltaX();
        double dy = player.getDeltaY();
        
        // this is all a bit crude and does not account for platform motion...
        
        if (dy < 0 && player.getYInt() < platform.getYInt() + platform.getHeight())
        {
          // collide from below
          newY = platform.getYInt() + platform.getHeight();
          dy = -dy;
        }
        else if (dx > 0 && (player.getXInt() + player.getWidth()) > platform.getXInt())
        {
          // collide from left
          newX = platform.getXInt() - player.getWidth();
          dx = 0;
        }
        else if (dx < 0 && player.getXInt() < platform.getXInt() + platform.getWidth())
        {      
          // collide from right
          newX = platform.getXInt() + platform.getWidth();   
          dx = 0;
        }

        player.setPosition(newX, newY);
        player.setVelocity(dx, dy);
        player.setGrounded(!jumping);

      }
    }
  }
  
  /**
   * Check whether an arrow key was released while player was ballistic, in
   * order to correctly set dx when landing.
   */
  private void checkArrowKeys()
  {
    // This is kind of tedious.  Since the player can't change dx when
    // ballistic, a release of the arrow key would not have been recorded
    // in player's dx.  So we have to explicitly check whether arrow key is 
    // down and set dx accordingly
    double dx = player.getDeltaX();
    if (leftArrow == 0 && rightArrow == 0) 
    {
      player.setVelocity(0, player.getDeltaY());
    }
    else if (leftArrow > 0)
    {
      if (dx < 0)
      {
        player.setVelocity(dx, 0);
      }
      else
      {
        player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
      }
    }
    else if (rightArrow > 0)
    {
      if (dx > 0)
      {
        player.setVelocity(dx, player.getDeltaY());
      }
      else
      {
        player.setVelocity(PLAYER_SPEED, player.getDeltaY());
      }
    }
  }
  
  /**
   * Check for player's collision with enemy.  If player lands on enemy from the
   * top, enemy dies; otherwise player dies.
   */
  private void checkCollidesWithEnemy(AbstractElement s)
  {
    if (player.collides(s))
    {
      // If we come in from top, we kill the enemy, but if we hit from side
      // then we lose.  So, if bottom of player is higher than half the enemy's height,
      // then it kills the enemy, otherwise it kills the player
      int playerBase = player.getYInt() + player.getHeight();
      int enemyBase = s.getYInt() + s.getHeight() / 2;
      if (playerBase < enemyBase)
      {
        //System.out.println("Enemy dies");
        s.markForDeletion();
        VanishingElement e = new VanishingElement(s.getXInt(), s.getYInt(), SIZE, SIZE, EXPLOSION_LIFETIME); 
        e.setRenderer(new ExplosionRenderer(EXPLOSION_LIFETIME, Color.ORANGE, getBackgroundColor()));
        explosions.add(e);
        score += ENEMY_HIT_SCORE;
      }
      else
      {
        //System.out.println("Player dies");
        over = true;
        renderer.flipVertical(true);
      }       
    } 
  }
  
  /**
   * Deletes all marked explosions or enemies.
   */
  private void deleteAllMarked()
  {
    ArrayList<VanishingElement> temp = new ArrayList<>();
    for (VanishingElement s : explosions)
    {
      if (!s.isMarked())
      {
        temp.add(s);
      }
    }
    explosions = temp;
    for (PlatformElement p : platforms)
    {
      p.deleteMarkedAssociated();
    }
    for (LiftElement p : elevators)
    {
      p.deleteMarkedAssociated();
    }
  }

  
  @Override
  protected void doKeyPressed(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      //System.out.println("left");
      leftArrow += 1;
      rightArrow = 0;
      player.setVelocity(-PLAYER_SPEED, player.getDeltaY());
      //playerFlipped = false;
      renderer.flipHorizontal(false);
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      //System.out.println("right");
      rightArrow += 1;
      leftArrow = 0;
      player.setVelocity(PLAYER_SPEED, player.getDeltaY());
      //playerFlipped = true;
      renderer.flipHorizontal(true);

    }
    else if (ch == KeyEvent.VK_A)
    {
      // jump
      if (player.isGrounded())
      {
        double jumpVelocity = PLAYER_JUMP_VELOCITY;
        
        // if we're on a moving platform, we carry that velocity too
        double deltaX = 0;
        if (currentPlatformElement instanceof PlatformElement)
        {
          deltaX = ((PlatformElement) currentPlatformElement).getDeltaX();
        }
        else if (currentPlatformElement instanceof LiftElement)
        {
          deltaX = ((LiftElement) currentPlatformElement).getDeltaX();
        }
        double dx = player.getDeltaX() + deltaX;
        
        // if we're moving fast, we can jump higher
        if (Math.abs(dx) >= PLAYER_SPEED * 2)
        {
          jumpVelocity *= 1.3;
        }
        player.setVelocity(dx, jumpVelocity);
        player.setGravity(GRAVITY);
        player.setGrounded(false);
      }
    }
  }
  
  @Override
  protected void doKeyReleased(int ch)
  {
    if (ch == KeyEvent.VK_LEFT)
    {
      //System.out.println("left released");
      leftArrow = 0;
      player.setVelocity(0, player.getDeltaY());       
    }
    else if (ch == KeyEvent.VK_RIGHT)
    {
      //System.out.println("right released");
      rightArrow = 0;
      player.setVelocity(0, player.getDeltaY());
    }
  }
}
