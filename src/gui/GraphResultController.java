package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gamerecord.GameRecord;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.VBox;

/**
 * 黒・白の石の獲得推移をグラフに描画するクラス
 */
public class GraphResultController implements Initializable {

    /** グラフ描画画面のルートペイン */
    @FXML
    private VBox graphRootPane;

    /** 描画先のグラフペイン */
    private LineChart<Number, Number> lineChart;

    /** X軸のインスタンス */
    private NumberAxis xAxis;

    /** Y軸のインスタンス */
    private NumberAxis yAxis;

    /** グラフに描画するデータ（黒・白の石の個数差） */
    private XYChart.Series<Number, Number> series;

    /**
     * 表示するグラフの初期化を行う
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // X軸を生成する
        xAxis = new NumberAxis();
        xAxis.setLabel("経過ターン数");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(0);

        // Y軸を生成する
        yAxis = new NumberAxis();
        yAxis.setLabel("黒石と白石の個数差（黒 - 白）");
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(0);
        yAxis.setAutoRanging(false);

        // 折れ線グラフを生成する
        series = new XYChart.Series<>();
        series.setName("石の個数差");
        series.getData().add(new Data<Number, Number>(0, 0));

        // 描画するグラフを生成する
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.getData().addAll(series);
        //        lineChart.setPadding(new Insets(30));

        // グラフをルートペインに追加する
        graphRootPane.getChildren().add(lineChart);
    }

    /**
     * グラフを作成する
     * @param record 棋譜
     */
    public void setGraph(GameRecord record) {
        for (int i = 0; i < record.size(); i++) {
            add(record, i);
        }
    }

    /**
     * グラフにデータを追加する
     * @param record 棋譜
     * @param index リストの要素番号
     */
    public void add(GameRecord record, int index) {
        int turnNum = record.getTurn(index);
        int blackDiscNum = record.getBlackDiscNum(index);
        int whiteDiscNum = record.getWhiteDiscNum(index);

        // グラフのデータを描画する
        int diff = blackDiscNum - whiteDiscNum;
        series.getData().add(new Data<Number, Number>(turnNum, diff));

        // X軸の最大値を設定する
        int xAxisLimit = (turnNum / 10 + 1) * 10;
        if (xAxisLimit > xAxis.getUpperBound()) {
            xAxis.setUpperBound(xAxisLimit);
        }

        // Y軸の最大・最小値を設定する
        int yAxisLimit = (Math.abs(diff) / 10 + 1) * 10;
        if (yAxisLimit > yAxis.getUpperBound()) {
            yAxis.setLowerBound(-yAxisLimit);
            yAxis.setUpperBound(yAxisLimit);
        }
    }
}
