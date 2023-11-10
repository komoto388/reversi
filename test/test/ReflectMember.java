package test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * クラスが持つ Private メンバ（フィールド、メソッド等）を外部参照可能な状態で取得するクラス<br>
 * リフレクションを使用して、参照可能な状態で取得する。
 * @implNote この機能は JUnit などのテストでの使用のみを想定しており、運用での使用は非推奨です。
 * @author komoto
 */
public class ReflectMember {

    /** リフレクションのメンバを持つクラス */
    private final Class<?> clazz;

    /**
     * 対象クラスの Private メンバを参照するために必要な初期化を行う
     * @param clazz 対象のPrivateメンバを定義しているクラス ({@code Sample sample} の場合では、{@code Sample.class} を指定する)
     */
    public ReflectMember(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * クラスで定義されるフィールド(Privateを含む)を、参照可能な状態で取得する。
     * @param name 参照したい Private 変数の名前
     * @return {@code name}で指定した名前の変数
     */
    public Field getField(String name) {
        Field field = null;

        Class<?> currentClazz = clazz;
        while (currentClazz != null) {
            try {
                field = currentClazz.getDeclaredField(name);
                field.setAccessible(true);
                break;
            } catch (Exception e) {
                currentClazz = currentClazz.getSuperclass();
            }
        }

        // スーパークラスを含めてフィールドが見つからなかった時
        try {
            if (field == null) {
                throw new NoSuchFieldError("スーパークラスを含め対象のフィールドは定義されていません: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return field;
    }

    /**
     * クラスで定義される Private メソッドを、参照可能な状態で取得する。
     * @param name 参照したい Private メソッドの名前
     * @param clazzArgs 対象のPrivateメソッドが宣言する引数のクラス
     * @return {@code name}で指定した名前のメソッド
     */
    public Method getMethod(String name, Class<?>... clazzArgs) {
        Method method = null;

        Class<?> currentClazz = clazz;
        while (currentClazz != null) {
            try {
                method = currentClazz.getDeclaredMethod(name, clazzArgs);
                method.setAccessible(true);
                break;
            } catch (Exception e) {
                currentClazz = currentClazz.getSuperclass();
            }
        }

        // スーパークラスを含めてフィールドが見つからなかった時
        try {
            if (method == null) {
                throw new NoSuchFieldError("スーパークラスを含め対象のメソッドは定義されていないか、引数の宣言が異なります: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return method;
    }
}
