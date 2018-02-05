public class Attacker {

    public void apply(int playerId, Iterable<AttackMove> attackMoves, Board board) {
        for (AttackMove move : attackMoves) {
            if (move != null) {
                apply(playerId, board, move);
            }
        }
    }

    public void apply(Board board, Iterable<AttackMove>... moves) {
    }

    private void apply(int playerId, Board board, AttackMove move) {
        if (isValidMove(playerId, board, move)) {
            executeMove(playerId, board, move);
        }
    }

    private boolean isValidMove(int playerId, Board board, AttackMove move) {
        return move.getAmount() <= getOriginCell(board, move).getNumSoldiers();
    }

    private Cell getOriginCell(Board board, AttackMove move) {
        return board.cellAt(move.getOriginCol(), move.getOriginRow());
    }

    private void executeMove(int playerId, Board board, AttackMove attackMove) {
        Cell originCell = getOriginCell(board, attackMove);
        Cell destCell = getDestCell(board, attackMove);

        int amount = attackMove.getAmount();

        originCell.setNumSoldiers(originCell.getNumSoldiers() - amount);
        destCell.setNumSoldiers(destCell.getNumSoldiers() + amount);
        destCell.setControllingPlayerId(playerId);
    }

    private Cell getDestCell(Board board, AttackMove attackMove) {
        return board.cellAt(attackMove.getDestCol(), attackMove.getDestRow());
    }
}