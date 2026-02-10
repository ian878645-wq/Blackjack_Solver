import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackjackAdvisor extends JFrame {
    private JTextField dealerCardField;
    private JCheckBox canSplitCheckBox;
    private JLabel handValueLabel;
    private JLabel dealerValueLabel;
    private JLabel actionLabel;
    private JTextArea reasonTextArea;
    private JPanel resultPanel;
    private JPanel playerCardsPanel;
    private List<JTextField> cardFields;
    private JPanel cardCountPanel;
    private JButton addCardButton;
    private JButton resetButton;
    private JScrollPane cardsScrollPane;
    private JPanel statsPanel;
    private JLabel hitStatsLabel;
    private JLabel standStatsLabel;
    private JLabel doubleStatsLabel;
    private JLabel splitStatsLabel;
    private int cardCount = 0;
    private JTextArea cCRecommendationTextArea;

    public BlackjackAdvisor() {
        setTitle("Blackjack Strategy Advisor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 900);
        setLocationRelativeTo(null);
        setResizable(true);

        cardFields = new ArrayList<>();

        // Main panel with color
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(15, 71, 43));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("♠ Blackjack Strategy Advisor ♠");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Get the optimal play for every hand");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitleLabel);
        titlePanel.add(Box.createVerticalStrut(15));

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(248, 249, 250));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Player cards section
        JLabel playerCardsLabel = new JLabel("Your Cards:");
        playerCardsLabel.setFont(new Font("Arial", Font.BOLD, 12));
        inputPanel.add(playerCardsLabel);
        inputPanel.add(Box.createVerticalStrut(4));

        // Scrollable player cards panel
        playerCardsPanel = new JPanel();
        playerCardsPanel.setBackground(new Color(248, 249, 250));
        playerCardsPanel.setLayout(new BoxLayout(playerCardsPanel, BoxLayout.X_AXIS));

        cardsScrollPane = new JScrollPane(playerCardsPanel);
        cardsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cardsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        cardsScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        cardsScrollPane.setBackground(new Color(248, 249, 250));
        
        inputPanel.add(cardsScrollPane);
        inputPanel.add(Box.createVerticalStrut(8));

        // Dealer card input
        JLabel dealerCardLabel = new JLabel("Dealer's Upcard:");
        dealerCardLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dealerCardField = new JTextField(3);
        dealerCardField.setFont(new Font("Arial", Font.PLAIN, 14));
        dealerCardField.setMaximumSize(new Dimension(60, 40));
        
        dealerCardField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRecommendation();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRecommendation();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRecommendation();
            }
        });
        
        JPanel dealerPanel = new JPanel();
        dealerPanel.setBackground(new Color(248, 249, 250));
        dealerPanel.setLayout(new BoxLayout(dealerPanel, BoxLayout.X_AXIS));
        dealerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        dealerPanel.add(dealerCardField);
        dealerPanel.add(Box.createHorizontalGlue());
        
        inputPanel.add(dealerCardLabel);
        inputPanel.add(Box.createVerticalStrut(3));
        inputPanel.add(dealerPanel);
        inputPanel.add(Box.createVerticalStrut(8));

        //Card count input panel
        cardCountPanel = new JPanel();
        cardCountPanel.setBackground(new Color(248, 249, 250));
        cardCountPanel.setLayout(new BoxLayout(cardCountPanel, BoxLayout.X_AXIS));
        cardCountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        //cardCountPanel.add(cardCountField);
        cardCountPanel.add(Box.createHorizontalGlue());

        // Split checkbox
        canSplitCheckBox = new JCheckBox("I can split (I have a pair)");
        canSplitCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        canSplitCheckBox.setBackground(new Color(248, 249, 250));
        canSplitCheckBox.setEnabled(false);
        canSplitCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRecommendation();
            }
        });
        inputPanel.add(canSplitCheckBox);
        inputPanel.add(Box.createVerticalStrut(8));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        addCardButton = new JButton("Hit (Add Card)");
        addCardButton.setFont(new Font("Arial", Font.BOLD, 12));
        addCardButton.setBackground(new Color(100, 150, 100));
        addCardButton.setForeground(Color.WHITE);
        addCardButton.setFocusPainted(false);
        addCardButton.setEnabled(false);
        addCardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCardField();
            }
        });

        resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setBackground(new Color(200, 100, 100));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        JButton dealRandomButton = new JButton("Deal Random Hand");
        dealRandomButton.setFont(new Font("Arial", Font.BOLD, 12));
        dealRandomButton.setBackground(new Color(70, 130, 180));
        dealRandomButton.setForeground(Color.WHITE);
        dealRandomButton.setFocusPainted(false);
        dealRandomButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dealRandomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealRandomHand();
            }
        });

        buttonPanel.add(addCardButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(resetButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(dealRandomButton);
        buttonPanel.add(Box.createHorizontalGlue());

        inputPanel.add(buttonPanel);

        // Card count / info panel (space between input and output)
        cardCountPanel = new JPanel();
        cardCountPanel.setBackground(new Color(248, 249, 250));
        cardCountPanel.setLayout(new BoxLayout(cardCountPanel, BoxLayout.X_AXIS));
        cardCountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        // Initialize card count input before the info area so it displays first
        addCountField();

        cCRecommendationTextArea = new JTextArea(3, 40);
        cCRecommendationTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        cCRecommendationTextArea.setLineWrap(true);
        cCRecommendationTextArea.setWrapStyleWord(true);
        cCRecommendationTextArea.setEditable(false);
        cCRecommendationTextArea.setBackground(new Color(255, 248, 225));
        JScrollPane cCScrollPane = new JScrollPane(cCRecommendationTextArea);
        cCScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cCScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        cCScrollPane.setBorder(BorderFactory.createTitledBorder("Info"));

        cardCountPanel.add(Box.createHorizontalStrut(10));
        cardCountPanel.add(cCScrollPane);
        inputPanel.add(cardCountPanel);
        inputPanel.add(Box.createVerticalStrut(8));

        // Result panel
        resultPanel = new JPanel();
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultPanel.setVisible(false);

        // Hand info panel
        JPanel handInfoPanel = new JPanel();
        handInfoPanel.setBackground(new Color(240, 247, 255));
        handInfoPanel.setLayout(new BoxLayout(handInfoPanel, BoxLayout.Y_AXIS));
        handInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel handLabel = new JLabel("Your hand:");
        handLabel.setFont(new Font("Arial", Font.BOLD, 12));
        handValueLabel = new JLabel("");
        handValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel dealerLabel = new JLabel("Dealer shows:");
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dealerValueLabel = new JLabel("");
        dealerValueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        handInfoPanel.add(handLabel);
        handInfoPanel.add(handValueLabel);
        handInfoPanel.add(Box.createVerticalStrut(8));
        handInfoPanel.add(dealerLabel);
        handInfoPanel.add(dealerValueLabel);
        
        resultPanel.add(handInfoPanel);
        resultPanel.add(Box.createVerticalStrut(20));

        // Recommendation section
        JLabel recommendationHeader = new JLabel("Recommended Action:");
        recommendationHeader.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(recommendationHeader);
        resultPanel.add(Box.createVerticalStrut(5));

        actionLabel = new JLabel("");
        actionLabel.setFont(new Font("Arial", Font.BOLD, 28));
        actionLabel.setForeground(new Color(15, 71, 43));
        actionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(actionLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        
        // Card count/info area is created below and placed between input and output panels.
        // Reason section
        reasonTextArea = new JTextArea(2, 40);
        reasonTextArea.setEditable(false);
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setWrapStyleWord(true);
        reasonTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        reasonTextArea.setBackground(new Color(255, 248, 225));
        reasonTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        resultPanel.add(reasonTextArea);
        resultPanel.add(Box.createVerticalStrut(20));

        // Statistics section
        JLabel statsHeader = new JLabel("Action Odds:");
        statsHeader.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(statsHeader);
        resultPanel.add(Box.createVerticalStrut(10));

        statsPanel = new JPanel();
        statsPanel.setBackground(new Color(240, 255, 240));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        hitStatsLabel = new JLabel("HIT: Win 50%");
        hitStatsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        standStatsLabel = new JLabel("STAND: Win 45%");
        standStatsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        doubleStatsLabel = new JLabel("DOUBLE: Win 50%");
        doubleStatsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        splitStatsLabel = new JLabel("SPLIT: Win 48%");
        splitStatsLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        statsPanel.add(hitStatsLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(standStatsLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(doubleStatsLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(splitStatsLabel);

        resultPanel.add(statsPanel);

        // Scroll pane for result
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(null);

        // Main layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titlePanel);
        topPanel.add(inputPanel);
        topPanel.add(cardCountPanel);
        topPanel.add(Box.createVerticalStrut(10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Initialize with 2 card fields after panels exist
        addCardField();
        addCardField();

        setVisible(true);
    }

    private void addCountField() {
        JLabel cardCountLabel = new JLabel("Please Enter the Card Count:");
        cardCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JTextField cardCountField = new JTextField(2);
        cardCountField.setFont(new Font("Arial", Font.PLAIN, 14));
        cardCountField.setMaximumSize(new Dimension(50, 40));
        cardCountField.setPreferredSize(new Dimension(50, 40));
        cardCountField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                String countText = cardCountField.getText().trim();
                if (countText.isEmpty()) {
                    cardCount = 0;
                } else {
                    try {
                        cardCount = Integer.parseInt(countText);
                    } catch (NumberFormatException ex) {
                        // Invalid input -> treat as 0 temporarily
                        cardCount = 0;
                    }
                }
                updateRecommendation();
            }

            @Override
            public void insertUpdate(DocumentEvent e) { update(); }

            @Override
            public void removeUpdate(DocumentEvent e) { update(); }

            @Override
            public void changedUpdate(DocumentEvent e) { update(); }
        });
            
        cardCountPanel.add(cardCountLabel);
        cardCountPanel.add(Box.createHorizontalStrut(10));
        cardCountPanel.add(cardCountField);
        cardCountPanel.add(Box.createHorizontalGlue());
    }

    private void addCardField() {
        JTextField cardField = new JTextField(3);
        cardField.setFont(new Font("Arial", Font.PLAIN, 14));
        cardField.setMaximumSize(new Dimension(50, 40));
        cardField.setPreferredSize(new Dimension(50, 40));
        
        cardField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCheckboxAndRecommendation();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCheckboxAndRecommendation();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCheckboxAndRecommendation();
            }
        });
        
        cardFields.add(cardField);

        playerCardsPanel.removeAll();
        for (int i = 0; i < cardFields.size(); i++) {
            if (i > 0) {
                JLabel plusLabel = new JLabel(" + ");
                plusLabel.setFont(new Font("Arial", Font.BOLD, 14));
                playerCardsPanel.add(plusLabel);
                playerCardsPanel.add(Box.createHorizontalStrut(5));
            }
            playerCardsPanel.add(cardFields.get(i));
        }
        playerCardsPanel.add(Box.createHorizontalGlue());

        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
        
        updateCheckboxAndRecommendation();
    }

    private void updateCheckboxAndRecommendation() {
        updateCheckboxAvailability();
        updateRecommendation();
    }

    private void updateCheckboxAvailability() {
        if (cardFields.size() >= 2) {
            String card1 = cardFields.get(0).getText().trim();
            String card2 = cardFields.get(1).getText().trim();
            boolean isPair = isPair(card1, card2);
            canSplitCheckBox.setEnabled(isPair && cardFields.size() == 2);
            if (!isPair) {
                canSplitCheckBox.setSelected(false);
            }
        } else {
            canSplitCheckBox.setEnabled(false);
            canSplitCheckBox.setSelected(false);
        }
    }

    private boolean isPair(String card1, String card2) {
        if (card1.isEmpty() || card2.isEmpty()) {
            return false;
        }
        
        try {
            BlackjackStrategy strategy = new BlackjackStrategy();
            int card1Value = strategy.cardValue(card1);
            int card2Value = strategy.cardValue(card2);
            
            return card1Value == card2Value;
        } catch (Exception e) {
            return false;
        }
    }

    private void updateRecommendation() {
        String dealerCardInput = dealerCardField.getText().trim();

        if (cardFields.size() < 2 || dealerCardInput.isEmpty()) {
            resultPanel.setVisible(false);
            addCardButton.setEnabled(false);
            return;
        }

        try {
            List<String> cards = new ArrayList<>();
            for (JTextField field : cardFields) {
                String card = field.getText().trim();
                if (!card.isEmpty()) {
                    cards.add(card);
                }
            }
            
            if (cards.size() < 2) {
                resultPanel.setVisible(false);
                addCardButton.setEnabled(false);
                return;
            }

            String[] cardStrings = cards.toArray(new String[0]);

            BlackjackStrategy strategy = new BlackjackStrategy();
            int playerValue = strategy.calculateHandValue(cardStrings);
            boolean hasAce = strategy.hasAce(cardStrings);
            boolean canSplit = canSplitCheckBox.isSelected();

            //Card Counting logic
            String countingAdvice;
            if (cardCount > 0) {
                countingAdvice = "The count is positive (" + cardCount + "). This suggests a higher proportion of high cards remain in the deck, which is favorable for the player. You might want to be more aggressive with your plays.";
            } else if (cardCount < 0) {
                countingAdvice = "The count is negative (" + cardCount + "). This indicates a higher proportion of low cards remain in the deck, which is unfavorable for the player. You might want to be more conservative with your plays.";
            } else {
                countingAdvice = "The count is neutral (0). This suggests an average distribution of cards in the deck. Follow standard basic strategy recommendations.";
            }
            if (cCRecommendationTextArea != null) {
                cCRecommendationTextArea.setText(countingAdvice);
            } else {
                // Fallback: append counting advice to reason area when card-count panel is disabled
                reasonTextArea.setText(countingAdvice + "\n\n" + reasonTextArea.getText());
            }

            // Check for bust
            if (playerValue > 21) {
                handValueLabel.setText("BUST! (" + playerValue + ")");
                dealerValueLabel.setText(dealerCardInput.toUpperCase());
                actionLabel.setText("BUST!");
                actionLabel.setForeground(new Color(200, 50, 50));
                reasonTextArea.setText("You have busted with a hand value of " + playerValue + ". Your hand exceeds 21 and you lose.\n\nThe game will reset in 3 seconds...");
                resultPanel.setVisible(true);
                addCardButton.setEnabled(false);
                
                // Reset after 3 seconds
                Timer timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actionLabel.setForeground(new Color(15, 71, 43)); // Reset color
                        resetGame();
                    }
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }

            String handName = strategy.getHandName(playerValue, hasAce);
            BlackjackStrategy.Recommendation rec = strategy.getRecommendation(playerValue, hasAce, dealerCardInput, canSplit);

            handValueLabel.setText(handName);
            dealerValueLabel.setText(dealerCardInput.toUpperCase());
            actionLabel.setText(rec.action);
            actionLabel.setForeground(new Color(15, 71, 43)); // Reset color to default
            reasonTextArea.setText(rec.reason);

            // Calculate and display statistics
            int dealerValue = strategy.cardValue(dealerCardInput);
            BlackjackStrategy.Statistics stats = strategy.calculateStatistics(playerValue, hasAce, dealerValue, canSplit && cardStrings.length == 2);
            
            hitStatsLabel.setText(String.format("■ HIT: Win %.0f%% | EV: %+.2f", stats.hitWinPercent, stats.hitEV));
            hitStatsLabel.setForeground(getEVColor(stats.hitEV));
            standStatsLabel.setText(String.format("■ STAND: Win %.0f%% | EV: %+.2f", stats.standWinPercent, stats.standEV));
            standStatsLabel.setForeground(getEVColor(stats.standEV));
            if (rec != null && "DOUBLE DOWN".equalsIgnoreCase(rec.action)) {
                doubleStatsLabel.setText(String.format("■ DOUBLE: Win %.0f%% | EV: %+.2f", stats.doubleDownWinPercent, stats.doubleDownEV));
                doubleStatsLabel.setForeground(getEVColor(stats.doubleDownEV));
            } else {
                doubleStatsLabel.setText("DOUBLE: do not Double Down");
                doubleStatsLabel.setForeground(Color.GRAY);
            }
            if (canSplit && cardStrings.length == 2) {
                splitStatsLabel.setText(String.format("■ SPLIT: Win %.0f%% | EV: %+.2f", stats.splitWinPercent, stats.splitEV));
                splitStatsLabel.setForeground(getEVColor(stats.splitEV));
            } else {
                splitStatsLabel.setText("SPLIT: Not available");
                splitStatsLabel.setForeground(Color.GRAY);
            }

            resultPanel.setVisible(true);
            addCardButton.setEnabled(true);
        } catch (Exception e) {
            // Log the exception for debugging and show a brief message in the UI
            e.printStackTrace();
            reasonTextArea.setText("Error computing recommendation: " + e.toString());
            resultPanel.setVisible(true);
            addCardButton.setEnabled(false);
        }
    }

    private void resetGame() {
        cardFields.clear();
        playerCardsPanel.removeAll();
        dealerCardField.setText("");
        canSplitCheckBox.setSelected(false);
        canSplitCheckBox.setEnabled(false);
        resultPanel.setVisible(false);
        addCardButton.setEnabled(false);
        
        // Reinitialize with 2 card fields
        addCardField();
        addCardField();
        
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    private void dealRandomHand() {
        resetGame();
        
        String[] cardValues = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        Random random = new Random();
        
        // Generate random player cards
        String card1 = cardValues[random.nextInt(cardValues.length)];
        String card2 = cardValues[random.nextInt(cardValues.length)];
        
        // Generate random dealer card
        String dealerCard = cardValues[random.nextInt(cardValues.length)];
        
        // Set the card fields
        cardFields.get(0).setText(card1);
        cardFields.get(1).setText(card2);
        dealerCardField.setText(dealerCard);
    }

    private Color getEVColor(double ev) {
        if (ev > 0.05) {
            return new Color(34, 139, 34); // Dark green
        } else if (ev < -0.05) {
            return new Color(178, 34, 34); // Firebrick red
        } else {
            return new Color(184, 134, 11); // Dark goldenrod (neutral)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BlackjackAdvisor advisor = new BlackjackAdvisor();

                // Debug helper: if JVM property autoDeal=true, programmatically deal a random hand
                // and print the displayed stats to console to aid debugging.
                String autoDeal = System.getProperty("autoDeal");
                if ("true".equalsIgnoreCase(autoDeal)) {
                    try {
                        java.lang.reflect.Method m = BlackjackAdvisor.class.getDeclaredMethod("dealRandomHand");
                        m.setAccessible(true);
                        m.invoke(advisor);

                        // allow UI to update
                        Thread.sleep(300);

                        java.lang.reflect.Field hitField = BlackjackAdvisor.class.getDeclaredField("hitStatsLabel");
                        java.lang.reflect.Field standField = BlackjackAdvisor.class.getDeclaredField("standStatsLabel");
                        java.lang.reflect.Field doubleField = BlackjackAdvisor.class.getDeclaredField("doubleStatsLabel");
                        java.lang.reflect.Field splitField = BlackjackAdvisor.class.getDeclaredField("splitStatsLabel");
                        hitField.setAccessible(true);
                        standField.setAccessible(true);
                        doubleField.setAccessible(true);
                        splitField.setAccessible(true);

                        javax.swing.JLabel hit = (javax.swing.JLabel) hitField.get(advisor);
                        javax.swing.JLabel stand = (javax.swing.JLabel) standField.get(advisor);
                        javax.swing.JLabel dbl = (javax.swing.JLabel) doubleField.get(advisor);
                        javax.swing.JLabel split = (javax.swing.JLabel) splitField.get(advisor);

                        System.out.println("[DEBUG] " + hit.getText());
                        System.out.println("[DEBUG] " + stand.getText());
                        System.out.println("[DEBUG] " + dbl.getText());
                        System.out.println("[DEBUG] " + split.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
