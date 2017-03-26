import java.awt.*;

/**
 * 爆炸类
 * Created by 徐溶延 on 2017/3/22.
 */
public class Explode {
    int x, y;
    int[] diameters = {5, 12, 23, 36, 52, 31, 10, 5};
    private boolean live = true;
    private TankWarClient tankWarClient;
    private int step = 0;

    /**
     * 爆炸类的构造方法
     * @param x 爆炸的x坐标
     * @param y 爆炸的y坐标
     * @param tankWarClient
     */
    public Explode(int x, int y, TankWarClient tankWarClient) {
        this.x = x;
        this.y = y;
        this.tankWarClient = tankWarClient;
    }

    /**
     * 画出爆炸
     * @param g 画笔
     */
    public void draw(Graphics g) {
        if (!live) {
            tankWarClient.getExplodeList().remove(this);
            return;
        }

        if (step == diameters.length) {
            live = false;
            step = 0;
            return;
        }
        
        Color color = g.getColor();
        g.setColor(Color.orange);
        g.fillOval(x, y, diameters[step], diameters[step]);
        step ++;
        g.setColor(color);
    }
}
