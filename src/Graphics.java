import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Graphics extends JPanel implements ActionListener {

    long startTime;
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int TICK_SIZE = 25;
    static final int BOARD_SIZE = (WIDTH * HEIGHT) / (TICK_SIZE * TICK_SIZE);
    final Font font = new Font("TimesRoman", Font.BOLD, 20);

    int [] snakePosX = new int[BOARD_SIZE];
    int [] snakePosY = new int[BOARD_SIZE];
    int snakeLength;

    Food food;
    int foodEaten;

    char direction = 'R';
    boolean isMoving = false;
    final Timer timer = new Timer(100, this);
    JButton rulesButton = new JButton("Rules");
    JButton javafxButton = new JButton("JavaFX");


    public Graphics(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.LIGHT_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isMoving){
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_LEFT:
                            if(direction !='R') {
                                direction = 'L';
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if(direction !='L') {
                                direction = 'R';
                            }
                            break;
                        case KeyEvent.VK_UP:
                            if(direction !='D') {
                                direction = 'U';
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if(direction !='U') {
                                direction = 'D';
                            }
                            break;
                    }
                } else{
                    start();
                }
            }
        });
        start();
    }

    protected void start() {

        startTime = System.currentTimeMillis();


        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int [BOARD_SIZE];
        snakeLength = 5;
        foodEaten = 0;
        direction = 'R';
        isMoving = true;
        spawnFood();
        timer.start();

        rulesButton.setVisible(false);
        javafxButton.setVisible(false);
    }


    @Override
    protected void paintComponent(java.awt.Graphics g){
        super.paintComponent(g);

        if(isMoving){
            g.setColor(Color.RED);
            g.fillOval(food.getPosX(), food.getPosY(), TICK_SIZE, TICK_SIZE);

            g.setColor(Color.BLACK);
            for(int i = 0; i < snakeLength; i++){
                g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
            }
        } else {
            String scoreText = String.format("The End... Score: %d... Press any key to play again!",foodEaten);
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(scoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, HEIGHT / 2);
            ScoreManager.saveScore(foodEaten, System.currentTimeMillis() - startTime);

                rulesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame rulesFrame = new JFrame("Rules");
                        rulesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JTextArea rulesText = new JTextArea("Reguli pentru jocul Snake:\n\n1. " +
                                "Folosiți tastele săgeți pentru a muta șarpele.\n2. " +
                                "Mâncați mâncarea pentru a obține puncte si a creste in lungime.\n3. " +
                                "Evitați să intrati în pereți sau în propria coadă");
                        rulesText.setEditable(false);
                        rulesText.setFont(font);
                        rulesFrame.add(rulesText);
                        rulesFrame.pack();
                        rulesFrame.setVisible(true);
                    }
                });

                javafxButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame javafxFrame = new JFrame("JavaFX & Java Swing");
                        javafxFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JTextArea javafxText = new JTextArea("JavaFX și Java Swing sunt biblioteci pentru crearea de interfețe cu utilizatorul în Java. . \n" +
                                "JavaFX este cea mai nouă dintre cele două biblioteci și este recomandată pentru proiecte noi, \n" +
                                "în timp ce Java Swing este încă utilizat pe scară largă în aplicațiile vechi.");
                        javafxText.setEditable(false);
                        javafxText.setFont(font);
                        javafxFrame.add(javafxText);
                        javafxFrame.pack();
                        javafxFrame.setVisible(true);
                    }
                });




            this.add(rulesButton);
            this.add(javafxButton);


            rulesButton.setVisible(true);
            javafxButton.setVisible(true);
        }
    }


    protected void move() {
        for(int i = snakeLength; i > 0; i--){
            snakePosX[i] = snakePosX[i-1];
            snakePosY[i] = snakePosY[i-1];
        }

        switch (direction){
            case 'U' -> snakePosY[0] -= TICK_SIZE;
            case 'D' -> snakePosY[0] += TICK_SIZE;
            case 'L' -> snakePosX[0] -= TICK_SIZE;
            case 'R' -> snakePosX[0] += TICK_SIZE;
        }
    }

    protected void spawnFood(){
        food = new Food();
    }

    protected void eatFood(){
        if((snakePosX[0] == food.getPosX()) && (snakePosY[0] == food.getPosY())){
            snakeLength++;
            foodEaten++;
            spawnFood();
        }
    }

    protected void collisionTest(){
        for(int i = snakeLength; i > 0; i--){
            if((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])) {
                isMoving = false;
                break;
            }
        }

        if (snakePosX[0] < 0 || snakePosX[0] > WIDTH - TICK_SIZE || snakePosY[0] < 0 || snakePosY[0] > HEIGHT - TICK_SIZE){
            isMoving = false;
        }

        if(!isMoving){
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(isMoving){
            move();
            collisionTest();
            eatFood();
        }
        repaint();
    }

}
