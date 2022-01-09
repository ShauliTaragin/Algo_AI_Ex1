import java.io.*;
import java.util.ArrayList;

public class Ex1 {
    public static void main(String[] args) {
        getinput a = new getinput("input.txt");
        a.readfromfile();
        String s = a.xml_path;
        xmlinput input = new xmlinput(s);
        BayesianN BN = new BayesianN("a" ,input.createNet());
        ArrayList<String> answers = new ArrayList<String>();//creating an arraylist of our answers we will later export to the output file
        Variable_Elimination ve;
        for (int i = 0; i < a.holds_q.size(); i++) { //now we are reading the questions we got from the input
            if(a.holds_q.get(i).charAt(0)!='P')//its a bayes ball question
                 answers.add(BayesBall(a.holds_q.get(i), BN));
            else {
                ve = new Variable_Elimination(BN, a.holds_q.get(i));
                String VE_Answer = ve.variable_elimination();
                answers.add(VE_Answer);
            }
        }
        writeToFile(answers);
    }
    /**
     * Writes the answers of the algorithm to a txt file
     * @param answers represents the result of the algorithm.
     *  Contains: for bayes ball yes or no
     *            for variable elimination the probability of a given query, amount of adds and amount of multiplication.
     *  creates output.txt represents which contains the answers.
     */
    public static void writeToFile(ArrayList<String> answers){
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            for (int i = 0; i <answers.size() ; i++) {
                myWriter.write(answers.get(i)+"\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
    @param quarry - we recieve the quarry we wish to ask the bayes ball algorithm
           BayesianN BN - the bayesian net for which we are operating on.
    In this function we parse are given quarry until we reach the nodes we wish to send to our bayes ball algorithm.
    after we are able to gather the given nodes and the quarry nodes we send them into are main bayes ball function which
    will return whether the nodes we are checking in our quarry is true or false.
     */
    public static String BayesBall(String quarry , BayesianN BN){
        Boolean ind = false;
        ArrayList<String> given= new ArrayList<String>();
        String[] question = quarry.split("\\|");
        if(question.length!=1){// only if we have any given variables
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
        ArrayList<String> visited_and_given = new ArrayList<>();
        ind = BayesBall(given , BN ,visited_and_given,false,current , target);
        if(ind==true)return "no";
        else return "yes";
    }

    /**
     *
     * @param given -> an array list of our givens
     * @param BN -> our bayesian network for which we wish to run bayes ball on
     * @param visited_and_given -> this will serve as our "memory". we save all our nodes which we visted and they are given
     *                          this way we will know when to stop our recursion
     * @param parent-> A flag which tells us whether we came from our parent or from our child.
     * @param current->the current node we are on in the recursion process
     * @param target -> The target node we wish to know if we are independent to or not
     * @return true = dependent
     *         false = independent
     */
    public static Boolean BayesBall(ArrayList<String> given , BayesianN BN ,ArrayList<String> visited_and_given,Boolean parent, Pnode current , Pnode target){
        if(current.getName()==target.getName())return true;
        //we have four cases. if i came from parent or child and if the current node is given or nor
        if(parent){
            if(given.contains(current.getName())){
                for (int i = 0; i < current.getParents().size(); i++) {
                    visited_and_given.add(current.getName());
                    if(!visited_and_given.contains(current.getParents().get(i).getName())) {
                        if(BayesBall(given, BN, visited_and_given, false, current.getParents().get(i), target))return true;
                    }
                }
                return false;
            }
            else { // current is not a given node
                for (int i = 0; i < current.getChildren().size(); i++) {
                    if(!visited_and_given.contains(current.getChildren().get(i).getName())) {
                        if(BayesBall(given, BN, visited_and_given, true, current.getChildren().get(i), target))return true;
                    }
                }
                return false;
            }
        }
        else{// we came from child
            if(given.contains(current.getName()))
                return false;
            else{//current is not a given node
                for (int i = 0; i < current.getParents().size(); i++) {
                    if(!visited_and_given.contains(current.getParents().get(i).getName())) {
                        if(BayesBall(given, BN, visited_and_given, false, current.getParents().get(i), target))return true;
                    }
                }
                for (int i = 0; i < current.getChildren().size(); i++) {
                    if(!visited_and_given.contains(current.getChildren().get(i).getName())) {
                        if(BayesBall(given, BN, visited_and_given, true, current.getChildren().get(i), target))return true;
                    }
                }
            }
        }
        return false;
    }
}
