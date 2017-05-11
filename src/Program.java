import java.util.*;

/**
 * Created by Isaac on 5/4/2017.
 */
public class Program {

    public static void main(String[] args){
        steepestHillClimbTester(21, 200);
        //System.out.println(geneticAlg(21, 10000, .10f));
    }

    public static State geneticAlg(int n, int pop, float mr){
        State[] initPop = new State[pop];

        generatePopulation(initPop, n);

        return geneticAlgHelperT(initPop, mr);
    }

    public static State geneticAlgHelperT(State[] population, float mr){

        Random rand = new Random();
        State best = population[0];

        while(attacks(best) != 0){
            State[] newPop = new State[population.length * 2];

            for(int i = 0; i < population.length * 2; i++){

                State child = reproduce(population[rand.nextInt(population.length)], population[rand.nextInt(population.length)]);

                if(rand.nextFloat() <= mr){
                    child = mutate(child);
                }

                if(best.fitness < child.fitness){
                    best = child;
                    System.out.println(best.getAttacks());
                }
                newPop[i] = child;
            }
            Arrays.sort(newPop);
            population = Arrays.copyOf(newPop, population.length);
        }
        return best;
    }

    public static State mutate(State s){
        Random rand = new Random();

        int[] board = Arrays.copyOf(s.getBoard(), s.getN());
        int idx = rand.nextInt(board.length);

        boolean mutated = false;
        while(!mutated){
            int num = rand.nextInt(board.length);
            if(board[idx] != num){
                board[idx] = num;
                mutated = true;
            }
        }

        return new State(board, attacks(board), board.length);
    }

    public static State reproduce(State x, State y){
        int n = x.getN();

        Random rand = new Random();

        int c = rand.nextInt(n);

        int[] resBoard = new int[n];

        int[] xBoard = x.getBoard();
        for(int i = 0; i < c; i++){
            resBoard[i] = xBoard[i];
        }

        int[] yBoard = y.getBoard();
        for(int i = c; i < n; i++){
            resBoard[i] = yBoard[i];
        }

        return new State(resBoard, attacks(resBoard), n);
    }

    public static void generatePopulation(State[] initPop, int n){
        Random rand = new Random();

        for(int i = 0; i < initPop.length; i++){
            int[] board = new int[n];
            for(int j = 0; j < board.length; j++){
                board[j] = rand.nextInt(n);
            }

            initPop[i] = new State(board, attacks(board), n);
        }
    }

    public static void steepestHillClimbTester(int n, int tests){
        int solutions = 0;
        long time = 0;
        int cost = 0;

        Random rand = new Random();
        for(int i = 0; i < tests; i++){
            int[] board = new int[n];

            for(int j = 0; j < board.length; j++){
                board[j] = rand.nextInt(8);
            }

            TestCase res = steepestHillClimb(board);

            time += res.time;
            cost += res.searchCost;

            if(solved(res.state)){
                solutions++;
                //System.out.println(res.state);
                //System.out.println("Solution Found!");
            }

        }

        System.out.println("Percentage Solved: " + ((solutions / (double)tests) * 100) + "%");
        System.out.println("Average Running Time(ns): " + time / tests);
        System.out.println("Average Search Cost: " + cost / tests);
    }

    public static TestCase steepestHillClimb(int[] board){
        State cur = new State(board, attacks(board), board.length);
        State min = cur;
        int searchCost = 0;

        long start = System.nanoTime();
        while(true){
            searchCost++;
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
                long end = System.nanoTime();
                return new TestCase(cur, end - start, searchCost);
            } else {
                cur = min;
            }
        }
    }

    public static boolean solved(State s){
        return (attacks(s.getBoard()) == 0);
    }

    public void swap(int[] a, int x, int y){
        int temp = a[x];
        a[x] = a[y];
        a[y] = temp;
    }

    public static int attacks(State s){
        return attacks(s.getBoard());
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
