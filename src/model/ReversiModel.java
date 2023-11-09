package model;

import common.Convert;
import common.Global;
import gamerecord.GameRecord;
import reversi.Dimension;
import reversi.Disc;
import reversi.Player;
import reversi.ResultType;

/**
 * リバーシのゲーム処理を行うクラス
 * @author komoto
 */
public class ReversiModel extends BaseModel {

    /** ゲームのイベント状態を表す */
    private EventStatus eventStatus;

    /** ゲームの勝敗結果を表す */
    private ResultType result;

    /** 街状態になった時に待機する時間(ミリ秒) */
    private final int waitInterval;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** デバッグ情報を表示・非表示を表すフラグ */
    private final Boolean isDebug;

    /** ゲームが終了したことを表すフラグ */
    private Boolean isFinish;

    /** 最近の石が置かれた座標 */
    private Dimension latestTarget;

    /** 画面のステータス欄に表示する文字列 */
    private String statusString;

    /** 画面のデバッグ欄に表示する文字列 */
    private String debugString;

    /** 棋譜の記録を行うインスタンス */
    private GameRecord gameRecord;

    /**
     * リバーシゲーム実行のデータ処理を行うモデル
     * @param data ゲーム実行に必要なデータ
     * @param isGui 動作する環境を表す。GUIの場合は真 {@code true}, CUIの場合は {@code false} にする。
     */
    public ReversiModel(ReversiData data, Boolean isGui) {
        super(data.getReversi(), data.getPlayerBlack(), data.getPlayerWhite());

        this.isDebug = data.getIsDebug();

        this.eventStatus = new EventStatus(reversi, EventStatusValue.PLAY);
        this.result = ResultType.NONE;
        this.isFinish = false;
        this.latestTarget = null;
        this.statusString = null;
        this.debugString = "デバッグ情報は特にありません";
        this.gameRecord = new GameRecord();

        if (isGui) {
            setWaitTime(Global.WAIT_MILLISEC_START);
            waitInterval = Global.WAIT_MILLISEC_INTERVAL;
        } else {
            // CUIではFPSの描画ではないため使用できない。そのため0に設定する。
            setWaitTime(0);
            waitInterval = 0;
        }
    }

    /**
     * デバッグ情報を表示するかを表す
     * @return 表示する場合は真 {@code true}, 表示しない場合は偽 {@code false} を返す
     */
    public Boolean isDebugMode() {
        return isDebug;
    }

    /**
     * リバーシ盤が操作可能（ユーザの操作待ち状態）かどうかを表す
     * @return 操作可能の場合は真 {@code true}, 操作不可の場合は偽 {@code false} を返す
     */
    public Boolean canUserControll() {
        return eventStatus.canUserControll();
    }

    /**
     * ゲームが終了したかを表すフラグを取得する
     * @return ゲームが終了しているの場合は真 {@code true}, 終了していない場合は偽 {@code false} を返す
     */
    public Boolean isGameFinish() {
        return isFinish;
    }

    /**
     * 最近置いた石の座標を取得する
     * @return 最近置いた石の座標
     */
    public Dimension getLatestTarget() {
        return latestTarget;
    }

    /**
     * イベントステータスを取得する<br>
     * 待機フレームが1以上で待機中の場合は、文字列に「(待機中)」を追加して表示する。
     * @return イベントステータスの名前の文字列
     */
    public String getEventStatus() {
        String eventStatusString = eventStatus.getName();
        if (waitFrame > 0) {
            eventStatusString += " (待機中)";
        }

        return eventStatusString;
    }

    /**
     * ゲームステータス情報を取得する
     * @return ゲームステータスの文字列
     */
    public String getGameStatusString() {
        return statusString;
    }

    /**
     * デバッグ情報を取得する
     * @return デバッグ情報の文字列
     */
    public String getDebugString() {
        return debugString;
    }

    /**
     * 現在の待機フレーム数を取得する
     * @return 現在の待機フレーム数
     */
    public int getWaitFrame() {
        return waitFrame;
    }

    /**
     * リバーシのゲームイベントを処理する
     */
    public Dimension run() {
        Dimension target = null;

        // 待機フレーム数を元にイベント処理を行うか判断する。
        // 待機フレーム数が1以上の時はインターバル中でイベント処理は行わない。
        // 待機フレーム数が0の時はイベント処理を進める。
        if (waitFrame > 0) {
            // 待機フレーム数の更新
            waitFrame--;
        } else {
            // スキップ処理
            if (eventStatus.getStatus() == EventStatusValue.PLAY && reversi.isSkip()) {
                eventStatus.set(EventStatusValue.SKIP);
            }

            // イベントステータス PLAY のステータス付け替え
            if (eventStatus.getStatus() == EventStatusValue.PLAY) {
                eventStatus.relabelPlayerStatus();
            }

            // イベントステータスに基づいたイベントを処理する
            try {
                target = runBasedEventStatus(eventStatus.getStatus());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return target;
    }

    /**
     * イベントステータスに従い、イベントを処理する
     * @param eventStatusValue イベントステータスの値
     * @return プレイヤーが算出した設置した石の座標。プレイヤー処理がない場合は {@code NULL}
     * @throws IllegalArgumentException イベントステータスの値が PLAY, または未定義の不正な値
     */
    private Dimension runBasedEventStatus(EventStatusValue eventStatusValue) throws IllegalArgumentException {
        Dimension target = null;
        Player currentPlayer = reversi.getCurrentPlayer();

        switch (eventStatusValue) {
        case PLAY_MANUAL: {
            // プレイヤーが手動入力の時は、何もしない
            break;
        }
        case PLAY_COM: {
            // アルゴリズムに従い処理を行う
            target = currentPlayer.run(board);
            put(target);
            break;
        }
        case SKIP: {
            // 棋譜にスキップを記録する
            gameRecord.addAsSkip(reversi.getTurnCount(), currentPlayer, board.getDiscNum(Disc.BLACK),
                    board.getDiscNum(Disc.WHITE));

            // 石がどこにも置けない時のスキップ処理を定義
            statusString = currentPlayer.getUseDisc().getPrefixForPlayerName() + " はスキップします。";
            reversi.next();
            eventStatus.set(EventStatusValue.PLAY);
            setWaitTime(waitInterval);
            break;
        }
        case JUDGE: {
            judge();
            break;
        }
        case FINISH: {
            isFinish = true;
            break;
        }
        case PLAY: {
            throw new IllegalArgumentException("イベントステータスが \"PLAY\" での本メソッドでの使用は想定されていません: " + eventStatusValue);
        }
        default:
            throw new IllegalArgumentException("Unexpected value: " + eventStatusValue);
        }

        return target;
    }

    /**
     * 石の設置を行う
     * @param target プレイヤーが石を置く座標
     * @return 石の設置ができた場合は真 {@code true}, 既に石が存在している等で設置できなかった場合は偽 {@code false} を返す。
     */
    public Boolean put(Dimension target) {
        Boolean isPut = false;
        Player currentPlayer = reversi.getCurrentPlayer();

        // 石の設置処理
        try {
            isPut = reversi.put(target);
        } catch (IllegalArgumentException e) {
            // 例外が発生した場合、石を置けないと判断して処理を続ける。
            e.printStackTrace();
            debugString = "出力された石の座標が NULL のため、プレイヤーの手はスキップとします";
            System.err.println(debugString);

            eventStatus.set(EventStatusValue.SKIP);
            setWaitTime(waitInterval);

            return false;
        }

        if (isPut) {
            /** 石の設置に成功した場合は値の更新処理を行い、イベントステータスを変更して判定処理に進める */

            // 棋譜を記録する
            gameRecord.add(reversi.getTurnCount(), currentPlayer, board.getDiscNum(Disc.BLACK),
                    board.getDiscNum(Disc.WHITE), target.getString());

            // 表示文字列、イベントステータスなど値の更新
            latestTarget = target;
            statusString = String.format("%s は %s に石を置きました。", currentPlayer.getUseDisc().getPrefixForPlayerName(),
                    target.getString());
            eventStatus.set(EventStatusValue.JUDGE);
        } else {
            /** 石の設置に失敗した場合、次のターンに進めない */
            debugString = String.format("%s には石を置けません", target.getString());
        }

        return isPut;
    }

    /**
     * 勝敗判定を行う
     */
    private void judge() {
        result = reversi.judge(gameRecord);

        if (result == ResultType.NONE) {
            // イベントステータスを更新し、次のターンに進める
            reversi.next();
            eventStatus.set(EventStatusValue.PLAY);
        } else {
            eventStatus.set(EventStatusValue.FINISH);
        }
        setWaitTime(waitInterval);
    }

    /**
     * 処理の待ち時間を設定する
     * @param waitMilliSec 待ち時間。
     */
    private void setWaitTime(int waitMilliSec) {
        try {
            waitFrame = Convert.convertFrame(waitMilliSec, Global.FPS);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("処理待ち時間をなし、画面操作を可能な状態として設定します。");
            waitFrame = 0;
            eventStatus.set(EventStatusValue.PLAY);
        }
    }

    /**
     * 結果画面の実行に必要なデータをエクスポートする
     * @return 結果画面処理の実行に必要なデータ
     */
    public ResultData exportForResult() {
        ResultData data = new ResultData(reversi, playerBlack, playerWhite, result, gameRecord);

        return data;
    }
}
