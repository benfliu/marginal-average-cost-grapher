package application;
	
import java.util.Comparator;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;


public class Main extends Application {		
	public TableView<DataPoint> table = new TableView<DataPoint>();
	public ObservableList<DataPoint> data = FXCollections.observableArrayList (
			new DataPoint(0, 300, 0),
			new DataPoint(1, 300, 700),
			new DataPoint(5, 300, 3100),
			new DataPoint(2, 300, 1300),
			new DataPoint(3, 300, 1800),
			new DataPoint(4, 300, 2400),
			new DataPoint(6, 300, 3840));
	public final NumberAxis xAxis = new NumberAxis();
	public final NumberAxis yAxis = new NumberAxis();
	public final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
	public XYChart.Series<Number, Number> seriesAFC = new XYChart.Series<>();
	public XYChart.Series<Number, Number> seriesAVC = new XYChart.Series<>();
	public XYChart.Series<Number, Number> seriesATC = new XYChart.Series<>();
	public XYChart.Series<Number, Number> seriesMC = new XYChart.Series<>();
	
	public HBox addTitleHBox() {
		HBox titleHBox = new HBox();
		titleHBox.setPadding(new Insets(15, 12, 15, 12));
		titleHBox.setSpacing(12);
		titleHBox.setStyle("-fx-background-color: #336699;");
		
		Text title = new Text("Marginal-Avg Cost Grapher");
		title.setFont(Font.font("verdana", FontWeight.BOLD, 20));
		title.setFill(Color.WHITE);

	    Text byLine = new Text("by Ben Liu - December 7, 2021");
	    byLine.setFont(Font.font("verdana", FontWeight.LIGHT, 14));
	    byLine.setFill(Color.WHITE);
	    
	   titleHBox.getChildren().addAll(title, byLine);
	   titleHBox.setAlignment(Pos.CENTER_LEFT);
	    return titleHBox;
	}
	
	public HBox addCalcHBox() {
		HBox calcHBox = new HBox();
		calcHBox.setPadding(new Insets(12, 6, 12, 12));
		
		//right side of calcHBox
		xAxis.setLabel("Quantity");
	
		lineChart.setTitle("Marginal-Average Cost Graph");
		lineChart.setMinWidth(700);
		seriesAFC.setName("AFC");
		seriesAVC.setName("AVC");
		seriesATC.setName("ATC");
		seriesMC.setName("MC");
		for (DataPoint dp : data) {
			if (dp.getQuantity() != 0) {
				dp.setAFC(dp.getFC()/dp.getQuantity());
				dp.setAVC(dp.getVC()/dp.getQuantity());
				dp.setATC(dp.getTC()/dp.getQuantity());
			}
			if (data.indexOf(dp) > 0) {
				DataPoint prevE = data.get(data.indexOf(dp)-1);
				dp.setMC((dp.getTC()-prevE.getTC())/(dp.getQuantity()-prevE.getQuantity()));
				seriesAFC.getData().add(new Data(dp.getQuantity(), dp.getAFC()));
				seriesAVC.getData().add(new Data(dp.getQuantity(), dp.getAVC()));
				seriesATC.getData().add(new Data(dp.getQuantity(), dp.getATC()));
				seriesMC.getData().add(new Data(dp.getQuantity(), dp.getMC()));
			}
		}
		/*
		//AFC series
		XYChart.Series seriesAFC = new XYChart.Series();
		seriesAFC.setName("AFC");
		int[][] dataAFC = {	{1,300},
							{2,150},
							{3,100},
							{4,75},
							{5,60},
							{6,50}};
		for (int dpIndex = 0; dpIndex <= dataAFC.length-1; dpIndex++) {
			seriesAFC.getData().add(new XYChart.Data(dataAFC[dpIndex][0], dataAFC[dpIndex][1]));
		}
		
		//AVC series
		XYChart.Series seriesAVC = new XYChart.Series();
		seriesAVC.setName("AVC");
		int[][] dataAVC = {	{1,700},
							{2,650},
							{3,675},
							{4,600},
							{5,620},
							{6,640}};
		for (int dpIndex = 0; dpIndex <= dataAVC.length-1; dpIndex++) {
			seriesAVC.getData().add(new XYChart.Data(dataAVC[dpIndex][0], dataAVC[dpIndex][1]));
		}
		//ATC series
		XYChart.Series seriesATC = new XYChart.Series();
		seriesATC.setName("ATC");
		if (dataAFC.length == dataAVC.length) {
			int[][] dataATC = new int[dataAFC.length][dataAFC[0].length];
			for (int dpIndex = 0; dpIndex <= dataATC.length-1; dpIndex++) {
				if (dataAFC[dpIndex][0] == dataAVC[dpIndex][0]) {
					dataATC[dpIndex][0] = dataAFC[dpIndex][0];
					dataATC[dpIndex][1] = dataAFC[dpIndex][1] + dataAVC[dpIndex][1];
				}
				seriesATC.getData().add(new XYChart.Data(dataATC[dpIndex][0], dataATC[dpIndex][1]));
			}
		}
		else {
			System.out.println("ERROR: AFC/AVC mismatch. AFC length "+dataAFC.length+", AVC length "+dataAVC.length);
		}
		*/
		lineChart.setLegendVisible(true);
		lineChart.getData().add(seriesAFC);
		lineChart.getData().add(seriesAVC);
		lineChart.getData().add(seriesATC);
		lineChart.getData().add(seriesMC);
		lineChart.setAnimated(false);
		
		//left side of calcHBox
		VBox leftVBox = new VBox();
		
		//top of leftVBox
		table.setEditable(true);
		table.setItems(data);
		TableColumn quantityCol = new TableColumn("Quantity");
		quantityCol.setMinWidth(80);
		quantityCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("quantity"));
		TableColumn fcCol = new TableColumn("Total Fixed Cost");
		fcCol.setMinWidth(100);
		fcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("fc"));
		TableColumn vcCol = new TableColumn("Total Var Cost");
		vcCol.setMinWidth(100);
		vcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("vc"));
		TableColumn tcCol = new TableColumn("Total Cost");
		tcCol.setMinWidth(100);
		tcCol.getStyleClass().add("blue");
		tcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("tc"));
		TableColumn afcCol = new TableColumn("Avg Fixed Cost");
		afcCol.setMinWidth(100);
		afcCol.getStyleClass().add("blue");
		afcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("afc"));
		TableColumn avcCol = new TableColumn("Avg Var Cost");
		avcCol.setMinWidth(100);
		avcCol.getStyleClass().add("blue");
		avcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("avc"));
		TableColumn atcCol = new TableColumn("Avg Total Cost");
		atcCol.setMinWidth(100);
		atcCol.getStyleClass().add("blue");
		atcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("atc"));
		TableColumn mcCol = new TableColumn("Marginal Cost");
		mcCol.setMinWidth(100);
		mcCol.getStyleClass().add("blue");
		mcCol.setCellValueFactory(
				new PropertyValueFactory<DataPoint, Double>("mc"));
		table.getColumns().addAll(quantityCol, fcCol, vcCol, tcCol, afcCol, avcCol, atcCol, mcCol);
		//middle1 of leftVBox
		HBox labelHBox = new HBox();
		labelHBox.setPadding(new Insets(8, 20, 5, 7));
		labelHBox.setSpacing(52);
		Text quantityLabel = new Text("Q");
		Text fcLabel = new Text("FC");
		Text vcLabel = new Text("VC");
		labelHBox.getChildren().addAll(quantityLabel, fcLabel, vcLabel);
		//middle2 of leftVBox
		HBox dataEntryHBox = new HBox();
		dataEntryHBox.setPadding(new Insets(0, 0, 30, 0));
		TextField quantityField = new TextField();
		quantityField.setPrefWidth(65);
		TextField fcField = new TextField();
		fcField.setPrefWidth(65);
		TextField vcField = new TextField();
		vcField.setPrefWidth(65);
		Button addDataButton = new Button("Add data");
		addDataButton.setDefaultButton(true);
		addDataButton.setOnAction(value -> {
			if (!quantityField.getText().isEmpty() && !fcField.getText().isEmpty() && !vcField.getText().isEmpty()) {
				DataPoint dpInput = new DataPoint(Double.parseDouble(quantityField.getText()),Double.parseDouble(fcField.getText()),Double.parseDouble(vcField.getText()));
				data.add(dpInput);
				sortData();
				quantityField.clear();
				fcField.clear();
				vcField.clear();
				seriesMC.getData().clear();
				for (DataPoint dp : data) {
					if (dp.getQuantity() != 0) {
						dp.setAFC(dp.getFC()/dp.getQuantity());
						dp.setAVC(dp.getVC()/dp.getQuantity());
						dp.setATC(dp.getTC()/dp.getQuantity());
					}
					if (data.indexOf(dp) > 0) {
						DataPoint prevE = data.get(data.indexOf(dp)-1);
						dp.setMC((dp.getTC()-prevE.getTC())/(dp.getQuantity()-prevE.getQuantity()));
						seriesMC.getData().add(new Data(dp.getQuantity(), dp.getMC()));
					}
				}
				if (dpInput.getQuantity() != 0) {
					seriesAFC.getData().add(new Data(dpInput.getQuantity(), dpInput.getAFC()));
					seriesAVC.getData().add(new Data(dpInput.getQuantity(), dpInput.getAVC()));
					seriesATC.getData().add(new Data(dpInput.getQuantity(), dpInput.getATC()));
				}
				lineChart.getData().add(seriesAFC);
				lineChart.getData().add(seriesAVC);
				lineChart.getData().add(seriesATC);
				lineChart.getData().add(seriesMC);
			}
		});
		dataEntryHBox.getChildren().addAll(quantityField, fcField, vcField, addDataButton);
		//bottom of leftVBox
		Button clearDataButton = new Button("Clear data");
		clearDataButton.setOnAction(value -> {
			data.clear();
			seriesAFC.getData().clear();
			seriesAVC.getData().clear();
			seriesATC.getData().clear();
			lineChart.setAnimated(false);
			lineChart.getData().clear();
			lineChart.setAnimated(true);
			
		});
		leftVBox.getChildren().addAll(table, labelHBox, dataEntryHBox, clearDataButton);
		
		
		
		calcHBox.getChildren().addAll(leftVBox, lineChart);
		return calcHBox;
	}
	
	@Override
	public void start(Stage stage) {
		try {
			sortData();
			BorderPane border = new BorderPane();
			stage.setTitle("Line Chart");
			/*
			//MC series
			XYChart.Series seriesMC = new XYChart.Series();
			seriesMC.setName("MC");
			int[][] dataMC = {	{1,300},
								{2,150},
								{3,100},
								{4,75},
								{5,60},
								{6,50},
								{7,45}};
			for (int dpIndex = 0; dpIndex <= dataMC.length-1; dpIndex++) {
				seriesMC.getData().add(new XYChart.Data(dataMC[dpIndex][0], dataMC[dpIndex][1]));
			}
			*/
			
			
			HBox titleHBox = addTitleHBox();
			HBox calcHBox = addCalcHBox();
			border.setCenter(calcHBox);
			border.setTop(titleHBox);
			Scene scene = new Scene(border, 1500, 720);
			scene.setOnKeyPressed(event -> {
			    if (event.getCode() == KeyCode.BACK_SPACE) {
			    	DataPoint dpSelect = table.getSelectionModel().getSelectedItem();
			    	System.out.println(dpSelect.getQuantity()+", "+dpSelect.getAFC());
			    	if (data.indexOf(dpSelect) != 0) {
			    		seriesAFC.getData().remove(data.indexOf(dpSelect)-1);
				    	seriesAVC.getData().remove(data.indexOf(dpSelect)-1);
				    	seriesATC.getData().remove(data.indexOf(dpSelect)-1);
				    	seriesMC.getData().remove(data.indexOf(dpSelect)-1);
			    	}
			    	else {
			    		seriesAFC.getData().remove(data.indexOf(dpSelect));
				    	seriesAVC.getData().remove(data.indexOf(dpSelect));
				    	seriesATC.getData().remove(data.indexOf(dpSelect));
				    	seriesMC.getData().remove(data.indexOf(dpSelect));
			    	}
			    	data.remove(dpSelect);
			    	seriesMC.getData().clear();
					for (DataPoint dp : data) {
						if (data.indexOf(dp) > 0) {
							DataPoint prevE = data.get(data.indexOf(dp)-1);
							dp.setMC((dp.getTC()-prevE.getTC())/(dp.getQuantity()-prevE.getQuantity()));
							seriesMC.getData().add(new Data(dp.getQuantity(), dp.getMC()));
						}
					}
			    }
			});
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public class DataPoint {
		private DoubleProperty quantity;
		private DoubleProperty fc;
		private DoubleProperty vc;
		private DoubleProperty tc;
		private DoubleProperty afc;
		private DoubleProperty avc;
		private DoubleProperty atc;
		private DoubleProperty mc;
		
		private DataPoint(double d, double e, double f) {
			this.quantity = new SimpleDoubleProperty(d);
			this.fc = new SimpleDoubleProperty(e);
			this.vc = new SimpleDoubleProperty(f);
			this.tc = new SimpleDoubleProperty(e+f);
			this.afc = new SimpleDoubleProperty();
			this.avc = new SimpleDoubleProperty();
			this.atc = new SimpleDoubleProperty();
			this.mc = new SimpleDoubleProperty();
			//System.out.println(data.indexOf());
			/*if (data.indexOf(this) > 0) {
				System.out.println(data.indexOf(this));
				//this.prevE = data.get(data.indexOf(this)-1);
				//this.mc = new SimpleDoubleProperty(((fcInput+vcInput)-prevE.getTC())/(qInput-prevE.getQuantity()));
			}
			*/
		}
		
		public DoubleProperty quantityProperty() {
			return quantity;
		}
		public double getQuantity() {
			return quantity.get();
		}
		public void setQuantity(double qInput) {
			quantity.set(qInput);
		}
		
		public DoubleProperty fcProperty() {
			return fc;
		}
		public double getFC() {
			return fc.get();
		}
		public void setFC(double fcInput) {
			fc.set(fcInput);
		}
		
		public DoubleProperty vcProperty() {
			return vc;
		}
		public double getVC() {
			return vc.get();
		}
		public void setVC(double vcInput) {
			vc.set(vcInput);
		}
		
		public DoubleProperty tcProperty() {
			return tc;
		}
		public double getTC() {
			return tc.get();
		}
		public void setTC(double tcInput) {
			tc.set(tcInput);
		}
		
		public DoubleProperty afcProperty() {
			return afc;
		}
		public double getAFC() {
			return afc.get();
		}
		public void setAFC(double afcInput) {
			afc.set(afcInput);
		}
		
		public DoubleProperty avcProperty() {
			return avc;
		}
		public double getAVC() {
			return avc.get();
		}
		public void setAVC(double avcInput) {
			avc.set(avcInput);
		}
		
		public DoubleProperty atcProperty() {
			return atc;
		}
		public double getATC() {
			return atc.get();
		}
		public void setATC(double atcInput) {
			atc.set(atcInput);
		}
		public DoubleProperty mcProperty() {
			return mc;
		}
		public double getMC() {
			return mc.get();
		}
		public void setMC(double mcInput) {
			mc.set(mcInput);
		}
	}
	
	public static double reverseNumberInRange(double value, double minVal, double maxVal) {
	    /*
	     * y0 is top left on screen, and bottom left in chart, need to reverse.
	     */
	    return (maxVal + minVal) - value;
	}
	
	public void sortData() {
		Comparator<DataPoint> comparator = Comparator.comparingDouble(DataPoint::getQuantity);
		FXCollections.sort(data, comparator);
	}
}
