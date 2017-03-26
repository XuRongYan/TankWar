import java.awt.*;

/**
 * 血块类
 * Created by 徐溶延 on 2017/3/26.
 */
public class Blood {
    private int x, y;
    private boolean live = true;
    private TankWarClient tankWarClient;

    public void draw(Graphics g) {
        if (!live) return;
        Color color = g.getColor();
        g.setColor(Color.GREEN);
        g.fillOval(x, y, 10, 10);
        g.setColor(color);
    }

    public Blood(int x, int y, TankWarClient tankWarClient) {
        this.x = x;
        this.y = y;
        this.tankWarClient = tankWarClient;
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
