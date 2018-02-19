package rasos.players;

import com.google.common.collect.Iterables;
import rasos.*;

import java.util.ArrayList;

public class ReinforcePlayer extends Player {

    private PlayerUtils playerUtils = new PlayerUtils(this);

    @Override
    public Iterable<ReinforcementMove> onReinforcement(Board board, int reinforcement) {
        ArrayList<ReinforcementMove> reinforcementMoves = new ArrayList<>();
        Iterable<CellCoordinates> myCellsCoordinates = playerUtils.getControlledCells(board);
        int numControlledCells = Iterables.size(myCellsCoordinates);

        for (CellCoordinates cc :
                myCellsCoordinates) {
            reinforcementMoves.add(new ReinforcementMove(cc.getColIdx(), cc.getRowIdx(), reinforcement / numControlledCells));
        }

        return reinforcementMoves;
    }

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        return null;
    }
}
