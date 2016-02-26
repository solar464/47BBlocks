import java.util.Arrays;
import java.util.HashSet;


public class Pair
{
   //for use in moving blocks
   public final static Pair UP = new Pair(0,-1), 
                            DOWN = new Pair(0,1),
                            LEFT = new Pair(-1,0),
                            RIGHT = new Pair(1,0);
   
   //for looping purposes in Solver.solve()
   public final static Pair[] DIRS = { UP, DOWN, LEFT, RIGHT };
   
   //instance variables
   private int x, y;
   
   //debugging controls
   private static boolean PAIR1 = false, PAIR2 = false, PAIR3 = false;
   public static void setDebug(boolean one, boolean two, boolean three){
      PAIR1 = one; PAIR2 = two; PAIR3 = three;
      if(PAIR1)
         setCount(0);
      if(PAIR2)
         setInstanceCount(0);
   }
   
   //counts the number of (Pair) objects created
   private static int count;
   
   public static void setCount(int num){ count = num; }
   public static int getCount(){ return count; }
   public static void printCount(){
      if(PAIR1)
         System.out.println("Number of (Pair) objects created besides (table) and directions: " + count);
   }
   
   //counts the number of references to (Pair) objects created
   private static int instanceCount;
   
   public static void setInstanceCount(int num){ instanceCount = num; }
   public static int getInstanceCount(){ return instanceCount; }
   public static void printInstanceCount(){
      if(PAIR2)
         System.out.println("Number of (Pair) references created: " + instanceCount);
   }
   
   //count the frequency of (Pair) use for each unique (Pair)
   private static int[][] freq;
   public static int[][] getFreq(){ return freq; }
   public static void printFreq(){
      if(PAIR3 && freq != null){
         System.out.println("Frequency of Pair use: ");
         for(int i = 0; i < freq[0].length; i++){
            for(int j = 0; j < freq.length; j++){
               System.out.print("[" + freq[j][i] + "]");
            }
            System.out.print("\n");
         }
      }
   }
   public static void resetFreq(int h, int w){
      if(PAIR3)
         freq = new int[w][h]; 
   }
   
   //table to store references to all Pair objects that may be needed to avoid
   //   wasting memory creating redundant Pair objects
   private static Pair[][] table = null;
   
   //returns a reference to the Pair that contains the coordinate given
   //returns null if beyond the dimensions of (table) and therefore, the board
   public static Pair getInstance(int xCoor, int yCoor){
      instanceCount++;
      
      try{
         if(freq != null)
            freq[xCoor][yCoor]++;
         return table[xCoor][yCoor]; 
      }
      catch(ArrayIndexOutOfBoundsException a){ return null; }
      catch(NullPointerException n){
         System.out.println("Table of pairs not initialized"); 
         return null;
      }
   }
   
   //initializes (table) with needed Pair objects, used in Solver.main
   //will require regeneration of table if beginning new puzzle with different dimensions
   public static void generateTable(int width, int height){
      //prevent erasure of original table, alter if consecutive solving required
      if(table != null)
         return;
               
      table = new Pair[width][height];
      for(int i = 0; i < width; i++)
         for(int j = 0; j < height; j++)
            table[i][j] = new Pair(i, j);
   }
   
   //constructor only used in generateTable() and final static Pair at top
   private Pair(int xCoor, int yCoor){
      x = xCoor; y = yCoor;
      count++;
   }
   
   //various accessor methods
   public int getX() { return x; }
   public int getY() { return y; }
   public static int getW(){return table.length;}
   public static int getH(){return table[0].length;}
   
   //add operation, returns reference to the Pair that contains the sum of the
   // two given Pairs if it exists
   public Pair add(Pair other){
      if(other == null)
         return null;
      return getInstance(x + other.x, y + other.y); 
   }
   
   //Pair objects other than UP and LEFT cannot have negative numbers
   public Pair sub(Pair other){
      if(other == null){
         return null;
      }
      return getInstance(x - other.x, y - other.y); 
   }
   
   //returns reference to a multiple of the Pair
   public Pair mult(int m){
      return getInstance(x * m, y * m);
   }
   
   //largely used for HashSet when storing empty spaces
   public int hashCode(){
      if(x < 0 || y < 0)
         return (x + "" + y).hashCode();
      return Integer.parseInt(x + "" + y);
   }
   
   public String toString(){
      return y + " " + x;
   }
   
   //calculates the manhattan distance between two (Pair)s
   public static int manhattanDis(Pair here, Pair there){
      return Math.abs(here.x - there.x) + Math.abs(here.y - there.y);
   }
   
   //various operations testing Pair operations and initialization
   public static void main(String args[]){
      Pair.generateTable(4, 4);
      System.out.println("Available Pair objects after generateTable(4,4): " + Arrays.deepToString(table));
      Pair.generateTable(0, 0);
      System.out.println("Available Pair objects after second generateTable(0,0): " + Arrays.deepToString(table));
      System.out.println("\nAttempting getInstance(5,0): " + getInstance(5,0));
      System.out.println("Attempting getInstance(-3,0): " + getInstance(-3,0));
      System.out.println("Attempting getInstance(0,0): " + getInstance(0,0));
      System.out.println("Attempting new Pair(5,0): " + new Pair(5,0));
      
      
      System.out.println("\nHashCode testing: ");
      Pair a = getInstance(1,0);
      System.out.println("Current Pair: " + a);
      System.out.println("HashCode of " + a + " = " + a.hashCode());

      System.out.println("\nequals() testing: ");
      System.out.println("Different objects: " + a.equals(new Pair(1,0)));
      System.out.println("Using getInstance() : " + a.equals(getInstance(1,0)));
      
      System.out.println("\nadd() testing: ");
      Pair b = getInstance(2,3);
      Pair c = getInstance(3,3);
      Pair zero = new Pair(0,0);
      System.out.println(a + " + " + b + " = " + a.add(b));
      System.out.println(a + " + " + c + " = " + a.add(c));
      System.out.println(a + " + " + zero + " = " + a.add(zero));

      
      System.out.println("\nsub() testing: ");
      System.out.println(b + " - " + a + " = " + b.sub(a));
      System.out.println(a + " - " + c + " = " + a.sub(c));
      System.out.println(c + " - " + a + " = " + c.sub(a));
      System.out.println(a + " - " + a + " = " + a.sub(a));
      System.out.println(a + " - " + zero + " = " + a.sub(zero));

      System.out.println("\nmult() testing");
      Pair d = a.mult(2);
      System.out.println(a + " * 2 = " + d);
      System.out.println(a + " * 5 = " + a.mult(5));
      System.out.println(a + " * 0 = " + a.mult(0));
      System.out.println(a + " * -1 = " + a.mult(-1));

      
      System.out.println("\nHashSet testing");
      HashSet<Pair> s = new HashSet<Pair>();
      s.add(getInstance(0,0));s.add(getInstance(0,1));s.add(getInstance(0,2));
      System.out.println("Original set s = " + s);
      
      System.out.println("s1 = s");
      HashSet<Pair> s1 = s;
      s1.remove(getInstance(0,0));
      System.out.println("After removing (0,0): ");
      System.out.println("s = " + s);
      System.out.println("s1 = " + s1);
      
      s.add(getInstance(0,0));
      System.out.println("\nOriginal set s = " + s);
      System.out.println("After copying references into s2 and removing (0,0) from s2:");
      
      HashSet<Pair> s2 = new HashSet<Pair>();
      for(Pair p: s)
         s2.add(p);
      
      s2.remove(getInstance(0,0));
      
      System.out.println("s = " + s);
      System.out.println("s2 = " + s2);
      
      System.out.println("manhattanDis() testing:");
      System.out.println(a + " to " + c + " = " + manhattanDis(a, c));
      System.out.println(c + " to " + a + " = " + manhattanDis(c, a));
      System.out.println(a + " to " + a + " = " + manhattanDis(a, a));
      System.out.println(a + " to " + zero + " = " + manhattanDis(a, zero));
      System.out.println("(-123,456) to (0,455) = " + manhattanDis(new Pair(-123, 456), new Pair(0,455)));


   }
}
