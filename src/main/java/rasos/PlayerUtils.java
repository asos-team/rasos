package rasos;

public class PlayerUtils {
    private final Player player;

    public PlayerUtils(Player player) {
        this.player = player;
    }

    public Iterable<CellCoordinates> getControlledCells(Board board) {
        return board.getControlledCoordinates(player.getPlayerId());
    }
}