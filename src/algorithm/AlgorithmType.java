package algorithm;

/**
 * アルゴリズムの種類を定義する
 * @author komoto
 */
public enum AlgorithmType {
    MANUAL ("プレイヤー"), 
    RANDOM ("COM: ランダム"),
    ORIGINAL_01 ("COM: 端取り優先"),
    MINI_MAX_01 ("COM: Mini-Max1"),
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
