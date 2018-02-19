package rasos.players;


import com.google.common.collect.Lists;
import rasos.AttackMove;
import rasos.Board;
import rasos.CellCoordinates;
import rasos.PlayerUtils;

public class AttackPlayer extends ReinforcePlayer {

    PlayerUtils pu = new PlayerUtils(this);

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        Iterable<CellCoordinates> controlledCells = pu.getControlledCells(board);
        return Lists.newArrayList(new AttackMove(1, 1, 2, 2, 9));
    }
}
