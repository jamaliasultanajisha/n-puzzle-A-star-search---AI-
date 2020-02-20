import java.util.Stack;

public class SolverHamming {
    private class Move implements Comparable<Move> {
        private Move previous;
        private Board board;
        private int numMoves = 0;

        public Move(Board board) {
            this.board = board;
        }

        public Move(Board board, Move previous) {
            this.board = board;
            this.previous = previous;
            this.numMoves = previous.numMoves + 1;
        }

        public int compareTo(Move move) {
            return (this.board.hamming() - move.board.hamming()) + (this.numMoves - move.numMoves);
        }
    }

    private Move lastMove;

    int anothernode = 0;
    public SolverHamming(Board initial) {
        MinPQ<Move> moves = new MinPQ<Move>();
        moves.insert(new Move(initial));

        MinPQ<Move> twinMoves = new MinPQ<Move>();
        twinMoves.insert(new Move(initial.twin()));

        while(true) {
            lastMove = expand(moves);
            anothernode++;
            if (lastMove != null || expand(twinMoves) != null) {
                return;
            }
        }
    }

    int node = 0;
    private Move expand(MinPQ<Move> moves) {
        if(moves.isEmpty()) {
            return null;
        }
        Move bestMove = moves.delMin();
        if (bestMove.board.isGoal()){
            return bestMove;
        }
        for (Board neighbor : bestMove.board.neighbors()) {
            if (bestMove.previous == null || !neighbor.equals(bestMove.previous.board)) {
                moves.insert(new Move(neighbor, bestMove));
                node++;
            }
        }
        return null;
    }

    public boolean isSolvable() {
        if(lastMove == null){
            return (false);
        }
        return (lastMove.board.isSolvable() && lastMove != null);
    }



    public int moves() {
        return isSolvable() ? lastMove.numMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()){
            System.out.println("not solvable");
            return null;
        }
        Stack<Board> moves = new Stack<Board>();
        int count = 0;
        while(lastMove != null) {
            moves.push(lastMove.board);
            lastMove = lastMove.previous;
            count++;
        }

        while(!moves.empty()){
            System.out.println(moves.pop());
        }
        System.out.println("Steps required to solve the puzzle : " +(count-1));//anothernode
        System.out.println("node number :"+node);
        return moves;
    }
}
