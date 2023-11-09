package reversi;

/**
 * リバーシの石を定義する列挙型
 */
public enum Disc {
    BLACK("黒", "先手・黒", "black", "disc-black"), WHITE("白", "後手・白", "white", "disc-white");

    /** リバーシの石の名前 */
    private final String name;

    /** プレイヤーが石を使用した時のプレフィックス名 */
    private final String prefixName;

    /** 石のカラーコード */
    private final String colorCode;

    /** リバーシを画面描画する時に使用する fxid (CSSで使用する) */
    private final String fxId;

    /**
     * 初期値を設定する
     * @param name リバーシの石の名前
     * @param prefixName プレイヤーが石を使用した時のプレフィックス名
     * @param colorCode 石のカラーコード
     * @param fxId リバーシを画面描画する時に使用する fxid
     */
    private Disc(String name, String prefixName, String colorCode, String fxId) {
        this.name = name;
        this.prefixName = prefixName;
        this.colorCode = colorCode;
        this.fxId = fxId;
    }

    /**
     * リバーシの石の名前を取得する
     * @return リバーシの石の名前の文字列
     */
    public String getName() {
        return name;
    }

    /**
     * 「先手・黒」のような、プレイヤーの名前の前に付けるプレフィックス名を取得する
     * @return プレイヤーが石を使用した時のプレフィックス名
     */
    public String getPrefixForPlayerName() {
        return prefixName;
    }

    /**
     * 石のカラーコードを取得する
     * @return 石のカラーコードの文字列
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * リバーシを画面描画する時に使用する fxid を取得する
     * @return リバーシを画面描画する時に使用する fxid の文字列
     */
    public String getFxId() {
        return fxId;
    }

    /**
     * 次の要素を取得する
     * @return 次の要素
     */
    public Disc next() {
        Disc[] discs = Disc.values();
        int i = this.ordinal();

        if (++i < discs.length) {
            return discs[i];
        } else {
            return discs[0];
        }
    }
}
