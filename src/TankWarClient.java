import sun.rmi.runtime.Log;

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
    private static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    /**
     * 整个坦克游戏的宽度
     */
    public static final int WIDTH = screen.width;
    /**
     * 整个坦克游戏的高度
     */
    public static final int HEIGHT = screen.height;
    private Tank tank = new Tank(50, 50, this, true);
    private Wall wall = new Wall(100, 100, 50, 300, this);
    private List<Tank> tankList = new ArrayList<>();
    private List<Explode> explodeList = new ArrayList<>();
    private List<Missile> missileList = new ArrayList<>();
    private List<Blood> bloodList = new ArrayList<>();
    private Image offScreenImage = null;
    private static Random random = new Random();
    private Thread bloodThread;
    int x = 0;

    public static void main(String[] args) {
        TankWarClient tankWarClient = new TankWarClient();
        tankWarClient.launch();
    }

    public void launch() {
        this.setLocation(0, 0);
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
            addTank();
        }
        new Thread(new PaintThread()).start();
        bloodThread = new Thread(new AddBloodThread());
        bloodThread.start();
    }

    /**
     * 利用绘制虚拟背景图的原理实现双缓冲，虽然机子牛逼不用这个也不闪烁
     * 将要绘制的图片先绘制在一张Image放在屏幕后
     *
     * @param g 画笔
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
        //g.drawString("missile count: " + missileList.size(), 10, 50);
        //g.drawString("explode count: " + explodeList.size(), 10, 70);
        g.drawString("enemy count: " + tankList.size(), 10, 50);
        //g.drawString("life: " + tank.getLife(), 10, 110);
        g.drawString("blood size: " + bloodList.size(), 10, 70);
        //g.drawString("test: " + x, 10, 150);
        tank.collidesWall(wall);
        tank.collidesTanks(tankList);
        tank.eatBloodAll(bloodList);
        tank.draw(g);
        wall.draw(g);

        if (tankList.size() <= 0) {
            for (int i = 0; i < 5; i++) {
                addTank();
            }
        }


        for (int i = 0; i < bloodList.size(); i++) {
            Blood blood = bloodList.get(i);
            blood.hitWall(wall);
            blood.draw(g);
        }


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

    /**
     * 添加坦克的方法
     */
    private void addTank() {
        int x = random.nextInt(WIDTH);
        int y = random.nextInt(HEIGHT);
        Tank tank = new Tank(x, y, this, false);
        if (!tank.collidesWall(wall)) {
            tankList.add(tank);
        }
    }

//    private class AddEnemy implements Runnable {
//
//        @Override
//        public void run() {
//            while (true) {
//                if (tankList.size() <= 0) {
//                    for (int i = 0; i < 10; i++) {
//                        addTank();
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     * 添加血块的线程
     */
    private class AddBloodThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (bloodList.size() <= 2) {
                    System.out.println("执行了");
                    bloodList.add(new Blood(TankWarClient.this));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //怪事，不加这一句话有很大的几率会产生血块不产生的问题
                System.out.println(bloodList.size());
            }
        }
    }

    /**
     * 重绘线程
     */
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

    /**
     * 键盘监听器类
     */
    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            tank.onKeyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_F12) {
                if (!tank.isLive()) {
                    tank = new Tank(50, 50, TankWarClient.this, true);
                }
            }
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

    public List<Blood> getBloodList() {
        return bloodList;
    }
}
