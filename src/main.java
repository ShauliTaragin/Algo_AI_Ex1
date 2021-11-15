import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        String v = "af=aa,ef=ff,jsf=tt";
        String[] st = v.split(",");
        String base ="";
        while (st[1].charAt(0) != '=') {
            base += st[1].charAt(0);
            st[1] = st[1].substring(1);
        }
        System.out.println(base);
        getinput a = new getinput("input.txt");
        a.readfromfile();
        String s = a.xml_path;
        xmlinput input = new xmlinput(s);
        BayesianN BN = new BayesianN("a" ,input.createNet());
        ArrayList<Object> answers = new ArrayList<Object>();//creating an arraylist of our answers we will later export to the output file
        for (int i = 0; i < a.holds_q.size(); i++) { //now we are reading the questions we got from the input
            if(a.holds_q.get(i).charAt(0)!='P')//its a bayes ball question
                 answers.add(BayesBall(a.holds_q.get(i), BN));
        }

    }

    public static String BayesBall(String quarry , BayesianN BN){
        Boolean ind = false;
        ArrayList<String> given= new ArrayList<String>();
        String[] question = quarry.split("\\|");
        if(question.length==1) // we don't have any given variables
            given= null;
        else{
            /*The idea of the next code is to add all the given events name into our given list.
            * we will do this by taking apart the line of the quarry we have received(remembering this is a bayesball quarry)
            * until we reach the char/s of our given events name.  */
            String[] get_given_names = question[1].split(",");
            for (int i = 0; i <get_given_names.length ; i++) {
                String given_name = "";
                while (get_given_names[i].charAt(0) != '=') { //the name can be more then 1 char. However we know when we reached char '=' that the name has ended
                    given_name += get_given_names[i].charAt(0);
                    get_given_names[i]=get_given_names[i].substring(1);
                }
                given.add(given_name);//adding the given event we had just found to the given list
            }
            //we now have the givens list
        }
        String[] values_to_answer = question[0].split("-");
        Pnode current = BN.getNodeByName(values_to_answer[0]);
        Pnode target = BN.getNodeByName(values_to_answer[1]);
        ind = BayesBall(given , BN ,current,true,current , target);
        if(ind==true)return "no";
        else return "yes";
    }
    /*
     */
    public static Boolean BayesBall(ArrayList<String> given , BayesianN BN ,Pnode src,Boolean parent, Pnode current , Pnode target){
        if(current.getName()==target.getName())return true;
        //we have four cases. if i came from parent or child and if the current node is given or nor
        if(parent){
            if(given.contains(current)){
                for (int i = 0; i < current.getParents().size(); i++) {

                }
            }
            else { // current is not a given node
                for (int i = 0; i < current.getChildren().size(); i++) {
                    BayesBall(given,BN,src,true,current.getChildren().get(i),target);
                }
                return false;
            }
        }
        else{// is child

        }
        return false;
    }
    public void addclass(int a){
        /*
        **
        *
        *

         */
    }
}
