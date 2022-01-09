import java.util.ArrayList;

public class BayesianN {
    public String BNname;
    public ArrayList<Pnode> events;

    /**
     * Simple constructor for our Bayesian Network
     * @param a -> The name of our network
     * @param events -> All our nodes in our network after they are already connected
     */
    public BayesianN(String a , ArrayList<Pnode> events){
        this.BNname =a ;
        this.events = new ArrayList<Pnode>();
        for (int i = 0; i <events.size() ; i++) {
            this.events.add(events.get(i));
        }
    }

    /**
     *
     * @param name -> We receive a name of a node in our network(if there is no such Node we throw an exception)
     * @return -> Returning the node itself for which we got by it's id.
     */
    public Pnode getNodeByName(String name) { //A helper function to let us get any node in our bayesian net by its name
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
