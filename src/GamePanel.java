import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{
    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static final int Unit_Size = 25;
    static final int Game_Unit =(WIDTH*HEIGHT)/Unit_Size;
    static final int DELAY = 75;

    //X is going to hold all the x coordinates of the snake.
    final int x[] = new int[Game_Unit];
    //Y is going to hold all the y coordinates of the snake.
    final int y[] = new int[Game_Unit];


    int bodyParts = 5;
    int applesEaten;
    //x and y of the position of the apple.
    int appleX;
    int appleY;


    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    //constructor
    GamePanel()
    {
        random=new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new TheKeyAdapter());
        startGame();
    }

    //Game methods
    public void startGame()
    {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    //the grid layout of the game
    public void draw(Graphics g)
    {
        if(running) {
            for (int i = 0; i < HEIGHT / Unit_Size; i++) {

                g.drawLine(i * Unit_Size, 0, i * Unit_Size, HEIGHT);
                g.drawLine(0, i * Unit_Size, WIDTH, i * Unit_Size);
            }
            g.setColor(Color.green);
            g.fillOval(appleX, appleY, Unit_Size, Unit_Size);


            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.red);
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                } else {
                    g.setColor(new Color(139, 0, 0));
                    g.fillRect(x[i], y[i], Unit_Size, Unit_Size);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (WIDTH - metrics.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());
        }
        else
        {
          gameOver(g);
        }

    }

    public void newApple()
    {// generates a new apple in a random position.
        appleX=random.nextInt((int)(WIDTH/Unit_Size))*Unit_Size;
        appleY=random.nextInt((int)(HEIGHT/Unit_Size))*Unit_Size;
    }


    public void move()
    {
        for(int i=bodyParts; i>0; i--)
        {
            x[i]=x[i-1];
            y[i]=y[i-1];
        }

        switch(direction)
        {
            case 'U':
                y[0]-=Unit_Size;
                break;
            case 'D':
                y[0]+=Unit_Size;
                break;
            case 'L':
                x[0]-=Unit_Size;
                break;
            case 'R':
                x[0]+=Unit_Size;
                break;
        }

    }

    public void checkApple()
    {
        if(x[0]==appleX && y[0]==appleY)
        {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions()
    { // checks if the snake has hit itself
        for(int i=bodyParts; i>0; i--)
        {
            if(x[0]==x[i] && y[0]==y[i])
            {
                running=false;
            }
        }
        // checks if the snake has hit the edge
        //left border
        if(x[0] < 0)
        {
            running=false;
        }
        //right border
        if(x[0] > WIDTH)
        {
            running=false;
        }
        //top border
        if(y[0] < 0)
        {
            running=false;
        }
        //bottom border
        if(y[0] > HEIGHT)
        {
            running=false;
        }

        if(!running)
        {
            timer.stop();
        }
    }

    public void gameOver(Graphics g)
    {
        //GAME OVER text
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (WIDTH - metrics1.stringWidth("Game Over")) / 2, HEIGHT / 2);
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (WIDTH - metrics2.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(running)
        {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class TheKeyAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                //doesn't allow 180 turns
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
