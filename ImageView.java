import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ImageView extends JPanel implements MouseListener, MouseMotionListener {
    public transient Image image = null;
    public Grid mesh;
    public String path = null;
    private int width, height;
    private int mwidth, mheight;
    private int movingX, movingY;
    private int offsetx, offsety;

    public ImageView(Image img, String pth, Grid m) {
        this(img, pth);
        mesh = m;
    }

    public ImageView(Image img, String absolutePath) {
        super();
        path = absolutePath;
        image = img;

        while (img.getWidth(this) == -1) ;
        width = img.getWidth(this);

        while (img.getHeight(this) == -1) ;
        height = img.getHeight(this);

        mesh = new Grid(MainMorph.XMESH, MainMorph.YMESH, width, height);
        mwidth = MainMorph.XMESH;
        mheight = MainMorph.YMESH;

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void drawMesh(Graphics g, int offsetx, int offsety) {
        this.offsetx = offsetx;
        this.offsety = offsety;

        for (int i = 0; i < mwidth; i++) {
            for (int j = 0; j < mheight; j++) {
                Point p = mesh.points[i][j];

                /* Draw connecting lines */
                g.setColor(Color.white);
                if (j != mheight - 1) {
                    Point next = mesh.points[i][j + 1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if (i != mwidth - 1) {
                    Point next = mesh.points[i + 1][j];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }
                if (j != mheight - 1 && i != mwidth - 1) {
                    Point next = mesh.points[i + 1][j + 1];
                    g.drawLine(p.x + offsetx, p.y + offsety, next.x + offsetx, next.y + offsety);
                }

                /* Draw circles */
                int radius = 6;
                g.setColor(Color.black);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);

                radius = 3;
                g.setColor(Color.green);
                g.fillOval(offsetx - radius + p.x, offsety - radius + p.y, 2 * radius, 2 * radius);


            }
        }
    }

    public void update(Graphics g) {
        int offsetx = 20, offsety = 20;

        g.drawImage(image, offsetx, offsety, this);
        drawMesh(g, offsetx, offsety);
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void paintComponent(Graphics g) {
        paint(g);
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX() - offsetx;
        int y = e.getY() - offsety;

        for (int i = 0; i < mwidth; i++) {
            for (int j = 0; j < mheight; j++) {
                Point p = mesh.points[i][j];
                int radius = 6;
                if (p.distance(x, y) <= radius) {
                    if (i != 0 && i != mwidth - 1 && j != 0 && j != mheight - 1) {
                        movingX = i;
                        movingY = j;
                        return;
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        movingX = movingY = -1;
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() - offsetx;
        int y = e.getY() - offsety;

        if (movingX != -1 && movingY != -1) {
            mesh.points[movingX][movingY].x = x;
            mesh.points[movingX][movingY].y = y;
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}