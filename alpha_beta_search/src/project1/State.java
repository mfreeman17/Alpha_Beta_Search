package project1;

import java.util.ArrayList;

/**
 * 
 * @author Matthew Freeman Patrick Crnogorac
 *
 */
public class State {

	public enum pawn {
		White, Black, None;
	}

	private pawn[][] board;
	public boolean wTurn;
	private int width, height;
	private boolean terminal = false;
	private int eval;
	private ArrayList<Moves> posMoves;

	public void init(pawn[][] board, boolean turn, int width, int height) {
		this.board = board;
		this.wTurn = turn;
		this.width = width;
		this.height = height;
		posMoves = new ArrayList<Moves>(); // possible Moves
		int minBlack = Integer.MAX_VALUE;
		int minWhite = Integer.MAX_VALUE;
		int numBlack = 0;// this keeps track of the number of blacks and whites left
		int numWhite = 0; // to be used in the eval
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {

				if (wTurn) {
					whiteTurn(i, j); // finds possible white Moves
				} else if (!wTurn) {
					blackTurn(i, j); // finds possible black Moves if black turn
				}
				/*
				 * this evaluates the state
				 */
				if (board[i][j].equals(pawn.Black)) {
					numBlack++;
					if (i == height - 1) {
						terminal = true;
						eval = -100;
					} else if (height - i - 1 < minBlack) {
						minBlack = height - i - 1;
					}
				} else if (board[i][j].equals(pawn.White)) {
					numWhite++;
					if (i == 0) {
						terminal = true;
						eval = 100;

					}
					if (i < minWhite) {
						minWhite = i;
					}
				}
			}
		}
		if (posMoves.size() == 0) {
			terminal = true;
			eval = 0;
		}
		if (!terminal) {
			eval = minBlack - minWhite + numWhite - numBlack;
		}
		if (!wTurn) {
			eval = -eval; // always evaluate from white perspective
		}
	}

	public int evaluate() {
		return eval;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public ArrayList<Moves> getPossibleMoves() {
		return posMoves;

	}

	/**
	 * finds possible black Moves
	 * 
	 * @param i current location
	 * @param j current location
	 */
	private void blackTurn(int i, int j) {
		if (board[i][j].equals(pawn.Black)) {
			if (i != height - 1) {
				if (board[i + 1][j].equals(pawn.None)) {
					Moves move = new Moves(j, i, j, i + 1);
					posMoves.add(move);
				}
				if (j != width - 1) {
					if (board[i + 1][j + 1].equals(pawn.White)) {
						Moves move1 = new Moves(j, i, j + 1, i + 1);
						posMoves.add(move1);
					}
				}
				if (j != 0) {
					if (board[i + 1][j - 1].equals(pawn.White)) {
						Moves move2 = new Moves(j, i, j - 1, i + 1);
						posMoves.add(move2);
					}
				}
			}
		}
	}

	/**
	 * @param i
	 * @param j adds possible Moves during white turn
	 */
	private void whiteTurn(int i, int j) {
		if (board[i][j].equals(pawn.White) && i != 0) {
			if (board[i - 1][j].equals(pawn.None)) {
				Moves move = new Moves(j, i, j, i - 1);
				posMoves.add(move);
			}
			if (j != width - 1) {
				if (board[i - 1][j + 1].equals(pawn.Black)) {
					Moves move1 = new Moves(j, i, j + 1, i - 1);
					posMoves.add(move1);
				}
			}
			if (j != 0) {
				if (board[i - 1][j - 1].equals(pawn.Black)) {
					Moves move2 = new Moves(j, i, j - 1, i - 1);
					posMoves.add(move2);
				}
			}
		}
	}

	public State changeState(Moves move) {
		pawn[][] tempBoard = new pawn[height][];
		for (int i = 0; i < height; i++) {
			tempBoard[i] = board[i].clone(); // deep copy
		}
		pawn tmpPawn = tempBoard[move.y1][move.x1];
		tempBoard[move.y1][move.x1] = pawn.None;
		tempBoard[move.y2][move.x2] = tmpPawn;
		State state2 = new State();
		state2.init(tempBoard, !wTurn, width, height);
		return state2;

	}

}
