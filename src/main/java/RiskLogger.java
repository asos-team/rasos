public interface RiskLogger {
    void logStart();

    void logSuccessfulReinforcement(int playerId, ReinforcementMove movesToLog);
}
