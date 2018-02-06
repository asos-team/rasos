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
        destCell.updateNumSoldiers(destCell.getNumSoldiers() + amount);
        destCell.updateControllingPlayerId(playerId);
    }

    private Cell getDestCell(Board board, AttackMove attackMove) {
        return board.cellAt(attackMove.getDestCol(), attackMove.getDestRow());
    }
}