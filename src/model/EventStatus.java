package model;

import reversi.Reversi;

/**
 * ゲームのイベント状態の値
 */
enum EventStatusValue {
    PLAY("プレイヤー処理"), PLAY_MANUAL("マニュアル操作処理"), PLAY_COM("COM処理"), SKIP("スキップ処理"), JUDGE("判定処理"), FINISH(
            "終了処理");

    /** イベントステータスの名前 */
    private final String name;

    /**
     * 値を設定する
     * @param name イベントステータスの名前
     */
    private EventStatusValue(String name) {
        this.name = name;
    }

    /**
     * イベントステータスの名前を取得する
     * @return イベントステータスの名前
     */
    public String getName() {
        return name;
    }
}

/**
 * イベントステータスを操作するクラス
 */
class EventStatus {

    /** リバーシを制御するインスタンス */
    private Reversi reversi;

    /** 現在のイベントステータス */
    private EventStatusValue eventStatus;

    /**
     * ユーザーがリバーシ盤を操作可能かを表すフラグ
     * 操作可能の場合は真 {@code true}, 操作不可の場合は偽 {@code false}
     */
    private Boolean canUserControll;

    /**
     * イベントステータスを初期化する
     * @param reversi リバーシを制御するインスタンス
     * @param status イベントステータスの初期値として設定する値
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

    /**
     * リバーシ盤が操作可能（ユーザの操作待ち状態）かどうかを表す
     * @return 操作可能の場合は真 {@code true}, 操作不可の場合は偽 {@code false}
     */
    public Boolean canUserControll() {
        return canUserControll;
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
     * @param status 設定するイベントステータスの値
     */
    public void set(EventStatusValue eventStatus) {
        // 引数の正常性確認
        // 指定されたイベントステータスが PLAY_COM または PLAY_MANUAL の場合、 PLAY に付け直す
        try {
            if (eventStatus == EventStatusValue.PLAY_COM) {
                throw new IllegalArgumentException("PLAY_COM は内部処理で使用するため、指定できません: " + eventStatus);
            }
            if (eventStatus == EventStatusValue.PLAY_MANUAL) {
                throw new IllegalArgumentException("PLAY_COM は内部処理で使用するため、指定できません: " + eventStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("\"PLAY\" としてイベントステータスを設定します");
            eventStatus = EventStatusValue.PLAY;
        }

        // イベントステータスを更新する
        this.eventStatus = eventStatus;

        // ユーザー操作を拒否する
        // プレイヤーがマニュアルの場合については、relabelPlayerStatus() で付け直す際に許可する
        canUserControll = false;
    }

    /**
     * イベントステータス PLAY を、PLAY_MANUAL または PLAY_COM に付け替える
     * @return イベントステータスの付け替えが成功の場合は真 {@code true}, 失敗の場合は偽 {@code false}
     */
    public Boolean relabelPlayerStatus() {
        // 引数の正常性確認
        try {
            if (eventStatus != EventStatusValue.PLAY) {
                throw new IllegalArgumentException("イベントステータスが PLAY 以外のため、メソッドを使用できません: " + eventStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // 現在のプレイヤーがマニュアルの場合は PLAY_MANUAL、COMの場合は PLAY_COM にイベントステータスを付け替える
        if (reversi.getCurrentPlayer().isManual()) {
            eventStatus = EventStatusValue.PLAY_MANUAL;
            canUserControll = true;
        } else {
            eventStatus = EventStatusValue.PLAY_COM;
            canUserControll = false;
        }

        return true;
    }
}
