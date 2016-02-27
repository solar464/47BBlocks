import java.util.*;
//representation of a block using it dimensions and position on a (Board)
public class Block
{
   //x, y refer to coordinates of top left corner
   private int hght, wdth;
   private Pair pos;
   private static HashMap<Integer, Block> allPieces;
   private static int hashFactor;
   public static int getHashFactor(){ return hashFactor; }
   
   //debug controllers
   private static boolean BLOCK1 = false, BLOCK2 = false, BLOCK3 = false;
   public static boolean getBLOCK1(){ return BLOCK1; }
   public static void setDebug(boolean one, boolean two, boolean three){
      BLOCK1 = one; BLOCK2 = two; BLOCK3 = three;
      if(BLOCK1){
         setCount(0);
         setInstanceCount(0);
      }
      if(BLOCK3)
         resetTimes();
   }
   
   //counts the number of (Block) objects created
   private static int count;
   public static void setCount(int num){ count = num; }
   public static int getCount(){ return count; }
   public static void printCount(){
      if(BLOCK1)
         System.out.println("Number of (Block) objects created: " + count); 
   }
   
   //track total runtime of various methods
   private static long startTime;
   private static long shouldBeEmptyTime, moveTime;
   public static void resetTimes(){ shouldBeEmptyTime = 0; moveTime = 0; }
   public static long getShouldBeEmpty(){ return shouldBeEmptyTime; }
   public static long getMove(){ return moveTime; }
   public static void printTimes(){
      if(BLOCK3){
         System.out.println("\nTotal runtimes of relevant methods in (Block): ");
         System.out.println("shouldBeEmpty(): " + shouldBeEmptyTime/1000000 +
               "\nmove(): " + moveTime/1000000);
      }   
   }
   
   private static int instanceCount;
   public static void setInstanceCount(int i){ instanceCount = i; }
   public static int getInstanceCount(){ return instanceCount; }
   public static void printInstanceCount(){
      System.out.println("Number of (Block) references created: " + instanceCount);
   }
   
   //initialize storage of (Block)s
   public static void initializePieces(){
      if(allPieces == null)
         allPieces = new HashMap<Integer, Block>(1000);
      int dimension = Math.max(Pair.getH(), Pair.getW());
      for(int i = 1;; i++){
         dimension = dimension / 10;
         if(dimension < 10){
            hashFactor = (int) Math.pow(10,i);
            break;
         }
      }
      
   }
   
   public static HashMap<Integer,Block> getPieces(){ return allPieces; }
   
   //retrieve (Block) with the desired information, create it if it doesn't exist
   public static Block getInstance(int height, int width, int xPos, int yPos){
      instanceCount++;
      int hash = (int) (height*(Math.pow(hashFactor,3)) + width*(Math.pow(hashFactor,2)) + yPos*hashFactor + xPos);
      Block toReturn = allPieces.get(hash);
      if(toReturn == null){
         toReturn = new Block(height, width, xPos, yPos);
         allPieces.put(hash, toReturn);
      }
      //System.out.println(height+ ""+width+yPos+xPos + ":" + toReturn);
      return toReturn;
   }
   public static Block getInstance(int height, int width, Pair pos){
      return getInstance(height, width, pos.getX(), pos.getY());
   }
   
   //constructor
   private Block(int height, int width, int xPos, int yPos){
      hght = height;
      wdth = width;
      pos = Pair.getInstance(xPos, yPos);
      count++;
   }
   
   private Block(int height, int width, Pair coor){
      hght = height;
      wdth = width;
      pos = coor;
      count++;
   }
   //various accessor methods
   public int getHeight(){ return hght; }
   public int getWidth() { return wdth; }
   public Pair getPos()  { return pos; }
   public int getXPos()  { return pos.getX(); }
   public int getYPos()  { return pos.getY(); }
   
   /*
   //two (Block)s only equal if all their instance data is equal
   public boolean equals(Object other){
      if(!(other instanceof Block))
         return false;
      return ((Block)other).pos == pos && ((Block)other).hght == hght && ((Block)other).wdth == wdth;
   }
   */
   
   //for use in storing in (Board)s
   public int hashCode(){
      return (hght + "" + wdth + pos.toString()).hashCode();
   }
   
   //calculates the empty spaces required for (this) to move in the given (direction)
   //returns null if cannot move in the given direction
   public LinkedList<Pair> shouldBeEmpty(Pair direction, HashSet<Pair> empty){
      startTime = System.nanoTime();
      int numToCheck;
      Pair beginCheck, dirToCheck;
      if(direction == Pair.UP){
         numToCheck = wdth;
         beginCheck = pos.add(Pair.UP);
         dirToCheck = Pair.RIGHT;
      }
      else if(direction == Pair.DOWN){
         numToCheck = wdth;
         beginCheck = pos.add(Pair.DOWN.mult(hght));
         dirToCheck = Pair.RIGHT;
      }
      else if(direction == Pair.RIGHT){
         numToCheck = hght;
         beginCheck = pos.add(Pair.RIGHT.mult(wdth));
         dirToCheck = Pair.DOWN;
      }
      else if(direction == Pair.LEFT){
         numToCheck = hght;
         beginCheck = pos.add(Pair.LEFT);
         dirToCheck = Pair.DOWN;
      }
      else{
         if(BLOCK3)
            shouldBeEmptyTime += System.nanoTime() - startTime;
         return null;
      }
      
      //(beginCheck is null if the blocks is trying to move out of the board
      if(beginCheck == null || !empty.contains(beginCheck)){
         if(BLOCK3)
            shouldBeEmptyTime += System.nanoTime() - startTime;
         return null;
      }
      //generate list of needed empty spaces
      Pair maybeEmpty;
      LinkedList<Pair> toCheck = new LinkedList<Pair>();
      toCheck.add(beginCheck);
      for(int i = 1; i < numToCheck; i++){
         maybeEmpty = beginCheck.add(dirToCheck.mult(i));
         if(!empty.contains(maybeEmpty)){
            if(BLOCK3)
               shouldBeEmptyTime += System.nanoTime() - startTime;
            return null;
         }
         toCheck.add(maybeEmpty); 
      }
      
      
      if(BLOCK3)
         shouldBeEmptyTime += System.nanoTime() - startTime;
      return toCheck;
      
   }
   
   /*
   //No longer used, function absorbed by shouldBeEmpty()
   //checks if the spaces required for movement are actually empty
   //(should) should be the Pair[] generated from shouldBeEmpty()
   //(is) should be (Board.getSpaces()), the (Board)'s empty spaces
   public boolean canMove(Pair[] should, HashSet<Pair> is){
      startTime = System.nanoTime();
      if(should == null || should.length == 0){
         if(BLOCK3)
            canMoveTime += System.nanoTime() - startTime;
         return false;
      }
      for(Pair space: should)
         if(!is.contains(space)){
            if(BLOCK3)
               canMoveTime += System.nanoTime() - startTime;
            return false;
         }
      
      if(BLOCK3)
         canMoveTime += System.nanoTime() - startTime;
      return true;
   }
   */
   
   //returns a (Block) as if moved (this) in the given (direction) 
   //updates (spaces) according to (toFill), the spaces to be occupied, and the 
   //  spaces to be vacated
   public Block move(Pair direction, LinkedList<Pair> toFill, HashSet<Pair> spaces){
      if(BLOCK2){
         System.out.println(this + " attempting to move in direction " + direction);
         //System.out.println("Current empty spaces: " + spaces);
      }
      startTime = System.nanoTime();
      Pair oppSide;
      int numToOpp;
      if(toFill.size() == wdth)
         numToOpp = hght;
      else
         numToOpp = wdth;
      
      for(Pair p: toFill){
         spaces.remove(p);
         
         if(direction == Pair.UP || direction == Pair.LEFT)
            oppSide = p.add(direction.mult(-numToOpp));

         else //if(direction == Pair.DOWN || direction == Pair.RIGHT)
            oppSide = p.sub(direction.mult(numToOpp));
         
         spaces.add(oppSide);
      }      
      Block toReturn = getInstance(hght,wdth,pos.add(direction));
      
      if(BLOCK3)
         moveTime += System.nanoTime() - startTime;
      if(BLOCK2)
         //System.out.println("New (Block) data: " + this + "\tEmpty spaces: " + spaces);
         System.out.println("New (Block) data: " + toReturn + "\tEmpty spaces: ");
      
      return toReturn;
   }
   
   /*
   //no longer used
   //checks if a given coordinate lies within the area of (this)
   public boolean withinBlock(Pair coor){
      return coor.getX() >= getXPos() && coor.getX() < getXPos() + wdth &&
             coor.getY() >= getYPos() && coor.getY() < getYPos() + hght;
   }
   */
   
   public String toString(){
      return hght + " " + wdth + " " + pos;
   }
   
   /*
   //for use in move generation, (Block) movements in one (Board) should not
   //  affect the (Block)s of other (Board)s
   public Block deepClone(){
      return new Block(hght, wdth, pos);
   }
   */
   
   //various tests, testing initialization and movement
   public static void main(String[] args){
      Pair.generateTable(4, 5);
      initializePieces();
      Block b = getInstance(2,2,1,1);
      HashSet<Pair> space = new HashSet<Pair>();
      space.add(Pair.getInstance(0,1));
      space.add(Pair.getInstance(0,2));
      space.add(Pair.getInstance(1,0));
      space.add(Pair.getInstance(2,0));
      
      System.out.println("move() testing: ");
      LinkedList<Pair> into;
      for(Pair toMove: Pair.DIRS){
         into = b.shouldBeEmpty(toMove, space);
         System.out.println("\nCurrent empty spaces: " + space);
         System.out.println("Block's information: " + b);
         System.out.println("Direction to move: " + toMove);
         System.out.println("Required empty spaces: " + into);
      
         if(into != null){
            System.out.println("Able to move.");
            b = b.move(toMove, into, space);
         }
         else
            System.out.println("No movement possible");
      
      
         System.out.println("Block's new information: " + b);
         into = b.shouldBeEmpty(toMove, space);
         System.out.println("Required empty spaces to move again: " + into);
      }
      
      System.out.println("\nmove() testing with semi-valid input");
      Block b1 = new Block(2, 1, 0, 0);
      space = new HashSet<Pair>();
      Pair toMove = Pair.getInstance(2,0);
      try{
         into = b1.shouldBeEmpty(toMove, space);
         for(Pair p: into)
            space.add(p);
      }
      catch(Exception e){
         System.out.println("Unable to test invalid directions due to shouldBeEmpty()");
         System.out.println("...Proceeding to bypass the method and generate needed space by hand");
         space.add(Pair.getInstance(3, 4));
         space.add(Pair.getInstance(1, 3));
         into = new LinkedList<Pair>();
         into.add(Pair.getInstance(3, 4));
         into.add(Pair.getInstance(1, 3));
      }
      
      System.out.println("Current empty spaces: " + space);
      System.out.println("Block's information: " + b1);
      System.out.println("Direction to move: " + toMove);
      System.out.println("Required empty spaces: " + into);
   
      if(into != null){
         System.out.println("Able to move.");
         try{b1 = b1.move(toMove, into, space);}
         catch(Exception e){System.out.println("Despite valid (into) and (space), errors occurred due to invalid (toMove)");}
      }
      else
         System.out.println("No movement possible");
   
   
      System.out.println("Block's new information: " + b1);
      into = b1.shouldBeEmpty(toMove, space);
      System.out.println("Required empty spaces to move again: " + into);
      System.out.println("Current empty spaces: " + space);
      
      System.out.println("\nequals() testing");
      Block b2 = new Block(0,0,1,1);
      Block b3 = getInstance(0,0,1,1);
      Block b4 = new Block(0,1,1,1);
      Block b5 = b2;
      System.out.println(b2 + " and " + b3 + " : " + b2.equals(b3));
      System.out.println(b2 + " and " + b4 + " : " + b2.equals(b4));
      System.out.println(b2 + " and " + b5 + " : " + b2.equals(b5));
      System.out.println(b2 + " and " + 1 + " : " + b2.equals(1));
      
      System.out.println("\nHashCode() testing");
      System.out.println(b2 + " hashCode = " + b2.hashCode());
      System.out.println(b4 + " hashCode = " + b4.hashCode());

   }
}
