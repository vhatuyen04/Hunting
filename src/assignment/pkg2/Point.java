/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment.pkg2;

/**
 *
 * @author hatuyen
 * This is the Point class, illustrates location of buttons as points in the board.
 */
public class Point {
    private int x;
    private int y;
    public Point (int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX () {
        return x;
    }
    public int getY () {
        return y;
    }
    public void setX (int x) {
        this.x = x;
    }
    public void setY (int y) {
        this.y = y;
    }
}
