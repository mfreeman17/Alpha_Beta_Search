import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import project1.Search;
import project1.State;
import project1.State.pawn;
import project1.Moves;

public class RandomAgent implements Agent
{
	private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private State state;
	private boolean wTurn;
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
		this.role = role;
		System.out.println(role);
		System.out.println(role.equals("white"));
		this.playclock = playclock;
		myTurn = !role.equals("white");
		wTurn=role.equals("white");
		this.width = width;
		this.height = height;
		pawn[][] board= new pawn[height][width];
		for (int j=0; j<width; j++) {
			board[0][j]=pawn.Black;
			board[1][j]=pawn.Black;
			board[height-1][j]=pawn.White;
			board[height-2][j]=pawn.White;
		}
		for (int j=2; j<height-2; j++) {
			for (int i=0; i<width; i++) {
				board[j][i]=pawn.None;
			}
		}
		
		state= new State();
		state.init(board, true, width, height);
		state.evaluate();
		ArrayList<Moves> posMoves = state.getPossibleMoves();
		for (int m=0; m<posMoves.size(); m++) {
			posMoves.get(m).print();
		}
		/*
		Search search = new Search();
		Moves bestMove = search.alphaBetaRoot(4, state, -1000, 1000);
		State state2= state.changeState(bestMove);
		bestMove.print();
		bestMove=search.alphaBetaRoot(4, state2, -1000, 1000);
		bestMove.print();
		*/

		
		// TODO: add your own initialization code here
		
    }

    /**
     * transforms coords of last move
     * to a move that is readable by the state
     * function
     * @param lastMove
     * @return
     */
    private Moves transform (int[] lastMove) {
		int x1=lastMove[0]-1;
		int y1=height-lastMove[1];
		int x2=lastMove[2]-1;
		int y2=height-lastMove[3];
		Moves Move= new Moves(x1,y1,x2,y2);
    	return Move;
    }

	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	if (lastMove != null) {
    		int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
    		String roleOfLastPlayer;
    		if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
    			roleOfLastPlayer = "white";
    		} else {
    			roleOfLastPlayer = "black";
    		}
   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
    		// TODO: 1. update your internal world model according to the action that was just executed
   			if(!myTurn) {
	   			Moves transformed=transform(lastMove);
	   			state=state.changeState(transformed);
   			}
	   			
    	}
		
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			long initTime= System.currentTimeMillis();
			long fplayClock=(long) (playclock*1000);
			Search search = new Search();
			ArrayList<Moves> posMoves =state.getPossibleMoves();
			PriorityQueue<Moves> orderedMoves;
			orderedMoves=new PriorityQueue<Moves>(Comparator.comparingInt(Moves::getEval));		
			Moves bestMove=new Moves(0,0,0,0); //temporary best move will get replaced. 
			try {
				for (int d=1; d<100; d++) {
					orderedMoves.clear();
					for (int i=0; i<posMoves.size(); i++) {
						Moves curMove=posMoves.get(i);
						orderedMoves.add(curMove);
					}
					if (orderedMoves.size()!=0) {
						bestMove=search.alphaBetaRoot(d, state, -10000, 10000, orderedMoves, initTime, fplayClock);
					}
					System.out.println(d);
	
				}
		
			
			System.out.println(bestMove.myString(height));
			System.out.println(state.evaluate());
			}
			catch (TimeoutException e) {
				System.out.println("here");
			}
			state=state.changeState(bestMove); //sets state to current move
			System.out.println(search.numExpansions);
			return bestMove.myString(height);
			
			
			
		}
		return "noop";
    
		
	}

	@Override
	public void cleanup() {
		state=null;
		
		// TODO: cleanup so that the agent is ready for the next match
	}

}