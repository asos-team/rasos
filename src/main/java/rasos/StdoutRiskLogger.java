package rasos;

public class StdoutRiskLogger implements RiskLogger {

    public static final String ASOS = "\n" +
            "  __ _ ___  ___  ___ \n" +
            " / _` / __|/ _ \\/ __|\n" +
            "| (_| \\__ \\ (_) \\__ \\\n" +
            " \\__,_|___/\\___/|___/\n" +
            "                     \n";
    private final Printer printer;

    public StdoutRiskLogger() {
        this(System.out::println);
    }

    public StdoutRiskLogger(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void logGameStart() {
        printer.print(ASOS);
        printer.print(("Game started"));
    }

    @Override
    public void logSuccessfulReinforcement(int playerId, ReinforcementMove move) {
        printer.print((String.format("player %d executes reinforcement move %s", playerId, move)));
    }

    @Override
    public void logFailedReinforcement(int playerId, ReinforcementMove move) {
        printer.print(String.format("player %d failed to execute reinforcement move %s", playerId, move));
    }

    @Override
    public void logSuccessfulAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %d executes attack move %s", playerId, move));
    }

    @Override
    public void logFailedAttack(int playerId, AttackMove move) {
        printer.print(String.format("player %d failed to execute attack move %s", playerId, move));
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

    public interface Printer {
        void print(String s);
    }
}
