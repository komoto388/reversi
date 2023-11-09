package gui;

/**
 * 実際のFPSの計測・算出するクラス
 * @author komoto
 */
class Fps {

    /** FPSを計測する単位時間の定数（ミリ秒） */
    private final int FPS_INTERVAL = 1000;

    /** FPS計測時間の開始時刻(1秒ごとに更新) */
    private long startTime;

    /** FPS計測時間に描画したフレーム数(1秒ごとにリセット) */
    private int countFrame;

    /** 現在のFPS */
    private double currnetFps;

    /**
     * 初期化を行う。
     */
    public Fps() {
        startTime = System.currentTimeMillis();
        countFrame = 0;
        currnetFps = 0;
    }

    /**
     * 現在のFPSを取得する
     * @return 現在のFPSの値
     */
    public double getFps() {
        return currnetFps;
    }

    /**
     * 現在の時間と描画フレーム数を更新する<br>
     * 本メソッドはフレーム描画時に使用する。
     */
    public void update() {
        long currentTime = System.currentTimeMillis();
        countFrame++;

        if (currentTime - startTime >= FPS_INTERVAL) {
            currnetFps = countFrame * 1000 / (double) (currentTime - startTime);
            startTime = currentTime;
            countFrame = 0;
        }
    }
}
