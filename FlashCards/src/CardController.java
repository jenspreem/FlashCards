import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CardController implements Initializable {
	private final CardDB db;
	private String questionstring;
	private String answerstring;
	private Stage stage; //required later by fileChooser in load/saveDictionary methods
	private boolean originalDirection=true;//questions taken from db.getDictionary not reverse
	
	@FXML // fx:id="question"
    private Label question; // Value injected by FXMLLoader
    
    @FXML // fx:id="answer"
    private TextField answer; // Value injected by FXMLLoader
    
    @FXML // fx:id="save"
    private MenuItem save; // Value injected by FXMLLoader
    
    @FXML // fx:id="changeDirButton"
    private MenuItem changeDirButton; // Value injected by FXMLLoader   
	
	
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

    
    private void clear_and_wait(){
    	answer.clear();
        answer.setDisable(true);
        answer.getParent().requestFocus();
    }

    @FXML 
    private void newQuestion(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER)//KeyCode.Space + requestfocus started the textfield with a space inserted
    	{ 
        	questionstring=getQuestion();
    		question.setText(questionstring);
        	answer.setDisable(false);
        	answer.requestFocus();//space + requestfocus started the textfield with a space inserted
        	answer.clear();//which is weird as we have this here? shouldnt it clear the space then
    	}     
    }


    @FXML
    private void changeDirection(){
    	originalDirection=!originalDirection;
    	clear_and_wait();   	
    }
   
  
    @FXML
    private void saveDictionary(ActionEvent event)
    {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showSaveDialog(stage);
        String eol = System.getProperty("line.separator");

        try (Writer writer = new FileWriter(file))
        {
          for (Map.Entry<String, String> entry : db.getDictionary().entrySet())
            {
            writer.append(entry.getKey())
                  .append('\t')
                  .append(entry.getValue())
                  .append(eol);
            }
        } 
        catch (IOException ex) 
        {
          ex.printStackTrace(System.err);
        }
    }
 
    @FXML
    private void loadDictionary(ActionEvent event) throws FileNotFoundException{
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showOpenDialog(stage);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        db.fillCards(br);     
        }
    
    @FXML 
    private void showAnswer(ActionEvent event) throws InterruptedException {
     answerstring=((TextField) event.getSource()).getText();
     if (originalDirection){
    	 if(questionstring.equalsIgnoreCase(db.getReverseDictionary().get(answerstring))){
             question.setText("Õige\n"+questionstring+"="+answerstring);
             db.getDictionary().remove(questionstring); //you'll save a csv without this pair
             clear_and_wait();

         }
         else{
        	 question.setText("Vale!\n"+questionstring+"="+db.getDictionary().get(questionstring));
             clear_and_wait();
         }
     
     }
     else {
    	 if(questionstring.equalsIgnoreCase(db.getDictionary().get(answerstring))){
             question.setText("Õige\n"+questionstring+"="+answerstring);
             db.getDictionary().remove(answerstring); //you'll save a csv without this pair
             db.getReverseDictionary().remove(questionstring);//don't ask again in this session either
             //in future remove only  from reverse and don't remove whole pair?
             //then the csv file need extra encoding and processing
             clear_and_wait();

         }
         else{
        	 question.setText("Vale!\n"+questionstring+"="+db.getReverseDictionary().get(questionstring));
             clear_and_wait();


         }
     }

    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		questionstring=(this.getQuestion());
		question.setText(questionstring);
        question.setTextAlignment(TextAlignment.CENTER);
	    question.setFont(new Font(26));
	    question.setTextFill(Color.CHOCOLATE);
	    
	    
		
	}




}