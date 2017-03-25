import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * 子弹类
 * Created by 徐溶延 on 2017/3/19.
 */
public class Missile {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private int x, y;
    Tank.Direction direction;
    private static final int SPEED = 10;
    private boolean live = true;
    private boolean good;
    private TankWarClient tankWarClient;

    public Missile(int x, int y, Tank.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Missile(int x, int y, boolean good, Tank.Direction direction, TankWarClient tankWarClient) {
        this(x, y, direction);
        this.good = good;
        this.tankWarClient = tankWarClient;
    }

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

    public boolean isLive() {
        return live;
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitWall(Wall wall) {
        if (live && this.getRect().intersects(wall.getRect())) {
            live = false;
            return true;
        }
        return false;
    }

    public boolean hitTank(Tank tank) {
        if (live && this.getRect().intersects(tank.getRect()) && tank.isLive() && this.good != tank.isGood()) {
            live = false;
            tank.setLive(false);
            Explode explode = new Explode(x, y, this.tankWarClient);
            this.tankWarClient.getExplodeList().add(explode);
            return true;
        }
        return false;
    }



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
