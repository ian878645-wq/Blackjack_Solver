public class BlackjackStrategy {
    
    public static class Recommendation {
        public String action;
        public String reason;
        
        public Recommendation(String action, String reason) {
            this.action = action;
            this.reason = reason;
        }
    }

    public static class Statistics {
        public double standWinPercent;
        public double hitWinPercent;
        public double doubleDownWinPercent;
        public double splitWinPercent;
        public double standEV;
        public double hitEV;
        public double doubleDownEV;
        public double splitEV;
        
        public Statistics(double standWinPercent, double hitWinPercent, double doubleDownWinPercent, double splitWinPercent,
                         double standEV, double hitEV, double doubleDownEV, double splitEV) {
            this.standWinPercent = standWinPercent;
            this.hitWinPercent = hitWinPercent;
            this.doubleDownWinPercent = doubleDownWinPercent;
            this.splitWinPercent = splitWinPercent;
            this.standEV = standEV;
            this.hitEV = hitEV;
            this.doubleDownEV = doubleDownEV;
            this.splitEV = splitEV;
        }
    }

    public int cardValue(String card) {
        card = card.toUpperCase().trim();
        if (card.equals("A")) return 11;
        if (card.equals("K") || card.equals("Q") || card.equals("J")) return 10;
        try {
            return Integer.parseInt(card);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid card: " + card);
        }
    }

    public int calculateHandValue(String[] cards) {
        int total = 0;
        int aces = 0;

        for (String card : cards) {
            int val = cardValue(card);
            if (val == 11) aces++;
            total += val;
        }

        // Adjust for aces if busting
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    public boolean hasAce(String[] cards) {
        int total = 0;
        int aces = 0;

        for (String card : cards) {
            int val = cardValue(card);
            if (val == 11) aces++;
            total += val;
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return aces > 0;
    }

    public String getHandName(int value, boolean hasAce) {
        if (hasAce) return "Soft " + value;
        return "Hard " + value;
    }

    public String getCardCountRecommendation(int cardCount){
        String betUnits;
        if (cardCount <= 0) {
            betUnits = "bet the lowest amount.";
        }
        else if (cardCount == 1){
            betUnits = "change your bet by 4 times your normal bet.";
        }
        else if (cardCount == 2){
            betUnits = "change your bet by 6 times your normal bet.";
        }
        else if (cardCount == 3){
            betUnits = "change your bet by 8 times your normal bet.";
        }
        else {
            betUnits = "bet the table limit.";
        }
        return "A true card count of "+ cardCount + " changes the EV by " + (cardCount * 0.5) + "% " + betUnits;
    }

    public Recommendation getRecommendation(int playerValue, boolean playerHasAce, String dealerCard, boolean canSplit) {
        int dealerValue = cardValue(dealerCard);

        // Pair splitting strategy
        if (canSplit && playerValue % 2 == 0) {
            int cardVal = playerValue / 2;
            
            if (cardVal == 11) {
                // Split Aces against everything except Ace
                if (dealerValue != 11) {
                    return new Recommendation("SPLIT", "Split Aces against dealer 2-10.");
                }
                return new Recommendation("HIT", "Hit pair of Aces against dealer Ace.");
            }
            if (cardVal == 8) {
                return new Recommendation("SPLIT", "Always split 8s.");
            }
            if (cardVal == 9) {
                if (dealerValue >= 2 && dealerValue <= 6) {
                    return new Recommendation("SPLIT", "Split 9s against weak dealer cards.");
                }
                if (dealerValue == 8 || dealerValue == 9) {
                    return new Recommendation("SPLIT", "Split 9s on 8 or 9.");
                }
                return new Recommendation("STAND", "Stand on pair of 9s against 7, 10, or Ace.");
            }
            if (cardVal == 7) {
                if (dealerValue >= 2 && dealerValue <= 7) {
                    return new Recommendation("SPLIT", "Split 7s against dealer 2-7.");
                }
                return new Recommendation("HIT", "Hit pair of 7s otherwise.");
            }
            if (cardVal == 6) {
                if (dealerValue >= 2 && dealerValue <= 6) {
                    return new Recommendation("SPLIT", "Split 6s against dealer 2-6.");
                }
                return new Recommendation("HIT", "Hit pair of 6s otherwise.");
            }
            if (cardVal == 5) {
                return new Recommendation("DOUBLE DOWN", "Double on pair of 5s (equals 10).");
            }
            if (cardVal == 4) {
                if (dealerValue == 5 || dealerValue == 6) {
                    return new Recommendation("SPLIT", "Split 4s against 5-6 only.");
                }
                return new Recommendation("HIT", "Hit pair of 4s otherwise.");
            }
            if (cardVal == 3 || cardVal == 2) {
                if (dealerValue >= 2 && dealerValue <= 7) {
                    return new Recommendation("SPLIT", "Split " + cardVal + "s against dealer 2-7.");
                }
                return new Recommendation("HIT", "Hit pair of " + cardVal + "s otherwise.");
            }
            if (cardVal == 10) {
                return new Recommendation("STAND", "Never split 10s.");
            }
        }

        // Soft hands (with Ace)
        if (playerHasAce) {
            if (playerValue == 21) {
                return new Recommendation("STAND", "You have Blackjack! Stand and win.");
            }
            if (playerValue == 20) {
                return new Recommendation("STAND", "Soft 20 is excellent - always stand.");
            }
            if (playerValue == 19) {
                return new Recommendation("STAND", "Soft 19 is strong - always stand.");
            }
            if (playerValue == 18) {
                if (dealerValue >= 9) {
                    return new Recommendation("HIT", "Hit Soft 18 against dealer 9, 10, or Ace.");
                }
                if (dealerValue >= 3 && dealerValue <= 6) {
                    return new Recommendation("DOUBLE DOWN", "Double Soft 18 against dealer 3-6.");
                }
                return new Recommendation("STAND", "Stand on Soft 18 otherwise.");
            }
            if (playerValue == 17) {
                if (dealerValue >= 3 && dealerValue <= 6) {
                    return new Recommendation("DOUBLE DOWN", "Double Soft 17 against dealer 3-6.");
                }
                return new Recommendation("HIT", "Hit Soft 17 otherwise - always improve.");
            }
            return new Recommendation("HIT", "Always hit on Soft 17 or less.");
        }

        // Hard hands (no Ace)
        if (playerValue == 21) {
            return new Recommendation("STAND", "You have Blackjack! Stand and win.");
        }
        if (playerValue >= 17) {
            return new Recommendation("STAND", "Stand on Hard 17 or higher.");
        }
        
        if (playerValue == 16) {
            if (dealerValue >= 7) {
                return new Recommendation("HIT", "Hit Hard 16 against dealer 7 or higher (dealer likely strong).");
            }
            return new Recommendation("STAND", "Stand on Hard 16 against dealer 2-6 (dealer likely busts).");
        }
        if (playerValue == 15) {
            if (dealerValue >= 7) {
                return new Recommendation("HIT", "Hit Hard 15 against dealer 7 or higher.");
            }
            return new Recommendation("STAND", "Stand on Hard 15 against dealer 2-6.");
        }
        if (playerValue == 13 || playerValue == 14) {
            if (dealerValue >= 7) {
                return new Recommendation("HIT", "Hit Hard " + playerValue + " against dealer 7 or higher.");
            }
            return new Recommendation("STAND", "Stand on Hard " + playerValue + " against dealer 2-6.");
        }
        if (playerValue == 12) {
            if (dealerValue >= 4 && dealerValue <= 6) {
                return new Recommendation("STAND", "Stand on Hard 12 against dealer 4-6.");
            }
            return new Recommendation("HIT", "Hit Hard 12 against dealer 2-3 or 7 and up.");
        }

        // 11 or less - always hit (or double down on 11)
        if (playerValue == 11) {
            return new Recommendation("DOUBLE DOWN", "Double on Hard 11 - best offensive move.");
        }
        if (playerValue == 10) {
            if (dealerValue <= 9) {
                return new Recommendation("DOUBLE DOWN", "Double on Hard 10 against dealer 2-9.");
            }
            return new Recommendation("HIT", "Hit Hard 10 against dealer 10 or Ace.");
        }

        return new Recommendation("HIT", "Always hit on 9 or less.");
    }

    public Statistics calculateStatistics(String[] playerCards, String dealerCard, boolean canSplit) {
        // Build deck counts (4 of each rank) and remove known cards (player cards + dealer upcard)
        java.util.Map<String, Integer> counts = buildDeckCounts();
        for (String c : playerCards) removeCardFromCounts(counts, c);
        removeCardFromCounts(counts, dealerCard);

        int playerValue = calculateHandValue(playerCards);
        boolean playerHasAce = hasAce(playerCards);

        // Quick blackjack checks
        boolean isPlayerBlackjack = playerValue == 21 && playerHasAce;
        int dealerUpValue = cardValue(dealerCard);
        boolean dealerCanHaveBlackjack = dealerUpValue == 11 || dealerUpValue == 10;
        if (isPlayerBlackjack && !dealerCanHaveBlackjack) {
            return new Statistics(100, 100, 100, 100, 1.5, 1.5, 1.5, 1.5);
        }
        if (isPlayerBlackjack && dealerCanHaveBlackjack) {
            return new Statistics(95, 95, 95, 95, 1.3, 1.3, 1.3, 1.3);
        }

        // Stand: compute dealer final distribution and evaluate outcomes
        java.util.Map<Integer, Double> dealerDistStand = computeDealerDistribution(counts, dealerCard);
        double standWins = 0.0;
        for (java.util.Map.Entry<Integer, Double> e : dealerDistStand.entrySet()) {
            int d = e.getKey();
            double p = e.getValue();
            if (d == 22) { // dealer bust
                standWins += p;
            } else if (playerValue > d) {
                standWins += p;
            }
        }

        // Hit: player draws one card then stands
        double hitWins = 0.0;
        int remaining = totalRemaining(counts);
        if (remaining > 0) {
            for (String rank : new java.util.ArrayList<>(counts.keySet())) {
                int cnt = counts.get(rank);
                if (cnt <= 0) continue;
                double pDraw = (double) cnt / remaining;
                // simulate player draw
                String[] afterDraw = appendCard(playerCards, rank);
                int newPlayerVal = calculateHandValue(afterDraw);
                if (newPlayerVal > 21) {
                    // bust -> lose
                    continue;
                }
                // build counts after drawing this card
                java.util.Map<String,Integer> countsAfter = copyCounts(counts);
                removeCardFromCounts(countsAfter, rank);
                java.util.Map<Integer, Double> dealerDistAfter = computeDealerDistribution(countsAfter, dealerCard);
                double win = 0.0;
                for (java.util.Map.Entry<Integer, Double> e : dealerDistAfter.entrySet()) {
                    int d = e.getKey();
                    double p = e.getValue();
                    if (d == 22) win += p;
                    else if (newPlayerVal > d) win += p;
                }
                hitWins += pDraw * win;
            }
        }

        // Double: same single-card draw outcome probabilities as Hit
        double doubleWins = hitWins;

        double splitWins = 0.0;
        if (canSplit) splitWins = 0.0; // not implemented for exact

        double standEV = 2.0 * standWins - 1.0;
        double hitEV = 2.0 * hitWins - 1.0;
        double doubleEV = 4.0 * doubleWins - 2.0;
        double splitEV = 0.0;

        return new Statistics(standWins * 100, hitWins * 100, doubleWins * 100, splitWins * 100,
                standEV, hitEV, doubleEV, splitEV);
    }

    private java.util.Map<String,Integer> buildDeckCounts() {
        java.util.Map<String,Integer> counts = new java.util.LinkedHashMap<>();
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        for (String r : ranks) counts.put(r, 4);
        return counts;
    }

    private void removeCardFromCounts(java.util.Map<String,Integer> counts, String card) {
        if (card == null) return;
        String c = card.toUpperCase().trim();
        if (!counts.containsKey(c)) return;
        counts.put(c, Math.max(0, counts.get(c) - 1));
    }

    private java.util.Map<String,Integer> copyCounts(java.util.Map<String,Integer> src) {
        java.util.Map<String,Integer> copy = new java.util.LinkedHashMap<>();
        for (java.util.Map.Entry<String,Integer> e : src.entrySet()) copy.put(e.getKey(), e.getValue());
        return copy;
    }

    private int totalRemaining(java.util.Map<String,Integer> counts) {
        int s = 0;
        for (int v : counts.values()) s += v;
        return s;
    }

    private String[] appendCard(String[] cards, String add) {
        String[] out = new String[cards.length + 1];
        System.arraycopy(cards, 0, out, 0, cards.length);
        out[cards.length] = add;
        return out;
    }

    private java.util.Map<Integer, Double> computeDealerDistribution(java.util.Map<String,Integer> countsInput, String dealerUp) {
        java.util.Map<Integer, Double> out = new java.util.HashMap<>();
        java.util.Map<String,Integer> counts = copyCounts(countsInput);
        int remaining = totalRemaining(counts);
        if (remaining <= 0) return out;

        // dealer hole card is drawn from remaining counts
        for (String rank : new java.util.ArrayList<>(counts.keySet())) {
            int cnt = counts.get(rank);
            if (cnt <= 0) continue;
            double pHole = (double) cnt / remaining;
            counts.put(rank, cnt - 1);
            java.util.List<String> hand = new java.util.ArrayList<>();
            hand.add(dealerUp);
            hand.add(rank);
            dealerRec(hand, counts, totalRemaining(counts), pHole, out);
            counts.put(rank, cnt);
        }

        return out;
    }

    private void dealerRec(java.util.List<String> hand, java.util.Map<String,Integer> counts, int remaining, double prob, java.util.Map<Integer, Double> out) {
        String[] hs = hand.toArray(new String[0]);
        int val = calculateHandValue(hs);
        if (val >= 17) {
            int key = val > 21 ? 22 : val;
            out.put(key, out.getOrDefault(key, 0.0) + prob);
            return;
        }

        if (remaining <= 0) {
            // no more cards, record current total
            int key = val > 21 ? 22 : val;
            out.put(key, out.getOrDefault(key, 0.0) + prob);
            return;
        }

        for (String rank : new java.util.ArrayList<>(counts.keySet())) {
            int cnt = counts.get(rank);
            if (cnt <= 0) continue;
            double p = prob * ((double) cnt / remaining);
            counts.put(rank, cnt - 1);
            hand.add(rank);
            dealerRec(hand, counts, remaining - 1, p, out);
            hand.remove(hand.size() - 1);
            counts.put(rank, cnt);
        }
    }
}
