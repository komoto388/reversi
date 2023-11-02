package common;

public class Global {
    
    /* リバーシに関する定数 */
    /** リバーシ盤の幅（マス） */
    public static final int BOARD_WIDTH = 8;

    /** リバーシ盤の高さ（マス） */
    public static final int BOARD_HEIGHT = 8;

    /** デフォルトで使用するアルゴリズムの要素番号 */
    public static final int DEFAULT_ALGORITHM = 0;
    
    /* 画面描画のデザインに関する定数 */
    /** ルートペインの横幅 */
    public static final int ROOT_PANE_WIDTH = 1000;

    /** ルートペインの高さ */
    public static final int ROOT_PANE_HEIGHT = 800;

    /** 結果表示のタブ内に表示するペインの横幅 */
    public static final int RESULT_TAB_PANE_WIDTH = 640;

    /** 結果表示のタブ内に表示するペインの高さ */
    public static final int RESULT_TAB_PANE_HEIGHT = 480;
    
    /** アルゴリズム選択のラジオボタンのオブジェクトの幅 */
    public static final int RADIO_BUTTON_WIDTH = 280;

    /** アルゴリズム選択のラジオボタンのオブジェクトの高さ */
    public static final int RADIO_BUTTON_HEIGHT = 40;
    
    /** ゲーム中に表示する、リバーシ盤のマスの大きさ（縦・横同じ） */
    public static final double GRID_SIZE = 60;
    
    /** 結果画面に表示する、リバーシ盤のマスの大きさ（縦・横同じ） */
    public static final double GRID_SIZE_RESULT = 50;
    
    /* 画面描画のタイマーイベントに関する定数 */
    /** 1秒間に画面描画する回数(FPS) */
    public static final int FPS = 30;

    /** プレイヤーの入力待ち状態の時に設定する値（値の変更不可） */
    public static final int WAIT_FRAME_INFINITE = -1;

    /** 最初に画面描写してからゲーム開始するまでに処理を待つ時間（ミリ秒） */
    public static final int WAIT_MILLISEC_START = 800;
    
    /** ターン間に操作を待つ時間（ミリ秒） */
    public static final int WAIT_MILLISEC_INTERVAL = 500;
}
