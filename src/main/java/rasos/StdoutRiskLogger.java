package rasos;

import static rasos.Config.ID_A;
import static rasos.Config.ID_B;

public class StdoutRiskLogger implements RiskLogger {

    private static final String ASOS = "\n" +
            "  __ _ ___  ___  ___ \n" +
            " / _` / __|/ _ \\/ __|\n" +
            "| (_| \\__ \\ (_) \\__ \\\n" +
            " \\__,_|___/\\___/|___/\n" +
            "                     \n";
    private final Printer printer;

    StdoutRiskLogger() {
        this(System.out::println);
    }

    StdoutRiskLogger(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void logGameStart() {
        printer.print(ASOS);
        printer.print(("Game started!"));
    }

    @Override
    public void logSuccessfulReinforcement(int playerId, ReinforcementMove move) {
        printer.print((String.format("player %s reinforced %d soldier(s) in %d,%d",
                getPlayerSymbol(playerId), move.getAmount(), move.getRow(), move.getCol())));
    }

    @Override
    public void logFailedReinforcement(int playerId, ReinforcementMove move) {
        printer.print(String.format("player %s failed to reinforce (move: %s)", getPlayerSymbol(playerId), move));
    }

    @Override
    public void logSuccessfulAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %s attacked from %d,%d to %d,%d with %d soldier(s)",
                getPlayerSymbol(playerId), move.getOriginRow(), move.getOriginCol(), move.getDestRow(), move.getDestCol(), move.getAmount()));
    }

    @Override
    public void logFailedAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %s failed to attack (move: %s)", getPlayerSymbol(playerId), move));
    }

    @Override
    public void logRoundStart() {
        printer.print("-------========NEW ROUND========-------");
    }

    @Override
    public void logRoundEnd(Board board) {
        printer.print("-------========ROUND ENDED========-------");
        printer.print(board.toString());
        printer.print(String.format("Player A controls %d cells.", board.getPlayerCellCount(ID_A)));
        printer.print(String.format("Player B controls %d cells.", board.getPlayerCellCount(ID_B)));
    }

    @Override
    public void logGameEnd(int winnerId) {
        printer.print("-------==========================-------");
        printer.print("-------========GAME ENDED========-------");
        printer.print("-------===========WINNER=========-------");
        printer.print("-------============IS============-------");
        printer.print(String.format("-------============{%s}============-------", getPlayerSymbol(winnerId)));
        printer.print("-------==========================-------");
        printer.print("-------==========================-------");
        printer.print("-------==========================-------");

        printer.print("Brought to you by:");

        printer.print(ASOS);
    }

    @Override
    public void logPlayerReinforcementCodeException(int playerId, Exception e) {
        printer.print(String.format("player %s threw an exception on reinforcement (exception: %s)",
                getPlayerSymbol(playerId), e.getMessage()));
    }

    @Override
    public void logPlayerAttackCodeException(int playerId, Exception e) {
        printer.print(String.format("player %s threw an exception on attack (exception: %s)",
                getPlayerSymbol(playerId), e.getMessage()));
    }

    private String getPlayerSymbol(int playerId) {
        if (playerId == ID_A) {
            return "A";
        } else if (playerId == ID_B) {
            return "B";
        } else {
            return "UNKNOWN";
        }
    }

    public interface Printer {

        void print(String s);
    }
}
