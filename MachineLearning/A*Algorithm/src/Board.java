//import java.lang.Math;
//import java.util.Arrays;

public class Board
{
    private char refBlockRow;
    private char refBlockCol;
    
    final private char [][]boardBlocks;
    final private char rowSize;
    final private char colSize;
	//private Board parent;
	private char hammingPriority;
	private char manhattanPriority;

    public Board(int[][] blocks) {
        // construct a board from an N-by-N array of blocks
        // (where blocks[i][j] = block in row i, column j)
        char tempBlock;
        char refValueRow, refValueCol;
        rowSize = (char)blocks.length;
        colSize = (char)blocks[0].length;

        if (rowSize == 0 
            || colSize == 0) {
            throw new IllegalArgumentException("Expected Non-zero row column");
        }
        hammingPriority = 0;
        manhattanPriority = 0;
        boardBlocks = new char[rowSize][colSize];
        for (char i = 0; i < rowSize; i++) {
            for (char j = 0; j < colSize; j++) {
                tempBlock = (char)blocks[i][j];    
                boardBlocks[i][j] = (char)blocks[i][j];
                if (blocks[i][j] == 0) {
                    refBlockRow = i;
                    refBlockCol = j;
                }

                /* Compute the hamming priority */
                if (tempBlock != 0) {
                    if (tempBlock != ((i * rowSize) + (j + 1))) {
                        hammingPriority++;
                    }
                /*} else {
                    if ((i != rowSize - 1)
                        && (j != colSize - 1)) {
                            hammingPriority++;
                        }*/
                /* Compute the Manhattan priority */
                    refValueRow = (char) ((Math.ceil((double) tempBlock
                                        / (double) rowSize)) - (char) 1);
                    refValueRow = refValueRow < 0 ? 0 : refValueRow;
                    refValueCol = (char)(tempBlock - (char) 1 - (char) (refValueRow * rowSize));
                    refValueCol = (refValueCol < 0) ? 0 : refValueCol;
                    manhattanPriority += (Math.abs(refValueRow - i)
                                         + Math.abs(refValueCol - j));
                }
            }
        }
		//parent = null;
    }

    public int dimension() {
        // board dimension N
        return rowSize;
    }

    public int hamming() {
        // number of blocks out of place
        return hammingPriority;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        return manhattanPriority;
    }

    public boolean isGoal() {
        // is this board the goal board?
        if (manhattan() == 0) {
        //if (hamming() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Board twin() {
        // a board obtained by exchanging two adjacent blocks in the same row
        int [][]twinBlocks = new int[rowSize][colSize];
        for (char i = 0; i < rowSize; i++) {
            for (char j = 0; j < colSize; j++) {
                twinBlocks[i][j] = (int)boardBlocks[i][j];
            }
        }
        for (char i = 0; i < rowSize; i++) {
            if (i != refBlockRow) {
                twinBlocks[i][0] = (char)boardBlocks[i][1];
                twinBlocks[i][1] = (char)boardBlocks[i][0];
                break;
            }
        }

        Board twinBoard = new Board(twinBlocks);
        return twinBoard;
    }

    public boolean equals(Object y) {
        if ( !(y instanceof Board) ) return false;
        Board comparedObject = (Board) y;

        if (this.rowSize != comparedObject.rowSize
            || this.colSize != comparedObject.colSize){
            //throw new IllegalArgumentException("Arguments with incorrect dimensions");
            return false;
        }

        //return deepEquals(this.boardBlocks, comparedObject.boardBlocks);
        boolean isEquals = true;
        for (char i = 0; i < rowSize; i++) {
            for (char j = 0; j < colSize; j++) {
                if (comparedObject.boardBlocks[i][j]
                           != this.boardBlocks[i][j]) {
                    isEquals = false;
                }
            }
        }
        return isEquals;
    }
    
    public Iterable<Board> neighbors() {
        // all neighboring boards
        ResizingArrayStack<Board> neighborStack =
                                new ResizingArrayStack<Board>();
        int [][]neighborBlocks = new int[rowSize][colSize];
        /* Create Neighbors and store it in the STACK */
        if (refBlockRow > 0) {
            //Up neighbor exists
            //System.out.println("Up neighbor exist");
            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i) &&
                            (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i - 1][j];
                        neighborBlocks[i - 1][j] = boardBlocks[i][j];
                    } else if (!(i == refBlockRow &&
                                 j == refBlockCol)) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                }
            }
            Board upBoard = new Board(neighborBlocks);
			//upBoard.parent = this;
			//System.out.println(upBoard.toString());
            neighborStack.push(upBoard);
        }

        //if ((initBST.get(0) + size) < size * size) {
        if (refBlockRow < (rowSize - 1)) {
            //bottom neighbor exist
            //System.out.println("Bottom neighbor exist");
            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i) &&
                            (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i + 1][j];
                        neighborBlocks[i + 1][j] = boardBlocks[i][j];
                    } else if (!((i == refBlockRow
                              || i == (refBlockRow + 1))
                              && j == refBlockCol)) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                    //System.out.println(neighborBlocks[i][j]);
                }
            }
            Board bottomBoard = new Board(neighborBlocks);
			//bottomBoard.parent = this;
			//System.out.println(bottomBoard.toString());
            neighborStack.push(bottomBoard);
        }

        //if (initBST.get(0) % size == 0) {
        if (refBlockCol == 0) {
            //Only right neighbor exist
            //System.out.println("Right neighbor exist");
            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i)
                        && (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i][j + 1];
                        neighborBlocks[i][j + 1] = boardBlocks[i][j];
                    } else if (!(i == refBlockRow
                              && (j == refBlockCol
                              || j == (refBlockCol + 1)))) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                }
            }
            Board rightBoard = new Board(neighborBlocks);
			//rightBoard.parent = this;
			//System.out.println(rightBoard.toString());
            neighborStack.push(rightBoard);
        //} else if (initBST.get(0) % (size - 1) == 0) {
        } else if (refBlockCol == (colSize - 1)) {
            //Only left neighbor exist
            //System.out.println("Left neighbor exist");
            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i)
                        && (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i][j - 1];
                        neighborBlocks[i][j - 1] = boardBlocks[i][j];
                    } else if (!(i == refBlockRow
                              && j == refBlockCol
                              || j == (refBlockCol - 1))) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                }
            }
            Board leftBoard = new Board(neighborBlocks);
			//leftBoard.parent = this;
			//System.out.println(leftBoard.toString());
            neighborStack.push(leftBoard);
        } else {
            //Both right and left neighbor exist
            //System.out.println("Both right and left neighbor exist");
            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i)
                        && (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i][j + 1];
                        neighborBlocks[i][j + 1] = boardBlocks[i][j];
                    } else if (!(i == refBlockRow
                              && j == refBlockCol
                              || j == (refBlockCol + 1))) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                }
            }
            Board rightBoard = new Board(neighborBlocks);
			//System.out.println(rightBoard.toString());
			//rightBoard.parent = this;
            neighborStack.push(rightBoard);

            for (char i = 0; i < rowSize; i++) {
                for (char j = 0; j < colSize; j++) {
                    if ((refBlockRow == i)
                        && (refBlockCol == j)) {
                        neighborBlocks[i][j] = boardBlocks[i][j - 1];
                        neighborBlocks[i][j - 1] = boardBlocks[i][j];
                    } else if (!(i == refBlockRow
                              && j == refBlockCol
                              || j == (refBlockCol - 1))) {
                        neighborBlocks[i][j] = boardBlocks[i][j];
                    }
                }
            }
            Board leftBoard = new Board(neighborBlocks);
			//leftBoard.parent = this;
			//System.out.println(leftBoard.toString());
            neighborStack.push(leftBoard);
        }
        
        //ResizingArrayStack<Board> returnStack = neighborStack;
        //neighborStack = null;
        //neighborStack = new ResizingArrayStack<Board>();
        //return returnStack;
        return neighborStack;
    }

    public String toString() {
        // string representation of the board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                //System.out.println(boardBlocks[i][j]);
                s.append(String.format("%2d ", (int)boardBlocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial.toString());
        System.out.println(initial.hamming());
        System.out.println(initial.manhattan());
        //System.out.println(initial.isGoal());
        for (Board neighborBoard : initial.neighbors()) {
            if (neighborBoard != null) {
                //System.out.println(initial.equals(neighborBoard));
                System.out.println(neighborBoard.toString());
                System.out.println(neighborBoard.hamming());
                System.out.println(neighborBoard.manhattan());
            } else {
                break;
            }
        }

        // solve the puzzle
        /*Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        } */
    }
}
