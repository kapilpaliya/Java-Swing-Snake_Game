//Import Statement
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

public class Snake_Game extends JPanel implements ActionListener, KeyListener {
    //Variable Declaration For Images.
    ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\snaketitle2.png")));
    ImageIcon leftmouth = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\leftmouth.png")));
    ImageIcon rightmouth = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\rightmouth.png")));
    ImageIcon upmouth = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\upmouth.png")));
    ImageIcon downmouth = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\downmouth.png")));
    ImageIcon enemy = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\enemy.png")));
    ImageIcon snakeimage = new ImageIcon(Objects.requireNonNull(getClass().getResource("image\\snakeimage.png")));

    //Variable Declaration For Direction. (left,right,up,down)
    private boolean left = false;  private boolean right = true;  private boolean up = false;  private boolean down = false;

    //Variable Declaration For Snake Length, (in X-Direction) & (in Y-Direction)
    private int lengthOfSnake=3;  private final int[] snakexlength=new int[850]; private final int[] snakeylength=new int[625];

    //Variable Declaration For Apple Position On X-Direction & Y-Direction
    private final int[] xpos = {25,50,75,100,125,150,175,200,225,250,
                                275,300,325,350,375,400,425,450,475,500,
                                525,550,575,600,625,650,675,700,725,750,
                                775,800,825,850};
    private final int[] ypos = {75,100,125,150,175,200,225,250,275,300,
                                325,350,375,400,425,450,475,500,525,550,
                                575,600,625};
    private int enemyX,enemyY;

    //Variable Declaration For Des-Bord
    private int score=0;
    private boolean gameover=false;
    private int moves = 0;

    //Variable Declaration For Random Apple Position & Delay For Snake Movement.
    private final Timer timer; private final int delay=200;
    private final Random random = new Random();

    //Constructor Creation For Direction Run-Program With Creating Object.
    Snake_Game(){
        //Add ActionListener
        addKeyListener(this);
 
        //Snake Control By ArrowKeys
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);

        //Speed Of Snake Movement.
        timer = new Timer(delay,this);
        timer.start();

        //Add Apple Position.
        newenemy();
    }

    //Paint Methode Override
    @Override
    public void paint(Graphics g){
        super.paint(g);
        //Rectangle Title Part
        g.setColor(Color.white);
//        g.drawRect(24,10,851,55);
        g.drawRect(-1,0,900,73);
        //Rectangle Game Bord Part
        g.setColor(Color.black);
        g.drawRect(24,74,851,576);
        image.paintIcon(this,g,25,0);
        g.setColor(Color.black);
        g.fillRect(25,75,850,575);

        //Movement Of Snake In Run Time.
        if(moves==0){
            snakexlength[0]=100;
            snakexlength[1]=75;
            snakexlength[2]=50;
            snakeylength[0]=100;
            snakeylength[1]=100;
            snakeylength[2]=100;
        }
        if(left){
            leftmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(right){
            rightmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(up){
            upmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(down){
            downmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }

        //Increment Of Snake Length.
        for(int i=1;i<lengthOfSnake;i++){
            snakeimage.paintIcon(this,g,snakexlength[i],snakeylength[i]);
        }

        //Used To Define The Length Of Snake & Score.
        g.setColor(Color.green);
        g.setFont(new Font("Arial",Font.BOLD,20));
        g.drawString("Score : "+score,44,70);
        g.drawString("Length : "+lengthOfSnake,760,70);

        //Enemy Image Add and Other 2 Methods.
        enemy.paintIcon(this,g,enemyX,enemyY);
        callenemy();
        callenemybody();

        //Game Over Condition Creation.
        if(gameover){
            g.setColor(Color.magenta);
            g.setFont(new Font("Arial",Font.BOLD,50));
            g.drawString("GAME OVER",300,300);
            g.setFont(new Font("Arial",Font.PLAIN,20));
            g.drawString("Press Space To Restart",340,350);
        }
        g.dispose();
    }
    //ActionListener Class Method Override
    @Override
    public void actionPerformed(ActionEvent e){
        //Used To Movement Of All Snake Body.
        for(int i=lengthOfSnake-1;i>0;i--){
            snakexlength[i]=snakexlength[i-1];
            snakeylength[i]=snakeylength[i-1];
        }
        //Add 25px In Snake Mouth Index[0], If True Condition Of Left, Right, UP & Down.
        if(left){
            snakexlength[0] = snakexlength[0]-25;
        }
        if(right){
            snakexlength[0] = snakexlength[0]+25;
        }
        if(up){
            snakeylength[0] = snakeylength[0]-25;
        }
        if(down){
            snakeylength[0] = snakeylength[0]+25;
        }

        //Border Property Out Of Bounds.
        if(snakexlength[0]>850)snakexlength[0]=25; //Width Of Snake Is Large Thane Frame.
        if(snakexlength[0]<25)snakexlength[0]=850; //Width Of Snake Is Smaller Thane Frame.
        if(snakeylength[0]>625)snakeylength[0]=75; //Height Of Snake Is Large Thane Frame.
        if(snakeylength[0]<75)snakeylength[0]=625; //Height Of Snake Is Smaller Thane Frame.
        this.repaint();
    }

    //Key Listener All Methods Override 1. KeyType, 2. keyReleased Are Note-Used.
    @Override
    public void keyTyped(KeyEvent e) { //Note Used Of KeyTyped Listener.
    }
    @Override
    public void keyReleased(KeyEvent e) {//Note Used Of KeyReleased Listener.
    }

    //Key Listener All Methods Override, 3. keyPressed,
    @Override
    public void keyPressed(KeyEvent e) {

        //Restart game
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            restart();
        }

        //Moving game
        if(e.getKeyCode()== KeyEvent.VK_LEFT && (!right)){
            left=true;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()== KeyEvent.VK_RIGHT && (!left)){
            right=true;
            up=false;
            down=false;
            moves++;
        }
        if(e.getKeyCode()== KeyEvent.VK_UP && (!down)){
            left=false;
            right=false;
            up=true;
            moves++;
        }
        if(e.getKeyCode()== KeyEvent.VK_DOWN && (!up)){
            left=false;
            right=false;
            down=true;
            moves++;
        }
    }

    //Enemy Position In Random Border Position.
    private void newenemy() {
        enemyX=xpos[random.nextInt(34)];
        enemyY=ypos[random.nextInt(23)];

        //Check Whether Enemy Position Not Upon the Snake Body.
        for(int i=lengthOfSnake-1;i>0;i--){
            if(snakexlength[i]==enemyX && snakeylength[i]==enemyY){
                newenemy();
            }
        }
    }

    //Give Condition To Mouth Of Snake Are Equal Enemy X-Y Point
    private void callenemy(){// In Future We Will Merge callenemybody() In This Methode.
        if(snakexlength[0]==enemyX && snakeylength[0]==enemyY){
            newenemy();
            lengthOfSnake++;
            score++;
        }
    }
    private void callenemybody(){ // In Future We Will Merge call-enemy() In This Methode.
        for(int i=lengthOfSnake-1;i>0;i--){
            if(snakexlength[i]==snakexlength[0] && snakeylength[i]==snakeylength[0]){
                timer.stop();
                gameover=true;
            }
        }
    }

    //restart method implement
    void restart(){
        score=0;
        moves=0;
        lengthOfSnake=3;
        left=false;
        right=true;
        up=false;
        down=false;
        gameover=false;
        timer.start();
        repaint();
        newenemy();
    }
    public static void main(String[] args) {
        //Add Frame.
        JFrame f = new JFrame("Snake_Game");
        f.setBounds(300,20,905,700);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Add Panel
        Snake_Game panel = new Snake_Game();
        panel.setBackground(Color.darkGray);
        f.add(panel);
        f.setVisible(true);
    }
}
