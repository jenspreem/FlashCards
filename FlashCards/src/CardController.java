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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
    	Object[] values = this.db.getCards().values().toArray();
    	String randomValue = (String)values[generator.nextInt(values.length)];
    	return randomValue;
    }



	@FXML // fx:id="question"
    private Label question; // Value injected by FXMLLoader
    
    @FXML // fx:id="answer"
    private TextField answer; // Value injected by FXMLLoader
    
    
    @FXML
    void showAnswer(ActionEvent event) throws InterruptedException {
     answerstring=((TextField) event.getSource()).getText();
     if(questionstring.equalsIgnoreCase(db.getCards().get(answerstring))){
         question.setText("Õige\n"+questionstring+"="+answerstring);
         answer.clear();
         answer.setDisable(true);
         animation.setCycleCount(1);
         animation.play();

     }
     else{
    	 question.setText("Vale!\n"+questionstring+"="+db.getCards().get(questionstring));
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