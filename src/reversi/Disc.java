package reversi;

/**
 * リバーシの石を定義する列挙型
 */
public enum Disc {
    BLACK("黒", "先手・黒"), WHITE("白", "後手・白");
    
    /** リバーシの石の名前 */
    private final String name;
    
    /** プレイヤーが石を使用した時のプレフィックス名 */
    private final String prefixName;
    
    private Disc(String name, String prefixName) {
        this.name = name;
        this.prefixName = prefixName;
    }
    
    /**
     * リバーシの石の名前を取得する
     * @return name リバーシの石の名前の文字列
     */
    public String getName() {
        return name;
    }
    
    /**
     * 「先手・黒」のような、プレイヤーの名前の前に付けるプレフィックス名を取得する
     * @return prefixName プレイヤーが石を使用した時のプレフィックス名
     */
    public String getPrefixForPlayerName() {
        return prefixName;
    }
    
    /**
     * 次の要素を取得する
     * @return
     */
    public Disc next() {
        Disc[] discs = Disc.values();
        int i = this.ordinal();
        
        if(++i < discs.length) {
            return discs[i];
        } else {
            return discs[0];
        }
    }
}
