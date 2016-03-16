import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class CardController implements Initializable {
	private final CardDB db;
	private String questionstring;
	private String answerstring;
	

    public CardController(CardDB model) {
        this.db = model;
    }
  //check for mistakes this one was a woozy
    final Timeline animation = new Timeline(
            new KeyFrame(Duration.seconds(2),
            new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
            		questionstring=getQuestion();
            		question.setText(questionstring);
                	answer.setDisable(false);
                }
            }));
    
    private String getQuestion(){
    	Random generator = new Random();
    	Object[] values = this.db.getReverseDictionary().values().toArray();
    	String randomValue = (String)values[generator.nextInt(values.length)];
    	return randomValue;
    }



	@FXML // fx:id="question"
    private Label question; // Value injected by FXMLLoader
    
    @FXML // fx:id="answer"
    private TextField answer; // Value injected by FXMLLoader
    
    @FXML // fx:id="save"
    private MenuItem save; // Value injected by FXMLLoader
    

    private Stage stage; //required later by fileChooser
    

    
  
    @FXML
    void saveDictionary(ActionEvent event){
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showSaveDialog(stage);
        //todo csv save
       
        }
 
    @FXML
    void loadDictionary(ActionEvent event){
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showOpenDialog(stage);
        //todo csv load
       
        }
    @FXML
    void showAnswer(ActionEvent event) throws InterruptedException {
     answerstring=((TextField) event.getSource()).getText();
     if(questionstring.equalsIgnoreCase(db.getReverseDictionary().get(answerstring))){
         question.setText("Õige\n"+questionstring+"="+answerstring);
         answer.clear();
         answer.setDisable(true);
         animation.setCycleCount(1);
         animation.play();

     }
     else{
    	 question.setText("Vale!\n"+questionstring+"="+db.getDictionary().get(questionstring));
    	 answer.clear();
    	 answer.setDisable(true);
    	 animation.setCycleCount(1);
    	 animation.play();

     }

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		questionstring=(this.getQuestion());
		question.setText(questionstring);
        question.setTextAlignment(TextAlignment.CENTER);
	    question.setFont(new Font(26));
	    question.setTextFill(Color.CHOCOLATE);
	    
	    
		
	}




}