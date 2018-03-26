package rasos;

import java.util.ArrayList;
import java.util.List;

public class Attacker {

    private RiskLogger logger;

    Attacker(RiskLogger logger) {
        this.logger = logger;
    }

    public void apply(Board board, Iterable<AttackMove> movesA, Iterable<AttackMove> movesB, int idA, int idB) {
        Board[] intermediateBoards = generateIntermediateBoards(board, movesA, movesB, idA, idB);
        Board reduced = reduce(intermediateBoards);
        copy(board, reduced);
    }

    private Board[] generateIntermediateBoards(Board board, Iterable<AttackMove> movesA, Iterable<AttackMove> movesB, int idA, int idB) {
        Board[] projectedBoards = createProjectedBoards(board);
        fillIntermediateBoards(movesA, movesB, projectedBoards, idA, idB);
        return projectedBoards;
    }

    private Board[] createProjectedBoards(Board board) {
        List<Integer> playerIds = getPlayerIds(board);
        Board[] iBoards = new Board[playerIds.size()];
        for (int i = 0; i < playerIds.size(); i++) {
            iBoards[i] = project(board, playerIds.get(i));
        }
        return iBoards;
    }

    private List<Integer> getPlayerIds(Board board) {
        List<Integer> playerIds = new ArrayList<>();
        for (int i = 1; i <= board.getDim(); i++) {
            for (int j = 1; j <= board.getDim(); j++) {
                int controllingPlayerId = board.cellAt(i, j).getControllingPlayerId();
                if (controllingPlayerId != 0 && !playerIds.contains(controllingPlayerId)) {
                    playerIds.add(controllingPlayerId);
                }
            }
        }
        return playerIds;
    }

    private Board project(Board board, int playerId) {
        int dim = board.getDim();
        Board res = new Board(dim);
        copy(res, board);
        for (int i = 1; i <= dim; i++) {
            for (int j = 1; j <= dim; j++) {
                if (!board.cellAt(i, j).isControlledBy(playerId)) {
                    res.cellAt(i, j).makeNeutral();
                }
            }
        }
        return res;
    }

    private void fillIntermediateBoards(Iterable<AttackMove> movesA, Iterable<AttackMove> movesB, Board[] intermediateBoards, int idA, int idB) {
        fillIntermediateBoard(intermediateBoards[0], idA, movesA);
        fillIntermediateBoard(intermediateBoards[1], idB, movesB);
    }

    private void fillIntermediateBoard(Board intermediateBoard, int playerId, Iterable<AttackMove> playerMoves) {
        for (AttackMove playerMove : playerMoves) {
            if (isValidMove(intermediateBoard, playerMove)) {
                applyMove(intermediateBoard, playerId, playerMove);
                logger.logSuccessfulAttack(playerId, playerMove);
            } else {
                logger.logFailedAttack(playerId, playerMove);
            }
        }
    }

    private boolean isValidMove(Board board, AttackMove move) {
        return !isExceedingBoardMove(board, move) &&
                isValidAmount(board, move) &&
                isAmongNeighbouringCells(move);
    }

    private boolean isValidAmount(Board board, AttackMove move) {
        return move.getAmount() <= getOriginCell(board, move).getNumSoldiers();
    }

    private Cell getOriginCell(Board board, AttackMove move) {
        return board.cellAt(move.getOriginCol(), move.getOriginRow());
    }

    private boolean isAmongNeighbouringCells(AttackMove move) {
        return Math.abs(move.getOriginCol() - move.getDestCol()) <= 1 &&
                Math.abs(move.getOriginRow() - move.getDestRow()) <= 1;
    }

    private boolean isExceedingBoardMove(Board board, AttackMove move) {
        return (move.getDestCol() > board.getDim() || move.getDestRow() > board.getDim()) ||
                (move.getOriginCol() < 1 || move.getOriginRow() < 1);
    }

    private void applyMove(Board playerIB, int playerId, AttackMove playerMove) {
        int amount = playerMove.getAmount();

        Cell originCell = getOriginCell(playerIB, playerMove);
        Cell destCell = getDestCell(playerIB, playerMove);

        originCell.updateNumSoldiers(originCell.getNumSoldiers() - amount);
        destCell.setValues(playerId, destCell.getNumSoldiers() + amount);
    }

    private Cell getDestCell(Board board, AttackMove attackMove) {
        return board.cellAt(attackMove.getDestCol(), attackMove.getDestRow());
    }

    private Board reduce(Board[] intermediateBoards) {
        Board emptyBoard = new Board(intermediateBoards[0].getDim());
        Board reduced = reduce(emptyBoard, intermediateBoards[0]);
        for (int i = 1; i < intermediateBoards.length; i++) {
            reduced = reduce(reduced, intermediateBoards[i]);
        }
        return reduced;
    }

    private Board reduce(Board a, Board b) {
        int dim = a.getDim();
        Board res = new Board(dim);
        for (int i = 1; i <= dim; i++) {
            for (int j = 1; j <= dim; j++) {
                int controllingPlayerId = getReducedControllingPlayerId(i, j, a, b);
                int numSoldiers = getReducedNumSoldiers(i, j, a, b);
                res.cellAt(i, j).setValues(controllingPlayerId, numSoldiers);
            }
        }
        return res;
    }

    private int getReducedControllingPlayerId(int colIdx, int rowIdx, Board a, Board b) {
        int soldiersA = a.cellAt(colIdx, rowIdx).getNumSoldiers();
        int soldiersB = b.cellAt(colIdx, rowIdx).getNumSoldiers();
        if (soldiersA < soldiersB) {
            return b.cellAt(colIdx, rowIdx).getControllingPlayerId();
        } else if (soldiersA > soldiersB) {
            return a.cellAt(colIdx, rowIdx).getControllingPlayerId();
        } else {
            return 0;
        }
    }

    private int getReducedNumSoldiers(int colIdx, int rowIdx, Board a, Board b) {
        return Math.abs(a.cellAt(colIdx, rowIdx).getNumSoldiers() - b.cellAt(colIdx, rowIdx).getNumSoldiers());
    }

    private void copy(Board target, Board source) {
        for (int i = 1; i <= target.getDim(); i++) {
            for (int j = 1; j <= target.getDim(); j++) {
                Cell cell = source.cellAt(i, j);
                target.cellAt(i, j).setValues(cell.getControllingPlayerId(), cell.getNumSoldiers());
            }
        }
    }
}