import java.util.Arrays;
import java.util.HashSet;

//representation of a block using it dimensions and position on a (Board)
public class Block
{
   //x, y refer to coordinates of top left corner
   private int hght, wdth;
   private Pair pos;
   
   //debug controllers
   private static boolean BLOCK1 = false, BLOCK2 = false, BLOCK3 = false;
   public static boolean getBLOCK1(){ return BLOCK1; }
   public static void setDebug(boolean one, boolean two, boolean three){
      BLOCK1 = one; BLOCK2 = two; BLOCK3 = three;
      if(BLOCK1)
         setCount(0);
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
   private static long shouldBeEmptyTime, canMoveTime, moveTime;
   public static void resetTimes(){ shouldBeEmptyTime = 0; canMoveTime = 0; moveTime = 0; }
   public static long getShouldBeEmpty(){ return shouldBeEmptyTime; }
   public static long getCanMove(){ return canMoveTime; }
   public static long getMove(){ return moveTime; }
   public static void printTimes(){
      if(BLOCK3){
         System.out.println("\nTotal runtimes of relevant methods in (Block): ");
         System.out.println("shouldBeEmpty(): " + shouldBeEmptyTime/1000000 + "\ncanMove(): "
            + canMoveTime/1000000 + "\nmove(): " + moveTime/1000000);
      }
      
   }
   
   //constructor
   public Block(int height, int width, int xPos, int yPos){
      hght = height;
      wdth = width;
      pos = Pair.getInstance(xPos, yPos);
      count++;
   }
   
   public Block(int height, int width, Pair coor){
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
   
   //two (Block)s only equal if all their instance data is equal
   public boolean equals(Object other){
      if(!(other instanceof Block))
         return false;
      return ((Block)other).pos == pos && ((Block)other).hght == hght && ((Block)other).wdth == wdth;
   }
   
   //for use in storing in (Board)s
   public int hashCode(){
      return (hght + "" + wdth + pos.toString()).hashCode();
   }
   
   //calculates the empty spaces required for (this) to move in the given (direction)
   public Pair[] shouldBeEmpty(Pair direction){
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
      if(beginCheck == null){
         if(BLOCK3)
            shouldBeEmptyTime += System.nanoTime() - startTime;
         return null;
      }
      
      Pair[] toCheck = new Pair[numToCheck];
      for(int i = 0; i < numToCheck; i++)
         toCheck[i] = beginCheck.add(dirToCheck.mult(i));
      
      if(BLOCK3)
         shouldBeEmptyTime += System.nanoTime() - startTime;
      return toCheck;
      
   }
   
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
   
   //moves (this) in the given (direction) 
   //updates (this.pos) according to (direction)
   //updates (spaces) according to (toFill), the spaces to be occupied, and the 
   //  spaces to be vacated
   public void move(Pair direction, Pair[] toFill, HashSet<Pair> spaces){
      if(BLOCK2){
         System.out.println(this + " attempting to move in direction " + direction);
         //System.out.println("Current empty spaces: " + spaces);
      }
      startTime = System.nanoTime();
      Pair oppSide;
      int numToOpp;
      if(toFill.length == wdth)
         numToOpp = hght;
      else
         numToOpp = wdth;
      
      pos = pos.add(direction);
      for(Pair p: toFill){
         spaces.remove(p);
         
         if(direction == Pair.UP || direction == Pair.LEFT)
            oppSide = p.add(direction.mult(-numToOpp));

         else //if(direction == Pair.DOWN || direction == Pair.RIGHT)
            oppSide = p.sub(direction.mult(numToOpp));
         
         spaces.add(oppSide);
      }      
      
      if(BLOCK3)
         moveTime += System.nanoTime() - startTime;
      if(BLOCK2)
         //System.out.println("New (Block) data: " + this + "\tEmpty spaces: " + spaces);
         System.out.println("New (Block) data: " + this + "\tEmpty spaces: ");
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
   
   //for use in move generation, (Block) movements in one (Board) should not
   //  affect the (Block)s of other (Board)s
   public Block deepClone(){
      return new Block(hght, wdth, pos);
   }
   
   //various tests, testing initialization and movement
   public static void main(String[] args){
      Pair.generateTable(4, 5);
      Block b = new Block(2,2,1,1);
      HashSet<Pair> space = new HashSet<Pair>();
      space.add(Pair.getInstance(0,1));
      space.add(Pair.getInstance(0,2));
      space.add(Pair.getInstance(1,0));
      space.add(Pair.getInstance(2,0));
      
      System.out.println("move() testing: ");
      Pair[] into;
      for(Pair toMove: Pair.DIRS){
         into = b.shouldBeEmpty(toMove);
         System.out.println("\nCurrent empty spaces: " + space);
         System.out.println("Block's information: " + b);
         System.out.println("Direction to move: " + toMove);
         System.out.println("Required empty spaces: " + Arrays.deepToString(into));
      
         if(b.canMove(into, space)){
            System.out.println("Able to move.");
            b.move(toMove, into, space);
         }
         else
            System.out.println("No movement possible");
      
      
         System.out.println("Block's new information: " + b);
         into = b.shouldBeEmpty(toMove);
         System.out.println("Required empty spaces to move again: " + Arrays.deepToString(into));
      }
      
      System.out.println("\nmove() testing with semi-valid input");
      Block b1 = new Block(2, 1, 0, 0);
      space = new HashSet<Pair>();
      Pair toMove = Pair.getInstance(2,0);
      try{
         into = b1.shouldBeEmpty(toMove);
         for(Pair p: into)
            space.add(p);
      }
      catch(Exception e){
         System.out.println("Unable to test invalid directions due to shouldBeEmpty()");
         System.out.println("...Proceeding to bypass the method and generate needed space by hand");
         space.add(Pair.getInstance(3, 4));
         space.add(Pair.getInstance(1, 3));
         into = new Pair[2];
         into[0] = Pair.getInstance(3, 4);
         into[1] = Pair.getInstance(1, 3);
      }
      
      System.out.println("Current empty spaces: " + space);
      System.out.println("Block's information: " + b1);
      System.out.println("Direction to move: " + toMove);
      System.out.println("Required empty spaces: " + Arrays.deepToString(into));
   
      if(b1.canMove(into, space)){
         System.out.println("Able to move.");
         try{b1.move(toMove, into, space);}
         catch(Exception e){System.out.println("Despite valid (into) and (space), errors occurred due to invalid (toMove)");}
      }
      else
         System.out.println("No movement possible");
   
   
      System.out.println("Block's new information: " + b1);
      into = b1.shouldBeEmpty(toMove);
      System.out.println("Required empty spaces to move again: " + Arrays.deepToString(into));
      System.out.println("Current empty spaces: " + space);
      
      System.out.println("\nequals() testing");
      Block b2 = new Block(0,0,1,1);
      Block b3 = new Block(0,0,Pair.getInstance(1, 1));
      Block b4 = new Block(0,1,1,1);
      Block b5 = b2;
      System.out.println(b2 + " and " + b3 + " : " + b2.equals(b3));
      System.out.println(b2 + " and " + b4 + " : " + b2.equals(b4));
      System.out.println(b2 + " and " + b5 + " : " + b2.equals(b5));
      System.out.println(b2 + " and " + 1 + " : " + b2.equals(1));
      System.out.println(b2 + " and clone : " + b2.equals(b2.deepClone()));
      System.out.println("Would have tested new Block(0,0,new Pair(1,1)) but constructor not visible.");
      
      System.out.println("\nHashCode() testing");
      System.out.println(b2 + " hashCode = " + b2.hashCode());
      System.out.println(b4 + " hashCode = " + b4.hashCode());

   }
}
