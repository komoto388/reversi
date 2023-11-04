package common;

public class Global {

    /* システムに関する定数 */
    /** 正常終了時の終了コード */
    public static final int EXIT_SUCCESS = 0;

    /** 何らかの理由による異常終了時の終了コード */
    public static final int EXIT_FAILURE = 1;
    
    /* リバーシに関する定数 */
    /** リバーシ盤の幅（マス） */
    public static final int BOARD_WIDTH = 8;

    /** リバーシ盤の高さ（マス） */
    public static final int BOARD_HEIGHT = 8;

    /** デフォルトで使用するアルゴリズムの要素番号 */
    public static final int DEFAULT_ALGORITHM = 0;
    
    /** 先手・黒の名前のデフォルト値 */
    public static final String DEFAULT_PLAYER_NAME_BLACK = "プレイヤー1";

    /** 後手・白の名前のデフォルト値 */
    public static final String DEFAULT_PLAYER_NAME_WHITE= "プレイヤー2";

    /* 画面描画のデザインに関する定数 */
    /** ルートペインの横幅 */
    public static final double ROOT_PANE_WIDTH = 1152.0;

    /** ルートペインの高さ */
    public static final double ROOT_PANE_HEIGHT = 864.0;

    /** 結果表示のタブ内に表示するペインの横幅 */
    public static final double RESULT_TAB_PANE_WIDTH = 640.0;

    /** 結果表示のタブ内に表示するペインの高さ */
    public static final double RESULT_TAB_PANE_HEIGHT = 480.0;

    /** アルゴリズム選択のラジオボタンのオブジェクトの幅 */
    public static final double RADIO_BUTTON_WIDTH = 280.0;

    /** アルゴリズム選択のラジオボタンのオブジェクトの高さ */
    public static final double RADIO_BUTTON_HEIGHT = 40.0;

    /** ゲーム中に表示する、リバーシ盤のマスの大きさ（縦・横同じ） */
    public static final double GRID_SIZE = 70.0;

    /** 結果画面に表示する、リバーシ盤のマスの大きさ（縦・横同じ） */
    public static final double GRID_SIZE_RESULT = 50.0;

    /* 画面描画のタイマーイベントに関する定数 */
    /** 1秒間に画面描画する回数(FPS) */
    public static final int FPS = 30;

    /** 最初に画面描写してからゲーム開始するまでに処理を待つ時間（ミリ秒） */
    public static final int WAIT_MILLISEC_START = 800;

    /** ターン間に操作を待つ時間（ミリ秒） */
    public static final int WAIT_MILLISEC_INTERVAL = 500;
}
