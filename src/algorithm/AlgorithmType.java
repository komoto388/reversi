package algorithm;

/**
 * アルゴリズムの種類を定義する
 * @author komoto
 */
public enum AlgorithmType {
    Manual ("プレイヤー"), 
    Random ("COM: ランダム"),
    Original_01 ("COM: オリジナル1"),
    Original_02 ("COM: 深さ探索1"),
    MiniMax_01 ("COM: Mini-Max1"),
    ;
    
    /** アルゴリズムの名前 */
    private final String name;
    
    /**
     * 値を設定する
     * @param name アルゴリズムの名称
     */
    private AlgorithmType(String name) {
        this.name = name;
    }
    
    /**
     * アルゴリズムの名前を取得する
     * @return アルゴリズムの名前
     */
    public String getName() {
        return name;
    }
}
