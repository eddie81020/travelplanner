package MVC;

import java.net.URL;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    HBox root;
    VBox right;
    VBox left;
    TravalPlannerModel myModel;

    /**
     *    * @param args the command line arguments    
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traval Planner (beta ver.1.2.63 demo)");

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
        hotelInfoLab.setPrefSize(300, 800);
        hotelInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        hotelInfoTab.setContent(hotelInfoLab);
        
        Tab sightInfoTab = new Tab();
        sightInfoTab.setText("Sights");
        sightInfoTab.setClosable(false);
        InfoTabLabel sightInfoLab = new InfoTabLabel("sights", "SIGHTS INFO!!!!!");
        sightInfoLab.setPrefSize(300, 800);
        sightInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        sightInfoTab.setContent(sightInfoLab);
        
        Tab ticketInfoTab = new Tab();
        ticketInfoTab.setText("Tickets");
        ticketInfoTab.setClosable(false);
        InfoTabLabel ticketInfoLab = new InfoTabLabel("ticket", "TICKETS INFO!!!!!");
        ticketInfoLab.setPrefSize(300, 800);
        ticketInfoLab.setStyle(" -fx-background-color: LAVENDER;");
        ticketInfoTab.setContent(ticketInfoLab);
        
        infoSec.getTabs().addAll(hotelInfoTab, sightInfoTab, ticketInfoTab);
        myModel.addListener(hotelInfoLab);
        myModel.addListener(sightInfoLab);
        myModel.addListener(ticketInfoLab);

        right.setPadding(new Insets(0, 0, 0, 20));
        right.getChildren().addAll(infoLab, infoSec);

        left.setSpacing(10);
        left.setPadding(new Insets(0, 20, 0, 0));
        TextField departureTF = new TextField("Enter Departure");
        TextField destinationTF = new TextField("Enter Destination");
        Label depLab = new Label("Departure:");
        Label desLab = new Label("Destination");
        depLab.setFont(Font.font(20));
        desLab.setFont(Font.font(20));
        Button submitButton = new Button("Submit");
        left.getChildren().addAll(depLab, departureTF, desLab, destinationTF, submitButton, myBrowser);

        submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String DepTemp = departureTF.getText();
                String DesTemp = destinationTF.getText();
                webEngine.executeScript("inputStartEnd('" + DepTemp + "','" + DesTemp + "')");
                myModel.setDeparture(DepTemp);
                myModel.setDestination(DesTemp);
            }
        });

        root.getChildren().addAll(left, right);
        scene = new Scene(root, 1200, 810);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //browser for google map
    class MyBrowser extends Region {

        public MyBrowser() {

            final URL urlGoogleMaps = getClass().getResource("googleMap.html");
            webEngine.load(urlGoogleMaps.toExternalForm());
            webEngine.setJavaScriptEnabled(true);
            getChildren().add(webView);

        }
    }
}
