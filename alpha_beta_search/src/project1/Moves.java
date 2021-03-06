package project1;
/**
 * 
 * This class takes x1, y1, x2,y2 as parameters so
 * that it can be used to run actions to change states
 *
 */
public class Moves {
	public int x1;
	public int y1;
	public int x2;
	public int y2;
	private int eval;
	
	public Moves(int x1, int y1, int x2, int y2 ){
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		eval=-1000;
	}
	public void print() {
		System.out.printf("x1: %d,y1: %d x2: %d,y2: %d \n", x1,y1,x2,y2);
	}
	public String myString(int height) {
		String sx1,sy1,sx2,sy2;
		sx1=Integer.toString(x1+1);
		sy1=Integer.toString(height-y1);
		sx2=Integer.toString(x2+1);
		sy2=Integer.toString(height-y2);
		return "(move " + sx1 + " " + sy1 + " " + sx2 + " " + sy2 + ")";
	}
	/**
	 * setEval is used to put the evaluation of a move (returned from alpha beta search to
	 * the move so that the best move can be found
	 */
	public void setEval(int eval) {
		this.eval=eval;
		
	}
	public int getEval() {
		return eval;
	}


}
