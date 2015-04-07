package MVC;

import currecny.CurrencyConverter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

/**
 *
 *
 */
public class TravalPlanner extends Application {

    private Scene scene;
    MyBrowser currentBrowser;
    MyBrowser browser0;
    MyBrowser browser1;
    int browser = 0;
    WebView webView0 = new WebView();
    WebEngine webEngine0 = webView0.getEngine();
    WebView webView1 = new WebView();
    WebEngine webEngine1 = webView1.getEngine();
    HBox root;
    VBox right;
    VBox left;
    TravalPlannerModel myModel;
    Distance distance;
    double distKM;
    JSObject window;
    CurrencyConverter converter;
    double ratio;
    ChoiceBox<String> fromCurrencyCB;
    ChoiceBox<String> toCurrencyCB;
    Label toCurrencyLabel;
    TextField fromCurrencyTF;

    VBox hotelInfoBox;
    VBox sightInfoBox;
    VBox ticketInfoBox;

    /**
     *    * @param args the command line arguments    
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Traval Planner (beta ver.3.6.22 demo)");

        root = new HBox();
        right = new VBox();
        left = new VBox();
        //distance = new Distance();

        browser0 = new MyBrowser(0);
        browser1 = new MyBrowser(1);
        currentBrowser = browser0;
        TabPane infoSec = new TabPane();
        myModel = new TravalPlannerModel();

        root.setPadding(new Insets(10, 20, 20, 20));

        Label infoLab = new Label("Information");
        infoLab.setFont(Font.font(20));

        Tab hotelInfoTab = new Tab();
        hotelInfoTab.setText("Hotels");
        hotelInfoTab.setClosable(false);
        InfoTabLabel hotelInfoLab = new InfoTabLabel("hotel", "HOTEL INFO!!!!!");
        hotelInfoLab.setPrefSize(320, 700);
        hotelInfoBox = new VBox();
        hotelInfoBox.getChildren().add(hotelInfoLab);
        hotelInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        ScrollPane hotelInfoPane = new ScrollPane();
        hotelInfoPane.setPrefSize(320, 800);
        hotelInfoPane.setContent(hotelInfoBox);
        hotelInfoTab.setContent(hotelInfoPane);

        Tab sightInfoTab = new Tab();
        sightInfoTab.setText("Sights");
        sightInfoTab.setClosable(false);
        InfoTabLabel sightInfoLab = new InfoTabLabel("sights", "SIGHTS INFO!!!!!");
        sightInfoLab.setPrefSize(320, 700);
        sightInfoBox = new VBox();
        sightInfoBox.getChildren().add(sightInfoLab);
        sightInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        ScrollPane sightInfoPane = new ScrollPane();
        sightInfoPane.setPrefSize(320, 800);
        sightInfoPane.setContent(sightInfoBox);
        sightInfoTab.setContent(sightInfoPane);

        Tab ticketInfoTab = new Tab();
        ticketInfoTab.setText("Tickets");
        ticketInfoTab.setClosable(false);
        InfoTabLabel ticketInfoLab = new InfoTabLabel("ticket", "TICKETS INFO!!!!!");
        ticketInfoLab.setPrefSize(320, 700);
        ticketInfoBox = new VBox();
        ticketInfoBox.getChildren().add(ticketInfoLab);
        ticketInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        ScrollPane ticketInfoPane = new ScrollPane();
        ticketInfoPane.setPrefSize(320, 800);
        ticketInfoPane.setContent(ticketInfoBox);
        ticketInfoTab.setContent(ticketInfoPane);

        infoSec.setPrefSize(320, 800);
        infoSec.getTabs().addAll(hotelInfoTab, sightInfoTab, ticketInfoTab);
        myModel.addListener(hotelInfoLab);
        myModel.addListener(sightInfoLab);
        myModel.addListener(ticketInfoLab);

        right.setPadding(new Insets(0, 0, 0, 20));
        right.setSpacing(10);
        right.getChildren().addAll(infoLab, infoSec);

        left.setSpacing(10);
        left.setPadding(new Insets(0, 20, 0, 0));
        TextField departureTF = new TextField("Saskatoon");
        TextField destinationTF = new TextField("Toronto");
        Label depLab = new Label("Departure:");
        Label desLab = new Label("Destination");
        depLab.setFont(Font.font(20));
        desLab.setFont(Font.font(20));

        Button submitButton = new Button("Submit");
        ChoiceBox<String> travelMethod = new ChoiceBox<>();
        travelMethod.getItems().addAll("DRIVING", "TRANSIT", "WALKING", "BICYCLING", "FLIGHT");
        travelMethod.getSelectionModel().select(0);
        travelMethod.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if (travelMethod.getSelectionModel().getSelectedIndex() != 4) {
                    if (browser != 0) {
                        currentBrowser = browser0;
                        browser = 0;
                        left.getChildren().remove(5);
                        left.getChildren().add(currentBrowser);
                    }
                } else {
                    if (browser != 1) {
                        currentBrowser = browser1;
                        browser = 1;
                        left.getChildren().remove(5);
                        left.getChildren().add(currentBrowser);
                    }
                }
            }
        });

        converter = CurrencyConverter.getInstance();
        fromCurrencyCB = new ChoiceBox<>();
        toCurrencyCB = new ChoiceBox<>();
        try {
            fromCurrencyCB.getItems().addAll(converter.getCurrencies());
            toCurrencyCB.getItems().addAll(converter.getCurrencies());
            fromCurrencyCB.getSelectionModel().select(22);
            toCurrencyCB.getSelectionModel().select(22);
        } catch (ParseException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        }

        Label currencyNameLabel = new Label("  Currency:");
        currencyNameLabel.setPadding(new Insets(5, 0, 0, 0));
        toCurrencyLabel = new Label();
        fromCurrencyTF = new TextField("1");
        fromCurrencyTF.setPrefWidth(60);
        toCurrencyLabel.setPadding(new Insets(5, 0, 0, 0));
        ratio = 0;
        try {
            ratio = converter.convert(1.0, fromCurrencyCB.getSelectionModel().getSelectedItem(), toCurrencyCB.getSelectionModel().getSelectedItem());
        } catch (ParseException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        toCurrencyLabel.setText("to    " + String.format("%.3f%n", ratio));

        HBox submitPanel = new HBox();
        submitPanel.setSpacing(20);
        submitPanel.getChildren().addAll(travelMethod, currencyNameLabel, fromCurrencyTF, fromCurrencyCB, toCurrencyLabel, toCurrencyCB, submitButton);

        submitButton.setOnMouseClicked((MouseEvent event) -> {
            String DepTemp = departureTF.getText();
            String DesTemp = destinationTF.getText();
            if (travelMethod.getSelectionModel().getSelectedIndex() != 4) {
                webEngine0.executeScript("setTravelMethod('" + travelMethod.getSelectionModel().getSelectedItem() + "')");
                webEngine0.executeScript("inputStartEnd('" + DepTemp + "','" + DesTemp + "')");
                myModel.setDeparture(DepTemp);
                myModel.setDestination(DesTemp);
            } else {
                webEngine1.executeScript("setLocation('" + DepTemp + "','" + DesTemp + "')");
            }
            recalculateRatio();
            setTabs(DesTemp);
        });

        left.getChildren().addAll(depLab, departureTF, desLab, destinationTF, submitPanel, currentBrowser);

        root.getChildren().addAll(left, right);
        scene = new Scene(root, 1200, 810);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void recalculateRatio() {
        calculateRatio(Double.parseDouble(fromCurrencyTF.getText()), fromCurrencyCB.getSelectionModel().getSelectedItem(), toCurrencyCB.getSelectionModel().getSelectedItem());
        toCurrencyLabel.setText("to    " + String.format("%.3f%n", ratio));
    }

    private double calculateRatio(double i, String from, String to) {
        try {
            ratio = converter.convert(i, from, to);
        } catch (IOException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(TravalPlanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ratio;
    }

    private void setTabs(String dest) {
        hotelInfoBox.getChildren().clear();
        sightInfoBox.getChildren().clear();
        ticketInfoBox.getChildren().clear();
        LinkedList<Info> hotel = myModel.getHotelInfo();
        LinkedList<Info> sight = myModel.getSightInfo();
        LinkedList<Info> flight = myModel.getFlightInfo();
        int j = 0;
        for (Info i : hotel) {
            if (Math.random() < 0.7) {
                AnchorPane template = new AnchorPane();
                template.setPrefSize(303, 150);
                if (j % 3 == 0) {
                    template.setStyle(" -fx-background-color: LAVENDER;");
                } else if (j % 3 == 1) {
                    template.setStyle(" -fx-background-color: #9AF8D4;");
                } else {
                    template.setStyle(" -fx-background-color: #FCB0B0;");
                }
                VBox labelBox = new VBox();
                Label nameLabel = new Label();
                nameLabel.setText(i.getName() + " " + dest);
                Label priceLabel = new Label();
                calculateRatio(i.getPrice(), "CAD", toCurrencyCB.getSelectionModel().getSelectedItem());
                priceLabel.setText("Price: " + String.format("%.1f%n", ratio) + " per night");
                Label rateLabel = new Label();
                rateLabel.setText("Rating: " + i.getRating() + "/5");
                labelBox.setPrefSize(193, 150);
                labelBox.setSpacing(20);
                labelBox.setPadding(new Insets(30, 0, 0, 0));
                labelBox.getChildren().addAll(nameLabel, priceLabel, rateLabel);
                Image img = new Image(i.getURL());
                ImageView imgv = new ImageView();
                imgv.setImage(img);
                imgv.setFitHeight(100);
                imgv.setFitWidth(100);
                AnchorPane.setLeftAnchor(imgv, 10.0);
                AnchorPane.setTopAnchor(imgv, 25.0);
                AnchorPane.setLeftAnchor(labelBox, 120.0);
                template.getChildren().addAll(imgv, labelBox);
                hotelInfoBox.getChildren().add(template);
                j++;
            }
        }

        for (Info i : sight) {
            if (Math.random() < 0.7) {
                AnchorPane template = new AnchorPane();
                template.setPrefSize(303, 150);
                if (j % 3 == 0) {
                    template.setStyle(" -fx-background-color: LAVENDER;");
                } else if (j % 3 == 1) {
                    template.setStyle(" -fx-background-color: #9AF8D4;");
                } else {
                    template.setStyle(" -fx-background-color: #FCB0B0;");
                }
                VBox labelBox = new VBox();
                Label nameLabel = new Label();
                nameLabel.setText(i.getName() + " " + dest);
                Label priceLabel = new Label();
                if (i.getPrice() > 0) {
                    calculateRatio(i.getPrice(), "CAD", toCurrencyCB.getSelectionModel().getSelectedItem());
                    priceLabel.setText("Price: " + String.format("%.1f%n", ratio) + " per person");
                } else {
                    priceLabel.setText("Price: free");
                }
                Label rateLabel = new Label();
                rateLabel.setText("Rating: " + i.getRating() + "/5");
                labelBox.setPrefSize(193, 150);
                labelBox.setSpacing(20);
                labelBox.setPadding(new Insets(30, 0, 0, 0));
                labelBox.getChildren().addAll(nameLabel, priceLabel, rateLabel);
                Image img = new Image(i.getURL());
                ImageView imgv = new ImageView();
                imgv.setImage(img);
                imgv.setFitHeight(100);
                imgv.setFitWidth(100);
                AnchorPane.setLeftAnchor(imgv, 10.0);
                AnchorPane.setTopAnchor(imgv, 25.0);
                AnchorPane.setLeftAnchor(labelBox, 120.0);
                template.getChildren().addAll(imgv, labelBox);
                sightInfoBox.getChildren().add(template);
                j++;
            }
        }
        if (browser == 1) {
            distKM = ((Distance) window.getMember("app")).getDist() / 1000.0;
            for (Info i : flight) {
                if (Math.random() < 0.7) {
                    AnchorPane template = new AnchorPane();
                    template.setPrefSize(303, 150);
                    if (j % 3 == 0) {
                        template.setStyle(" -fx-background-color: LAVENDER;");
                    } else if (j % 3 == 1) {
                        template.setStyle(" -fx-background-color: #9AF8D4;");
                    } else {
                        template.setStyle(" -fx-background-color: #FCB0B0;");
                    }
                    VBox labelBox = new VBox();
                    Label nameLabel = new Label();
                    nameLabel.setText(i.getName());
                    Label priceLabel = new Label();
                    calculateRatio(Math.round(i.getPrice() * distKM), "CAD", toCurrencyCB.getSelectionModel().getSelectedItem());
                    priceLabel.setText("Price: " + String.format("%.1f%n", ratio) + " per person");
                    //priceLabel.setText("Dist: " + distKM);
                    Label rateLabel = new Label();
                    rateLabel.setText("Rating: " + i.getRating() + "/5");
                    labelBox.setPrefSize(193, 150);
                    labelBox.setSpacing(20);
                    labelBox.setPadding(new Insets(30, 0, 0, 0));
                    labelBox.getChildren().addAll(nameLabel, priceLabel, rateLabel);
                    Image img = new Image(i.getURL());
                    ImageView imgv = new ImageView();
                    imgv.setImage(img);
                    imgv.setFitHeight(60);
                    imgv.setFitWidth(100);
                    AnchorPane.setLeftAnchor(imgv, 10.0);
                    AnchorPane.setTopAnchor(imgv, 45.0);
                    AnchorPane.setLeftAnchor(labelBox, 120.0);
                    template.getChildren().addAll(imgv, labelBox);
                    ticketInfoBox.getChildren().add(template);
                    j++;
                }
            }
        }
    }

    //browser for google map
    class MyBrowser extends Region {

        final URL urlGoogleMaps = getClass().getResource("googleMap.html");
        final URL urlGoogleMapsFlight = getClass().getResource("googleMapFlight.html");

        public MyBrowser(int i) {
            if (i == 1) {
                webEngine1.load(urlGoogleMapsFlight.toExternalForm());
                webEngine1.setJavaScriptEnabled(true);
                getChildren().add(webView1);
                window = (JSObject) webEngine1.executeScript("window");
                window.setMember("app", new Distance());
            }
            if (i == 0) {
                webEngine0.load(urlGoogleMaps.toExternalForm());
                webEngine0.setJavaScriptEnabled(true);
                getChildren().add(webView0);
            }
        }

    }

    public class Distance {

        private int dist;

        public Distance() {
            dist = 0;
        }

        public void setDist(int d) {
            dist = d;
        }

        public void printDist() {
            System.out.println(dist);
        }

        public int getDist() {
            return dist;
        }
    }
}
