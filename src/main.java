import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        getinput a = new getinput("input.txt");
        a.readfromfile();
        String s = a.xml_path;
        xmlinput input = new xmlinput(s);
        BayesianN BN = new BayesianN("a" , input.createNet());
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
