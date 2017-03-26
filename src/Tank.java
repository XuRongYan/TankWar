import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * 坦克类
 * Created by 徐溶延 on 2017/3/18.
 */
public class Tank {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    private static final int SPEED = 5;
    private static Random random = new Random();
    private int x, y;
    private int oldX, oldY;
    private int step = 0;
    private int life = 100;
    private TankWarClient tc;
    private BloodBar bloodBar = new BloodBar();
    private boolean good;
    private boolean live = true;
    private boolean bU = false, bD = false, bL = false, bR = false;
    private Direction[] directions = Direction.values();


    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    Direction dir = Direction.STOP;
    Direction barrelDir = Direction.D; //炮筒方向

    /**
     *
     * @param x 坦克坐标x
     * @param y 坦克坐标y
     */

    private Tank(int x, int y) {
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
    }

    /**
     *
     * @param x 坦克坐标x
     * @param y 坦克坐标y
     * @param tc 坦克客户端类对象的引用
     */
    public Tank(int x, int y, TankWarClient tc) {
        this(x, y);
        this.tc = tc;
    }

    public Tank(int x, int y, TankWarClient tc, boolean good) {
        this(x, y, tc);
        this.good = good;
    }

    /**
     * 坦克的draw方法，画出坦克
     * @param g
     */
    public void draw(Graphics g) {
        if (!isLive()) {
            if (!good) {
                tc.getTankList().remove(this);
            }
            return;
        }
        Color c = g.getColor();
        g.setColor(good ? Color.GREEN : Color.blue);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        if (good) {
            bloodBar.draw(g);
        }
        switch (barrelDir) {
            case L:
                g.drawLine(x + WIDTH / 2, y + WIDTH / 2, x, y + HEIGHT / 2);
                break;
            case LU:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y);
                break;
            case U:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y);
                break;
            case RU:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y);
                break;
            case R:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT / 2);
                break;
            case RD:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH, y + HEIGHT);
                break;
            case D:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x + WIDTH / 2, y + HEIGHT);
                break;
            case LD:
                g.drawLine(x + WIDTH / 2, y + HEIGHT / 2, x, y + HEIGHT);
                break;

        }
        move();
        g.setColor(c);
    }

    /**
     * 移动坦克的方法
     */
    private void move() {
        oldX = x;
        oldY = y;

        switch (dir) {
            case L:
                x -= SPEED;
                break;
            case LU:
                x -= Math.sqrt(SPEED * SPEED / 2);
                y -= Math.sqrt(SPEED * SPEED / 2);
                break;
            case U:
                y -= SPEED;
                break;
            case RU:
                x += Math.sqrt(SPEED * SPEED / 2);
                y -= Math.sqrt(SPEED * SPEED / 2);
                break;
            case R:
                x += SPEED;
                break;
            case RD:
                x += Math.sqrt(SPEED * SPEED / 2);
                y += Math.sqrt(SPEED * SPEED / 2);
                break;
            case D:
                y += SPEED;
                break;
            case LD:
                x -= Math.sqrt(SPEED * SPEED / 2);
                y += Math.sqrt(SPEED * SPEED / 2);
                break;
            case STOP:
                break;
        }

        if (dir != Direction.STOP) {
            barrelDir = dir;
        }



        if (x < 0) {
            x = 0;
        }
        if (y < 31) {
            y = 31;
        }
        if (x + Tank.WIDTH > TankWarClient.WIDTH) {
            x = TankWarClient.WIDTH - Tank.WIDTH;
        }
        if (y + Tank.HEIGHT > TankWarClient.HEIGHT) {
            y = TankWarClient.HEIGHT - Tank.HEIGHT;
        }

        if (!good) {
            if (step == 0) {
                step = random.nextInt(12) + 3;
                dir = directions[random.nextInt(directions.length)];
            } else {
                step --;
            }

            if (random.nextInt(40) > 37) {
                this.fire();
            }
        }
    }

    /**
     * 处理键盘按下事件
     * @param e
     */
    public void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_CONTROL:
                break;
        }
        locateDirection();
    }

    /**
     * 处理方向定位
     */
    private void locateDirection() {
        if (bL && !bU && !bR && !bD) dir = Direction.L;
        else if (bL && bU && !bR && !bD) dir = Direction.LU;
        else if (!bL && bU && !bR && !bD) dir = Direction.U;
        else if (!bL && bU && bR && !bD) dir = Direction.RU;
        else if (!bL && !bU && bR && !bD) dir = Direction.R;
        else if (!bL && !bU && bR && bD) dir = Direction.RD;
        else if (!bL && !bU && !bR && bD) dir = Direction.D;
        else if (bL && !bU && !bR && bD) dir = Direction.LD;
        else if (!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    /**
     * 处理键盘抬起事件
     * @param e
     */
    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
        }
        locateDirection();
    }

    /**
     * 坦克发射导弹的事件
     * @return 发射的导弹
     */
    public Missile fire() {
        if (!isLive()) {
            return null;
        }
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
        Missile missile = new Missile(x, y, good, barrelDir, tc);
        tc.getMissileList().add(missile);
        return missile;
    }

    /**
     * 坦克发射导弹的事件
     * @param dir 发射导弹的方向
     * @return 发射的导弹
     */
    public Missile fire(Direction dir) {
        if (!isLive()) {
            return null;
        }
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH / 2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.WIDTH / 2;
        Missile missile = new Missile(x, y, good, dir, tc);
        tc.getMissileList().add(missile);
        return missile;
    }

    /**
     * 发射超级导弹
     * 同时从八个方向发射导弹
     */
    public void superFire() {
        Direction[] directions = Direction.values();
        for (int i = 0; i < 8; i++) {
            fire(directions[i]);
        }
    }

    /**
     * 吃血块的方法
     * @param blood 想要吃的血块
     * @return 是否将血块吃掉 是返回true，否返回false
     */
    public boolean eatBlood(Blood blood) {
        if (this.isLive() && blood.isLive() && this.isGood() && this.getRect().intersects(blood.getRect())) {
            life = 100;
            blood.setLive(false);
            return true;
        }
        return false;
    }

    /**
     * 判断是否吃掉全局范围的血块
     * @param bloodList 全局的血块容器
     * @return 是返回true， 否返回false
     */
    public boolean eatBloodAll(List<Blood> bloodList) {
        for (int i = 0; i < bloodList.size(); i++) {
            Blood blood = bloodList.get(i);
            if (eatBlood(blood)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否撞墙
     * @param wall 墙的引用
     * @return 是返回true， 否返回false
     */
    public boolean collidesWall(Wall wall) {
        if (this.isLive() && this.getRect().intersects(wall.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    /**
     * 是否撞到其他坦克
     * @param tankList 敌方坦克容器
     * @param tanks 其他的坦克
     * @return 是返回true， 否返回false
     */
    public boolean collidesTanks(List<Tank> tankList, Tank...tanks) {
        for (int i = 0; i < tankList.size(); i++) {
            Tank tank = tankList.get(i);
            if (this != tank &&this.isLive() && tank.isLive() && this.getRect().intersects(tank.getRect())) {
                this.stay();
                tank.stay();
                return true;
            }
        }

        for (int i = 0; i < tanks.length; i++) {
            if (this != tanks[i] && this.isLive() && tanks[i].isLive() && this.getRect().intersects(tanks[i].getRect())) {
                this.stay();
                tanks[i].stay();
                return true;
            }
        }
        return false;
    }

    /**
     * 让坦克停住不动
     */
    private void stay() {
        this.x = oldX;
        this.y = oldY;
    }

    /**
     * 获取坦克的碰撞单位
     * @return 返回一个坦克的碰撞单位
     */
    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /**
     * 判断坦克是否存活
     * @return 是返回true，否返回false
     */
    public boolean isLive() {
        return live;
    }

    /**
     * 设置坦克是否存活
     * @param live
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * 判断坦克是敌军还是友军
     * @return 是返回true，否返回false
     */
    public boolean isGood() {
        return good;
    }

    /**
     * 获取坦克当前生命值
     * @return 坦克当前生命值
     */
    public int getLife() {
        return life;
    }

    /**
     * 设置坦克当前生命值
     * @param life 坦克当前生命值
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * 血条类，用于显示坦克的血条
     */
    private class BloodBar {
        /**
         * 画出坦克血条
         * @param g
         */
        public void draw(Graphics g) {
            Color color = g.getColor();
            g.setColor(Color.BLACK);
            g.drawRect(x, y - 20, WIDTH, 10);
            g.setColor(Color.RED);
            int width = (WIDTH - 2) * life / 100;
            g.fillRect(x + 1, y - 19, width, 9);
            g.setColor(color);
        }
    }
}
