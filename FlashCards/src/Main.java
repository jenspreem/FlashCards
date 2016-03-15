import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws IOException {
	//create model and attach it to CardController
	final CardDB model = new CardDB();
	model.fillCards();
	//loader loads xml, modified controllerfactory
	//allows us to attach our model to controller on creation
	FXMLLoader loader = new FXMLLoader();
	loader.setLocation(getClass().getResource("CardGUI.fxml"));
	loader.setControllerFactory(new Callback<Class<?>, Object>() {
        @Override
        public Object call(Class<?> aClass) {
            return new CardController(model);
        }
    });
	
    // setting the stage
    AnchorPane pane =  (AnchorPane) loader.load();
    Scene scene = new Scene( pane );
    primaryStage.setScene( scene );
    primaryStage.setTitle( "Flashcards_v1.0" );
    primaryStage.show();
    
  }

  public static void main(String[] args) {
    launch(args);
  }
}