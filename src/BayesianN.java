import java.util.ArrayList;

public class BayesianN {
    public String BNname;
    public ArrayList<Pnode> events;

    public BayesianN(String a , ArrayList<Pnode> events){
        this.BNname =a ;
        this.events = new ArrayList<Pnode>();
        for (int i = 0; i <events.size() ; i++) {
            this.events.add(events.get(i));
        }
    }

    public Pnode getNodeByName(String name) {
        int i=0;
        while(i < this.events.size()){
            if (this.events.get(i).getName().equals(name))
                return this.events.get(i);
            i++;
        }
        if(i==this.events.size()) throw new RuntimeException("No such Node in our Bayesian Network. returning null.");
        return null;
    }

}
