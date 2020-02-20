public class Main
{
    public static void main(String[] args)
    {
        //Initialise new board
    /*    Board b = new Board(new int[][] {{1, 2, 3},
                {0, 4, 6},
                {7, 5, 8}});
*/
        Board b = new Board(new int[][] {{7, 2, 4},
                {6, 0, 5},
                {8, 3, 1}});
        System.out.println("Initial State");

        System.out.println();
      //  Puzzle p = new Puzzle(new int[]{7,3,4,6,0,5,8,2,1});
        //Solve the puzzle via Hamming Distance method
        Solver s = new Solver(b);
        SolverHamming sh = new SolverHamming(b);
        SolverLinearConflict slc = new SolverLinearConflict(b);
        System.out.println("hamming distance "+b.hamming());
        System.out.println("manhattan distance "+b.manhattan());
        System.out.println("linear conflict " +b.linearconflict());
        System.out.println("is the puzzle solvable : "+s.isSolvable());
        if(b.isSolvable() && s.isSolvable()){
            System.out.println("In manhattarn distance method");
            s.solution();
            System.out.println("In hamming distance method");
            sh.solution();
            System.out.println("In linear conflict method");
            slc.solution();
        }
       // System.out.println("Cost for manhatten : "+(b.manhattan()+s.cost));
        for(int i=0;i<b.dimension();i++){
            for(int j=0;j<b.dimension();j++){
                System.out.print((i+j)+" ");
            }
            System.out.println();
        }

    }
}