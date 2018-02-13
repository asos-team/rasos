package rasos;

public class Attacker {

    public void apply(Board board, Iterable<AttackMove>... moves) {
        int playerId = 1;
        for (Iterable<AttackMove> playerMoves : moves) {
            for (AttackMove playerMove : playerMoves) {
                if (isValidMove(board, playerMove)) {
                    executeMove(playerId, board, playerMove);
                }
            }
            playerId++;
        }
    }

    private boolean isValidMove(Board board, AttackMove move) {
        return isValidAmount(board, move) &&
                isBetweenNeighbouringCells(move);
    }

    private boolean isValidAmount(Board board, AttackMove move) {
        return move.getAmount() <= getOriginCell(board, move).getNumSoldiers();
    }

    private boolean isBetweenNeighbouringCells(AttackMove move) {
        return Math.abs(move.getOriginCol() - move.getDestCol()) <= 1 &&
                Math.abs(move.getOriginRow() - move.getDestRow()) <= 1;
    }

    private Cell getOriginCell(Board board, AttackMove move) {
        return board.cellAt(move.getOriginCol(), move.getOriginRow());
    }

    private void executeMove(int playerId, Board board, AttackMove attackMove) {
        executeMove(playerId, getOriginCell(board, attackMove), getDestCell(board, attackMove), attackMove.getAmount());
    }

    private Cell getDestCell(Board board, AttackMove attackMove) {
        return board.cellAt(attackMove.getDestCol(), attackMove.getDestRow());
    }

    private void executeMove(int playerId, Cell originCell, Cell destCell, int amount) {
        changeOriginCell(originCell, amount);
        changeDestCell(playerId, destCell, amount);
    }

    private void changeOriginCell(Cell originCell, int amount) {
        originCell.updateNumSoldiers(originCell.getNumSoldiers() - amount);
    }

    private void changeDestCell(int playerId, Cell destCell, int amount) {
        if (isReinforcementAttackMove(playerId, destCell)) {
            executeReinforcementAttackMove(playerId, destCell, amount);
        } else if (isConqueringAttackMove(destCell, amount)) {
            executeConqueringAttackMove(playerId, destCell, amount);
        } else if (isNonConqueringAttackMove(destCell, amount)) {
            executeNonConqueringAttackMove(destCell, amount);
        }
    }

    private boolean isReinforcementAttackMove(int playerId, Cell destCell) {
        return destCell.isControlledBy(playerId);
    }

    private void executeReinforcementAttackMove(int playerId, Cell destCell, int amount) {
        destCell.setValues(playerId, destCell.getNumSoldiers() + amount);
    }

    private boolean isConqueringAttackMove(Cell destCell, int amount) {
        return amount > destCell.getNumSoldiers();
    }

    private void executeConqueringAttackMove(int playerId, Cell destCell, int amount) {
        destCell.setValues(playerId, amount - destCell.getNumSoldiers());
    }

    private boolean isNonConqueringAttackMove(Cell destCell, int amount) {
        return amount <= destCell.getNumSoldiers();
    }

    private void executeNonConqueringAttackMove(Cell destCell, int amount) {
        destCell.updateNumSoldiers(destCell.getNumSoldiers() - amount);
    }
}