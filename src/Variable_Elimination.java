import java.util.ArrayList;

public class Variable_Elimination {

    public BayesianN Bn;
    private ArrayList<Factor> factors;
    private ArrayList<String> JoinOrder;
    private ArrayList<String> order_join_eliminate; //also holds all the hidden nodes by name
    private ArrayList<String> evidence; //holds all our evidence
    private ArrayList<String> evidence_values; // holds the values of our evidence each index i hold value for evidence in index i
    private String quarry_node; //hold the node we are asking the question on and its outcome e.g "B=T"
    public int numOfMul;// Counting how many multiplication occur during the algorithm
    public int numOfAdd;// Counting how many adding operations occur during the algorithm

    public Variable_Elimination(BayesianN bayesian_net , String quarry) {
        //leave here room to initiate all the variables of our class.


        this.order_join_eliminate = new ArrayList<String>();
        this.numOfMul=0;
        this.numOfAdd=0;
        String[] split_quarry_and_hidden = quarry.split(" ");// e.g-> [P(B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace("P(", "");//e.g-> [B=T|J=T,M=T),A-E]
        split_quarry_and_hidden[0] = split_quarry_and_hidden[0].replace(")", "");//e.g-> [B=T|J=T,M=T,A-E]
        if(split_quarry_and_hidden[1].length()>0) {
            String[] order_of_hidden = split_quarry_and_hidden[1].split("-");//e.g-> [A,E]
            for (int i = 0; i <order_of_hidden.length ; i++) {
                this.order_join_eliminate.add(order_of_hidden[i]);
            }
        }
        this.evidence = new ArrayList<String>();//initiate evidence array list
        this.evidence_values = new ArrayList<String>();
        String[] split_quarry_and_evidence = split_quarry_and_hidden[0].split("\\|");//e.g-> [B=T,J=T,M=T]
        this.quarry_node = split_quarry_and_evidence[0]; //initiate quarry node

        if(split_quarry_and_evidence[1].length()>0) {
            String[] input_evidence = split_quarry_and_hidden[1].split(",");
            for (int i = 0; i <input_evidence.length ; i++) {
                String[] input_evidence_value = input_evidence[i].split("=");
                this.evidence.add(input_evidence_value[0]);
                this.evidence_values.add(input_evidence_value[1]);
            }
        }
        this.factors = new ArrayList<Factor>();
    }

    /**
     * We start by iterating over all the nodes and checking which node is not relevant according to 3 criterias.
     * 1.if is not ancestor of quarry or
     * 2.if is not ancestor of any evidence
     * 3.if it is one of the ancestors we found in 1/2 the check if its independent to our quarry
     * The nodes which are not relevant will not enter the list of factors
     */
    public void variable_elimination(){
        //first we kick out anyone who is not relevent(not an anccestor/is independent)
        //on the anccestors we check on the quarry and evidence
        //bayesball we send the quarry given the evidence
        String[] Q = this.quarry_node.split("=");
        Pnode Quarry_node = Bn.getNodeByName(Q[0]);
        for (int i = 0; i <Bn.events.size() ; i++) {
            Pnode curr = Bn.events.get(i);
            if (!curr.getName().equals(Quarry_node.getName())) {
                Boolean isRelevent = false;
                if (Quarry_node.isAncestor(curr)) {
                    isRelevent = true;
                }
                if (!isRelevent){
                    for (int j = 0; j < this.evidence.size(); j++) {
                        Pnode evi = this.Bn.getNodeByName(this.evidence.get(j));
                        if(evi.isAncestor(curr)){
                            isRelevent = true;
                            break;
                        }
                    }
                }
                if(isRelevent){//check if the one we found to be ancestor is dependent to our quarry given our evidence
                    if(evidence.contains(curr.getName())){
                        //then remove the node from evidence and create it for bayesball
                        // remembering quarry looks like this-> B-E|J=T
                    }
                    else{

                    }
                }
            }
        }
    }


    //in here we will have a function for join which will join 2 different factors
}
