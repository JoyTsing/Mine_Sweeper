import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AlgoVisualizer {

    private MineData data;        // 数据
    private AlgoFrame frame;    // 视图
    private static final int blockSide = 32;
    private static final int DELAY = 10;
    private AlgoMouseListener mouseListener;
    public AlgoVisualizer(int N, int M, int mineNumber) {

        // 初始化数据
        data = new MineData(N, M, mineNumber);

        // 初始化视图
        int sceneWidth = M * blockSide;
        int sceneHeight = N * blockSide;

        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("Mine", sceneWidth, sceneHeight);
            mouseListener=new AlgoMouseListener();
            frame.addMouseListener(mouseListener);
            new Thread(() -> {
                run();
            }).start();
        });
    }

    // 动画逻辑
    private void run() {
        setData(false,-1,-1);
    }

    private void setData(boolean isLeftClicked,int x,int y) {
        if(data.inArea(x,y)){
            if(isLeftClicked) {

                if(data.isMine(x,y)){
                    data.open[x][y]=true;
                    frame.removeMouseListener(mouseListener);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(new JPanel(),"ops!!!,踩到雷了!","游戏结束",JOptionPane.WARNING_MESSAGE);
                            frame.dispose();
                            System.exit(0);
                        }
                    }).start();
                }
                else
                    data.open(x,y);

            }
            else
                data.flags[x][y]=!data.flags[x][y];
        }
        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }

    private class AlgoMouseListener extends MouseAdapter {
        @Override
        public void mouseReleased(MouseEvent event) {
            event.translatePoint(
                    -(int)(frame.getBounds().width-frame.getCanvasWidth()),
                    -(int)(frame.getBounds().height-frame.getCanvasHeight())
            );
            Point pos=event.getPoint();

            int w=frame.getCanvasWidth()/data.getM();
            int h=frame.getCanvasHeight()/data.getN();

            int x=pos.y/h;
            int y=pos.x/w;

            if(SwingUtilities.isLeftMouseButton(event))
                setData(true,x,y);
            else
                setData(false,x,y);
        }
    }

    public static void main(String[] args) {
        int N = 20;
        int M = 20;
        int mineNumber = 50;
        AlgoVisualizer vis = new AlgoVisualizer(N, M, mineNumber);
    }
}