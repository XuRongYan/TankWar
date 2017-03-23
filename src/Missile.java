import java.awt.*;

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
    private TankWarClient tankWarClient;

    public Missile(int x, int y, Tank.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Missile(int x, int y, Tank.Direction direction, TankWarClient tankWarClient) {
        this(x, y, direction);
        this.tankWarClient = tankWarClient;
    }

    public void draw(Graphics g) {
        if (!isLive()) {
            tankWarClient.getMissileList().remove(this);
        }
        Color color = g.getColor();
        g.setColor(Color.BLACK);
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

    public boolean hitTank(Tank tank) {
        if (this.getRect().intersects(tank.getRect()) && tank.isLive()) {
            live = false;
            tank.setLive(false);
            return true;
        }
        return false;
    }
}
