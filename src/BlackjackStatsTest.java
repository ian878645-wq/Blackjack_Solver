public class BlackjackStatsTest {
    public static void main(String[] args) {
        BlackjackStrategy s = new BlackjackStrategy();
        String[] dealerCards = {"7","8","9","10","A"};
        int[] playerTotals = {13,14,15,16};

        System.out.println("Player\tDealer\tSTAND%\tHIT%\tDOUBLE%\tSPLIT%");
        for (int pv : playerTotals) {
            for (String d : dealerCards) {
                int dv = s.cardValue(d);
                BlackjackStrategy.Statistics stats = s.calculateStatistics(pv, false, dv, false);
                System.out.printf("%d\t%s\t%.1f\t%.1f\t%.1f\t%.1f\n", pv, d, stats.standWinPercent, stats.hitWinPercent, stats.doubleDownWinPercent, stats.splitWinPercent);
            }
        }
    }
}
