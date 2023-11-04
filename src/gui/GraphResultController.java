package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.AnchorPane;
import reversi.RecordRow;

/**
 * 黒・白の石の獲得推移をグラフに描画するクラス
 */
public class GraphResultController implements Initializable {

    /** グラフ描画画面のルートペイン */
    @FXML
    private AnchorPane graphRootPane;

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
        xAxis = new NumberAxis();
        xAxis.setLowerBound(0);

        yAxis = new NumberAxis();
        yAxis.setLowerBound(-40);
        yAxis.setUpperBound(40);

        lineChart = new LineChart<Number, Number>(xAxis, yAxis);

        // 表示するデータを設定
        series = new XYChart.Series<>();
        series.setName("石の個数差");
        series.getData().add(new Data<Number, Number>(0, 0));

        // グラフのデータを設定
        lineChart.getData().addAll(series);

        graphRootPane.getChildren().setAll(lineChart);
    }

    /**
     * グラフにデータを追加する
     * @param turnNum ターン数
     * @param recordRow {@code turnNum} 手目の棋譜
     */
    public void addData(int turnNum, RecordRow recordRow) {
        int diff = recordRow.blackDiscNum - recordRow.whiteDiscNum;
        series.getData().add(new Data<Number, Number>(turnNum, diff));
    }
}
