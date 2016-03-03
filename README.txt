folder: 47BBlocks

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
         various accessor methods: initial (Board), goal (Board), empty spaces
            generated for (init), height and width of the (Board)s
         
    - public Board solve()
         contains the algorithm used to solve the given puzzle and returns
            the final board if solved
         
    - public void printPath(Board)
         prints the path from the given board to the initial configuration using
            through the references to the parent (Board) in each (Board)
         prints various debugging statements depending on the mode
         
    - public static void printOptions()
         prints the available debugging modes and their keys
         called only by "-ooptions"
         
    - public void setDebugMode(String)
         sets the debugging mode depending on the given string
     
          
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
         initializes timer variables for relevant methods, these timers total the
            amount of time each method runs for during puzzle solving
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
         sets the parent and the move needed to unmake (this)
         
    - public HashSet<Block> getBlocks()
    - public HashSet<Pair> getSpaces()
    - public Board getParent()
    - public Block getMovedBlock()
    - public Pair getMovedDirection()
    - public int getCost()
         various accessor methods
         the blocks and their information in (this)
         the empty spaces in (this) represented by (Pair)s
         the 3 variables storing path information
         a number representing the distance from (this) to the goal, for heuristics 
         
    - public Board makeMove(Block,Pair) 
         returns a new (Board) with the given (Block) moved in the given (Pair)
            direction, then uses setPath() to store path and provide a check against
            unmaking the move
            
    - public boolean equals(Object)
         true if both (allBlocks) contain equal (Block)s, empty spaces ignored
         
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
            in Solver    
    - public static boolean getBLOCK1()
         accessor method to needed debug controller for use in Solver
         
    - public static void setCount(int)
         initializes (count) which is used to count the number of this type
            of object created       
    - public static int getCount()
         accessor method for (count)
    - public static void printCount()
         prints debugging information regarding (count)
         
    - public static void setInstanceCount(int)
         initialize (instanceCount) which counts the number of references to 
            (Block)s created
    - public static int getInstanceCount()
         accessor method for (instanceCount)
    - public static void printInstanceCount()
         prints debugging information regarding (instanceCount)
         
    - public static void resetTimes()
         initializes timer variables for relevant methods, these timers total the
            amount of time each method runs for during puzzle solving
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
         returns an int that is essentially height, width, yPos, xPos concatenated
            together
         
    - public LinkedList<Pair> shouldBeEmpty(Pair)
         returns the empty spaces required to move in the given (Pair) direction
         returns null if cannot move in the given direction
         
    - public Block move(Pair,LinkedList<Pair>,HashSet<Pair>)
         returns a (Block) with updated (pos) and updates the given HashSet, which
            should be the HashSet of empty spaces, to reflect the move
         
    - public String toString()
         returns a string representation of (this)
         
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
------------------------------------------------------------------------------
   (Pair) is used as an abstraction of vector algebra and the cartesian coordinate system.
   
   (Block) is used to store the height, width, and position of the upper left corner
of a block, along with methods to serve as another abstraction barrier
   
   (Board) stores a HashSet<Block> of the blocks in the current configuration and 
most should store a HashSet<Pair> for the coordinates of the empty spaces. The use
of HashSet is for the constant runtime of contains(). This allows for relatively easy
comparison between boards and move generation.
   
   The objects used here are largely static due to the singleton implementation of 
(Block) and (Pair) to prevent the construction of redundant objects. Also, (Board)s
cannot be mutated since they are stored in (past) to check against past configurations.
   
   The move generation method is to attempt to move every (Block) in every direction
(represented by (Pair)s) in a nested for loop and, if the block can move, it generates 
a list of empty spaces that will be replaced by new coordinates.  Then a new Board 
is generated for each successful move, which is compared to past configurations.
It's discarded it if it has been encountered before, and stored for further processing otherwise. 
Once all the moves for the current (Board) have been exhausted, the next (Board) is
processed for new (Board)s, this continues until a solution is found or all reachable
(Board) configurations have been exhausted.

   The use of different data structures to store the (board)s to be processed results
in different search algorithms. I originally planned to use a PriorityQueue, ordering
the (Board)s by their costs. Their costs would be calculated by summing the manhattan
distances from each goal block to the nearest block of the same size in current board,
disregarding duplicates though. 
   Strangely enough, it seems that an error I made in cost generation actually increases
the speed of the algorithm in non-extreme cases. If the cost of each (Board) is always
0, the algorithm essentially becomes a brute force depth-first search algorithm, yet
it is significantly faster than the A* search algorithm I implemented except for such
puzzles as the big.tray.3 with 2 blocks and 10000 spaces.