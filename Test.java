import java.util.*;
import java.io.*;
public class Test {
   public static void main(String[] args) throws FileNotFoundException {
      Scanner input = new Scanner(new File("dictionary2.txt"));
      List<String> dictionary = new ArrayList<String>();
      while (input.hasNext()) {
          dictionary.add(input.next().toLowerCase());
      }
   
      HangmanManager x = new HangmanManager(dictionary, 4, 8);
      
      System.out.println("'" + x.pattern() + "'");
      x.record('e');
      System.out.println("'" + x.pattern() + "'");
      x.record('o');
      System.out.println("'" + x.pattern() + "'");
      x.record('d');
      System.out.println("'" + x.pattern() + "'");
      x.record('c');
      System.out.println("'" + x.pattern() + "'");
   }
}