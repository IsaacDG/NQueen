import java.util.Arrays;
import java.util.Random;

/**
 * Created by Isaac on 5/4/2017.
 */
public class Program {

    public static void main(String[] args){

        int[] boardT = {2, 4, 7, 4, 8, 5, 5, 2};
        System.out.println(attacks(boardT));

        int n = 21;

        int solutions = 0;
        Random rand = new Random();
        for(int i = 0; i < 100; i++){
            int[] board = new int[n];

            for(int j = 0; j < board.length; j++){
                board[j] = rand.nextInt(8);
            }

            State res = steepestHillClimb(board);
            if(attacks(res.getBoard()) == 0){
                solutions++;
                System.out.println("Solution Found!");
                System.out.println(res);
            }

        }

        System.out.println(((solutions / 100.0) * 100) + "%");
    }

    public static State geneticAlg(int[] board){
        int totPossiblePairs = board.length * ((board.length - 1) / 2);

        return null;
    }

    public static State steepestHillClimb(int[] board){
        State cur = new State(board, attacks(board), board.length);
        State min = cur;

        boolean stuck = false;
        while(!stuck){
            int ogAttacks = cur.getAttacks();
            int minAttacks = ogAttacks;
            int[] curBoard = cur.getBoard();

            for(int i = 0; i < curBoard.length; i++){
                int prev = curBoard[i];
                for(int j = 0; j < curBoard.length; j++){
                    if(j == prev) continue;
                    int change = calcChange(curBoard, j, i, ogAttacks);
                    if(change < minAttacks){
                        int[] a = Arrays.copyOf(curBoard, curBoard.length);
                        a[i] = j;
                        min = new State(a, change, board.length);
                        minAttacks = change;
                    }
                }
            }

            if(min == cur){
                return cur;
            } else {
                cur = min;
            }
        }
        return cur;
    }

    public static boolean solved(State s){
        return (attacks(s.getBoard()) == 0);
    }

    public void swap(int[] a, int x, int y){
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }

    public static int attacks(int[] board){
        int attacks = 0;
        for(int i = 0; i < board.length; i++){
            int iNum = board[i];

            for(int j = i+1; j < board.length; j++){
                int jNum = board[j];
                if(iNum == jNum){
                    attacks++;
                } else if(j - i == Math.abs(jNum - iNum)){
                    attacks++;
                }
            }
        }
        return attacks;
    }

    public static int calcChange(int[] board, int mov, int col, int prev){

        board = Arrays.copyOf(board, board.length);
        prev -= calcAttackForPosition(board, col);
        board[col] = mov;
        prev += calcAttackForPosition(board, col);
        return prev;

    }

    public static int calcAttackForPosition(int[] board, int pos){
        int attacks = 0;
        int row = board[pos];

        for(int i = 0; i < pos; i++){
            int cur = board[i];
            if(cur == row){
                attacks++;
            } else if(pos - i == Math.abs(row - cur)){
                attacks++;
            }
        }

        for(int i = pos + 1; i < board.length; i++){
            int cur = board[i];
            if(cur == row){
                attacks++;
            } else if(i - pos == Math.abs(row - cur)){
                attacks++;
            }
        }

        return attacks;

    }


}
