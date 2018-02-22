package rasos;

public class StdoutRiskLogger implements RiskLogger {

    public static final String ASOS = "\n" +
            "  __ _ ___  ___  ___ \n" +
            " / _` / __|/ _ \\/ __|\n" +
            "| (_| \\__ \\ (_) \\__ \\\n" +
            " \\__,_|___/\\___/|___/\n" +
            "                     \n";

    @Override
    public void logGameStart() {
        System.out.println(ASOS);
        System.out.print("Game started" + System.lineSeparator());
    }

    @Override
    public void logSuccessfulReinforcement(int playerId, ReinforcementMove move) {
        System.out.println(String.format("player %d executes reinforcement move %s", playerId, move));
    }

    @Override
    public void logFailedReinforcement(int playerId, ReinforcementMove move) {
        System.out.println(String.format("player %d failed to execute reinforcement move %s", playerId, move));
    }

    @Override
    public void logSuccessfulAttack(int playerId, AttackMove move) {
        System.out.println(String.format("player %d executes attack move %s", playerId, move));
    }

    @Override
    public void logFailedAttack(int playerId, AttackMove move) {
        System.out.println(String.format("player %d failed to execute attack move %s", playerId, move));
    }

    @Override
    public void logRoundStart() {
        System.out.println("-------========NEW ROUND========-------");
    }

    @Override
    public void logRoundEnd(Board board) {
        System.out.println("-------========ROUND ENDED========-------");
        System.out.println(board);

    }

    @Override
    public void logGameEnd(int winnerId) {
        System.out.println("-------==========================-------");
        System.out.println("-------========GAME ENDED========-------");
        System.out.println("-------===========WINNER=========-------");
        System.out.println("-------============IS============-------");
        System.out.println(String.format("-------============{%d}============-------", winnerId));
        System.out.println("-------==========================-------");
        System.out.println("-------==========================-------");
        System.out.println("-------==========================-------");

        System.out.println("Brought to you by:");

        System.out.println(ASOS);
    }
}
