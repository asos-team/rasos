package rasos;

public class StdoutRiskLogger implements RiskLogger {
    @Override
    public void logStart() {
        System.out.print("Game started" + System.lineSeparator());
    }

    @Override
    public void logSuccessfulReinforcement(int playerId, ReinforcementMove movesToLog) {

    }

    @Override
    public void logFailedReinforcement(int playerId, ReinforcementMove move) {

    }

    @Override
    public void logSuccessfulAttack(int playerId, AttackMove move) {
        
    }
}
