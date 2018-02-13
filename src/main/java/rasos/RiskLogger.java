package rasos;

public interface RiskLogger {
    void logStart();

    void logSuccessfulReinforcement(int playerId, ReinforcementMove movesToLog);

    void logFailedReinforcement(int playerId, ReinforcementMove move);
}
