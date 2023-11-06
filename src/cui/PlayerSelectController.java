package cui;

import java.util.Scanner;

import algorithm.AlgorithmType;
import model.PlayerSelectData;
import model.PlayerSelectModel;

public class PlayerSelectController {

    /** プレイヤー選択・設定のデータ処理を行うインスタンス（モデル） */
    private PlayerSelectModel playerSelectModel;

    /** コマンドプロンプトからの入力を受け取るスキャナー */
    private static Scanner scanner;

    /**
     * 初期化を行う
     * @param scanner 入力を受け取るスキャナー
     */
    public PlayerSelectController(Scanner scanner) {
        this.playerSelectModel = new PlayerSelectModel();
        PlayerSelectController.scanner = scanner;
    }

    /**
     * プレイヤー選択処理を実行する
     * @return 次のゲーム処理に渡すデータ
     */
    public PlayerSelectData run() {
        // 先手・黒のアルゴリズム種別を決める
        Boolean playerIsBlack = true;
        playerSelectModel.setPlayerAlgorithm(playerIsBlack, selectAlgorithm(playerIsBlack));

        // 後手・白のアルゴリズム種別を決める
        playerIsBlack = false;
        playerSelectModel.setPlayerAlgorithm(playerIsBlack, selectAlgorithm(playerIsBlack));

        // プレイヤーとリバーシのインスタンスを作成する
        PlayerSelectData model = playerSelectModel.generateData();

        return model;
    }

    /**
     * プレイヤーが使用するアルゴリズムを選択する
     * @param 対象のプレイヤー (黒の場合は真 {@code true}, 白の場合は偽 {@code false})
     * @return 選択したアルゴリズム種別
     */
    private AlgorithmType selectAlgorithm(Boolean isPlayerBlack) {
        AlgorithmType[] typeList = AlgorithmType.values();
        int inputNum = 0;

        String name = playerSelectModel.getName(isPlayerBlack);

        do {
            System.out.printf("%sが使用するアルゴリズムを選択してください。手動で操作したい場合は、「%s」を選択してください。\n",
                    name, AlgorithmType.Manual.getName());

            // 選択肢の表示
            for (int i = 0; i < typeList.length; i++) {
                System.out.printf("%2d. %s\n", i + 1, typeList[i].getName());
            }
            System.out.printf("\n選択肢？: ");

            // 入力した文字列が想定されたものか確認する。想定されていない場合は再入力させる。
            try {
                inputNum = scanner.nextInt();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("数字以外の文字は想定されていません。");
                System.err.flush();
            }

            if (inputNum > 0 && inputNum <= typeList.length) {
                // 範囲内の数値が入力された場合は、次の処理に進む
                break;
            } else {
                // 範囲外の数値が入力された場合は、再入力を促す
                System.err.println("想定されていない値です: " + inputNum);
                System.err.flush();
                continue;
            }
        } while (true);

        AlgorithmType type = typeList[inputNum - 1];

        System.out.printf("「%d. %s」が選択されました。\n\n", inputNum, type);

        return type;
    }
}
