import sun.awt.image.ToolkitImage;

import java.awt.*;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageProducer;
import java.net.URLEncoder;

/**
 * 爆炸类
 * Created by 徐溶延 on 2017/3/22.
 */
public class Explode {
    int x, y;
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();

    private static Image[] images = {
            toolkit.getImage(Explode.class.getResource("images/0.gif")),
            toolkit.getImage(Explode.class.getResource("images/1.gif")),
            toolkit.getImage(Explode.class.getResource("images/2.gif")),
            toolkit.getImage(Explode.class.getResource("images/3.gif")),
            toolkit.getImage(Explode.class.getResource("images/4.gif")),
            toolkit.getImage(Explode.class.getResource("images/5.gif")),
            toolkit.getImage(Explode.class.getResource("images/6.gif")),
            toolkit.getImage(Explode.class.getResource("images/7.gif")),
            toolkit.getImage(Explode.class.getResource("images/8.gif")),
            toolkit.getImage(Explode.class.getResource("images/9.gif")),
            toolkit.getImage(Explode.class.getResource("images/10.gif"))
    };
    /**
     * 爆炸的半径变化
     */
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

        if (step == images.length) {
            live = false;
            step = 0;
            return;
        }
        
        g.drawImage(images[step], x, y, null);
        step ++;
    }
}
