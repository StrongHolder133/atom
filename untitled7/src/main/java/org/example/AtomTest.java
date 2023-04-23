package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


public class AtomTest extends JPanel implements ActionListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private final Point origin = new Point(WIDTH / 2, HEIGHT / 2);
    private Point mousePt;

    private final double unit = 20;

    private final JButton btnCreateCube;
    private final JButton btnCreateSphere;
    private final JButton btnSelect;
    private final JTextField txtLowerBound;
    private final JTextField txtUpperBound;
    private final int[] selectedArea = new int[2];
    private boolean isCube = false, isSphere = false;

    public AtomTest() {

        btnCreateCube = new JButton("Создать куб");
        btnCreateSphere = new JButton("Создать шар");
        btnSelect = new JButton("Выделить");
        Label dLabel = new Label("Нижняя граница");
        Label upLabel = new Label("Верхняя граница");
        txtLowerBound = new JTextField(2);
        txtUpperBound = new JTextField(2);
        btnCreateCube.addActionListener(this);
        btnCreateSphere.addActionListener(this);
        btnSelect.addActionListener(this);

        add(btnCreateCube);
        add(btnCreateSphere);
        add(btnSelect);
        add(dLabel);
        add(txtLowerBound);
        add(upLabel);
        add(txtUpperBound);

        selectedArea[0] = -1;
        selectedArea[1] = -1;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - mousePt.x;
                int dy = e.getY() - mousePt.y;
                origin.setLocation(origin.x + dx, origin.y + dy);
                mousePt = e.getPoint();
                repaint();
            }
        });
    }

    public void coordinateSystem(Graphics g) {
        g.drawLine(WIDTH, origin.y, origin.x, origin.y);
        g.drawLine(origin.x, 0, origin.x, origin.y);
        g.drawLine(origin.x, origin.y, origin.x - 1000, origin.y + 1000);
        for (int i = 1; i < WIDTH; i++) {
            g.drawLine(origin.x + (int) unit * i, origin.y - 2,origin.x + (int) unit * i, origin.y + 2);
            g.drawString("" + i, (origin.x + (int) unit * i) - 3, origin.y + 15);
        }
        for (int i = 1; i < HEIGHT; i++) {
            g.drawLine(origin.x - 2, origin.y - (int) unit * i, origin.x + 2, origin.y - (int) unit * i);

            g.drawString("" + i, origin.x - 15, (origin.y - (int) unit * i) + 3);
        }
        for (int i = 1; i < HEIGHT; i++) {
            g.drawString("" + i, origin.x - (18 + i * (int) unit / 2), (origin.y - (int) unit / 2 * (-i)) + 6);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        coordinateSystem(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLACK);
        if (isCube) {
            g.setColor(Color.BLUE);
            int rectHigh = (int) (10 * unit);
            Point p1 = new Point(origin.x, origin.y - (rectHigh));
            Point p2 = new Point(origin.x + rectHigh, origin.y - rectHigh);
            Point p3 = new Point(origin.x, origin.y);
            Point p4 = new Point(origin.x + rectHigh, origin.y);
            Point p5 = new Point(origin.x - (int) (5 * unit), origin.y - (int) (5 * unit));
            Point p6 = new Point(origin.x + (int) (5 * unit), origin.y - (int) (5 * unit));
            Point p7 = new Point(origin.x - (int) (5 * unit), origin.y + (int) (5 * unit));
            Point p8 = new Point(origin.x + (int) (5 * unit), origin.y + (int) (5 * unit));
            if (selectedArea[0] != -1 && selectedArea[1] != -1) {
                rectHigh = (int) ((selectedArea[1] - selectedArea[0]) * unit);
                p1.y += (int) ((10-selectedArea[1]) * unit);
                g2.draw3DRect(p1.x, p1.y, (int) (10 * unit),rectHigh, true);
                p5.y += (int) ((10-selectedArea[1]) * unit);
                g2.draw3DRect(p5.x, p5.y,(int) (10 * unit),rectHigh, true);
                p2.y = p1.y;
                p6.y = p5.y;
                p3.y = p1.y + rectHigh;
                p7.y = p5.y + rectHigh;
                p4.y = p3.y;
                p8.y = p7.y;
            } else {
                g2.draw3DRect(p1.x, p1.y, rectHigh, rectHigh, true);
                g2.draw3DRect(p5.x, p5.y, rectHigh, rectHigh, true);
            }
            g2.drawLine(p1.x, p1.y, p5.x, p5.y);
            g2.drawLine(p2.x, p2.y, p6.x, p6.y);
            g2.drawLine(p3.x, p3.y, p7.x, p7.y);
            g2.drawLine(p4.x, p4.y, p8.x, p8.y);
        } else if (isSphere) {
            g.setColor(Color.RED);
            if (selectedArea[0] != -1 && selectedArea[1] != -1) {
                Shape oldClip = g2.getClip();
                Rectangle newClip = new Rectangle(origin.x, (int) (origin.y - (int) (10 * unit) + (10 - selectedArea[1]) * unit), 200, (int) ((selectedArea[1] - selectedArea[0]) * unit)); // новый clip - прямоугольник размером 200x100, начинающийся в точке (0, 0)
                g2.clip(newClip);
                g2.fillOval(origin.x, origin.y - (int) (10 * unit), (int) (10 * unit), (int) (10 * unit));
                g2.setClip(oldClip);
            } else {
                g2.fillOval(origin.x, origin.y - (int) (10 * unit), (int) (10 * unit), (int) (10 * unit));
            }
        }
    }

    static JFrame getFrame() {
        JFrame jFrame = new JFrame() {
        };
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(800, 800);
        return jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreateCube) {
            isCube = true;
            isSphere = false;
            selectedArea[0] = -1;
            selectedArea[1] = -1;
            repaint();
        } else if (e.getSource() == btnCreateSphere) {
            isSphere = true;
            isCube = false;
            selectedArea[0] = -1;
            selectedArea[1] = -1;
            repaint();
        } else if (e.getSource() == btnSelect) {
            if (!txtLowerBound.getText().isEmpty() && !txtUpperBound.getText().isEmpty()) {
                int lower = Integer.parseInt(txtLowerBound.getText());
                int upper = Integer.parseInt(txtUpperBound.getText());
                if (lower >= 0 && upper <= 10 && lower < upper) {
                    selectedArea[0] = lower;
                    selectedArea[1] = upper;
                    repaint();
                }
            }
        }
    }

    public static void main(String[] args) {
        AtomTest atomTest = new AtomTest();
        atomTest.setVisible(true);
        JFrame jFrame = getFrame();
        jFrame.add(atomTest);
    }
}
