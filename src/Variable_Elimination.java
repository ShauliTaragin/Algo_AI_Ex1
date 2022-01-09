import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Variable_Elimination {

    public BayesianN Bn;
    private ArrayList<Factor> factors;
    private ArrayList<String> order_join_eliminate; //also holds all the hidden nodes by name
    private ArrayList<String> evidence; //holds all our evidence
    private ArrayList<String> evidence_values; // holds the values of our evidence each index i hold value for evidence in index i
    private ArrayList<String> not_relevant;//holds the non relevant values
    private String quarry_node; //hold the node we are asking the question on and its outcome e.g "B=T"
    public int numOfMul;// Counting how many multiplication occur during the algorithm
    public int numOfAdd;// Counting how many adding operations occur during the algorithm

    /**
     * Our constructor for Variable_Elimination.
     * @param bayesian_net->Receiving our bayesian net
     * @param quarry-> the quarry we wish to get answers for. note- we receive the quarry before parsing
     *       First we parse our quarry into organized lists holding our quarry, evidence, hidden and there values(if needed)
     *       once we have all are lists ready we can start the actual VE.
     *
     */
    public Variable_Elimination(BayesianN bayesian_net, String quarry) {
        this.Bn = bayesian_net;
        this.order_join_eliminate = new ArrayList<String>();
        this.numOfMul = 0;
        this.numOfAdd = 0;
        String[] split_quarry_and_hidden = quarry.split(" ");// e.g-> [P(B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace("P(", "");//e.g-> [B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace(")", "");//e.g-> [B=T|J=T,M=T,A-E]
        if (split_quarry_and_hidden.length>1 && split_quarry_and_hidden[1].length() > 0) {
            String[] order_of_hidden = split_quarry_and_hidden[1].split("-");//e.g-> [A,E]
            for (int i = 0; i < order_of_hidden.length; i++) {
                this.order_join_eliminate.add(order_of_hidden[i]);
            }
        }
        this.evidence = new ArrayList<String>();//initiate evidence array list
        this.evidence_values = new ArrayList<String>();
        String[] split_quarry_and_evidence = split_quarry_and_hidden[0].split("\\|");//e.g-> [B=T,J=T,M=T]
        this.quarry_node = split_quarry_and_evidence[0]; //initiate quarry node

        if (split_quarry_and_evidence.length>1 && split_quarry_and_evidence[1].length() > 0) {//checking only if we have evidence
            String[] input_evidence = split_quarry_and_evidence[1].split(",");
            for (int i = 0; i < input_evidence.length; i++) {
                String[] input_evidence_value = input_evidence[i].split("=");
                this.evidence.add(input_evidence_value[0]);
                this.evidence_values.add(input_evidence_value[1]);
            }
        }
        this.factors = new ArrayList<Factor>();
        this.not_relevant = new ArrayList<String>();
    }

    /**
     * We start by iterating over all the nodes and checking which node is not relevant according to 3 criterias.
     * 1.if is not ancestor of quarry or
     * 2.if is not ancestor of any evidence
     * 3.if it is one of the ancestors we found in 1/2 the check if its independent to our quarry
     * The nodes which are not relevant will not enter the list of factors
     * then we will check if we have our answer to our quarry right away, if so we return it.
     * then we start "taking care of our hidden variables. we iterate over all our hidden factors by order and join and eliminate each one.
     * if we finished eliminating all our hidden factors we join and eliminate all our quarry factors.
     * When we finish that we will have one factor left. we normalize and return the correct value.
     */
    public String variable_elimination() {
        //first we kick out anyone who is not relevent(not an anccestor/is independent)
        //on the ancestors we check on the quarry and evidence
        //bayesball we send the quarry given the evidence
        String[] Q = this.quarry_node.split("=");
        Pnode Quarry_node = Bn.getNodeByName(Q[0]);
        //I want an arraylist that contains the evidence with their values i.e -> [E=T,B=F] because thats how my factors constructor works
        ArrayList<String> evidence_with_value = new ArrayList<>();
        for (int i = 0; i < evidence.size(); i++) {
            evidence_with_value.add(evidence.get(i) + "=" + evidence_values.get(i));
        }
        for (int i = 0; i < Bn.events.size(); i++) {
            Pnode curr = Bn.events.get(i);
            if (this.order_join_eliminate.contains(curr.getName())) {//only running and checking the hidden nodes
                Boolean isRelevant = false;
                if (Quarry_node.isAncestor(curr)) {
                    isRelevant = true;
                }
                if (!isRelevant) {
                    for (int j = 0; j < this.evidence.size(); j++) {
                        Pnode evi = this.Bn.getNodeByName(this.evidence.get(j));
                        if (evi.isAncestor(curr)) {
                            isRelevant = true;
                            break;
                        }
                    }
                }
                if (isRelevant) {//check if the one we found to be ancestor is dependent to our quarry given our evidence
                    String enter_quarry_BB = Quarry_node.getName() + "-" + curr.getName() + "|";
                    for (int j = 0; j < evidence.size(); j++) {
                        if (j < evidence.size() - 1)
                            enter_quarry_BB += evidence.get(j) + "=" + evidence_values.get(j) + ",";
                        else
                            enter_quarry_BB += evidence.get(j) + "=" + evidence_values.get(j);

                    }
                    Factor curr_init_factor = new Factor(curr, evidence_with_value);
                    if (Ex1.BayesBall(enter_quarry_BB, this.Bn).equals("no")) {
                        factors.add(curr_init_factor);
                    } else
                        this.not_relevant.add(curr.getName()); //add the curr hidden node to non relevant and later check if its occurrence appears in any other factor, if so delete that factor
                }
            }
        }
        this.factors.add(new Factor(Quarry_node, evidence_with_value));
        for (int i = 0; i < this.evidence.size(); i++) {
            Factor evidence_factor_to_add = new Factor(Bn.getNodeByName(this.evidence.get(i)), evidence_with_value);
            if (evidence_factor_to_add.size_of_rows != 1)// if a factor becomes one valued, discard it.
                this.factors.add(evidence_factor_to_add);
        }
        //now we will check if we can reach the answer right away
        Boolean right_away = true;
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < this.factors.get(i).size_of_rows; j++) {
                HashMap<String, String> row = this.factors.get(i).table.get(j);
                if (row.containsKey(Q[0]) && row.get(Q[0]).equals(Q[1]) ) {
                    for (int k = 0; k < this.evidence.size(); k++) {
                        if (row.containsKey(this.evidence.get(k)) && row.get(this.evidence.get(k)).equals(this.evidence_values.get(k)) && row.size()==this.evidence.size()+1) {//3rd condition is if my row only contains evidence and quarry
                            right_away = true;
                        }
                        else {
                            right_away = false;
                            break;
                        }
                    }
                    if (right_away) {//if the return value is a box that we found we return it rigth away.
                        String answer_directly = row.get("Pr");
                        answer_directly += "," + this.numOfAdd + "," + this.numOfMul;
                        return answer_directly;
                    }
                }
            }
        }
        remove_irrelevent_nodes();
        //from this point forward we deal with the actual join and eliminate
        this.factors.sort(Factor::compareTo);//we first sort our factors according to size and ascii
        while (this.order_join_eliminate.size() > 0) {// we will go with each hidden variable(by order) and join the factors which contain it.
            String hidden = this.order_join_eliminate.get(0);
            int iterator = 0;
            Factor a = new Factor();
            while (iterator < this.factors.size()) {
                if (this.factors.get(iterator).size_of_rows == 1) {// if a factor becomes one valued, discard it.
                    this.factors.remove(iterator);
                    iterator = 0;
                }
                HashMap<String, String> first_row = this.factors.get(iterator).table.get(0);
                if ((first_row.containsKey(hidden) && a.size_of_rows == 0)) {
                    a = this.factors.get(iterator++);
                    continue;
                }
                if (first_row.containsKey(hidden)) {
                    Factor b = this.factors.get(iterator++);
                    join(a, b);
                    a = new Factor();
                    iterator = 0;
                    continue;
                }
                iterator++;
            }
            for (int i = 0; i < this.factors.size(); i++) {
                if (this.factors.get(i).table.get(0).containsKey(hidden))
                    eliminate(this.factors.get(i), hidden);
            }
            //if we finished the loop this means there is only one factor containing are hidden variable
            //then we should do eliminate
            this.order_join_eliminate.remove(0);
        }
        //once we finished all our hiddens we should join and eliminate out all our quarry factors left until we are left with one factor
        Factor a = new Factor();
        int iterator = 0;
        while (this.factors.size() > 1) {
            HashMap<String, String> first_row = this.factors.get(iterator).table.get(0);
            if (first_row.containsKey(Q[0]) && a.size_of_rows == 0) {//Q[0] is name of our quarry node
                a = this.factors.get(iterator++);
                continue;
            }
            if (first_row.containsKey(Q[0])) {
                Factor b = this.factors.get(iterator++);
                join(a, b);
                a = new Factor();
                iterator = 0;
                continue;
            }
            iterator++;
        }
        //at this point we a are left with one factor therefore we normalize and get the correct value in our factor
        // for which we asked the quarry one.
        Factor last_factor = this.factors.get(0);
        normalize(last_factor);
        double return_probability = 0;
        for (int i = 0; i < last_factor.size_of_rows; i++) {
            if (last_factor.table.get(i).get(Q[0]).equals(Q[1])) {
                return_probability = Double.parseDouble(last_factor.table.get(i).get("Pr"));
                break;
            }
        }
        NumberFormat format_5_digits = new DecimalFormat("#0.00000");
        String answer = format_5_digits.format(return_probability);
        answer += "," + this.numOfAdd + "," + this.numOfMul;
        return answer;
    }

    /**
     * Now we remove all the factors for which we find that they contain a irrelevant node(which we found through bayesball because it is independent to quarry and evidence)
     */
    public void remove_irrelevent_nodes() {
        int[] list_of_factors_to_rmv_by_index = new int[this.factors.size()];
        Arrays.fill(list_of_factors_to_rmv_by_index ,-1);
        int counter_for_factors_removed = 0;
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < this.factors.get(i).size_of_rows; j++) {
                HashMap<String, String> row = this.factors.get(i).table.get(j);
                for (int k = 0; k < not_relevant.size(); k++) {
                    if (row.containsKey(not_relevant.get(k)))
                        list_of_factors_to_rmv_by_index[i] = 1;
                }
            }
        }
        for (int i = 0; i <list_of_factors_to_rmv_by_index.length; i++) {
            if (list_of_factors_to_rmv_by_index[i]!=-1) {
                this.factors.remove(i-counter_for_factors_removed);
                counter_for_factors_removed++;
            }
        }
    }

    /**
     * The joining two factors function.
     * @param a -> receiving 2 different factors
     * @param b
     * We start by creating a new factor c which will be the joining result of the 2 factors we received.
     * we first find all the common variables of a and b (because there can be a complicated join of a few variables)
     * Then we iterate over a and b's rows and our common variables, if we find rows for which the outcome for all common variables are the same
     *      we will do join there.
     * Once we find which rows we join, we multiply their probabilities and create a new row in c with their joint probability.
     *      and add to that same row in c both a and b's variables as keys and their outcomes as values.
     * Finally we remove a and b from our factors list and add c. and resort the factors list.
     */
    public void join(Factor a, Factor b) {
        Factor c = new Factor();
        ArrayList<String> common_variables = new ArrayList<>();
        for (String key1 : a.table.get(0).keySet()) {
            if (b.table.get(0).containsKey(key1) && !key1.equals("Pr")) //only if they have the same key and that key is not the probability key
                common_variables.add(key1);
        }
        for (int i = 0; i < a.size_of_rows; i++) {
            HashMap<String, String> row_a = a.table.get(i);
            for (int j = 0; j < b.size_of_rows; j++) {
                HashMap<String, String> row_b = b.table.get(j);
                Boolean do_factor_here = true;
                for (int k = 0; k < common_variables.size(); k++) {
                    if (!row_a.get(common_variables.get(k)).equals(row_b.get(common_variables.get(k)))) {//if the hidden value is the same only then merge them
                        do_factor_here = false;
                        break;
                    }
                }
                if (do_factor_here) {
                    double joint_probabilty = Double.parseDouble(row_a.get("Pr")) * Double.parseDouble(row_b.get("Pr"));
                    String str_joint_probabilty = "" + joint_probabilty;
                    c.add_row(str_joint_probabilty);
                    this.numOfMul++;
                    //now add each value of both factors to new factors.
                    //for both factors i will only add to c the variables that are not there yet.
                    for (String key1 : row_a.keySet()) {
                        if (!c.table.get(c.size_of_rows - 1).containsKey(key1)) // adding all the new variables we got from the join but only if they are not there yet
                            c.table.get(c.size_of_rows - 1).put(key1, row_a.get(key1));
                    }
                    for (String key2 : row_b.keySet()) {
                        if (!c.table.get(c.size_of_rows - 1).containsKey(key2))
                            c.table.get(c.size_of_rows - 1).put(key2, row_b.get(key2));
                    }
                }
            }
        }
        this.factors.remove(a);
        this.factors.remove(b);
        this.factors.add(c);
        this.factors.sort(Factor::compareTo);
        //at the end of join we need remove a and b add c and to sort factors again.
    }

    /**
     * The eliminate function
     * @param a -> receive the factor for which we wish to eliminate
     * @param hidden -> The hidden node we wish to eliminate out
     * Since we can't delete rows during running time we create an array for which every row we find that we need to remove
     *               we mark that index in our array with a 1. At the end of the function every index which is marked by a 1 we remove
     *               I used this method in different functions and classes through out this project.
     * Then we iterate over a's rows(comparing each 2 different rows) and our not hidden variables, if we find rows for which the outcome
     *              for all not hidden variables are the same then we will eliminate there.
     * Once we find which rows we eliminate(row i and j), we add their probabilities and put that probability in the first row(the i row)
     *      and add to the to delete array the j row in order to delete it. we will not iterate over that row we marked to delete again.
     */
    public void eliminate(Factor a, String hidden) {
        int[] to_delete = new int[a.size_of_rows];//similar to hash every row we will want to delete will be saved in the array with a 1. e.g row 2 to be deleted then -> to delete[2]=1
        Arrays.fill(to_delete, -1);
        int counter_for_rows_removed = 0;
        ArrayList<String> not_hidden = new ArrayList<>();
        for (String key : a.table.get(0).keySet()) {
            if (!key.equals(hidden))
                not_hidden.add(key);//first adding the non hidden variables to my list because we will know which rows to add according to these variables.
        }
        not_hidden.remove("Pr"); // Pr will be in every row but values will always be different so we are not intersted in it.
        for (int i = 0; i < a.size_of_rows; i++) {
            if (to_delete[i] == 1) continue;
            HashMap<String, String> first_row = a.table.get(i);
            for (int j = i + 1; j < a.size_of_rows; j++) {
                HashMap<String, String> second_row = a.table.get(j);
                boolean do_we_add = true;
                for (int k = 0; k < not_hidden.size(); k++) {
                    //if we get different values then don't add between these 2 rows.
                    if (!first_row.get(not_hidden.get(k)).equals(second_row.get(not_hidden.get(k)))) {
                        do_we_add = false;
                        break;
                    }
                }
                if (do_we_add) {
                    double added_probabilty = Double.parseDouble(first_row.get("Pr")) + Double.parseDouble(second_row.get("Pr"));
                    String str_added_probabilty = "" + added_probabilty;
                    first_row.replace("Pr", str_added_probabilty);
                    to_delete[j] = 1;
                    this.numOfAdd++;
                }
            }
            first_row.remove(hidden);//remove the hidden key since we finished eliminating it.
        }
        //now we remove all the to delete rows from our factor
        for (int i = 0; i < to_delete.length; i++) {
            if (to_delete[i] == 1) {
                a.table.remove(i - counter_for_rows_removed);
                a.size_of_rows--;
                counter_for_rows_removed++;
            }
        }
    }

    /**
     * Function to normalize our factor before we return it.
     * @param a we recieve the factor for which we wish to normalize.
     *   This factor is already ready to return meaning all thats left is to find the right row and divide that row by sum of all rows
     */
    public void normalize(Factor a) {
        double added_probabilty = Double.parseDouble(a.table.get(0).get("Pr"));
        //first for loop is to get the sum of all variables in our factor
        for (int i = 1; i < a.size_of_rows; i++) {
            added_probabilty += Double.parseDouble(a.table.get(i).get("Pr"));
            this.numOfAdd++;
        }
        for (int i = 0; i < a.size_of_rows; i++) {
            String[] hold_q = this.quarry_node.split("=");
            if (a.table.get(i).get(hold_q[0]).equals(hold_q[1])) {
                double the_value_we_return = Double.parseDouble(a.table.get(i).get("Pr"));
                the_value_we_return /= added_probabilty;
                String the_value_we_return_in_string = "" + the_value_we_return;
                a.table.get(i).replace("Pr", the_value_we_return_in_string);
                break;
            }
        }
    }
}
