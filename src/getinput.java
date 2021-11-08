import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getinput {
    private String file_path;
    public ArrayList<String> holds_q;
    public String xml_path;

    public getinput(String path){
        file_path = path;
        holds_q = new ArrayList<String>();
    }
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
