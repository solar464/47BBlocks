import java.util.*;

//Representation of a board with (Block)s and the empty spaces
public class Board
{
   //stores the blocks
   private HashSet<Block> allBlocks;
   //stores the empty spaces
   private HashSet<Pair> spaces;
   
   //stores information about the path to (this)
   //refers to the previous (Board) configuration and the (Block) in the parent
   //  that was moved and the (Pair) direction it was moved in
   private Board parent = null;
   private Block movedBlock = null;
   private Pair movedDirection = null;
   
   //debug controls
   private static boolean BOARD1 = false, BOARD2 = false, BOARD3 = false;
   public static boolean getBOARD1(){ return BOARD1; }
   public static boolean getBOARD2(){ return BOARD2; }
   public static void setDebug(boolean one, boolean two, boolean three){
      BOARD1 = one; BOARD2 = two; BOARD3 = three;
      if(BOARD1)
         setCount(0);
      if(BOARD3)
         resetTimes();
   }
   
   //counts the number of (Board) objects created
   private static int count;
   public static void setCount(int num){ count = num; }
   public static int getCount(){ return count; }
   public static void printCount(){
      if(BOARD1)
         System.out.println("Number of (Board)s created: " + count);
   }
   
   //track total runtimes of various methods
   private static long startTime;
   private static long makeMoveTime, deepCloneTime, checkSolvedTime, equalsTime;
   public static void resetTimes(){ makeMoveTime = 0; deepCloneTime = 0; checkSolvedTime = 0; equalsTime = 0;}
   public static long getMakeMove(){ return makeMoveTime; }
   public static long getDeepClone(){ return deepCloneTime; }
   public static long getCheckSolved(){ return checkSolvedTime; }
   public static long getEquals(){ return equalsTime; }
   public static void printTimes(){
      if(BOARD3){
         System.out.println("\nTotal runtimes of relevant methods in (Board): ");
         System.out.println("makeMove(): " + makeMoveTime/1000000 + "\ndeepClone(): "
            + deepCloneTime/1000000 + "\ncheckSolved(): " + checkSolvedTime/1000000 + "\nequals(): "
            + equalsTime/1000000);
      }
      
   }
   
   //constructor, used only for (goal) in Solver.java, where the empty spaces
   //  are irrelevant
   public Board(HashSet<Block> all){
      allBlocks = all;
      count++;
   }
   
   //overloaded constructor to include initialization of empty spaces as well
   public Board(HashSet<Block> all, HashSet<Pair> empty){
      allBlocks = all;
      spaces = empty;
      count++;
   }
   
   //sets the information regarding the path to (this)
   //only sets if the information was not there before
   public void setPath(Board prev, Block moved, Pair dir){
      if(parent == null && movedBlock == null && movedDirection == null){
         parent = prev;
         movedBlock = moved;
         movedDirection = dir;
      }
   }
   
   //various accessor methods
   public HashSet<Block> getBlocks(){ return allBlocks; }
   public HashSet<Pair> getSpaces(){ return spaces; }
   public Board getParent(){ return parent; }
   public Block getMovedBlock(){ return movedBlock; }
   public Pair getMovedDirection(){ return movedDirection; }
   
   //generates a new board with the given (Block) moved in the given (direction)
   //  for use in Solver.solve() in move generation
   //  returns null if the given move is illegal
   public Board makeMove(Block b, Pair direction){
      startTime = System.nanoTime();
      Board toReturn = null;
      if(b == movedBlock && direction == movedDirection){
         if(BOARD3)
            makeMoveTime += System.nanoTime() - startTime;
         return null;
      }
      LinkedList<Pair> toFill = b.shouldBeEmpty(direction, spaces);
      Block moved;
      if(toFill != null){
         //System.out.println("Move " + b + " in direction " + direction);
         toReturn = this.deepClone();
         //System.out.println(direction + " " + toFill + " " + toReturn.spaces);
         moved = b.move(direction, toFill, toReturn.spaces);
         //System.out.println(b + " " + moved);
         //remove original block and add the updated or moved version
         toReturn.allBlocks.remove(b);
         if(!toReturn.allBlocks.add(moved)){
            System.out.println("Failed to add " + moved);
         }
         
         //set the path
         Pair unmake = null;
         for(int i = 0; i < Pair.DIRS.length; i++){
            if(Pair.DIRS[i] == direction){
               unmake = Pair.DIRS[Pair.DIRS.length - 1 - i];
               break;
            }
         }
            
         toReturn.setPath(this, moved, unmake);
         
         //debugging statements
         if(BOARD3)
            makeMoveTime += System.nanoTime() - startTime;
         if(BOARD2)
            System.out.println("New (Board): " + toReturn);
      }
      
      return toReturn;      
   }
   
   
   /*
   //no longer used
   //was used to find the (Block)s around (spaces) in a (Board)
   //if a (Block) is next to an empty space, it might be able to move
   public HashMap<Block, Pair> adjacentBlocks(){
      HashMap<Pair, Pair> adjCoor = adjacentCoor();
      HashMap<Block, Pair> toReturn = new HashMap<Block, Pair>();
      for(Pair coor: adjCoor.keySet())
         for(Block b: allBlocks){
            if(b.withinBlock(coor)){
               toReturn.put(b, adjCoor.get(coor));
               break;
            }
         }
      return toReturn;
   }
   
   //no longer used
   //find the surrounding coordinates, used with Block.withinBlock() to find
   //   surrounding (Block) objects in adjacentBlocks(), which are then tested 
   //   to see if they can move by Block.shouldBeEmpty() and Block.canMove()
   public HashMap<Pair, Pair> adjacentCoor(){
      HashMap<Pair, Pair> toReturn = new HashMap<Pair, Pair>();
      int x, y;
      for(Pair coor: spaces){
         x = coor.getX(); y = coor.getY();
         if(x > 0)
            toReturn.put(coor.add(Pair.LEFT), Pair.RIGHT);
         if(x < Pair.getW() - 1)
            toReturn.put(coor.add(Pair.RIGHT), Pair.LEFT);
         
         if(y > 0)
            toReturn.put(coor.add(Pair.UP), Pair.DOWN);
         
         if(y < Pair.getH() - 1)
            toReturn.put(coor.add(Pair.DOWN), Pair.UP);
      }
      
      //some empty spaces may be added if adjacent to eachother, remove them
      for(Pair coor: spaces)
         toReturn.remove(coor);
      
      return toReturn;
   }
   */
   
   //two (Board)s are equal if their (Block) configuration are entirely the same
   public boolean equals(Object other){
      startTime = System.nanoTime();
      if(!(other instanceof Board)){
         if(BOARD3)
            equalsTime += System.nanoTime() - startTime;
         return false;
      }
      
      //debugging statement
      if(((Board) other).allBlocks.size() != allBlocks.size()){
         System.out.println("Something's wrong: number of blocks isn't consistent.");
         if(BOARD3)
            equalsTime += System.nanoTime() - startTime;
         return false;
      }
      
      for(Block b: allBlocks){
         if(!((Board) other).allBlocks.contains(b)){
            if(BOARD3)
               equalsTime += System.nanoTime() - startTime;
            return false;
         }
      }
      if(BOARD3)
         equalsTime += System.nanoTime() - startTime;
      return true;
   }
   
   //for use in Solver where (Board)s are stored in (past), past configurations
   public int hashCode(){
      int hash = 1;
      for(Block b: allBlocks)
         hash *= b.hashCode();
      return hash;
   }
   
   public String toString(){
      return allBlocks.toString();
   }
   
   //for use in move generation in makeMove()
   public Board deepClone(){
      startTime = System.nanoTime();
      HashSet<Block> nBlocks = new HashSet<Block>();
      HashSet<Pair> nSpaces = new HashSet<Pair>();
      
      
      //for(Block b: allBlocks)
      //   nBlocks.add(b.deepClone());
      //   nBlocks.add(b);
         
      nBlocks.addAll(allBlocks);
      
      //for(Pair s: spaces)
      //   nSpaces.add(s);
         
      nSpaces.addAll(spaces);
      
      if(BOARD3)
         deepCloneTime += System.nanoTime() - startTime;
      return new Board(nBlocks, nSpaces);
   }
   
   //checks if the given (Board) configuration is within (this)
   //used to check if (Solver.goal) is met
   public boolean checkSolved(Board goalBoard){
      startTime = System.nanoTime();
      for(Block goal: goalBoard.getBlocks()){
         if(!allBlocks.contains(goal)){
            if(BOARD3)
               checkSolvedTime += System.nanoTime() - startTime;
            return false;
         }
      }
      if(BOARD3)
         checkSolvedTime += System.nanoTime() - startTime;
      return true;
   }
   
   public void isOK() throws IllegalStateException{
      if(spaces == null)
         throw new IllegalStateException("No empty spaces specified.");
      boolean[][] modelBoard = new boolean[Pair.getW()][Pair.getH()];
      int coveredArea = 0;
      
      for(Block b: getBlocks()){
         coveredArea += b.getHeight() * b.getWidth();
         for(int i = 0; i < b.getWidth(); i++){
            for(int j = 0; j < b.getHeight(); j++){
               try{
                  if(modelBoard[b.getXPos() + i][b.getYPos() + j])
                     throw new IllegalStateException(b + " overlapped with another block.");
               }
               catch(ArrayIndexOutOfBoundsException a){
                  throw new IllegalStateException(b + " outside of board.");
               }
               modelBoard[b.getXPos() + i][b.getYPos() + j] = true;
            }
         }
      }
      
      if(modelBoard.length * modelBoard[0].length - coveredArea != spaces.size())
         throw new IllegalStateException("Number of empty spaces incorrect");
      else{
         for(Pair p: spaces){
            if(modelBoard[p.getX()][p.getY()])
               throw new IllegalStateException("Empty space " + p + " not in correct position");
         }
      }
   }
   
   //creates a visual representation of (this) using a 2-D array
   public void display(){
      String[][] squares = new String[Pair.getW()][Pair.getH()];
      int a = 0;
      
      for(Block b: allBlocks){
         for(int i = b.getXPos(); i < b.getWidth() + b.getXPos() && i < squares.length; i++){
            for(int j = b.getYPos(); j < b.getHeight() + b.getYPos() && j < squares[0].length; j++){
               squares[i][j] = a + "";
            }
         }
         a++;
      }
      
      if(spaces != null)
         for(Pair p: spaces)
            squares[p.getX()][p.getY()] = " ";
      
      for(int i = 0; i < squares[0].length; i++){
         for(int j = 0; j < squares.length; j++){
            System.out.print(" [" + squares[j][i] + "] ");
         }
         System.out.println();
      }
   }
   
   public static void main(String[] args){
      Pair.generateTable(3, 3);
      Block.initializePieces();
      HashSet<Block> pieces = new HashSet<Block>();
      pieces.add(Block.getInstance(1,1,0,0));
      pieces.add(Block.getInstance(2,2,0,1));
      Block b = Block.getInstance(1,1,2,0);
      pieces.add(b);
      
      HashSet<Pair> spaces = new HashSet<Pair>();
      spaces.add(Pair.getInstance(1, 0));
      spaces.add(Pair.getInstance(2, 1));
      spaces.add(Pair.getInstance(2, 2));
      
      HashSet<Pair> allPairs = new HashSet<Pair>();
      for(int i = 0; i < Pair.getW(); i++)
         for(int j = 0; j < Pair.getH(); j++)
            allPairs.add(Pair.getInstance(i,j));
      
      Board brd = new Board(pieces, spaces);
      Board blank = new Board(new HashSet<Block>(), allPairs);
      
      System.out.println("setPath() tests:");
      brd.setPath(brd, Block.getInstance(0,0,9,9), Pair.getInstance(0,2));
      brd.setPath(new Board(new HashSet<Block>()), Block.getInstance(1,1,1,1), Pair.getInstance(0,1));
      System.out.println("Parent Board: " + brd.getParent());
      System.out.println("Moved Block: " + brd.getMovedBlock());
      System.out.println("Moved direction: " + brd.getMovedDirection());
      
      System.out.println("\nmakeMove() tests");
      System.out.println("Current board: " + brd);
      Board brd1 = brd.makeMove(Block.getInstance(1,1,2,0), Pair.DOWN);
      Board brd2 = brd.makeMove(Block.getInstance(1,1,2,0), Pair.getInstance(0,1));
      Board brd3 = brd.makeMove(b, Pair.DOWN);
      Board brd4 = brd.makeMove(Block.getInstance(2,2,0,1), Pair.DOWN);
      System.out.println("Moving new " + b + " in direction " + Pair.DOWN + " : " + brd1);
      System.out.println("Moving new " + b + " in direction (0,1)"  + " : " + brd2);
      System.out.println("Moving " + b + " in direction " + Pair.DOWN + " : " + brd3);
      System.out.println("Moving new " + Block.getInstance(2,2,0,1) + " in direction " + Pair.DOWN + " : " + brd4);

      System.out.println("\nequals() testing:");
      System.out.println("Different Boards: " + brd.equals(brd2));
      System.out.println("Same Board: " + brd.equals(brd));
      System.out.println("Same blocks but different spaces: " + new Board(pieces).equals(brd));
      System.out.println("No blocks: " + new Board(new HashSet<Block>()).equals(blank));
      
      System.out.println("\nhashCode testing:");
      System.out.println(brd.hashCode() + " : " + brd);
      System.out.println(blank.hashCode() + " : " + blank);
      
      System.out.println("\ndeepClone testing: " );
      Board brdClone = brd.deepClone();
      System.out.println("Clone is equals(): " + brd.equals(brdClone));
      System.out.println("new setPath() for clone");
      brdClone.setPath(brd1, Block.getInstance(0,0,0,0), null);
      System.out.println("\nOriginal:");
      System.out.println("Parent Board: " + brd.getParent());
      System.out.println("Moved Block: " + brd.getMovedBlock());
      System.out.println("Moved direction: " + brd.getMovedDirection());
      System.out.println("\nClone::");
      System.out.println("Parent Board: " + brdClone.getParent());
      System.out.println("Moved Block: " + brdClone.getMovedBlock());
      System.out.println("Moved direction: " + brdClone.getMovedDirection());
      
      System.out.println("\ncheckSolved() testing:");
      System.out.println("If the goal is the self: " + brd.checkSolved(brd));
      System.out.println("If the goal is blank: " + brd.checkSolved(blank));
      System.out.println("Not there yet: " + brd.checkSolved(brd1));
      
      System.out.println("\nisOK() and display() testing: ");
      brd.display();
      try{brd.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      System.out.println();
      
      brd1.display();
      try{brd1.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      System.out.println();
      
      blank.display();
      try{blank.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      System.out.println();
      
      Board noSpaces = new Board(new HashSet<Block>());
      noSpaces.display();
      try{noSpaces.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      System.out.println();
      
      Board bigBlock = new Board(new HashSet<Block>(), allPairs);
      bigBlock.getBlocks().add(Block.getInstance(10,10,0,0));
      try{bigBlock.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      
      HashSet<Block> overlapBlocks = new HashSet<Block>();
      overlapBlocks.add(Block.getInstance(3,3,0,0));
      overlapBlocks.add(Block.getInstance(1,1,0,0));
      Board overlap = new Board(overlapBlocks, new HashSet<Pair>());
      try{overlap.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      
      Board wrongEmpty = brd.deepClone();
      wrongEmpty.getSpaces().remove(Pair.getInstance(1,0));
      try{wrongEmpty.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
      
      wrongEmpty.getSpaces().add(Pair.getInstance(0,2));
      try{wrongEmpty.isOK();}
      catch(IllegalStateException i){System.out.println(i.getMessage());}
   }
}
