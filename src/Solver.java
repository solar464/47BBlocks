//add unit tests, normal and edge cases
/*
//debugging information
-oisOK checks if each board is legal
-oloop checks if a configuration was encountered before
-omoves prints the moves tested for a processed board
-ogoal prints an integer representing distance to goal for each printed board
-oprintPath prints a more detailed list of moves
-odisplay prints a visual display for each board printed
-omoveBlock prints a detailed process for each block movement
-otime run times for various methods or stages
-onumbers number of various objects created
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
//workspace for solving a (Board) of sliding (Block)s
//  data for the configurations and (Block)s are read in through files
public class Solver
{
   //dimensions of the (Board) and (Pair.table)
   private int h, w;
   
   //the initial empty spaces, calculated in the constructor and used to 
   //  construct (init)
   private HashSet<Pair> empty;
   
   //stores the past (Board) configurations to prevent infinite loops
   private HashSet<Board> past;
   
   //the initial and final configurations of the (Board)
   private Board init, goal;
   
   //debug controllers
   private boolean time = false, numbers = false, newConfig = false, isOK = false, 
         printPath1 = false, printPath2 = false, SolveTime = false; 
   private static boolean printBase = true, heuristic = false;
   public static void setPrintBase(boolean b){ printBase = b; }
   public static void setHeuristic(boolean b){ heuristic = b; }
   public static boolean getHeuristic(){ return heuristic; }
   
   //borrowed and modified from HashTest.java
   //finds a a file from the given file name and path and returns a 
   //  BufferedReader that reads it
   public static BufferedReader reader (String fileName) throws Exception {
      File wordFile = new File (fileName);
      FileReader stuff = new FileReader(wordFile);
      return new BufferedReader (stuff);
   }
   
   //constructor
   public Solver(HashSet<Block> initConfig, HashSet<Block> goalConfig, int height, int width, String debugString){
      h = height;
      w = width;
      
      empty = new HashSet<Pair>();
      
      //used to find the empty spaces
      boolean[][] model = new boolean[w][h];
      //System.out.println("Width * Height: " + w + " * " + h);
      for(Block b: initConfig){
         //System.out.println("Dealing with Block: " + b);
         
         for(int i = 0; i < b.getWidth(); i++){
            for(int j = 0; j < b.getHeight(); j++){
               //System.out.println("Pos: " + (b.getXPos() + i) + ", " + (b.getYPos() + j));
               model[b.getXPos() + i][b.getYPos() + j] = true;
            }
         }
      }
      
      for(int i = 0; i < w; i++)
         for(int j = 0; j < h; j++){
            if(model[i][j])
               continue;
            empty.add(Pair.getInstance(i, j));
         }
      
      
      init = new Board(initConfig, empty);
      goal = new Board(goalConfig);
      if(h * w > 100 && init.getBlocks().size() < 200)
         heuristic = true;
      //set the debug mode
      setDebugMode(debugString);
   }
   
   //various accessor methods
   public Board getInit(){ return init; }
   public Board getGoal(){ return goal; }
   public HashSet<Pair> getEmpty() { return empty; }
   public int getH(){ return h; }
   public int getW(){ return w; }
   
   //solves the puzzle using Breadth First Search and return the a (Board) that
   //  contains the (goal) configuration or null if it doesn't exist
   public Board solve(){
      if(init.checkSolved(goal))
         return init;
      long startTime = System.nanoTime();
      PriorityQueue<Board> toProcess = new PriorityQueue<Board>();
      past = new HashSet<Board>();
      init.generateCost(goal);
      toProcess.add(init);
      past.add(init);
      Board current, nBoard;
      
      while(!toProcess.isEmpty()){
 
         current = toProcess.poll();
         if(Board.getBOARD2())
            System.out.println("Board being processed: " + current);
         //move generation, attempt to move each (Block) in (current) in each direction
         for(Block b: current.getBlocks()){
            
            for(Pair drctn: Pair.DIRS){
               if(Board.getBOARD2())
                  System.out.println("Attempting to move " + b + " in direction " + drctn);
               nBoard = current.makeMove(b, drctn);
               
               //if null, the move was illegal
               if(nBoard == null)
                  continue;
               
               //check legal is -oisOK mode
               if(isOK){
                  try{nBoard.isOK();}
                  catch(IllegalStateException i){
                     System.out.println("Error at board " + nBoard);
                     System.out.println(i.getMessage());
                     System.exit(1);
                  }
                  catch(Exception e){
                     System.out.println("Error within the isOK() method.");
                     System.exit(1);
                  }
               }
               //checks to see if the (Board) was seen before
               if(!past.contains(nBoard)){
                  
                  //check if (nBoard) contains (goal)
                  if(nBoard.checkSolved(goal)){
                     if(SolveTime)
                        System.out.println("Time elapsed for solving: " + (System.nanoTime() - startTime)/1000000);
                     return nBoard;
                  }
                  
                  past.add(nBoard);
                  if(heuristic)
                     nBoard.generateCost(goal);
                  
                  if(newConfig){
                     System.out.println("New (Board) configuration encountered: " + nBoard);
                     System.out.println("Cost: " + nBoard.getCost());
                  }
                  toProcess.add(nBoard);
               }
                      
            }
         }
      }
      if(SolveTime)
         System.out.println("Time elapsed to exhaust all reachable configurations: " + (System.nanoTime() - startTime)/1000000);
      return null;
   }
   
   //takes a (Board) and prints the path to it from (init)
   //used  to print the path from (init) and (goal) if it exists
   public void printPath(Board last){
      //no solution if (last) is null
      if(last == null)
         System.exit(1);
      //find the boards on the path, not necessarily the shortest path
      Stack<Board> path = new Stack<Board>();
      Board current = last;
      while(current != null){
         path.push(current);
         current = current.getParent();
      }
      int numInPath = path.size();
      
      Block moved;
      Pair dir;
      //begin printing
      if(printPath1)
         System.out.println("Initial Configuration: " + path.pop());
      while(!path.isEmpty()){
         current = path.pop();
         moved = current.getMovedBlock();
         dir = current.getMovedDirection();
         
         if(current.getParent() != null){
            //base output
            if(printBase)
               System.out.println(moved.getPos().add(dir) + " " + moved.getPos());
         
            if(printPath1){
               System.out.println("Move Block " + current.getMovedBlock() 
                     + " in direction " + current.getMovedDirection());
               System.out.println("Current Board: " + current);
            }
         }
         if(printPath2)
            current.display();
      }
      
      
      if(printPath1)
         System.out.println("Goal Configuration: " + goal);  
      
         
 
      //debugging information regarding (Pair)s
      Pair.printCount();
      Pair.printInstanceCount();
      Pair.printFreq();
      
      //debugging information regarding (Block)s
      Block.printCount();
      if(Block.getBLOCK1()){
         int maxH = 0, maxW = 0;
         for(Block b: init.getBlocks()){
            if(b.getHeight() > maxH)
               maxH = b.getHeight();
            if(b.getWidth() > maxW)
               maxW = b.getWidth();
         }
         System.out.println("Estimated number of unique (Block)s possible: "
               + (maxH * maxW) * (getH() * getW()));
      }
      Block.printInstanceCount();
      Block.printTimes();
      
      //debugging information regarding (Board)s
      Board.printCount();
      if(Board.getBOARD1())
         System.out.println("Number of unique (Board)s encountered: " + past.size());
      Board.printTimes();
      
      if(numbers)
         System.out.println("Number of moves made to reach goal: " + numInPath);
   }
   
   //debugging options, shown if first argument is "-ooptions"
   public static void printOptions(){
      System.out.println();
      String options = 
         "-oPair1    number of (Pair) objects created, not counting the direction (Pair)s \n"
       + "-oPair2    -oPair1 and number of references to (Pair) objects created, not counting the direction (Pair)s \n"
       + "-oPair3    -oPair2 and display number of references to each specific (Pair), not counting the direction (Pair)s \n\n"
       
       + "-oBlock1   number of (Block) objects created and estimated number of unique (Block)s possible\n"
       + "-oBlock2   -oBlock1 and for every call to move() print the (Block), direction, spaces, and updated (Block) and spaces\n"
       + "-oBlock3   -oBlock2 and prints total runtimes of relevant methods: shouldBeEmpty(), canMove(), move()\n\n"
    
       + "-oBoard1   number of (Board) objects created and number of unique (Board)s encountered\n"
       + "-oBoard2   -Board1, and print newly generated (Board)s\n"
       + "-oBoard3   -Board2 and prints total runtimes of relevant methods: makeMove(), deepClone(), checkSolved(), equals()\n\n"
    
       + "-oSolveTime   prints the time taken to solve the puzzle only\n"
       + "-oisOK        checks if each board is legal\n"
       + "-onewConfig   prints if a encounters a new configuration and its cost\n"
       + "-oprintPath1  prints a more detailed list of moves\n"
       + "-oprintPath2  -oprintPath1 with display for each (Board) in the path\n"
   
       + "-otime        prints all run times for various methods or stages of all classes\n"
       + "-onumbers     -oPair2, -oBlock1, -oBoard1, number of moves made to solve\n"
       + "\n*Preceding any command with \"--\" instead of \"-o\" will erase the base output\n"
       + "\n*Preceding any command with \"-h\" instead of \"-o\" will reverse the usage of the heuristic\n"
       + "\n*Preceding any command with \"-H\" instead of \"-o\" will have the combined effect of both above\n"
       + "*All run times are printed in milliseconds";
       System.out.println(options);
   }
   
   public void setDebugMode(String dbmode){
      if(dbmode == null){
         return;
      }
      if(dbmode.equals("Pair1"))
         Pair.setDebug(true, false, false);
      else if(dbmode.equals("Pair2"))
         Pair.setDebug(true, true, false);
      else if(dbmode.equals("Pair3")){
         Pair.setDebug(true, true, true);
         Pair.resetFreq(getH(), getW());
      }
      
      else if(dbmode.equals("Block1"))
         Block.setDebug(true, false, false);
      else if(dbmode.equals("Block2"))
         Block.setDebug(true, true, false);
      else if(dbmode.equals("Block3")){
         Block.setDebug(true, true, true);
      }
      
      else if(dbmode.equals("Board1"))
         Board.setDebug(true, false, false);
      else if(dbmode.equals("Board2"))
         Board.setDebug(true, true, false);
      else if(dbmode.equals("Board3")){
         Board.setDebug(true, true, true);
      }
      
      else if(dbmode.equals("numbers")){
         Pair.setDebug(true,  true,  false);
         Block.setDebug(true, false, false);
         Board.setDebug(true, false, false);
         numbers = true;
      }
      else if(dbmode.equals("time")){
         Pair.setDebug(false,  false, true);
         Block.setDebug(false, false, true);
         Board.setDebug(false, false, true);
         time = true;
      }
      
      else if(dbmode.equals("newConfig"))
         newConfig = true;
      else if(dbmode.equals("isOK"))
         isOK = true;
      else if(dbmode.equals("printPath1"))
         printPath1 = true;
      else if(dbmode.equals("printPath2")){
         printPath1 = true;
         printPath2 = true; 
      }
      else if(dbmode.equals("SolveTime"))
         SolveTime = true;
      else{
         System.out.println("Invalid debugging option.");
         System.exit(1);
      }
   }
   
   //main method
   public static void main(String[] args) throws Exception{
      if(args.length == 0 || args.length > 3 || args[0].length() < 2){
         System.out.println("Number of parameters should be 2 or 3 or \"-ooptions\".");
         System.exit(1);
      }
      
      String DBMode = null, initFile, goalFile;
      if(args[0].equals("-ooptions")){
         printOptions();
         System.exit(1);
      }
      
      char debugg = args[0].charAt(0);
      if(debugg == '-'){
         char identifier = args[0].charAt(1);
         if(identifier == '-')
            setPrintBase(false);
         else if(identifier == 'h')
            setHeuristic(!getHeuristic());
         else if(identifier == 'H'){
            setPrintBase(false);
            setHeuristic(!getHeuristic());
         }
         DBMode = args[0].substring(2);
         initFile = args[1];
         goalFile = args[2];
      }
      else{
         initFile = args[0];
         goalFile = args[1];
      }
      //Taken from HashTest.java and modified.
      BufferedReader wordReader;
      HashSet<Block> initConfig = new HashSet<Block>();
      HashSet<Block> goalConfig = new HashSet<Block>();
      String[] bVars;

      wordReader = reader(initFile);
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/resources/c33.txt");
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/resources/test_init.txt");
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/tests/hard/big.tray.3");
      //wordReader = reader("C:/Users/Kevin/workspace/47BBlocks/tests/easy/init.from.handout");
      bVars = wordReader.readLine().split(" ");
      int height = Integer.parseInt(bVars[0]);
      int width = Integer.parseInt(bVars[1]);
      
      //generate the Pair objects that may be needed
      Pair.generateTable(width, height);
      //initialize storage of (Block)s
      Block.initializePieces();
      
      do {
          try {
              bVars = wordReader.readLine ().split(" ");
          } catch (Exception e) {
              //System.err.println (e.getMessage());
              break;
          }
          if (bVars == null) {
              break;
          } else {
              initConfig.add (Block.getInstance(Integer.parseInt(bVars[0]),
                    Integer.parseInt(bVars[1]), Integer.parseInt(bVars[3]),
                    Integer.parseInt(bVars[2])));
          }
      } while (true);

      wordReader = reader(goalFile);
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/resources/c33goal.txt");
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/resources/test_goal.txt");
      //wordReader = reader ("C:/Users/Kevin/workspace/47BBlocks/tests/hard/big.tray.3.goal");
      //wordReader = reader("C:/Users/Kevin/workspace/47BBlocks/tests/easy/goal.2.from.handout");
      do {
          try {
              bVars = wordReader.readLine ().split(" ");
          } catch (Exception e) {
              //System.err.println (e.getMessage());
              break;
          }
          if (bVars == null) {
              break;
          } else {
              goalConfig.add (Block.getInstance(Integer.parseInt(bVars[0]),
                    Integer.parseInt(bVars[1]), Integer.parseInt(bVars[3]),
                    Integer.parseInt(bVars[2])));
          }
      } while (true);
      
      Solver puzzle = new Solver(initConfig, goalConfig, height, width, DBMode);
      
      /*
      //various debugging statements, confirm that (Solver) initialized correctly
      System.out.println("Board dimensions (w * h): " + puzzle.w + " * " + puzzle.h);
      System.out.println("Empty spaces: " + puzzle.empty);
      System.out.println("Starting board: " + puzzle.init);
      puzzle.init.display();
      System.out.println("Goal board: " + puzzle.goal);
      */

      //solves and prints the puzzle
      Board result = puzzle.solve();
      puzzle.printPath(result);
      
   }
   
   /*
   //helper class to associate cost values to (Board)s for use in (toProcess)
   private class BWrapper implements Comparable<BWrapper>{
      private Board self;
      private int cost;
      
      public BWrapper(Board b){
         self = b;
         cost = this.generateCost(goal);
      }
      
      private int generateCost(Board goal){
         LinkedList<Block> toCheck = new LinkedList<Block>();
         toCheck.addAll(self.getBlocks());
         Block minB;
         int toReturn = 0;
         
         for(Block b: goal.getBlocks()){
            toReturn += findMinCost(toCheck, b);
         }
         return toReturn;
      }
      
      private int findMinCost(LinkedList<Block> toCheck, Block goalB){
         int toReturn = Integer.MAX_VALUE;
         int maybeMin;
         Block closestBlock = null;
         for(Block b: toCheck){
            if(b.getHeight() != goalB.getHeight() || b.getWidth() != goalB.getWidth())
               continue;
            
            maybeMin = Pair.manhattanDis(goalB.getPos(), b.getPos());
            
            if(maybeMin < toReturn){
               toReturn = maybeMin;
               closestBlock = b;
            }
         }
         if(toReturn == Integer.MAX_VALUE)
            System.out.println("A goal block doesn't seem to exist...");
         toCheck.remove(closestBlock);
         return toReturn;
      }
      
      public Board getBoard(){ return self; }
      public int getCost(){ return cost; }
      
      @Override
      public int compareTo(BWrapper other)
      {
         return this.cost - other.cost;
      }
      
   }
   */
}
