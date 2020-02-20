import com.sun.rowset.internal.Row;

import static java.util.Arrays.binarySearch;

public class Puzzle {
    public static enum Axis { ROW, COL };

    private int[] state;
    private int side;

    public Puzzle(int[] state) {
        this.state = state;
        this.side = (int)Math.sqrt(state.length);
        if (side * side != state.length) {
            throw new IllegalArgumentException("Puzzle must be square");
        }
    }

    /**
     * Returns the squares of the puzzle for a specified row or column.
     *
     * @param rc row or col number (0-based)
     */
    public int[] tuple(Axis dir, int rc) {
        int[] result = new int[this.side];
        switch (dir) {
            case ROW:
                System.arraycopy(this.state, rc * this.side, result, 0, this.side);
                break;
            case COL:
                for (int i = 0, j = rc; i < this.side; i++, j += this.side) {
                    result[i] = this.state[j];
                }
                //System.arraycopy(this.state,rc+this.side,result,0,this.side);
                break;
        }
        return result;
    }

    /**
     * Returns the squares of the puzzle of this size as if it were in
     * its solved state for a specified row or column.
     *
     * @param rc row or col number (0-based)
     */
    public int[] idealTuple(Axis dir, int rc) {
        int[] result = new int[this.side];
        switch (dir) {
            case ROW:
                for (int i = 0, j = rc * this.side + 1; i < this.side; i++, j++) {
                    result[i] = (j < this.state.length) ? j : 0;
                }
                break;
            case COL:
                for (int i = 0, j = this.side + rc + 1; i < this.side; i++, j += this.side) {
                    result[i] = (j < this.state.length) ? j : 0;
                    //System.out.println("ideal "+result[i]);
                }
                break;
        }
        return result;
    }

    /**
     * Count inversions (linear conflicts) for a row or column.
     */
    public int inversions(Axis dir, int rc) {
        int[] have = this.tuple(dir, rc);
        int[] want = this.idealTuple(dir, rc);
        int inversions = 0;

        // For each pair of squares, if both numbers are supposed to be in this
        // tuple, and neither is 0 (blank)...
        for (int i = 1, iPos; i < this.side; i++) {
            if (have[i] != 0 && 0 <= (iPos = binarySearch(want, have[i]))) {
                for (int j = 0, jPos; j < i; j++) {
                    if (have[j] != 0 && 0 <= (jPos = binarySearch(want, have[j]))) {
                        // ... and are inverted, count it as a conflict.
                        if ((have[i] < have[j]) != (i < j)) {
                            inversions++;
                           // System.out.println("ipos :"+iPos+" jpos: "+jPos);
                        }
                    }
                }
            }
        }
       // System.out.println("conflicts/inversionts : "+conflict);
       return inversions;
    }

    public int inversionsVer(Axis dir, int rc) {
        int[] have = this.tuple(dir, rc);
        int[] want = this.idealTuple(dir, rc);
        int inversions = 0;

     //   for(int i=0;i<this.side;i++){

     //   }
        // System.out.println("conflicts/inversionts : "+conflict);
        return inversions;
    }

    public int linearConflict(int boardsize){
        int collisionrow = 0;
        int collisioncol = 0;

       for(int i=0;i<boardsize;i++){
            collisionrow = collisionrow + Puzzle.this.inversions(Axis.ROW,i);
            collisioncol = collisioncol + Puzzle.this.inversions(Axis.COL,i);
          //  System.out.printf("Row %d inversions = %d\n", 0, p.inversions(Axis.ROW, i));
          //  System.out.printf("Col %d inversions = %d\n", 0, p.inversions(Axis.COL, i));
       }
       // System.out.printf("Row linear conflict = %d\n",collisionrow );
       // System.out.printf("Col linear conflict = %d\n", collisioncol);

        return collisioncol+collisionrow;
    }

//    public static void main(String[] args)
//    {
//        Puzzle p =new Puzzle(new int[]{7,8,4,6,0,5,3,2,1});
//        System.out.println(p.linearConflict(p.side));
//
//    }
}