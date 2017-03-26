import java.awt.*;

/**
 * 墙类
 * Created by 徐溶延 on 2017/3/25.
 */
public class Wall {
    private int x, y, width, height;
    private TankWarClient tankWarClient;

    /**
     * 墙的构造方法
     * @param x 墙的x坐标
     * @param y 墙的y坐标
     * @param width 墙的宽度
     * @param height 墙的高度
     */
    private Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @param x 墙的x坐标
     * @param y 墙的y坐标
     * @param width 墙的宽度
     * @param height 墙的高度
     * @param tankWarClient 坦克客户端类的引用
     */
    public Wall(int x, int y, int width, int height, TankWarClient tankWarClient) {
        this(x, y, width, height);
        this.tankWarClient = tankWarClient;
    }

    /**
     * 画出墙
     * @param g
     */
    public void draw(Graphics g) {
        Color color = g.getColor();
        g.setColor(Color.black);
        g.fillRect(x, y, width, height);
        g.setColor(color);
    }

    /**
     * 墙的碰撞体积
     * @return 返回墙的碰撞体积
     */
    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
}
