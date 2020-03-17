/**
 * @description：TODO
 */
public class MineData {
    public static final String blockImageURL = "resources/block.png";
    public static final String flagImageURL = "resources/flag.png";
    public static final String mineImageURL = "resources/mine.png";

    public static String numberImageURL(int num) {
        if (num < 0 || num > 8)
            throw new IllegalArgumentException("resources Error");
        return "resources/" + num + ".png";
    }

    private int N, M;
    private boolean[][] mines;

    //一个地区周围8个方向有几个雷
    private int[][] numbers;
    public boolean[][] open;
    public boolean[][] flags;

    public MineData(int N, int M, int mineNumber) {
        if (N <= 0 || M <= 0)
            throw new IllegalArgumentException("size Wrong");

        if (mineNumber < 0 || mineNumber >= N * M)
            throw new IllegalArgumentException("mineNumber Wrong");

        this.N = N;
        this.M = M;

        mines = new boolean[N][M];
        open = new boolean[N][M];
        flags = new boolean[N][M];
        numbers = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                numbers[i][j] = 0;
            }
        }
        generateMines(mineNumber);
        calculateNumbers();
    }

    private void calculateNumbers() {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mines[i][j]) {
                    numbers[i][j] = -1;
                    continue;
                }
                numbers[i][j] = 0;
                for (int k = i - 1; k <= i + 1; k++) {
                    for (int l = j - 1; l <= j + 1; l++) {
                        if (inArea(k, l) && mines[k][l])
                            ++numbers[i][j];
                    }
                }
            }
        }

    }

    //均匀生成雷
    private void generateMines(int mineNumber) {

        for (int i = 0; i < mineNumber; i++) {
            int x = i / M;
            int y = i % M;
            mines[x][y] = true;
        }

        for (int i = N * M - 1; i >= 0; i--) {
            int iX = i / M;
            int iY = i % M;

            int randNumber = (int) (Math.random() * (i + 1));

            int randX = randNumber / M;
            int randY = randNumber % M;

            swap(iX, iY, randX, randY);
        }


    }

    private void swap(int x1, int y1, int x2, int y2) {
        boolean tmp = mines[x1][y1];
        mines[x1][y1] = mines[x2][y2];
        mines[x2][y2] = tmp;
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public int getNumber(int x, int y) {
        return numbers[x][y];
    }

    public boolean isMine(int x, int y) {
        if (!inArea(x, y))
            throw new IllegalArgumentException("index Error");
        return mines[x][y];
    }

    public boolean inArea(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < M;
    }

    //floodFill
    public void open(int x, int y) {
        if (!inArea(x, y))
            throw new IllegalArgumentException("Index Wrong");

        if (isMine(x, y))
            throw new IllegalArgumentException("Mine");

        open[x][y] = true;

        if (numbers[x][y] > 0)
            return;

        //8方向拓展
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (inArea(i, j) && !open[i][j] && !mines[i][j])
                    open(i, j);
            }
        }
    }

}
