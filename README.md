# ![icon](https://github.com/komoto388/Reversi/assets/146818126/86d1743b-cf2f-47f2-8f74-36413c0899fc) Reversi : リバーシ


**「Reversi : リバーシ」** はシンプルなリバーシのボードゲームです。

# 動作イメージ

## GUI 画面

<img src="https://github.com/komoto388/Reversi/assets/146818126/e04f6949-d83d-40ea-af94-8237d60d4fdf" width="480">

<img src="https://github.com/komoto388/Reversi/assets/146818126/d8d6b87d-f3b6-471c-82ac-330677a7d6e0" width="480">

<img src="https://github.com/komoto388/Reversi/assets/146818126/40776c76-3185-4d98-96e1-b21e6fc26561" width="480">

<img src="https://github.com/komoto388/Reversi/assets/146818126/1b7740b8-1c71-4d02-821c-1b820b9775ee" width="480">

## CUI 画面

<img src="https://github.com/komoto388/Reversi/assets/146818126/0fdaf7e2-8515-4785-86eb-d54d739dacb7" width="480">

# 特徴

* CUI と GUI の両方で動作します
* 人間 vs 人間、人間 vs COM、COM vs COM の対戦が可能です
* 棋譜を表示できます
* 対戦中の石の数の推移を、結果画面でグラフィカルに確認できます 

# 要件

「Windows 11 22H2」での動作することを確認しています。
他のOSでも動作可能と思われますが、未検証です。

本プログラムのプログラミング言語は Java を使用しています。
また、GUIフレームワークは JavaFX を使用しています。

* JDK 21
* Java FX 21


# 全体の状態遷移図

以下のように状態遷移します。

```mermaid
stateDiagram-v2
[*] --> PlayerSelect
PlayerSelect --> Reversi: Enter player setting
State Reversi {
    state if_skip <<choice>>
    state if_manual <<choice>>
    state if_judege <<choice>>
    
    [*] --> PLAY
    PLAY --> if_skip
    if_skip --> SKIP:Skip
    SKIP --> JUDGE
    if_skip --> if_manual:No Skip
    if_manual --> PLAY_MANUAL:Manual
    if_manual --> PLAY_COM:COM
    PLAY_MANUAL --> JUDGE:put
    PLAY_MANUAL --> SKIP:skip
    PLAY_COM --> JUDGE:put
    PLAY_COM --> SKIP:skip
    JUDGE --> if_judege
    if_judege --> PLAY:Not Finish
    if_judege --> FINISH:Finish
    FINISH --> [*]
}
Reversi --> Result
state Result {
    DetailResult
    Record
    Graph
}
Result --> [*]
``` 

スキップ処理は原則、システム側で自動的に石を置く場所がないか探索し、場所がない場合のみ SKIP 状態になります。
ただし、プレイヤー側が正しく算出できなかった場合のために、プレイヤーから SKIP に遷移することもあります。

# 開発者情報

* komoto

# License

* [MIT license](https://en.wikipedia.org/wiki/MIT_License)
