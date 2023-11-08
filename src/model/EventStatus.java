package model;

import reversi.Player;
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
     * */
    private Boolean canUserControll;

    /**
     * イベントステータスを初期化する
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

    /**
     * リバーシ盤が操作可能（ユーザの操作待ち状態）かどうかを表す
     * @return 操作可能の場合は真 {@code true}, 操作不可の場合は偽 {@code false} を返す
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
     * @param eventStatus 設定するイベントステータスの値
     */
    public void set(EventStatusValue eventStatus) {
        Player currentPlayer = reversi.getCurrentPlayer();

        // 値が PLAY の場合、値を PLAY_MANUAL または PLAY_COM に付け替える
        if (eventStatus == EventStatusValue.PLAY) {
            if (currentPlayer.isManual()) {
                eventStatus = EventStatusValue.PLAY_MANUAL;
            } else {
                eventStatus = EventStatusValue.PLAY_COM;
            }
        }

        // ステータス、リバーシ盤操作可否の更新
        this.eventStatus = eventStatus;

        if (eventStatus == EventStatusValue.PLAY_MANUAL) {
            canUserControll = true;
        } else {
            canUserControll = false;
        }
    }
}
