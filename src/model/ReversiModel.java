package model;

import common.Convert;
import common.Global;
import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

/**
 * ゲームのイベント状態の値
 */
enum EventStatusValue {
    PLAY("プレイヤー操作中"), PLAY_MANUAL("プレイヤー操作中(マニュアル)"), PLAY_COM("プレイヤー操作中(COM)"), SKIP("スキップ処理"), WAIT(
            "インターバル中"), JUDGE("判定処理"), WAIT_FINAL("終了待ち"), FINISH("終了");

    private final String name;

    private EventStatusValue(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

/**
 * リバーシのゲーム処理を行うクラス
 * @author komoto
 */
public class ReversiModel {

    /** リバーシを制御するインスタンス */
    private final Reversi reversi;

    /** 先手・黒のプレイヤーのインスタンス */
    private final Player playerBlack;

    /** 後手・白のプレイヤーのインスタンス */
    private final Player playerWhite;

    /** ゲームのイベント状態を表す */
    private EventStatus eventStatus;

    /** ゲームの勝敗結果を表す */
    private ResultType result;

    /** リバーシ盤の手動入力を無効する時間（フレーム数） */
    private int waitFrame;

    /** デバッグ情報を表示・非表示を表すフラグ */
    private final Boolean isDebug;

    /** ゲームが終了したことを表すフラグ */
    private Boolean isFinish;

    /** 画面のステータス欄に表示する文字列 */
    private String statusString;

    /** 画面のデバッグ欄に表示する文字列 */
    private String debugString;

    public ReversiModel(ReversiData data) {

        this.reversi = data.getReversi();
        this.playerBlack = data.getPlayerBlack();
        this.playerWhite = data.getPlayerWhite();
        this.isDebug = data.getIsDebug();

        this.eventStatus = new EventStatus(reversi, EventStatusValue.WAIT);
        this.result = ResultType.None;
        this.isFinish = false;
        this.statusString = null;
        this.debugString = "デバッグ情報は特にありません";

        setWaitTime(Global.WAIT_MILLISEC_START);
    }

    public Reversi getReversi() {
        return reversi;
    }

    public Board getBoard() {
        return reversi.getBoard();
    }

    public Dimension getBoardSize() {
        return reversi.getBoard().getSize();
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public Boolean getIsDebug() {
        return isDebug;
    }

    public Boolean getIsControll() {
        return eventStatus.getIsControll();
    }

    public Boolean getIsFinish() {
        return isFinish;
    }

    public String getEventStatus() {
        return eventStatus.getName();
    }

    public String getGameStatusString() {
        return statusString;
    }

    public String getDebugString() {
        return debugString;
    }

    public int getWaitFrame() {
        return waitFrame;
    }

    /**
     * リバーシのゲームイベントを処理する
     * @param プレイヤーが石をおいた座標。処理中に石を置かなかった場合には {@code NULL} を返す。
     */
    public Dimension run() {
        Dimension target = null;

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
            target = reversi.run();
            put(target);
            break;
        }
        case SKIP: {
            // 石がどこにも置けない時のスキップ処理を定義
            statusString = Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " はスキップします。";
            reversi.next();
            eventStatus.set(EventStatusValue.WAIT);
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
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
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
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
        // 座標に対して、石を置けるか判定する
        Boolean isPut = false;
        try {
            isPut = reversi.put(target);
        } catch (IllegalArgumentException e) {
            // 例外が発生した場合、石を置けないと判断して処理を続ける。
            e.printStackTrace();
            isPut = false;
        }

        if (isPut) {
            String playerString = Convert.getPlayerColor(reversi.getPlayerIsBlack());
            statusString = String.format("%s は %s に石を置きました。", playerString, target.getString());

            eventStatus.set(EventStatusValue.JUDGE);
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
        } else {
            statusString = String.format("%s には石を置けません", target.getString());
        }

        return isPut;
    }

    /**
     * 勝敗判定を行う
     */
    private void judge() {
        try {
            result = reversi.judge();
            switch (result) {
            case None: {
                reversi.next();
                eventStatus.set(EventStatusValue.WAIT);
                setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
                break;
            }
            case Drow:
            case Black:
            case White: {
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + reversi.judge());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultType.None;
        }
    }

    /**
     * 処理の待ち時間を設定する
     * @param waitMilliSec 待ち時間。
     */
    private void setWaitTime(int waitMilliSec) {
        try {
            if (waitMilliSec <= 0) {
                throw new IllegalArgumentException("待ち時間の値が0以下です: " + waitMilliSec);
            }
            waitFrame = Convert.convertFrame(waitMilliSec, Global.FPS);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("処理待ち時間をなし、画面操作を可能な状態として設定します。");
            waitFrame = 0;
            eventStatus.set(EventStatusValue.PLAY);
        }
    }

    /**
     * 結果画面に渡すデータを生成する
     */
    public ResultData generateData() {
        ResultData data = new ResultData(reversi, playerBlack, playerWhite, result);

        return data;
    }
}

/**
 * イベントステータスを操作するクラス
 */
class EventStatus {

    /** リバーシを制御するインスタンス */
    private final Reversi reversi;

    /** 現在のイベントステータス */
    private EventStatusValue eventStatus;

    /** リバーシ盤の操作可否フラグ */
    private Boolean isControll;

    /**
     * 初期化する
     * @param reversi リバーシを制御するインスタンス
     * @param status イベントステータスの初期値
     */
    public EventStatus(Reversi reversi, EventStatusValue status) {
        this.reversi = reversi;
        set(status);
    }

    /**
     * イベントステータスの値を取得する
     * @return イベントステータスの値
     */
    public EventStatusValue getStatus() {
        return eventStatus;
    }

    public Boolean getIsControll() {
        return isControll;
    }

    /**
     * イベントステータスの名前を取得する
     * @return イベントステータスの名前の文字列
     */
    public String getName() {
        return eventStatus.getName();
    }

    /**
     * イベントステータスの値を設定する
     * @param eventStatus 設定するイベントステータスの値
     */
    public void set(EventStatusValue eventStatus) {
        // 値が PLAY の場合、値を PLAY_MANUAL または PLAY_COM に付け替える
        if (eventStatus == EventStatusValue.PLAY) {
            if (reversi.isCurrentPlayerManual()) {
                eventStatus = EventStatusValue.PLAY_MANUAL;
            } else {
                eventStatus = EventStatusValue.PLAY_COM;
            }
        }

        // ステータス、リバーシ盤操作可否の更新
        this.eventStatus = eventStatus;

        if (eventStatus == EventStatusValue.PLAY_MANUAL) {
            isControll = true;
        } else {
            isControll = false;
        }
    }
}
