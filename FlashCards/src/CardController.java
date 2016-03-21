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
    	if(event.getCode() == KeyCode.SPACE){
        	questionstring=getQuestion();
    		question.setText(questionstring);
        	answer.setDisable(false);
        	//answer.requestFocus();//mingi kuradi nipiga nussib midagi 2ra siin
        	answer.clear();



    	}
       
        }


    @FXML
    private void changeDirection(){
    	originalDirection=!originalDirection;
    	clear_and_wait();
    	
    }

    
  
    @FXML
    private void saveDictionary(ActionEvent event){
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showSaveDialog(stage);
        //todo csv save, add options to denote which direction something is deleted?
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
    private void loadDictionary(ActionEvent event) throws FileNotFoundException{
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Dictionary");
        stage= (Stage) question.getScene().getWindow();       
        File file = fileChooser.showOpenDialog(stage);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        db.fillCards(br);
       
        }
    @FXML //todo refactor maybe you could cut down on repetitive code or something?
    private void showAnswer(ActionEvent event) throws InterruptedException {
     answerstring=((TextField) event.getSource()).getText();
     System.out.println(answerstring);
     System.out.println(db.getReverseDictionary());
     System.out.println(db.getReverseDictionary().get(answerstring));  //kuidas perset requestfocusiga see siis nii muutub, db-st enam seda ei saa?
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
             //in future remove only  from reverse and dont remove hwole pair
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
		// TODO Auto-generated method stub
		questionstring=(this.getQuestion());
		question.setText(questionstring);
        question.setTextAlignment(TextAlignment.CENTER);
	    question.setFont(new Font(26));
	    question.setTextFill(Color.CHOCOLATE);
	    
	    
		
	}




}