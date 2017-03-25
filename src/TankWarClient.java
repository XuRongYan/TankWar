import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * 客户端主类
 * Created by 徐溶延 on 2017/3/18.
 */
public class TankWarClient extends Frame {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
    Tank tank = new Tank(50, 50, this, true);
    Wall wall = new Wall(100, 100, 50, 300, this);
    private List<Tank> tankList = new ArrayList<>();
    private List<Explode> explodeList = new ArrayList<>();
    private List<Missile> missileList = new ArrayList<>();
    private Image offScreenImage = null;
    private static Random random = new Random();

    public static void main(String[] args) {
        TankWarClient tankWarClient = new TankWarClient();
        tankWarClient.launch();
    }

    public void launch() {
        this.setLocation(300, 400);
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setResizable(false);
        this.setBackground(Color.LIGHT_GRAY);
        this.setTitle("TankWar");
        this.addKeyListener(new KeyMonitor());
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        for (int i = 0; i < 10; i++) {
            tankList.add(new Tank(80 * (i + 1), 50, this, false));
        }
        new Thread(new PaintThread()).start();
    }

    /**
     * 利用绘制虚拟背景图的原理实现双缓冲，虽然机子牛逼不用这个也不闪烁
     * 将要绘制的图片先绘制在一张Image放在屏幕后
     * @param g
     */
    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WIDTH, HEIGHT);
        }
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.LIGHT_GRAY);
        gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        gOffScreen.dispose();
        g.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawString("missile count: " + missileList.size(), 10, 50);
        g.drawString("explode count: " + explodeList.size(), 10, 70);
        g.drawString("enemy count: " + tankList.size(), 10, 90);
        g.drawString("life: " + tank.getLife(), 10, 110);
        tank.collidesWall(wall);
        tank.collidesTanks(tankList);
        tank.draw(g);
        wall.draw(g);

        for (int i = 0; i < tankList.size(); i++) {
            Tank tank = tankList.get(i);
            tank.collidesWall(wall);
            tank.collidesTanks(tankList, tank);
            tank.draw(g);
        }

        for (int i = 0; i < explodeList.size(); i++) {
            Explode explode = explodeList.get(i);
            explode.draw(g);
        }
        for (int i = 0; i < missileList.size(); i++) {
            Missile missile = missileList.get(i);
            missile.hitTanks(tankList);
            missile.hitTank(tank);
            missile.hitWall(wall);
            missile.draw(g);
        }

    }

    private class PaintThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            tank.onKeyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            tank.onKeyReleased(e);
        }


    }


    public List<Missile> getMissileList() {
        return missileList;
    }

    public List<Explode> getExplodeList() {
        return explodeList;
    }

    public List<Tank> getTankList() {
        return tankList;
    }
}
