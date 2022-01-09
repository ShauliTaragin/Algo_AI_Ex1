import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getinput {
    private String file_path;
    public ArrayList<String> holds_q;
    public String xml_path;

    /**
     * I'll create a class for getting input inorder to work in an organized matter.
     * @param path -> Receiving the path of the txt file which contains our input
     */
    public getinput(String path){
        this.file_path = path;
        this.holds_q = new ArrayList<String>();
    }

    /**
     * In this function we read our file and organize all our quarries.
     * We also save the xml path in order to read our xml file for our network
     */
    public void readfromfile(){
        try {
            FileReader fr = new FileReader(this.file_path);
            BufferedReader br = new BufferedReader(fr);
            xml_path = br.readLine();
            String t = br.readLine();
            while(t!=null) {
                holds_q.add(t);
                t= br.readLine();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e ){
            e.printStackTrace();
        }
    }
}
