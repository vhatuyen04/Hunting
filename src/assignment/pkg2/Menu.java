/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author hatuyen
 * This class extends from JFrame, it is the menu of the game.
 */

public class Menu extends JFrame {
    private JFrame gameFrame;
    private JFrame helpFrame;
    private JTextArea helpText;
    private JButton startButton;
    private JButton helpButton;
    private JButton exitButton;
    BoardGame boardGame; 
    
    public Menu() {
        setTitle ("Game Menu");
        setSize (400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        startButton = new JButton ("Start");
        helpButton = new JButton ("Help");
        exitButton = new JButton ("Exit");

        startButton.addActionListener(e -> showGameModeSelection());
        helpButton.addActionListener(e -> showHelpDialog());
        exitButton.addActionListener(e -> System.exit(0));

        add(startButton);
        add(helpButton);
        add(exitButton);

        setVisible(true);
    }
        
    private void showGameModeSelection() {
        String[] choices = {"3x3", "5x5", "7x7"};
        int index = JOptionPane.showOptionDialog( 
                this, 
                "Choose the size of the Board:", 
                "Game Mode", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, 
                choices, 
                null);
        if (index >= 0) {
            int boardSize = (index+1)*2+1;
            startGame(boardSize);
        }
        else {
            System.exit(0);
        }
    }
    
    private void showHelpDialog() {
        helpFrame = new JFrame("Help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(400, 300);

        helpText = new JTextArea(
            "Game Rule:\n"+
            "There are 2 players: Player 1 has 4 buttons in 4 corners, player 2 has 1 button in the middle of the board.\n"+
            "For each turn, each player can move their button by using key arrow to move accordingly 1 unit in the grid.\n"+
            "The turn is cancelled if the button is used to move outside or the space is replaced by another button. Then the button is called trapped at that direction.\n"+
            "In player 1's turn, this player needs to choose the button by using key arrow and confirm the chosen button by pressing Enter first.\n"+
            "Player 1 wins if their buttons can trap the player 2 button in all direction within 4*n steps of its turn. Otherwise player 2 wins."
        );
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);

        helpFrame.add(new JScrollPane(helpText));
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setVisible(true);
    }
    
    private void startGame(int boardSize) {
        SwingUtilities.invokeLater(() -> {
            gameFrame = new JFrame("Hunting Game");
            boardGame = new BoardGame(boardSize);
            gameFrame.add(boardGame);
            gameFrame.pack();
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
            gameFrame.setResizable(false);

            Timer timer = new Timer (50, (ActionEvent e) -> {
                if (boardGame.winner() || boardGame.stepCount >= 4*boardSize) {
                    boardGame.announceWinner();
                    ((Timer) e.getSource()).stop();
                    gameFrame.dispose();
                    showGameModeSelection();
                }
            });

            timer.start();
            dispose();
        });
    }
}
