package aima.search.nqueens;

import java.util.List;

import aima.basic.XYLocation;
import aima.search.framework.GoalTest;

public class NQueensGoalTest implements GoalTest {
	NQueensBoard board;

	public boolean isGoalState(Object state) {

		board = (NQueensBoard) state;
		return (allQueensPlaced() && allQueenPositionsHaveZeroAttacks(board
				.getQueenPositions()));
	}

	private boolean allQueensPlaced() {
		return board.getNumberOfQueensOnBoard() == board.getSize();
	}

	private boolean allQueenPositionsHaveZeroAttacks(List positions) {
		boolean retVal = true;
		for (int i = 0; i < positions.size(); i++) {
			XYLocation location = (XYLocation) positions.get(i);
			if (board.getNumberOfAttacksOn(location) != 0) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
}