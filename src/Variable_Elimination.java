import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Variable_Elimination {

    public BayesianN Bn;
    private ArrayList<Factor> factors;
    private ArrayList<String> JoinOrder;
    private ArrayList<String> order_join_eliminate; //also holds all the hidden nodes by name
    private ArrayList<String> evidence; //holds all our evidence
    private ArrayList<String> evidence_values; // holds the values of our evidence each index i hold value for evidence in index i
    private ArrayList<String> not_relevant;//holds the non relevant values
    private String quarry_node; //hold the node we are asking the question on and its outcome e.g "B=T"
    public int numOfMul;// Counting how many multiplication occur during the algorithm
    public int numOfAdd;// Counting how many adding operations occur during the algorithm

    public Variable_Elimination(BayesianN bayesian_net, String quarry) {
        //leave here room to initiate all the variables of our class.


        this.order_join_eliminate = new ArrayList<String>();
        this.numOfMul = 0;
        this.numOfAdd = 0;
        String[] split_quarry_and_hidden = quarry.split(" ");// e.g-> [P(B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace("P(", "");//e.g-> [B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace(")", "");//e.g-> [B=T|J=T,M=T,A-E]
        if (split_quarry_and_hidden[1].length() > 0) {
            String[] order_of_hidden = split_quarry_and_hidden[1].split("-");//e.g-> [A,E]
            for (int i = 0; i < order_of_hidden.length; i++) {
                this.order_join_eliminate.add(order_of_hidden[i]);
            }
        }
        this.evidence = new ArrayList<String>();//initiate evidence array list
        this.evidence_values = new ArrayList<String>();
        String[] split_quarry_and_evidence = split_quarry_and_hidden[0].split("\\|");//e.g-> [B=T,J=T,M=T]
        this.quarry_node = split_quarry_and_evidence[0]; //initiate quarry node

        if (split_quarry_and_evidence[1].length() > 0) {
            String[] input_evidence = split_quarry_and_hidden[1].split(",");
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
     */
    public double[] variable_elimination() {
        //first we kick out anyone who is not relevent(not an anccestor/is independent)
        //on the anccestors we check on the quarry and evidence
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
        for (int i = 0; i < this.evidence.size(); i++)
            this.factors.add(new Factor(Bn.getNodeByName(this.evidence.get(i)), evidence_with_value));
        //now we will check if we can reach the answer right away
        Boolean right_away = false;
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < this.factors.get(i).size_of_rows; j++) {
                HashMap<String, String> row = this.factors.get(i).table.get(j);
                if (row.containsKey(Q[0]) && row.get(Q[0]).equals(Q[1])) {
                    for (int k = 0; k < this.evidence.size(); k++) {
                        if (row.containsKey(this.evidence.get(k)) && row.get(this.evidence.get(k)).equals(this.evidence_values.get(k)))
                            right_away = true;
                        else {
                            right_away = false;
                            break;
                        }
                    }
                    if (right_away) {
                        double[] answer_directly = new double[3];
                        answer_directly[0] = Double.parseDouble(row.get("Pr"));
                        answer_directly[1] = 0.0;
                        answer_directly[2] = 0.0;
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
            while (iterator < this.factors.size()) {
                Factor a = null;
                HashMap<String, String> first_row = this.factors.get(iterator).table.get(0);
                if (first_row.containsKey(hidden) && a == null) {//might be bug here
                    a = this.factors.get(iterator++);
                    continue;
                }
                if (first_row.containsKey(hidden)) {//might be bug here
                    Factor b = this.factors.get(iterator);
                    join(a, b, hidden);
                    iterator = 0;
                    continue;
                }
            }
            //if we finished the loop this means there is only one factor containing are hidden variable
            //then we should do eliminate

            this.order_join_eliminate.remove(0);
        }

        return null;
    }

    /**
     * Now we remove all the factors for which we find that they contain a irrelevent node(which we found through bayesball)
     */
    public void remove_irrelevent_nodes() {
        for (int i = 0; i < this.factors.size(); i++) {
            for (int j = 0; j < this.factors.get(i).size_of_rows; j++) {
                HashMap<String, String> row = this.factors.get(i).table.get(j);
                for (int k = 0; k < not_relevant.size(); k++) {
                    if (row.containsKey(not_relevant.get(i)))
                        factors.remove(i);
                }
            }
        }
    }

    public void join(Factor a, Factor b, String hidden) {
        Factor c = new Factor();
        ArrayList<String> common_variables = new ArrayList<>();
        for (String key1 : a.table.get(0).keySet()) {
            if (b.table.get(0).containsKey(key1))
                common_variables.add(key1);
        }
        for (int i = 0; i < a.size_of_rows; i++) {
            HashMap<String, String> row_a = a.table.get(i);
            for (int j = 0; j < b.size_of_rows; j++) {
                HashMap<String, String> row_b = b.table.get(j);
                Boolean do_factor_here = true;
                for (int k = 0; k < common_variables.size(); k++) {
                    if (!row_a.get(common_variables.get(k)).equals(row_b.get(common_variables.get(k)))) {//if the hidden value is the same only then merge them
                        do_factor_here=false;
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
                        if (!c.table.get(c.size_of_rows - 1).containsKey(key1))
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

    public void eliminate(Factor a, String hidden) {
        ArrayList<Integer> to_delete = new ArrayList<>();
        ArrayList<String> not_hidden = new ArrayList<>();
        for (String key: a.table.get(0).keySet()) {
            if (!key.equals(hidden))
                not_hidden.add(key);//first adding the non hidden variables to my list because we will know which rows to add according to these variables.
        }
        for (Integer i = 0; i < a.size_of_rows; i++) {
            if(to_delete.contains(i))continue;
            HashMap<String, String> first_row = a.table.get(i);
            for (int j = i + 1; j < a.size_of_rows; j++) {
                HashMap<String, String> second_row = a.table.get(j);
                if (a.table.get(i).get(hidden).equals(a.table.get(j).get(hidden))) {
                    double sum_of_prob=0.0;
                }
            }
        }
    }
    //in here we will have a function for join which will join 2 different factors
}
