import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getinput {
    private String file_path;
    public ArrayList<String> holds_info;
    public String xml_path;

    public getinput(String path){
        file_path = path;
        holds_info = new ArrayList<String>();
    }
    public void readfromfile(String name){
        name = this.file_path;
        try {
            FileReader fr = new FileReader(name);
            BufferedReader br = new BufferedReader(fr);
            xml_path=br.readLine();
            while(!br.readLine().isEmpty()) {
                holds_info.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e ){
            e.printStackTrace();
        }
    }
}
