import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        getinput a = new getinput("input.txt");
        a.readfromfile();
        System.out.println(a.xml_path + "\n rest of file is:" + a.holds_q);
    }

    public void addclass(int a){
        /*
        **
        *
        *

         */
    }
}
