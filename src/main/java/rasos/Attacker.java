package rasos;

import java.util.ArrayList;
import java.util.List;

public class Attacker {

    private RiskLogger logger;

    Attacker(RiskLogger logger) {
        this.logger = logger;
    }

    @Deprecated
    public void apply(Board board, Iterable<AttackMove>... moves) {
        Board[] intermediateBoards = generateIntermediateBoards(board, moves);
        Board reduced = reduce(intermediateBoards);
        copy(board, reduced);
    }

    public void applyTwoPlayers(Board board, Iterable<AttackMove> movesA, Iterable<AttackMove> movesB) {
        Board[] intermediateBoards = generateIntermediateBoards(board, new Iterable[]{movesA, movesB});
        Board reduced = reduce(intermediateBoards);
        copy(board, reduced);
    }

    private Board[] generateIntermediateBoards(Board board, Iterable<AttackMove>[] moves) {
        Board[] projectedBoards = createProjectedBoards(board);
        fillIntermediateBoards(moves, projectedBoards);
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
        for (int colIdx = 1; colIdx <= board.getDim(); colIdx++) {
            for (int rowIdx = 1; rowIdx <= board.getDim(); rowIdx++) {
                int controllingPlayerId = board.cellAt(colIdx, rowIdx).getControllingPlayerId();
                if (controllingPlayerId != 0 && !playerIds.contains(controllingPlayerId)) {
                    playerIds.add(controllingPlayerId);
                }
            }
        }
        playerIds.sort(Integer::compareTo);
        return playerIds;
    }

    private Board project(Board board, int playerId) {
        int dim = board.getDim();
        Board b = new Board(dim);
        for (int colIdx = 1; colIdx <= dim; colIdx++) {
            for (int rowIdx = 1; rowIdx <= dim; rowIdx++) {
                Cell cell = getCell(colIdx, rowIdx, board);
                if (cell.isControlledBy(playerId)) {
                    getCell(colIdx, rowIdx, b).setValues(playerId, cell.getNumSoldiers());
                }
            }
        }
        return b;
    }

    private Cell getCell(int colIdx, int rowIdx, Board a) {
        return a.cellAt(colIdx, rowIdx);
    }

    private void fillIntermediateBoards(Iterable<AttackMove>[] moves, Board[] intermediateBoards) {
        int playerId = 1;
        for (Iterable<AttackMove> playerMoves : moves) {
            fillIntermediateBoard(intermediateBoards[playerId - 1], playerId, playerMoves);
            playerId++;
        }
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
                isBetweenNeighbouringCells(move);
    }

    private boolean isValidAmount(Board board, AttackMove move) {
        return move.getAmount() <= getOriginCell(board, move).getNumSoldiers();
    }

    private Cell getOriginCell(Board board, AttackMove move) {
        return getCell(move.getOriginCol(), move.getOriginRow(), board);
    }

    private boolean isBetweenNeighbouringCells(AttackMove move) {
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
        return getCell(attackMove.getDestCol(), attackMove.getDestRow(), board);
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
        for (int colIdx = 1; colIdx <= dim; colIdx++) {
            for (int rowIdx = 1; rowIdx <= dim; rowIdx++) {
                int controllingPlayerId = getReducedControllingPlayerId(colIdx, rowIdx, a, b);
                int numSoldiers = getReducedNumSoldiers(colIdx, rowIdx, a, b);
                res.cellAt(colIdx, rowIdx).setValues(controllingPlayerId, numSoldiers);
            }
        }
        return res;
    }

    private int getReducedControllingPlayerId(int colIdx, int rowIdx, Board a, Board b) {
        int soldiersA = getCell(colIdx, rowIdx, a).getNumSoldiers();
        int soldiersB = getCell(colIdx, rowIdx, b).getNumSoldiers();
        if (soldiersA < soldiersB) {
            return getCell(colIdx, rowIdx, b).getControllingPlayerId();
        } else if (soldiersA > soldiersB) {
            return getCell(colIdx, rowIdx, a).getControllingPlayerId();
        } else {
            return 0;
        }
    }

    private int getReducedNumSoldiers(int colIdx, int rowIdx, Board a, Board b) {
        return Math.abs(getCell(colIdx, rowIdx, a).getNumSoldiers() - getCell(colIdx, rowIdx, b).getNumSoldiers());
    }

    private void copy(Board target, Board source) {
        for (int colIdx = 1; colIdx <= target.getDim(); colIdx++) {
            for (int rowIdx = 1; rowIdx <= target.getDim(); rowIdx++) {
                Cell cell = source.cellAt(colIdx, rowIdx);
                target.cellAt(colIdx, rowIdx).setValues(cell.getControllingPlayerId(), cell.getNumSoldiers());
            }
        }
    }
}