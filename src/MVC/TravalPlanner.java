package MVC;

import java.net.URL;
import java.util.LinkedList;
import javafx.application.Application;
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

/**
 *
 *
 */
public class TravalPlanner extends Application {

    private Scene scene;
    MyBrowser myBrowser;
    int browser = 1;
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    HBox root;
    VBox right;
    VBox left;
    TravalPlannerModel myModel;

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
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traval Planner (beta ver.3.6.22 demo)");

        root = new HBox();
        right = new VBox();
        left = new VBox();
        myBrowser = new MyBrowser();
        TabPane infoSec = new TabPane();
        myModel = new TravalPlannerModel();

        root.setPadding(new Insets(10, 20, 20, 20));

        Label infoLab = new Label("Information");
        infoLab.setFont(Font.font(20));

        Tab hotelInfoTab = new Tab();
        hotelInfoTab.setText("Hotels");
        hotelInfoTab.setClosable(false);
        InfoTabLabel hotelInfoLab = new InfoTabLabel("hotel", "HOTEL INFO!!!!!");
        hotelInfoLab.setPrefSize(320, 150);
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
        sightInfoLab.setPrefSize(320, 150);
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
        ticketInfoLab.setPrefSize(320, 150);
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
        travelMethod.getSelectionModel().select(4);

        HBox submitPanel = new HBox();
        submitPanel.setSpacing(20);
        submitPanel.getChildren().addAll(travelMethod, submitButton);

        submitButton.setOnMouseClicked((MouseEvent event) -> {
            String DepTemp = departureTF.getText();
                String DesTemp = destinationTF.getText();
            if (travelMethod.getSelectionModel().getSelectedIndex() != 4) {
                if (browser != 0) {
                    myBrowser.switchMap();
                    browser = 0;
                }
                webEngine.executeScript("setTravelMethod('" + travelMethod.getSelectionModel().getSelectedItem() + "')");
                webEngine.executeScript("inputStartEnd('" + DepTemp + "','" + DesTemp + "')");
                myModel.setDeparture(DepTemp);
                myModel.setDestination(DesTemp);
            } else {
                if (browser != 1) {
                    myBrowser.switchMap();
                    browser = 1;
                }
                webEngine.executeScript("setLocation('" + DepTemp + "','" + DesTemp + "')");
            }
            setTabs(DesTemp);
        });

        left.getChildren().addAll(depLab, departureTF, desLab, destinationTF, submitPanel, myBrowser);

        root.getChildren().addAll(left, right);
        scene = new Scene(root, 1200, 810);

        primaryStage.setScene(scene);
        primaryStage.show();
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
                priceLabel.setText("Price: " + i.getPrice() + " per night");
                Label rateLabel = new Label();
                rateLabel.setText("Rating: " + i.getRating() + "/5");
                labelBox.setPrefSize(193, 150);
                labelBox.setSpacing(20);
                labelBox.setPadding(new Insets(35, 0, 0, 0));
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
                if(i.getPrice()>0)
                    priceLabel.setText("Price: " + i.getPrice() + " per person");
                else
                    priceLabel.setText("Price: free");
                Label rateLabel = new Label();
                rateLabel.setText("Rating: " + i.getRating() + "/5");
                labelBox.setPrefSize(193, 150);
                labelBox.setSpacing(20);
                labelBox.setPadding(new Insets(35, 0, 0, 0));
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
                if(i.getPrice()>0)
                    priceLabel.setText("Price: " + i.getPrice() + " per person");
                else
                    priceLabel.setText("Price: free");
                Label rateLabel = new Label();
                rateLabel.setText("Rating: " + i.getRating() + "/5");
                labelBox.setPrefSize(193, 150);
                labelBox.setSpacing(20);
                labelBox.setPadding(new Insets(35, 0, 0, 0));
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

    //browser for google map
    class MyBrowser extends Region {

        private int map;
        final URL urlGoogleMaps = getClass().getResource("googleMap.html");
        final URL urlGoogleMapsFlight = getClass().getResource("googleMapFlight.html");

        public MyBrowser() {
            map = 1;

            webEngine.load(urlGoogleMapsFlight.toExternalForm());
            webEngine.setJavaScriptEnabled(true);
            getChildren().add(webView);

        }

        public void switchMap() {
            if (map == 0) {
                webEngine.load(urlGoogleMapsFlight.toExternalForm());
                map = 1;
            } else {
                webEngine.load(urlGoogleMaps.toExternalForm());
                map = 0;
            }
        }
    }

    class Distance {

        private Double dist;

        public Distance() {
            dist = 0.0;
        }

        public void setDist(Double d) {
            dist = d;
        }

        public Double getDist() {
            return dist;
        }
    }
}
