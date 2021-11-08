import java.util.ArrayList;

public class Pnode {
    private String name;
    private ArrayList<Pnode> parents;
    private ArrayList<Pnode> children;
    private ArrayList<String> values; //will hold the values for the event e.g true/false/maybe etc
    private ArrayList<String> cpt ;//this will change to array list of type cpt
    /* @param the name of the event e.g A
    *   A simple constructor for bayes ball */
    public Pnode(String name){
        this.name = name;
        this.parents = new ArrayList<Pnode>();
        this.children = new ArrayList<Pnode>();
        this.values= new ArrayList<String>();
        this.cpt= new ArrayList<String>();
    }
    //we will create functions to add to our arraylists
    public void addparent(Pnode Parent){
        this.parents.add(Parent);
    }
    public void addchild(Pnode Child){
        this.children.add(Child);
    }
    public void addvalues(String value){
        this.values.add(value);
    }
    public void addcpt(String probabilty){ //also will be changed
        this.cpt.add(probabilty);
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
        s+= "the nodes values are:  " + this.values.toString() + "\n";
        s+= "the nodes cpt is:  " + this.cpt.toString() + "\n";
        return s;
    }

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

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public ArrayList<String> getCpt() {
        return cpt;
    }

    public void setCpt(ArrayList<String> cpt) {
        this.cpt = cpt;
    }
}
