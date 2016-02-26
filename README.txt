project folder: project06_kevinl

src/Solver.java
    - Includes main, solves a given puzzle, multiple debugging modes
    
    - public static void setPrintBase(boolean)
         determines if the base output is printed or not, default is true
         
    - public static BufferedReader reader (String) throws Exception
         for parsing the initial board and goal board
         
    - public Solver(HashSet<Block>,HashSet<Block>,int,int,String)
         constructor
         
    - public Board getInit()
    - public Board getGoal()
    - public HashSet<Pair> getEmpty() 
    - public int getH()
    - public int getW()
         various accessor methods
         
    - public Board solve()
         contains the algorithm used to solve the given puzzle and returns
            the final board if solved
         
    - public void printPath(Board)
         prints the path from the given board to the initial configuration
         prints various debugging statements depending on the mode
         
    - public static void printOptions()
         prints the available debugging modes and their keys
         called only by "-ooptions"
         
    - public void setDebugMode(String)
         sets the debugging mode depending on the given string
         
      the class below is currently unused
      implemented in preparation for A* search
    - private class BWrapper implements Comparable<BWrapper>
          - public BWrapper(Board)
          - private int generateCost(Board)
          - private int findMinCost(LinkedList<Block>,Block)
          - public Board getBoard()
          - public int getCost()
          - public int compareTo(BWrapper)
          
src/Board.java
    - Includes main, entirely for method testing
    
    - public static void setDebug(boolean,boolean,boolean)
         sets the booleans used to control debugging information to be used
            in Solver
            
    - public static boolean getBOARD1()
    - public static boolean getBOARD2()
         accessor methods to needed debug controllers for debug statements
            in Solver
         
    - public static void setCount(int)
         initializes (count) which is used to count the number of this type
            of object created       
    - public static int getCount()
         accessor method for count
    - public static void printCount()
         prints debugging information regarding (count)
         
    - public static void resetTimes()
         initializes various timer variables for relevant methods
    - public static long getMakeMove()
    - public static long getDeepClone()
    - public static long getCheckSolved()
    - public static long getEquals()
         accessor methods for relevant timers
    - public static void printTimes()
         prints debugging information regarding the timers
         
    - public Board(HashSet<Block>)
         constructor without empty spaces, used only for (goal)
    - public Board(HashSet<Block>,HashSet<Pair>)
         constructor
         
    - public void setPath(Board,Block,Pair)
         sets the parent and the move made to make (this)
         
    - public HashSet<Block> getBlocks()
    - public HashSet<Pair> getSpaces()
    - public Board getParent()
    - public Block getMovedBlock()
    - public Pair getMovedDirection()
         various accessor methods
         
    - public Board makeMove(Block,Pair) 
         returns a new (Board) with the given (Block) moved in the given (Pair)
            direction, setPath() isn't used until verified it's a new configuration
            
    - public boolean equals(Object)
         true if (allBlocks) contain equal (Block)s, empty spaces ignored
         
    - public int hashCode()
         returns product of hashCodes of the (Block)s in (allBlocks)
         
    - public String toString()
         returns string representation of (allBlocks)
         
    - public Board deepClone()
         returns a new (Board) with references to same (Block)s and (Pair)s
            but not (allBlocks) and (spaces)
            
    - public boolean checkSolved(Board)
         checks if each of the (Block)s of the given (Board) are contained in
            (this), the given (Board) should be (goal)
    - public void isOK() throws IllegalStateException
         checks if (this) has a valid configuration: no (Block)s overlapping
            or out of bounds, no empty spaces inside (Block)s, etc.
            
    - public void display
         prints a visual representation of (this) 
         the number assigned to a (Block) is not constant across (Board)s
   
src/Block.java
    - Includes main, entirely method testing
    
    - public static void setDebug(boolean,boolean,boolean)
         sets the booleans used to control debugging information to be used
            in Solver    - public static boolean getBLOCK1()
    
    - public static void setCount(int)
         initializes (count) which is used to count the number of this type
            of object created       
    - public static int getCount()
         accessor method for (count)
    - public static void printCount()
         prints debugging information regarding (count)
         
    - public static void resetTimes()
         initializes timer variables for relevant methods
    - public static long getShouldBeEmpty()
    - public static long getCanMove()
    - public static long getMove()
         accessor methods for relevant timers
    - public static void printTimes()
         prints debugging information regarding the timers
         
    - public Block(int,int,int,int)
         constructor, constructs a (Pair) from the latter two int
    - public Block(int,int,Pair)
         overloaded constructor
         
    - public int getHeight()
    - public int getWidth()
    - public Pair getPos()
    - public int getXPos()
    - public int getYPos()
         various accessor methods
         
    - public boolean equals(Object)
         returns true if the two (Block)s have equivalent data
         
    - public int hashCode()
         returns hashCode of (this)'s string representation
         
    - public Pair[] shouldBeEmpty(Pair)
         returns the empty spaces required to move in the given (Pair) direction
         
    - public boolean canMove(Pair[],HashSet<Pair>)
         returns if (this) can move, checks if the empty spaces specified from
            shouldBeEmpty() are within the given HashSet, which should be the
            (spaces) of the (Board) (this) is in
            
    - public void move(Pair,Pair[],HashSet<Pair>)
         updates (pos) and the given HashSet to reflect movement of (this)
         
    - public String toString()
         returns a string representation of (this)
         
    - public Block deepClone()
         returns a new (Block) with the same data as (this)
         
src/Pair.java
    - Includes main method, entirely method testing

    - public static void setDebug(boolean,boolean,boolean)
         sets the booleans used to control debugging information to be used
            in Solver
            
      (count) should be 0 due to the singleton implementation
    - public static void setCount(int)
         initializes (count) which is used to count the number of this type
            of object created       
    - public static int getCount()
         accessor method for (count)
    - public static void printCount()
         prints debugging information regarding (count)
         
    - public static void setInstanceCount(int)
         initialize (instanceCount) which counts the number of references to 
            (Pair)s created
    - public static int getInstanceCount()
         accessor method for (instanceCount)
    - public static void printInstanceCount()
         prints debugging information regarding (instanceCount)
         
    - public static int[][] getFreq()
         accessor method for (freq), which counts the number of references to
            each unique (Pair) created
    - public static void printFreq()
         prints debugging information regarding (freq)
    - public static void resetFreq(int,int)
         initializes or resets the contents of (freq) to 0
    
    - public static Pair getInstance(int,int)
         returns a reference to the (Pair) corresponding to the given arguments
         return null if arguments invalid or table not initialized
    - public static void generateTable(int,int)
         initializes a table of (Pair)s with the given dimensions
         
    - private Pair(int,int)
         constructor, shouldn't be used aside from created the direction (Pair)s
            and (table)
            
    - public int getX()
    - public int getY()
    - public static int getW()
    - public static int getH()
         various accessor methods
         
    - public Pair add(Pair)
    - public Pair sub(Pair)
    - public Pair mult(int)
         various operations, based on vector algebra, return null if the result
            is outside of (table)
            
    - public int hashCode()
         hashCode of xy
    - public String toString()
         returns a string: x y
    - public static int manhattanDis(Pair,Pair)
         returns the manhattan distance between two (Pair)s
         
resources/*
    - Puzzles of various difficulties to be passed into Solver.java
    - test      : example on the Blocks project website
    - bigsearch2: puzzle from puzzle/easy
    - c33       : puzzle from puzzle/medium
    - c46       : puzzle from puzzle/hard
    
RUN.txt
    - console output of (java Solver -onumbers ../resources/c46.txt ../resources/c46goal.txt)
    - console output of (java Solver -otime ../resources/c46.txt ../resources/c46goal.txt)

README.txt
    - description of classes and files
    
---------------------Comments------------------------------
Note: Exception catching is expensive in terms of runtime.
      When adding to a new collection, cloning and adding new objects is moderately
         more expensive than adding references iteratively, which is slightly more
         expensive than using addAll().
   
Basis for conclusions below from the data in RUN.txt

   There are as many new (Block)s made as (Board)s, many of the (Block)s are duplicates
so implementing singleton (Block) would save memory. I plan to store unique (Blocks) in 
a HashSet and retrieve if needed, if it does not exist, create the needed (Block) and add it

   Currently, the algorithm for solving uses brute force, implementing a basic heuristic
would likely reduce the number of (Board)s to be searched. I plan to use a priorityQueue 
for processing in Solver.solve(), ordering the (Board)s using the Manhattan Distance
between their (Block)s and the (goal) (Blocks)

   Currently, for every move made, the algorithm will recognize unmaking it as a valid
move when processing the child (Board), wasting time and processing. I plan to modify 
(Board)'s setPath() so it can be used to check if the contemplated move is the parent (Board)

   Of the methods timed, Block.shouldBeEmpty takes the most time by far. It generates
an array of (Pair)s every single time it is called, and it is called more times than
the other methods as well. Perhaps the array is too expensive a data structure. I'm
considering combining Block.shouldBeEmpty() and Block.canMove()
   
------------------------------------------------------------------------------
After stage 3

   
