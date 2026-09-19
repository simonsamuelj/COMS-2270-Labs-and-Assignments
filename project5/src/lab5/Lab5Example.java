package lab5;

public class Lab5Example
{
  public static void main(String[] args)
  {
    System.out.println(longestRun("aabbbccd"));
    System.out.println("Expected 3");
    System.out.println(longestRun("aaa"));
    System.out.println("Expected 3");
    System.out.println(longestRun("aabbbb"));
    System.out.println("Expected 4");
  }


  public static int longestRun(String s)
  {
    if (s.length() == 0) {
      return 0;
    }

    int count = 1;
    int max = 1;

    char current = s.charAt(0);

    for (int i = 1; i < s.length(); i++)
    {
      char c = s.charAt(i);

      if (c == current)
      {
        count++;
      }
      else
      {
        if (count > max)
        {
          max = count;
        }

        current = c;
        count = 1;
      }
    }

    // check the last run
    if (count > max)
    {
      max = count;
    }

    return max;
  }

  
  
}
