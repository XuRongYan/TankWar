import java.awt.*;
import java.util.Random;

/**
 * 血块类
 * Created by 徐溶延 on 2017/3/26.
 */
public class Blood {
    private static final int SPEED = 3;
    private int x, y;
    private int oldX, oldY;
    private int step = 0;
    private boolean live = true;
    private Direction direction;
    private static Random random = new Random();
    private static Direction[] directions = Direction.values();
    private TankWarClient tankWarClient;

    public void draw(Graphics g) {
        if (!live) {
            tankWarClient.getBloodList().remove(this);
            return;
        }
        Color color = g.getColor();
        g.setColor(Color.GREEN);
        g.fillOval(x, y, 10, 10);
        g.setColor(color);
        move();
    }

    public Blood(TankWarClient tankWarClient) {
        this.x = random.nextInt(TankWarClient.WIDTH);
        this.y = random.nextInt(TankWarClient.HEIGHT);
        this.oldX = x;
        this.oldY = y;
        this.tankWarClient = tankWarClient;
        direction = directions[random.nextInt(directions.length)];
    }

    public void move() {
        oldX = x;
        oldY = y;
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
            case STOP:
                break;
        }

        if (x < 0) {
            x = 0;
        }
        if (y < 31) {
            y = 31;
        }
        if (x + 10 > TankWarClient.WIDTH) {
            x = TankWarClient.WIDTH - 10;
        }
        if (y + 10 > TankWarClient.HEIGHT) {
            y = TankWarClient.HEIGHT - 10;
        }

        if (step == 0) {
            step = random.nextInt(12) + 3;
            direction = directions[random.nextInt(directions.length)];
        } else {
            step --;
        }
    }

    public boolean hitWall(Wall wall) {
        if (live && this.getRect().intersects(wall.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    private void stay() {
        this.x = oldX;
        this.y = oldY;
    }


    public Rectangle getRect(){
        return new Rectangle(x, y, 10, 10);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }
}
