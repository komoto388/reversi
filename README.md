# ![icon](https://github.com/komoto388/reversi/assets/146818126/5fd5aafb-2417-4c24-9b9e-0b33c548781b) Reversi : リバーシ

**「Reversi : リバーシ」** はシンプルなリバーシのボードゲームです。

# 動作イメージ

## GUI 画面

<img src="https://github.com/komoto388/reversi/assets/146818126/7170a1de-c728-4e11-9ea3-1d0a5f0bfb2e" width="400">

<img src="https://github.com/komoto388/reversi/assets/146818126/2aa21ad8-bd54-4c97-9c5c-8e91898e7ab6" width="400">

<img src="https://github.com/komoto388/reversi/assets/146818126/2b133cdd-ed01-49b8-8f41-ec91bfe8abe8" width="400">

<img src="https://github.com/komoto388/reversi/assets/146818126/014f93c1-b48f-415b-8db7-59b822835185" width="400">

## CUI 画面

<img src="https://github.com/komoto388/reversi/assets/146818126/5f83a4bb-13b1-48fb-8917-8c0de9935015" width="400">

# 特徴

* CUI と GUI の両方で動作します
* 人間 vs 人間、人間 vs COM、COM vs COM の対戦が可能です
* 棋譜を表示できます
* 対戦中の石の数の推移を、結果画面でグラフィカルに確認できます 

# 要件

「Windows 11 22H2」での動作することを確認しています。（他のOSでの動作は未検証です）

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
