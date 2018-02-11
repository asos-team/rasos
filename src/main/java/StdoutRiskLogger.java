public class StdoutRiskLogger implements RiskLogger {
    @Override
    public void logStart() {
        System.out.print("Game started" + System.lineSeparator());
    }

    @Override
    public void logSuccessfulReinforcement(int playerId, ReinforcementMove movesToLog) {

    }
}
