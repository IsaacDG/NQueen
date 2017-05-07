/**
 * Created by Isaac on 5/4/2017.
 */
public class State{

    private int[] board;
    private int attacks;
    private int n;

    public State(int[] board, int attacks, int n){
        this.board = board;
        this.attacks = attacks;
        this.n = n;
    }

    public int[] getBoard(){
        return board;
    }

    public int getAttacks(){
        return attacks;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        boolean[][] occupied = new boolean[n][n];

        for(int i = 0; i < board.length; i++){
            occupied[board[i]][i] = true;
        }

        for(int i = 0; i < occupied.length; i++){
            for(int j = 0; j < occupied.length; j++){
                if(occupied[i][j]){
                    sb.append("X ");
                } else {
                    sb.append("O ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
