package project1;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.TimeoutException;

public class Search {
	public int numExpansions=0;
	
	public  Moves alphaBetaRoot(int d, State s,int alpha,int beta, PriorityQueue<Moves> Moves, long initTime, long playclock) throws TimeoutException {
		long curTime= System.currentTimeMillis();
		long timePast= curTime-initTime;
		if (timePast+20>= playclock) {
			throw new TimeoutException();
		}
		
		int bestValue = -10000; 
	
	
		Moves bestMove;
		bestMove=null;
		
		while (Moves.size()>0 ){
			State s1 = new State();
			Moves curMove= Moves.poll();
			s1 = s.changeState(curMove);
			int v;
			try {
				v = -1*AlphaBeta(d-1, s1, beta * -1, alpha * -1, initTime, playclock);
			}
			catch (TimeoutException e) {
				throw new TimeoutException();
			}
				
		
			curMove.setEval(v);
			if (v > bestValue) {
				bestMove = curMove;
				bestValue = v;
				if(v > alpha) {
					alpha = v ;
				}	
				}
		}
			
		return bestMove;
	}
	
	public int AlphaBeta (int depth, State s, int alpha, int beta, long initTime, long playclock) throws TimeoutException {
		
		long curTime= System.currentTimeMillis();
		long timePast= curTime-initTime;
		if (timePast+20>= playclock) {
			throw new TimeoutException(); //throws timeout exception when there are only 20 milliseconds left
		}
		numExpansions++;
		if (s.isTerminal()) {		
			int eval=s.evaluate();
			return eval;
		}
		else if(depth==0) {
			int eval=s.evaluate();
			return eval;
		}
		int bestValue = -10000;
		ArrayList<Moves> actions = s.getPossibleMoves();
		for (int i = 0; i<actions.size(); i++) {
			State child=new State();
			Moves curMove= actions.get(i);
			child =s.changeState(curMove);
			int value = -1*AlphaBeta(depth-1, child, beta * -1, alpha * -1, initTime, playclock);
			bestValue = Integer.max(value, bestValue);
			if (bestValue > alpha) {
				alpha = bestValue;
				if (alpha >= beta) {
					break;
				}
			}
		}
		
		return bestValue;
	}

}