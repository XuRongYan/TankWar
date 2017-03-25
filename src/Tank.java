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
    private TankWarClient tc;
    private boolean good;
    private boolean live = true;
    private boolean bU = false, bD = false, bL = false, bR = false;
    private Direction[] directions = Direction.values();


    enum Direction {L, LU, U, RU, R, RD, D, LD, STOP}

    Direction dir = Direction.STOP;
    Direction barrelDir = Direction.D; //炮筒方向

    private Tank(int x, int y) {
        this.x = x;
        this.y = y;
        oldX = x;
        oldY = y;
    }

    public Tank(int x, int y, TankWarClient tc) {
        this(x, y);
        this.tc = tc;
    }

    public Tank(int x, int y, TankWarClient tc, boolean good) {
        this(x, y, tc);
        this.good = good;
    }

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
        }
        locateDirection();
    }

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

    public boolean collidesWall(Wall wall) {
        if (this.isLive() && this.getRect().intersects(wall.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

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

    private void stay() {
        this.x = oldX;
        this.y = oldY;
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }
}
