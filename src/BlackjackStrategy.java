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

    public Statistics calculateStatistics(int playerValue, boolean playerHasAce, int dealerValue, boolean canSplit) {
        // Check if player has blackjack (21 with ace)
        boolean isPlayerBlackjack = playerValue == 21 && playerHasAce;
        
        // Check if dealer can have blackjack (showing Ace or 10-value card)
        boolean dealerCanHaveBlackjack = dealerValue == 11 || dealerValue == 10;
        
        // If player has blackjack and dealer cannot have blackjack, player wins 100%
        if (isPlayerBlackjack && !dealerCanHaveBlackjack) {
            return new Statistics(100, 100, 100, 100, 1.5, 1.5, 1.5, 1.5);
        }
        
        // If player has blackjack and dealer can have blackjack, it's a push or player wins
        if (isPlayerBlackjack && dealerCanHaveBlackjack) {
            return new Statistics(95, 95, 95, 95, 1.3, 1.3, 1.3, 1.3);
        }

        // Use Monte Carlo simulation to calculate accurate win percentages
        int numSimulations = 10000;
        double standWinPercent = monteCarloWinPercent(playerValue, playerHasAce, dealerValue, false, false, numSimulations);
        double hitWinPercent = monteCarloWinPercent(playerValue, playerHasAce, dealerValue, true, false, numSimulations);
        double doubleDownWinPercent = monteCarloWinPercent(playerValue, playerHasAce, dealerValue, true, true, numSimulations);
        double splitWinPercent = canSplit ? monteCarloWinPercent(playerValue, playerHasAce, dealerValue, true, false, numSimulations) * 0.95 : 0;
        
        // Calculate EV: profit = (win% * 1.0) - (loss% * 1.0) = 2*win% - 1
        // For double-down: profit = (win% * 2.0) - (loss% * 2.0) = 4*win% - 2
        double standEV = 2.0 * standWinPercent - 1.0;
        double hitEV = 2.0 * hitWinPercent - 1.0;
        double doubleDownEV = 4.0 * doubleDownWinPercent - 2.0;
        double splitEV = canSplit ? (4.0 * splitWinPercent - 2.0) : 0.0;
        
        return new Statistics(standWinPercent * 100, hitWinPercent * 100, doubleDownWinPercent * 100, splitWinPercent * 100,
                            standEV, hitEV, doubleDownEV, splitEV);
    }

    private double monteCarloWinPercent(int playerValue, boolean playerHasAce, int dealerValue, boolean playerHits, boolean playerDoubles, int numSimulations) {
        java.util.Random rand = new java.util.Random();
        int playerWins = 0;
        String[] deck = createStandardDeck();
        
        for (int i = 0; i < numSimulations; i++) {
            java.util.List<String> remainingDeck = new java.util.ArrayList<>();
            for (String card : deck) {
                remainingDeck.add(card);
            }
            
            // Remove dealer upcard
            String dealerCardStr = getDealerCardString(dealerValue);
            remainingDeck.remove(dealerCardStr);
            
            // Simulate player action
            int finalPlayerValue = playerValue;
            boolean finalPlayerHasAce = playerHasAce;
            
            if (playerHits) {
                String playerDrawn = drawRandomCard(remainingDeck, rand);
                // Approximate player's new total by adding drawn card value to known player total.
                finalPlayerValue = playerValue + cardValue(playerDrawn);
                // If player had an ace counted as 11 and now busts, reduce by 10 once
                if (finalPlayerHasAce && finalPlayerValue > 21) {
                    finalPlayerValue -= 10;
                    finalPlayerHasAce = false;
                }

                if (finalPlayerValue > 21) {
                    // Player busts, loses
                    continue;
                }
            }
            
            // Simulate dealer play
            int dealerFinalValue = dealerValue;
            while (dealerFinalValue < 17) {
                String dealerDrawn = drawRandomCard(remainingDeck, rand);
                dealerFinalValue += cardValue(dealerDrawn);
                if (dealerFinalValue > 21) break; // Dealer busts
            }
            
            // Determine outcome
            if (dealerFinalValue > 21) {
                playerWins++;
            } else if (finalPlayerValue > dealerFinalValue) {
                playerWins++;
            }
            // Push (tie) counts as no win
        }
        
        return (double) playerWins / numSimulations;
    }
    
    private String[] createStandardDeck() {
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] deck = new String[52];
        int idx = 0;
        for (int suit = 0; suit < 4; suit++) {
            for (String val : values) {
                deck[idx++] = val;
            }
        }
        return deck;
    }
    
    private String getDealerCardString(int dealerValue) {
        if (dealerValue == 11) return "A";
        if (dealerValue == 10) return "10";
        return String.valueOf(dealerValue);
    }
    
    private String drawRandomCard(java.util.List<String> deck, java.util.Random rand) {
        if (deck.isEmpty()) return "2"; // Fallback
        int idx = rand.nextInt(deck.size());
        return deck.remove(idx);
    }
}
