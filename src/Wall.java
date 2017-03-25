import java.awt.*;

/**
 * 墙类
 * Created by 徐溶延 on 2017/3/25.
 */
public class Wall {
    private int x, y, width, height;
    private TankWarClient tankWarClient;

    private Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Wall(int x, int y, int width, int height, TankWarClient tankWarClient) {
        this(x, y, width, height);
        this.tankWarClient = tankWarClient;
    }

    public void draw(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.black);
        g.fillRect(x, y, width, height);
        g.setColor(color);
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
}
