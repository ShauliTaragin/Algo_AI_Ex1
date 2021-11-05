import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] args) {


    }
    public static void readfromfile(String name){
        try {
            FileReader fr = new FileReader(name);
            BufferedReader br = new BufferedReader(fr);
            System.out.println(br.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e ){
            e.printStackTrace();
        }
    }
    public void addclass(int a){
        /*
        **
        *
        *

         */
    }
}
