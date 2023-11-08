# ReflectMember クラスの利用方法について
リフレクション操作を簡易的にするライブラリを作成した。
ここでは作成したライブラリの概要と利用方法を記載する。


# 概要
## 何ができるのか？
結論としては、<span style="color: red">**「本来アクセスできないprivateのメンバーに対して、外部のクラスから参照・変更できる」**</span>ようになる。


## 想定する利用シーン
主にJUnitなどの単体テストでの利用が考えられる。

* getterを定義していない Private フィールドの値が正しい値になっているか確認したい
* setterを定義していない Privateフィールドの値を変更して、異常時の動作を確認したい

なお、本来は制限してアクセスできないようにしているものに対して、**無理やり** アクセスして値を参照したり変更したりしている。そのため、運用での使用はしないようにする。


## 説明
クラス `TargetA` の中にあるメンバー（フィールド、メソッド）を、リフレクションを使用して外部クラスからアクセスできるようになる。
* クラスの外から、クラス内のフィールドの値を参照することができる
* クラスの外から、クラス内のフィールドの値を変更することができる
* クラスの外から、クラス内のメソッドを実行することができる

Public などクラスの外からアクセス可能なメンバーについてはメリットはないが、
特にリフレクションを使用しているので、本来外部からアクセスできない Private 修飾子がついたメンバーにも

値を参照したり、値を変更したりすることができる。

例えば、`Sample`クラスが以下のような構成を考えた時、`val1`～`val4`は参照・値の変更を行うことができる。
なお、`val5`のようにfinal修飾子があるフィールドについては参照のみで、値の変更はできない。

<!--
クラスのインスタンスもフィールドとして扱える
final修飾子の値変更はできるやり方があるようだが、実際の環境で変更できることを確認できていない
-->

```java
public class Sample {
    // リフレクションで参照・変更が可能
    public int val1;
    protected double val2;
    private OriginalClass val3;
    static Boolean val4;
    
    // リフレクションで参照のみ可能
    private static final int val5;

    // リフレクションで実行可能
    private void method1(int a) {
    };
}
```


# 使用方法
以降では以下のように`TargetA`クラスの Private メンバーに対して、`ReflectMember`クラスでのリフレクションを利用してアクセスする方法に焦点を当てて説明する。
Public など外部クラスからアクセスできるメンバーはリフレクションではなく、`sample.getNum()` のように通常の方法でアクセスする。

### アクセス元クラス（参照する側）のソースコード
```java
TargetA targetA = new TargetA();   // 検査対象のインスタンス
```
`targetA` はメンバーの値を見たいインスタンスである。
以降、 `targetA` のインスタンスの中にある Private なフィールドやメソッドを操作することを想定して記述する。

### アクセス先クラス（参照される側）のソースコード
```java
public class TargetA {
    private int field1;
    private static double field2;
    private OriginalClass field3;    // 作成したオリジナルのクラスのインスタンス

    private int method1(int num, Boolean flag) {
    }
}
```


## 1. 前準備
初めに以下のようなコードをアクセス元（外部クラスなど）にて記述する。
なお、以降についてもアクセス先の `TragetA` クラス自体にはコード修正の必要はない。
```java
ReflectMember reflclazz = new ReflectMember(TargetA.class);
```
* `ReflectMember` クラスのインスタンスを宣言・生成する。
* コンストラクタの引数には、アクセスしたい Private メンバーを持つクラスを `<クラス名>.class` という形式で指定する。


## 2. 非 Static フィールドの操作
### フィールドの取得

```java
Field reflField1 = reflClazz.getField("field1");
Field reflField2 = reflClazz.getField("field2");
Field reflField3 = reflClazz.getField("field3");
```
* メンバーそれぞれに Field 型のフィールドを用意して `getFeild()` で取得する。
* `getFeild()` の引数にはフィールドの名前を指定する。

### フィールドの値を取得
```java
int num = (int) reflField1.get(targetA);
```
* Filed 型フィールドの `get()` を使用して値を取得する。
* 引数には、確認対象のインスタンス（今回の場合は `targetA` ）を指定する。
* `get()` の戻り値は Object 型のため、型のキャストが必要である。

### フィールドの値の変更
```java
reflField1.set(targetA, 100);
```
* Filed 型フィールドの `set()` を使用して値を取得する。
* 引数には、確認対象のインスタンス（今回の場合は `targetA` ）と、設定する値を指定する。


## 3. Static フィールドの操作
*後日追記予定*


## 4. 非 Static メソッドの操作
### メソッドの取得

```java
Method reflMethod1 = reflClazz.getMethod("method1", int.class, Boolean.class);
```
* メンバーそれぞれに Method 型のフィールドを用意して `getMethod()` で取得する。
* `getFeild()` の引数には以下を指定する。
  * フィールドの名前
  * 引数のクラス\
今回のメソッドは、 `int method1(int num, Boolean flag)`と第一引数に int、第二引数に Boolean があるので、メソッドの名前の後に、「 `int.class, Boolean.class` 」を追加して指定する。\
`int method2()`のように元々のメソッドに引数がない場合は `getMethod("method2")` のようにメソッドの名前のみを指定する。

### メソッドの実行
```java
int x = 10;
int y = 20;
int num = (int) reflField1.get(targetA, x, y);
```
* Method 型フィールドの `invoke()` を使用してメソッドを実行する。
* 引数には以下を指定する。
  * 確認対象のインスタンス（今回の場合は `targetA` ）
  * メソッドに渡す引数
* `get()` の戻り値は Object 型のため、型のキャストが必要である。

## 5. Static メソッドの操作
*後日追記予定*


# 注意
* 静的メンバー(Static)については方法が異なるため、追って記載する。


# その他
* 対象のクラスが継承をしている場合でもスーパークラスのフィールドやメソッドを参照できるように処理している。