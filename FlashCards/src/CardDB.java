import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;


public class CardDB {

	private TreeMap<String,String> dictionary =new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
	private TreeMap<String,String> reverse_dictionary =new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
	

	
	public TreeMap<String, String> getDictionary() {
		return dictionary;
	}
	
	public TreeMap<String, String> getReverseDictionary() {
		return reverse_dictionary;
	}
	
	
	
	public void fillCards(BufferedReader br){
		
		dictionary.clear();
		reverse_dictionary.clear();
		String line = "";
		String cvsSplitBy = "\t";
		
		try {
	 
			while ((line = br.readLine()) != null) { 
				String[] flashpair = line.split(cvsSplitBy);
				dictionary.put(flashpair[0], flashpair[1]);  
				//i'll do the reverse also 
				//it's needed that both columns have only unique elements
				reverse_dictionary.put(flashpair[1], flashpair[0]);  
 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	
		
	}
		
	
	public void fillCards(){
		
		InputStream is = getClass().getResourceAsStream("Soome.csv");
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(isr);
		fillCards(br);

		
	
}
}	
