import java.util.ArrayList;

public class Pnode {
    String name;
    ArrayList<Pnode> parents;
    ArrayList<Pnode> child;

    boolean colored;
    /* @param
    * @return */
    public Pnode(String name){
        this.name = name;
        this.parents = new ArrayList<Pnode>();
        this.child = new ArrayList<Pnode>();
        this.colored = false;
    }
    //we will create functions to add to our arraylists
    public void addparent(Pnode Parent){
        this.parents.add(Parent);
    }
    public void addchild(Pnode Child){
        this.parents.add(Child);
    }
    @Override
    public String toString(){
        String s = "";
        return s;
    }
}
