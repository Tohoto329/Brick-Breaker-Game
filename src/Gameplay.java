import javax.swing.JPanel;
import javax.swing.Timer; 
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Rectangle;
import java.awt.Graphics2D;



public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21; 
    private Timer time;
    private int delay = 1;

    private int playerX = 310;

    private int ballpositionX = 120;
    private int ballpositionY = 350;
    private int ballXdirection = -1;
    private int ballYdirection = -2;

    private MapGenerator map;


    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D)g);

        //border
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(692, 0, 3, 592);
        
        //score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        //the paddle
        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 100, 8);

        //the ball
        g.setColor(Color.yellow);
        g.fillOval(ballpositionX, ballpositionY, 20, 20);

        if(totalBricks <= 0) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        if(ballpositionY > 570) {
            play = false;
            ballXdirection = 0;
            ballYdirection = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GameOver, Score : " +score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time.start();
        if(play) {
            if(new Rectangle(ballpositionX, ballpositionY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdirection = -ballYdirection;
            }

            for(int i=0; i<map.map.length; i++) {
                for(int j=0; j<map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballpositionX, ballpositionY, 20, 20);
                        Rectangle brickrect = rect;

                        if(ballRect.intersects(brickrect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballpositionX + 19 <= brickrect.x || ballpositionX + 1 >= brickrect.x + brickrect.width) {
                                ballXdirection = -ballXdirection;
                            }else{
                                ballYdirection = -ballYdirection;
                            }

                            break;
                        }
                    }
                }
            }
            ballpositionX += ballXdirection;
            ballpositionY += ballYdirection;
            if(ballpositionX < 0) {
                ballXdirection = - ballXdirection;
            }
            if(ballpositionY < 0) {
                ballYdirection = - ballYdirection;
            }
            if(ballpositionX > 670) {
                ballXdirection = - ballXdirection;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
          if(e.getKeyCode() == KeyEvent.VK_RIGHT){
              if(playerX >= 600 ){
                  playerX = 600;
              }else{
                  moveRight();
              }
          }
          if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10 ){
                playerX = 10;
            }else{
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballpositionX = 120;
                ballpositionY = 350;
                ballXdirection = -1;
                ballYdirection = -2;
                playerX = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }
    public void moveRight() {
        play = true;
        playerX+= 20;
    }
    public void moveLeft() {
        play = true;
        playerX-= 20;
    }
   
    
}
