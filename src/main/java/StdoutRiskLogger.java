public class StdoutRiskLogger implements RiskLogger{
    @Override
    public void logStart() {
        System.out.println("Game started");
    }
}
