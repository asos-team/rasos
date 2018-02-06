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
        return move.getAmount() <= getOriginCell(board, move).getNumSoldiers();
    }

    private Cell getOriginCell(Board board, AttackMove move) {
        return board.cellAt(move.getOriginCol(), move.getOriginRow());
    }

    private void executeMove(int playerId, Board board, AttackMove attackMove) {
        Cell originCell = getOriginCell(board, attackMove);
        Cell destCell = getDestCell(board, attackMove);

        int amount = attackMove.getAmount();

        originCell.updateNumSoldiers(originCell.getNumSoldiers() - amount);

        if (isReinforcementAttackMove(playerId, destCell)) {
            executeReinforcementAttackMove(playerId, destCell, amount);
        } else if (isConqueringAttackMove(destCell, amount)) {
            executeConqueringAttackMove(playerId, destCell, amount);
        } else if (isNonConqueringAttackMove(destCell, amount)) {
            executeNonConqueringAttackMove(destCell, amount);
        }
    }

    private Cell getDestCell(Board board, AttackMove attackMove) {
        return board.cellAt(attackMove.getDestCol(), attackMove.getDestRow());
    }

    private boolean isReinforcementAttackMove(int playerId, Cell destCell) {
        return destCell.isControlledBy(playerId);
    }

    private void executeReinforcementAttackMove(int playerId, Cell destCell, int amount) {
        destCell.updateNumSoldiers(destCell.getNumSoldiers() + amount);
        destCell.updateControllingPlayerId(playerId);
    }

    private boolean isConqueringAttackMove(Cell destCell, int amount) {
        return amount > destCell.getNumSoldiers();
    }

    private void executeConqueringAttackMove(int playerId, Cell destCell, int amount) {
        destCell.updateControllingPlayerId(playerId);
        destCell.updateNumSoldiers(amount - destCell.getNumSoldiers());
    }

    private boolean isNonConqueringAttackMove(Cell destCell, int amount) {
        return amount <= destCell.getNumSoldiers();
    }

    private void executeNonConqueringAttackMove(Cell destCell, int amount) {
        destCell.updateNumSoldiers(destCell.getNumSoldiers() - amount);
    }
}