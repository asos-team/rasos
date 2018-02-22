package rasos.players;


import rasos.AttackMove;
import rasos.Board;
import rasos.CellCoordinates;
import rasos.PlayerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AttackPlayer extends ReinforcePlayer {

    private PlayerUtils pu = new PlayerUtils(this);

    @Override
    public Iterable<AttackMove> onAttack(Board board) {
        Iterable<CellCoordinates> controlledCells = pu.getControlledCells(board);
        List<AttackMove> res = new ArrayList<>();
        for (CellCoordinates cc : controlledCells) {
            res.add(getAttackMoveFromCell(board, cc));
        }
        return res;
    }

    private AttackMove getAttackMoveFromCell(Board board, CellCoordinates cc) {
        int colIdx = cc.getColIdx();
        int rowIdx = cc.getRowIdx();
        int numSoldiers = board.cellAt(colIdx, rowIdx).getNumSoldiers();
        CellCoordinates randNeighbour = getRandomNeighbour(board, colIdx, rowIdx);
        return new AttackMove(colIdx, rowIdx, randNeighbour.getColIdx(), randNeighbour.getRowIdx(), numSoldiers / 2);
    }

    private CellCoordinates getRandomNeighbour(Board board, int colIdx, int rowIdx) {
        List<CellCoordinates> neighbours = pu.getNeighbours(board, new CellCoordinates(colIdx, rowIdx));
        Random r = new Random();
        return neighbours.get(r.nextInt(neighbours.size()));
    }

}
