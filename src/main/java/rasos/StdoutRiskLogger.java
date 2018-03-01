package rasos;

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
        printer.print((String.format("player %d reinforced %d soldier(s) in %d,%d",
                playerId, move.getAmount(), move.getRow(), move.getCol())));
    }

    @Override
    public void logFailedReinforcement(int playerId, ReinforcementMove move) {
        printer.print(String.format("player %d failed to reinforce (move: %s)", playerId, move));
    }

    @Override
    public void logSuccessfulAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %d attacked from %d,%d to %d,%d with %d soldier(s)",
                playerId, move.getOriginRow(), move.getOriginCol(), move.getDestRow(), move.getDestCol(), move.getAmount()));
    }

    @Override
    public void logFailedAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %d failed to attack (move: %s)", playerId, move));
    }

    @Override
    public void logRoundStart() {
        printer.print("-------========NEW ROUND========-------");
    }

    @Override
    public void logRoundEnd(Board board) {
        printer.print("-------========ROUND ENDED========-------");
        printer.print(board.toString());

    }

    @Override
    public void logGameEnd(int winnerId) {
        printer.print("-------==========================-------");
        printer.print("-------========GAME ENDED========-------");
        printer.print("-------===========WINNER=========-------");
        printer.print("-------============IS============-------");
        printer.print(String.format("-------============{%d}============-------", winnerId));
        printer.print("-------==========================-------");
        printer.print("-------==========================-------");
        printer.print("-------==========================-------");

        printer.print("Brought to you by:");

        printer.print(ASOS);
    }

    @Override
    public void logPlayerReinforcementCodeException(int playerId, Exception e) {
        printer.print("");
    }

    @Override
    public void logPlayerAttackCodeException(int eq, Exception e) {
        printer.print("");
    }

    public interface Printer {
        void print(String s);
    }
}
