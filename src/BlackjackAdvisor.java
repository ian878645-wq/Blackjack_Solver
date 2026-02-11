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
    private JLabel handValueLabel;
    private JLabel dealerValueLabel;
    private JLabel actionLabel;
    private JTextArea reasonTextArea;
    private JPanel resultPanel;
    private JPanel playerCardsPanel;
    private List<JTextField> cardFields;
    private JButton addCardButton;
    private JButton resetButton;

    public BlackjackAdvisor() {
        setTitle("Blackjack Strategy Advisor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        cardFields = new ArrayList<>();

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(15, 71, 43));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Blackjack Strategy Advisor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(Box.createVerticalStrut(15));
        titlePanel.add(titleLabel);
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

        JScrollPane cardsScrollPane = new JScrollPane(playerCardsPanel);
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
            public void insertUpdate(DocumentEvent e) { updateRecommendation(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateRecommendation(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateRecommendation(); }
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
        resultPanel.add(Box.createVerticalStrut(15));

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
        
        // Reason section
        reasonTextArea = new JTextArea(2, 40);
        reasonTextArea.setEditable(false);
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setWrapStyleWord(true);
        reasonTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        reasonTextArea.setBackground(new Color(255, 248, 225));
        reasonTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        resultPanel.add(reasonTextArea);

        // Scroll pane for result
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBorder(null);

        // Main layout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(titlePanel);
        topPanel.add(inputPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Initialize with 2 card fields
        addCardField();
        addCardField();

        setVisible(true);
    }

    private void addCardField() {
        JTextField cardField = new JTextField(3);
        cardField.setFont(new Font("Arial", Font.PLAIN, 14));
        cardField.setMaximumSize(new Dimension(50, 40));
        cardField.setPreferredSize(new Dimension(50, 40));
        
        cardField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateRecommendation(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateRecommendation(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateRecommendation(); }
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
        
        updateRecommendation();
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

            // Check for bust
            if (playerValue > 21) {
                handValueLabel.setText("BUST! (" + playerValue + ")");
                dealerValueLabel.setText(dealerCardInput.toUpperCase());
                actionLabel.setText("BUST!");
                actionLabel.setForeground(new Color(200, 50, 50));
                reasonTextArea.setText("You busted! Your hand value exceeds 21 and you lose.");
                resultPanel.setVisible(true);
                addCardButton.setEnabled(false);
                
                // Reset after 3 seconds
                Timer timer = new Timer(3000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actionLabel.setForeground(new Color(15, 71, 43));
                        resetGame();
                    }
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }

            String handName = strategy.getHandName(playerValue, hasAce);
            BlackjackStrategy.Recommendation rec = strategy.getRecommendation(playerValue, hasAce, dealerCardInput, false);

            handValueLabel.setText(handName);
            dealerValueLabel.setText(dealerCardInput.toUpperCase());
            actionLabel.setText(rec.action);
            actionLabel.setForeground(new Color(15, 71, 43));
            reasonTextArea.setText(rec.reason);

            resultPanel.setVisible(true);
            addCardButton.setEnabled(true);
        } catch (Exception e) {
            reasonTextArea.setText("Error: " + e.getMessage());
            resultPanel.setVisible(true);
            addCardButton.setEnabled(false);
        }
    } 

    private void resetGame() {
        cardFields.clear();
        playerCardsPanel.removeAll();
        dealerCardField.setText("");
        resultPanel.setVisible(false);
        addCardButton.setEnabled(false);
        
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlackjackAdvisor();
            }
        });
    }
}
