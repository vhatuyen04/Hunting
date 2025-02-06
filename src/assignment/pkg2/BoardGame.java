/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hatuyen
 * The board game, soul of the game is in here.
 */
public class BoardGame extends JPanel implements KeyListener {
    private int n;
    private List <Point> pl1; 
    private Point pl2;
    private boolean turn;
    private final int distance; 
    private JButton[] p1Buttons;
    private JButton p2Button;
    private int selectedButtonIndex;    
    private boolean chosenTurn1;
    public int stepCount;
    
    private JButton createButton (Color c, int x, int y) {
        JButton button = new JButton();
        button.setBackground(c);
        button.setBounds(x, y, 50, 50);
        button.setFocusable(false);
        return button;
    }
    
    public BoardGame (int n) {
        this.n = n;
        turn = true;
        distance = 50;
        setPreferredSize(new Dimension(n*this.distance, n*this.distance));
        setBackground(Color.WHITE);
        setLayout(null); 
        setFocusable(true);
        addKeyListener(this);
        pl2 = new Point ((n-1)/2, (n-1)/2);
        pl1 = new ArrayList<>();
        pl1.add(new Point(0, 0));
        pl1.add(new Point(0, n-1));
        pl1.add(new Point(n-1, 0));
        pl1.add(new Point(n-1, n-1));
        selectedButtonIndex = 0;
        p1Buttons = new JButton[4];
        chosenTurn1 = false;
        stepCount = 0;
        p1Buttons[0] = createButton (Color.RED, 0, 0);
        p1Buttons[1] = createButton (Color.RED, this.distance*(n-1), 0);
        p1Buttons[2] = createButton (Color.RED, 0, this.distance*(n-1));
        p1Buttons[3] = createButton (Color.RED, this.distance*(n-1), this.distance*(n-1));
        for (int i = 0; i < 4; i++) {
            add(p1Buttons[i]);
        }
        p2Button = createButton (Color.BLUE, this.distance*(n-1)/2, this.distance*(n-1)/2);
        add(p2Button);
        highlightSelectedButton();
    }
    
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int x = c*this.distance;
                int y = r*this.distance;
                g2d.drawRect(x, y, this.distance, this.distance);
            }
        }
    }
    
    private void neighborButton (int direction) {
        if (direction == 0 || direction == 2) { 
            selectedButtonIndex = (selectedButtonIndex+3)%4;
            return;
        }
        if (direction == 1 || direction == 3) {
            selectedButtonIndex = (selectedButtonIndex+1)%4;
        }
    }
        
    @Override
    public void keyPressed(KeyEvent e) {
        if (turn) {
            if (!chosenTurn1) {
                highlightSelectedButton();
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        neighborButton(2);  
                        highlightSelectedButton();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        neighborButton(3);
                        highlightSelectedButton();
                    }
                    case KeyEvent.VK_UP -> {
                        neighborButton(0);
                        highlightSelectedButton();
                    }
                    case KeyEvent.VK_DOWN -> {
                        neighborButton(1);
                        highlightSelectedButton();
                    }
                    case KeyEvent.VK_ENTER -> {
                        chosenTurn1 = true;
                        confirmedButton();
                    }
                }
            }
            else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        if (!checkAllButtonMovement(turn, 0)) {
                            moveButton(p1Buttons[selectedButtonIndex], 0, -distance);
                            pl1.get(selectedButtonIndex).setX(pl1.get(selectedButtonIndex).getX()-1);
                        }
                        break;
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (!checkAllButtonMovement(turn, 1)) {
                            moveButton(p1Buttons[selectedButtonIndex], 0, distance);
                            pl1.get(selectedButtonIndex).setX(pl1.get(selectedButtonIndex).getX()+1);
                        }
                        break;
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (!checkAllButtonMovement(turn, 2)) {
                            moveButton(p1Buttons[selectedButtonIndex], -distance, 0);
                            pl1.get(selectedButtonIndex).setY(pl1.get(selectedButtonIndex).getY()-1);
                        }
                        break;
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (!checkAllButtonMovement(turn, 3)) {
                            moveButton(p1Buttons[selectedButtonIndex], distance, 0);
                            pl1.get(selectedButtonIndex).setY(pl1.get(selectedButtonIndex).getY()+1);
                        }
                        break;
                    }
                }
                turn = false;
                delightSelectedButton1();
                selectedButtonIndex = 0;
                chosenTurn1 = false;
                stepCount++;
            }
        } 
        else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP -> {
                    if (!checkAllButtonMovement(turn, 0)) {
                        moveButton(p2Button, 0, -distance);
                        pl2.setX(Math.max(0, pl2.getX()-1));
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (!checkAllButtonMovement(turn, 1)) {
                        moveButton(p2Button, 0, distance);
                        pl2.setX(Math.min(n-1, pl2.getX()+1));
                    }
                }
                case KeyEvent.VK_LEFT -> {
                    if (!checkAllButtonMovement(turn, 2)) {
                        moveButton(p2Button, -distance, 0);
                        pl2.setY(Math.max(0, pl2.getY()-1));
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (!checkAllButtonMovement(turn, 3)) {
                        moveButton(p2Button, distance, 0);
                        pl2.setY(Math.min(n-1, pl2.getY()+1));
                    }
                }
            }
            turn = true;
            delightSelectedButton2();
        }
    }
    
    private boolean checkAllButtonMovement (boolean turn, int move) {
        if (turn) {
            switch (move) {
                case 0 -> {
                    if (pl1.get(selectedButtonIndex).getX() == 0) return true;
                    for (int i = 0; i < 4; i++) {
                        if (i == selectedButtonIndex) continue;
                        if (pl1.get(i).getX() == pl1.get(selectedButtonIndex).getX()-1 
                            && pl1.get(i).getY() == pl1.get(selectedButtonIndex).getY()) {
                            return true;
                        }
                    }
                    if (pl2.getX() == pl1.get(selectedButtonIndex).getX()-1
                        && pl2.getY() == pl1.get(selectedButtonIndex).getY()) {
                        return true;
                    }
                }
                case 1 -> {
                    if (pl1.get(selectedButtonIndex).getX() == n-1) return true;
                    for (int i = 0; i < 4; i++) {
                        if (i == selectedButtonIndex) continue;
                        if (pl1.get(i).getX() == pl1.get(selectedButtonIndex).getX()+1
                        && pl1.get(i).getY() == pl1.get(selectedButtonIndex).getY()) {
                            return true;
                        }
                    }
                    if (pl2.getX() == pl1.get(selectedButtonIndex).getX()+1
                        && pl2.getY() == pl1.get(selectedButtonIndex).getY()) {
                        return true;
                    }
                }
                case 2 -> {
                    if (pl1.get(selectedButtonIndex).getY() == 0) return true;
                    for (int i = 0; i < 4; i++) {
                        if (i == selectedButtonIndex) continue;
                        if (pl1.get(i).getY() == pl1.get(selectedButtonIndex).getY()-1
                            && pl1.get(i).getX() == pl1.get(selectedButtonIndex).getX()) {
                            return true;
                        }
                    }
                    if (pl2.getY() == pl1.get(selectedButtonIndex).getY()-1
                        && pl2.getX() == pl1.get(selectedButtonIndex).getX()) {
                        return true;
                    }
                }
                case 3 -> {
                    if (pl1.get(selectedButtonIndex).getY() == n-1) return true;
                    for (int i = 0; i < 4; i++) {
                        if (i == selectedButtonIndex) continue;
                        if (pl1.get(i).getY() == pl1.get(selectedButtonIndex).getY()+1
                            && pl1.get(i).getX() == pl1.get(selectedButtonIndex).getX()) {
                            return true;
                        }
                    }
                    if (pl2.getY() == pl1.get(selectedButtonIndex).getY()+1
                        && pl2.getX() == pl1.get(selectedButtonIndex).getX()) {
                        return true;
                    }
                }
            }            
            return false;
        }
        else {
            for (int i = 0; i < 4; i++) {
                switch (move) {
                    case 0 -> {
                        if (pl2.getX() == 0) return true;
                        if (pl1.get(i).getX() == pl2.getX()-1
                            && pl1.get(i).getY() == pl2.getY()) {
                            return true;
                        }
                    }
                    case 1 -> {
                        if (pl2.getX() == n-1) return true;
                        if (pl1.get(i).getX() == pl2.getX()+1
                            && pl1.get(i).getY() == pl2.getY()) {
                            return true;
                        }
                    }
                    case 2 -> {
                        if (pl2.getY() == 0) return true;
                        if (pl1.get(i).getY() == pl2.getY()-1
                            && pl1.get(i).getX() == pl2.getX()) {
                            return true;
                        }
                    }
                    case 3 -> {
                        if (pl2.getY() == n-1) return true;
                        if (pl1.get(i).getY() == pl2.getY()+1
                            && pl1.get(i).getX() == pl2.getX()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
    
    private void printOut () {
        System.out.println(pl2.getX() + " " + pl2.getY());
        for (int i = 0; i < 4; i++) {
            System.out.println(pl1.get(i).getX() + " " + pl1.get(i).getY());
        }
    }
    
    private void moveButton(JButton button, int dx, int dy) {
        int newX = Math.max(0, Math.min(getWidth() - button.getWidth(), button.getX() + dx));
        int newY = Math.max(0, Math.min(getHeight() - button.getHeight(), button.getY() + dy));
        button.setLocation(newX, newY);
        repaint();
    }
    
    private void confirmedButton() {
        for (int i = 0; i < 4; i++) {
            p1Buttons[i].setBorder(i == selectedButtonIndex ? BorderFactory.createLineBorder(Color.WHITE, 3) : null);
        }
    }
    
    private void highlightSelectedButton() {
        for (int i = 0; i < 4; i++) {
            p1Buttons[i].setBorder(i == selectedButtonIndex ? BorderFactory.createLineBorder(Color.YELLOW, 3) : null);
        }
    }
    
    private void delightSelectedButton1() {
        for (int i = 0; i < 4; i++) {
            p1Buttons[i].setBorder(null);
        }
        p2Button.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
    }
    private void delightSelectedButton2() {
        p2Button.setBorder(null);
        p1Buttons[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
    }
    private boolean inside (Point a) {
        return (a.getX() >= 0 && a.getY() >= 0 && a.getX() < n && a.getY() < n);
    }
    public boolean winner () {
        List <Point> check = new ArrayList<>();
        check.add(new Point(pl2.getX()-1, pl2.getY()));
        check.add(new Point(pl2.getX(), pl2.getY()-1));
        check.add(new Point(pl2.getX(), pl2.getY()+1));
        check.add(new Point(pl2.getX()+1, pl2.getY()));
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            if (!inside(check.get(i))) {
                cnt++;
                continue;
            }
            for (int j = 0; j < 4; j++) {
                if (check.get(i).getX() == pl1.get(j).getX() && check.get(i).getY() == pl1.get(j).getY()) {
                    cnt++;
                    break;
                }
            }
        }
        return (cnt == 4);
    }
    
    public void announceWinner () {
        if (winner()) {
            JOptionPane.showMessageDialog(this, "Player 1 wins!");
        }
        else {
            JOptionPane.showMessageDialog(this, "Player 2 wins!");
        }
    }
    
} 
