import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
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
	private Stage stage; //required later by fileChooser in load/saveDictionary methods
	private boolean originalDirection=true;//questions taken from db.getDictionary
	
	@FXML // fx:id="question"
    private Label question; // Value injected by FXMLLoader
    
    @FXML // fx:id="answer"
    private TextField answer; // Value injected by FXMLLoader
    
    @FXML // fx:id="save"
    private MenuItem save; // Value injected by FXMLLoader
    
    @FXML // fx:id="changeDirButton"
    private MenuItem changeDirButton; // Value injected by FXMLLoader
    
    //animation sets new question and unlocks answer box
    final Timeline animation = new Timeline(
            new KeyFrame(Duration.seconds(2),
            new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent actionEvent) {
            		questionstring=getQuestion();
            		question.setText(questionstring);
                	answer.setDisable(false);
                }
            }));
	
	
	
    public CardController(CardDB model) {
        this.db = model;
    }
    

    
    private String getQuestion(){
    	Random generator = new Random();
    	Object[] values;
    	if (originalDirection){
        	values = this.db.getDictionary().keySet().toArray();
    	}
    	else {values =this.db.getReverseDictionary().keySet().toArray();}
    	String randomValue = (String)values[generator.nextInt(values.length)];
    	return randomValue;
    }





    @FXML
    private void changeDirection(){
    	originalDirection=!originalDirection;
    	answer.setDisable(true);
    	animation.play();
    	
    }

    
  
    @FXML
    private void saveDictionary(ActionEvent event){
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showSaveDialog(stage);
        //todo csv save, add options to denote which direction somethin is deleted?
        String eol = System.getProperty("line.separator");

        try (Writer writer = new FileWriter(file)) {
          for (Map.Entry<String, String> entry : db.getDictionary().entrySet()) {
            writer.append(entry.getKey())
                  .append('\t')
                  .append(entry.getValue())
                  .append(eol);
          }
        } catch (IOException ex) {
          ex.printStackTrace(System.err);
        }
        }
 
    @FXML
    private void loadDictionary(ActionEvent event){
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showOpenDialog(stage);
        db.fillCards(file);
       
        }
    @FXML //todo refactor maybe you could cut down on repetitive code or something?
    private void showAnswer(ActionEvent event) throws InterruptedException {
     answerstring=((TextField) event.getSource()).getText();
     if (originalDirection){
    	 if(questionstring.equalsIgnoreCase(db.getReverseDictionary().get(answerstring))){
             question.setText("Õige\n"+questionstring+"="+answerstring);
             db.getDictionary().remove(questionstring); //you'll save a csv without this pair
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
     else {
    	 if(questionstring.equalsIgnoreCase(db.getDictionary().get(answerstring))){
             question.setText("Õige\n"+questionstring+"="+answerstring);
             db.getDictionary().remove(answerstring); //you'll save a csv without this pair
             //in future remove from reverse and dont remove hwole pair
             answer.clear();
             answer.setDisable(true);
             animation.setCycleCount(1);
             animation.play();

         }
         else{
        	 question.setText("Vale!\n"+questionstring+"="+db.getReverseDictionary().get(questionstring));
        	 answer.clear();
        	 answer.setDisable(true);
        	 animation.setCycleCount(1);
        	 animation.play();

         }
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