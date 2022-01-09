import java.util.ArrayList;
import java.util.HashMap;

public class Pnode {
    private String name;
    private ArrayList<Pnode> parents;
    private ArrayList<Pnode> children;
    private ArrayList<String> outcomes; //will hold the outcomes for the event e.g true/false/maybe etc
    public CPT cpt ;//this will change to array list of type cpt

    /**
     * @param name-> the name of the event e.g A
    *   A simple constructor for probability node */
    public Pnode(String name){
        this.name = name;
        this.parents = new ArrayList<Pnode>();
        this.children = new ArrayList<Pnode>();
        this.outcomes= new ArrayList<String>();
        this.cpt= new CPT();
    }

    /**
     * A copy constructor
     * @param other -> Receiving another probability node and creating our node from it
     */
    public Pnode(Pnode other){
        this.name = other.getName();
        this.parents = new ArrayList<>();
        for (int i = 0; i <other.parents.size() ; i++) {
            this.addparent(other.parents.get(i));
        }
        this.children = new ArrayList<Pnode>();
        for (int i = 0; i <other.children.size() ; i++) {
            this.addchild(other.children.get(i));
        }
        this.outcomes= new ArrayList<String>();
        for (int i = 0; i <other.outcomes.size() ; i++) {
            this.addoutcomes(other.outcomes.get(i));
        }
        this.cpt= new CPT(other.cpt);
    }

    //we will create functions to add to our arraylists
    public void addparent(Pnode Parent){
        this.parents.add(Parent);
    }
    public void addchild(Pnode Child){
        this.children.add(Child);
    }


    public void addoutcomes(String value){
        this.outcomes.add(value);
    }

    /**
     * The way we init our cpt.
     * @param probabilities -> From the xml input we receive the values for the cpt table. We pass those values as
     *                      A string into our function each index is 1 row of our cpr containing a probability.
     *                      i.e probabilities= ["0.8","0.66"]. then we will have 2 rows in our cpt.
     */
    public void setcpt(String[] probabilities){
        this.cpt.size_of_rows= probabilities.length;
        for (int i = 0; i < probabilities.length ; i++) {
            HashMap<String,String> rows = new HashMap<String,String>();
            rows.put(this.name , this.outcomes.get(i%outcomes.size()));
            int j=this.parents.size()-1;
            int amount_of_outcomes =outcomes.size();
            while ( j>=0 ){
                Pnode curr = this.parents.get(j);
                rows.put(curr.name, curr.outcomes.get((i/amount_of_outcomes % curr.outcomes.size())));
                j--;
                amount_of_outcomes*=curr.outcomes.size();
            }
            rows.put("Pr" , probabilities[i]);
            this.cpt.table.add(rows);
        }
    }

    /**
     * @param other -> receive another node and check if that node is my ancestor, e.g am I its descendant
     * @return true = other node is this nodes ancestor
                false = other node is *not* this nodes ancestor
     */
    public boolean isAncestor(Pnode other){
        if (other.children.contains(this))
                return true;
        boolean flag = false;
        for (int i = 0; i <other.children.size(); i++) {
            flag = isAncestor(other.children.get(i));
            if (flag)return true;
        }
        return false;
    }

    @Override
    public String toString(){
        String s = "the name of the event is: " + this.name + "\n";
        s+= "the nodes parents are:  ";
        for (int i = 0; i < this.parents.size(); i++) {
            s+= this.parents.get(i).getName() + " , ";
        }
        s+="\n";
        s+= "the nodes children are:  ";
        for (int i = 0; i < this.children.size(); i++) {
            s+= this.children.get(i).getName() + " , ";
        }
        s+="\n";
        s+= "the nodes outcomes are:  " + this.outcomes.toString() + "\n";
        s+= "the nodes cpt is:  " + this.cpt.toString() + "\n";
        return s;
    }
    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Pnode> getParents() {
        return parents;
    }

    public void setParents(ArrayList<Pnode> parents) {
        this.parents = parents;
    }

    public ArrayList<Pnode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Pnode> children) {
        this.children = children;
    }

    public ArrayList<String> getoutcomes() {
        return outcomes;
    }

    public void setoutcomes(ArrayList<String> outcomes) {
        this.outcomes = outcomes;
    }

}
