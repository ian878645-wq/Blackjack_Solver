public class BlackjackStrategy {
    
    public static class Recommendation {
        public String action;
        public String reason;
        
        public Recommendation(String action, String reason) {
            this.action = action;
            this.reason = reason;
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
}
