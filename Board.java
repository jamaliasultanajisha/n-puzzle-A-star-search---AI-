import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class Board {
    private static final int SPACE = 0;
    private int[][] blocks;
    private int[] puzzle;

    private Puzzle p;

    public Board(int[][] blocks) {
        this.blocks = copy(blocks);
        this.puzzle = mode(blocks);
    }

    public int[][] copy(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++)
                copy[row][col] = blocks[row][col];

        return copy;
    }

    public int cost(int [] puzzle){
        int cost = 0;
        for(int row = 0; row < puzzle.length; row++){
            if(puzzle[row] != 0 ){
                cost += ((row+1) - puzzle[row]);
            }
        }
        return cost;
    }

    public int dimension() {
        //System.out.println("size of the puzzle : "+blocks.length);
        return blocks.length;
    }

    public int[] mode(int[][] blocks) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                list.add(blocks[i][j]);
            }
        }

        puzzle = new int[list.size()];
        for (int i = 0; i < puzzle.length; i++) {
            puzzle[i] = list.get(i);
        }
        return puzzle;
    }

    public boolean isSolvable()
    {
       /* for(int k=0;k<puzzle.length;k++){
            System.out.print(puzzle[k] + " ");
        }*/
        int parity = 0;
        int gridWidth = (int) Math.sqrt(puzzle.length);
        int row = 0; // the current row we are on
        int blankRow = 0; // the row with the blank tile

        for (int i = 0; i < puzzle.length; i++)
        {
            if (i % gridWidth == 0) { // advance to next row
                row++;
            }
            if (puzzle[i] == 0) { // the blank tile
                blankRow = row; // save the row on which encountered
                //System.out.println("row number : "+blankRow);
                continue;
            }
            for (int j = i + 1; j < puzzle.length; j++)
            {
                if (puzzle[i] > puzzle[j] && puzzle[j] != 0)
                {
                    parity++;
                }
            }
        }
        //System.out.println(parity);
        if (gridWidth % 2 == 0) { // even grid
            if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                return parity % 2 == 0;
            } else { // blank on even row; counting from bottom
                return parity % 2 != 0;
            }
        } else { // odd grid
            return parity % 2 == 0;
        }
    }

    public int hamming() {
        int count = 0;
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++)
                if (blockIsNotInPlace(row, col)){
                    count++;
                }

        return count;
    }

    private boolean blockIsNotInPlace(int row, int col) {
        int block = block(row, col);

        return !isSpace(block) && block != goalFor(row, col);
    }

    private int goalFor(int row, int col) {
        return row*dimension() + col + 1;
    }

    private boolean isSpace(int block) {
        return block == SPACE;
    }

    public int manhattan() {
        int sum = 0;
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++) {
                sum += calculateDistances(row, col);
            }

        return sum;
    }

    private int calculateDistances(int row, int col) {
        int block = block(row, col);

        return (isSpace(block)) ? 0 : abs(row - row(block)) + abs(col - col(block));
    }

    public int linearconflict(){
        p = new Puzzle(puzzle);
        return manhattan()+(2 * p.linearConflict(blocks.length));
    }

    public int horizontalconflict(int[][] blocks){
        int linearconflict = 0;
        for(int i=0;i<blocks.length;i++){
            for(int j=0;j<blocks.length;j++){
                if(blocks[i][j] == 0){
                    if(blocks[i][j-1] > blocks[i][j+1] ){
                        linearconflict++;
                    }
                    if(blocks[i-1][j] > blocks[i+1][j]){
                        linearconflict++;
                    }
                }
            }
        }
        return manhattan()+(2*linearconflict);
    }

    private int block(int row, int col) {
        return blocks[row][col];
    }

    private int row (int block) {
        return (block - 1) / dimension();
    }

    private int col (int block) {
        return (block - 1) % dimension();
    }

    public boolean isGoal() {
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++)
                if (blockIsInPlace(row, col)) return false;

        return true;
    }

    private boolean blockIsInPlace(int row, int col) {
        int block = block(row, col);

        return !isSpace(block) && block != goalFor(row, col);
    }

    public Board twin() {
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length - 1; col++)
                if (!isSpace(block(row, col)) && !isSpace(block(row, col + 1))) {
                    return new Board(swap(row, col, row, col + 1));
                }
        throw new RuntimeException();
    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copy = copy(blocks);
        int tmp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = tmp;

        return copy;
    }

    public boolean equals(Object y) {
        if (y==this) {
            return true;
        }
        if (y==null || !(y instanceof Board) || ((Board)y).blocks.length != blocks.length) {
            return false;
        }
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++) {
                if (((Board) y).blocks[row][col] != block(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();

        int[] location = spaceLocation();
        int spaceRow = location[0];
        int spaceCol = location[1];

        if (spaceRow > 0) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow - 1, spaceCol)));
        }
        if (spaceRow < dimension() - 1) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow + 1, spaceCol)));
        }
        if (spaceCol > 0) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol - 1)));
        }
        if (spaceCol < dimension() - 1) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol + 1)));
        }

        return neighbors;
    }

    /*public Iterable<Board> directions() {
        LinkedList<Board> directions = new LinkedList<Board>();

        int[] location = spaceLocation();
        int spaceRow = location[0];
        int spaceCol = location[1];

        if (spaceRow > 0) {
            directions.add(1,new Board(swap(spaceRow, spaceCol, spaceRow - 1, spaceCol)));
        }
        if (spaceRow < dimension() - 1) {
            directions.add(2,new Board(swap(spaceRow, spaceCol, spaceRow + 1, spaceCol)));
        }
        if (spaceCol > 0) {
            directions.add(3,new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol - 1)));
        }
        if (spaceCol < dimension() - 1) {
            directions.add(4,new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol + 1)));
        }

        return directions;
    }*/

    public int[] spaceLocation() {
        for (int row = 0; row < blocks.length; row++)
            for (int col = 0; col < blocks.length; col++)
                if (isSpace(block(row, col))) {
                    int[] location = new int[2];
                    location[0] = row;
                    location[1] = col;
                    return location;
                }
        throw new RuntimeException();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(dimension() + "\n");
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++)
                str.append(String.format("%2d ", block(row, col)));
            str.append("\n");
        }

        return str.toString();
    }
}