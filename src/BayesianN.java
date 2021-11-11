import java.util.ArrayList;

public class BayesianN {
    public String BNname;
    public ArrayList<Pnode> events;

    public BayesianN(String a , ArrayList<Pnode> events){
        this.BNname =a ;
        for (int i = 0; i <events.size() ; i++) {
            this.events.add(events.get(i));
        }
    }

}
