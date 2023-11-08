package model;

import common.Convert;
import common.Global;
import gamerecord.GameRecord;
import reversi.Dimension;
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

        this.eventStatus = new EventStatus(reversi, EventStatusValue.WAIT);
        this.result = ResultType.None;
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
     * イベントステータスの名前を取得する
     * @return イベントステータスの名前の文字列
     */
    public String getEventStatus() {
        return eventStatus.getName();
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
     * @param プレイヤーが石をおいた座標。処理中に石を置かなかった場合には {@code NULL} を返す。
     */
    public Dimension run() {
        Dimension target = null;
        Player currentPlayer = reversi.getCurrentPlayer();

        // ステータスを設定する
        if (eventStatus.getStatus() == EventStatusValue.PLAY_MANUAL
                || eventStatus.getStatus() == EventStatusValue.PLAY_COM) {
            if (reversi.isSkip()) {
                eventStatus.set(EventStatusValue.SKIP);
            }
        }

        switch (eventStatus.getStatus()) {
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
            gameRecord.addAsSkip(reversi.getTurnCount(), currentPlayer.isBlack(), board.getDiscNum(true),
                    board.getDiscNum(false));

            // 石がどこにも置けない時のスキップ処理を定義
            statusString = Convert.getPlayerColor(currentPlayer.isBlack()) + " はスキップします。";
            reversi.next();
            eventStatus.set(EventStatusValue.WAIT);
            setWaitTime(waitInterval);
            break;
        }
        case WAIT: {
            // 待ち状態の場合は何もせず、画面描画のみ行う
            if (waitFrame <= 0) {
                eventStatus.set(EventStatusValue.PLAY);
            }
            break;
        }
        case JUDGE: {
            judge();
            if (result != ResultType.None) {
                eventStatus.set(EventStatusValue.WAIT_FINAL);
            } else {
                eventStatus.set(EventStatusValue.WAIT);
            }
            setWaitTime(waitInterval);
            break;
        }
        case WAIT_FINAL: {
            if (waitFrame <= 0) {
                eventStatus.set(EventStatusValue.FINISH);
            }
            break;
        }
        case FINISH: {
            isFinish = true;
            break;
        }
        case PLAY:
        default:
            throw new IllegalArgumentException("Unexpected value: " + eventStatus);
        }

        // 待機フレーム数の更新
        if (waitFrame > 0) {
            waitFrame--;
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
            gameRecord.add(reversi.getTurnCount(), currentPlayer.isBlack(), board.getDiscNum(true),
                    board.getDiscNum(false), target.getString());

            // 表示文字列、イベントステータスなど値の更新
            latestTarget = target;
            String playerString = Convert.getPlayerColor(currentPlayer.isBlack());
            statusString = String.format("%s は %s に石を置きました。", playerString, target.getString());
            eventStatus.set(EventStatusValue.JUDGE);
            setWaitTime(waitInterval);
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

        if (result == ResultType.None) {
            // イベントステータスを更新し、次のターンに進める
            reversi.next();
            eventStatus.set(EventStatusValue.WAIT);
            setWaitTime(waitInterval);
        }
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
