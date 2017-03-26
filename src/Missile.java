import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 子弹类
 * Created by 徐溶延 on 2017/3/19.
 */
public class Missile {
    /**
     * 子弹的宽度
     */
    public static final int WIDTH = 10;
    /**
     * 子弹的大小
     */
    public static final int HEIGHT = 10;
    private int x, y;
    Tank.Direction direction;
    /**
     * 子弹的飞行速度
     */
    private static final int SPEED = 10;
    private boolean live = true;
    private boolean good;
    private TankWarClient tankWarClient;

    /**
     * 子弹的构造方法
     * @param x 子弹x坐标
     * @param y 子弹y坐标
     * @param direction 子弹方向
     */
    public Missile(int x, int y, Tank.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * 子弹构造方法
     * @param x 子弹x坐标
     * @param y 子弹y坐标
     * @param good 子弹是否是己方
     * @param direction 子弹方向
     * @param tankWarClient 坦克客户端类对象的引用
     */
    public Missile(int x, int y, boolean good, Tank.Direction direction, TankWarClient tankWarClient) {
        this(x, y, direction);
        this.good = good;
        this.tankWarClient = tankWarClient;
    }

    /**
     * 画出子弹
     * @param g 画笔
     */
    public void draw(Graphics g) {
        if (!isLive()) {
            tankWarClient.getMissileList().remove(this);
        }
        Color color = g.getColor();
        g.setColor(good ? Color.BLACK : Color.RED);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(color);
        move();

    }

    /**
     * 移动子弹
     */
    private void move() {
        switch (direction) {
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
        }
        //判断是否出界
        if (x < 0 || y < 0 || x > TankWarClient.WIDTH || y > TankWarClient.HEIGHT) {

            live = false;
        }
    }

    /**
     * 判断子弹是否存活
     * @return 是返回true，否返回false
     */
    public boolean isLive() {
        return live;
    }

    /**
     * 获取子弹的碰撞体积
     * @return 返回子弹的碰撞体积
     */
    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /**
     * 判断子弹是否打墙
     * @param wall 墙的引用
     * @return 是返回true， 否返回false
     */
    public boolean hitWall(Wall wall) {
        if (live && this.getRect().intersects(wall.getRect())) {
            live = false;
            return true;
        }
        return false;
    }

    /**
     * 判断子弹是否打到坦克
     * @param tank 坦克的引用
     * @return 是返回true，否返回false
     */
    public boolean hitTank(Tank tank) {
        if (live && this.getRect().intersects(tank.getRect()) && tank.isLive() && this.good != tank.isGood()) {
            live = false;
            if (tank.isGood()) {
                tank.setLife(tank.getLife() - 20);
                if (tank.getLife() <= 0) {
                    tank.setLive(false);
                }
            } else {
                tank.setLive(false);
            }
            Explode explode = new Explode(x, y, this.tankWarClient);
            this.tankWarClient.getExplodeList().add(explode);
            return true;
        }
        return false;
    }


    /**
     * 打多辆坦克
     * @param tankList 敌军坦克集合
     * @return 是返回true， 否返回false
     */
    public boolean hitTanks(List<Tank> tankList) {
        for (int i = 0; i < tankList.size(); i++) {
            Tank tank = tankList.get(i);
            if (hitTank(tank)) {
                return true;
            }
        }
        return false;
    }
}
