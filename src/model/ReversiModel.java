package model;

import common.Convert;
import common.Global;
import reversi.Board;
import reversi.Dimension;
import reversi.Player;
import reversi.ResultType;
import reversi.Reversi;

public class ReversiModel {

    /**
     * ゲームのイベント状態の値
     */
    public enum EventStatus {
        PLAY("ゲーム処理"), SKIP("スキップ処理"), WAIT("インターバル中"), JUDGE("判定処理"), WAIT_FINAL("終了待ち"), FINISH("終了");

        private final String name;

        private EventStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

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

    /** リバーシ盤の操作可否フラグ */
    private Boolean isBoardEnable;

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

        this.result = ResultType.None;
        this.isBoardEnable = false;
        this.isFinish = false;
        this.statusString = null;
        this.debugString = "デバッグ情報は特にありません";

        setEventStatus(EventStatus.WAIT);
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

    public Boolean getIsBoardEnable() {
        return isBoardEnable;
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
     * イベントステータスの値を設定する
     * @param eventStatus 設定するイベントステータスの値
     */
    private void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;

        try {
            switch (eventStatus) {
            case PLAY: {
                if (reversi.isCurrentPlayerManual()) {
                    isBoardEnable = true;
                } else {
                    isBoardEnable = false;
                }
                break;
            }
            case WAIT:
            case SKIP:
            case JUDGE:
            case WAIT_FINAL:
            case FINISH: {
                isBoardEnable = false;
                break;
            }
            default:
                throw new IllegalArgumentException("Unexpected value: " + eventStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * リバーシのゲームイベントを処理する
     * @param プレイヤーが石をおいた座標。処理中に石を置かなかった場合には {@code NULL} を返す。
     */
    public Dimension run() {
        Dimension target = null;

        // ステータスを設定する
        if (eventStatus == EventStatus.PLAY) {
            if (reversi.isSkip()) {
                setEventStatus(EventStatus.SKIP);
            }
        }

        switch (eventStatus) {
        case PLAY: {
            if (reversi.isCurrentPlayerManual()) {
                // プレイヤーが手動入力の時は、何もしない
            } else {
                // プレイヤーが手動入力の時は、アルゴリズムに従い処理を行う
                target = reversi.run();
                put(target);
            }
            break;
        }
        case SKIP: {
            // 石がどこにも置けない時のスキップ処理を定義
            statusString = Convert.getPlayerColor(reversi.getPlayerIsBlack()) + " はスキップします。";
            reversi.next();
            setEventStatus(EventStatus.WAIT);
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
            break;
        }
        case WAIT: {
            // 待ち状態の場合は何もせず、画面描画のみ行う
            if (waitFrame <= 0) {
                setEventStatus(EventStatus.PLAY);
            }
            break;
        }
        case JUDGE: {
            judge();
            if (result != ResultType.None) {
                setEventStatus(EventStatus.WAIT_FINAL);
            } else {
                setEventStatus(EventStatus.WAIT);
            }
            setWaitTime(Global.WAIT_MILLISEC_INTERVAL);
            break;
        }
        case WAIT_FINAL: {
            if (waitFrame <= 0) {
                setEventStatus(EventStatus.FINISH);
            }
            break;
        }
        case FINISH: {
            isFinish = true;
            break;
        }
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

            setEventStatus(EventStatus.JUDGE);
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
                setEventStatus(EventStatus.WAIT);
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
            setEventStatus(EventStatus.PLAY);
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
