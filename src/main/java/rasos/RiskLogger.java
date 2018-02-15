package rasos;

public interface RiskLogger {
    void logGameStart();

    void logSuccessfulReinforcement(int playerId, ReinforcementMove movesToLog);

    void logFailedReinforcement(int playerId, ReinforcementMove move);

    void logSuccessfulAttack(int playerId, AttackMove move);

    void logFailedAttack(int playerId, AttackMove move);

    void logRoundStart();

    void logRoundEnd(Board board);
}
