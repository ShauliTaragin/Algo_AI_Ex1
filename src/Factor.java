import java.util.ArrayList;
import java.util.HashMap;

public class Factor {
    public String id;
    public Pnode eventNode;
    public ArrayList<HashMap> table; //similar table to cpt.
    public int size_of_rows;

    /**
     * We create a factor for each node from its cpt.
     *
     * @param event-  we receive the node and by its cpt create the factor.
     *                however we will only add the rows of the cpt for which the givens values are correct
     * @param givens- receive the evidence and there values i.e [J=T , M=F] and only add dont add to the factor the rows
     *                for which the evidence is not the value it should be.
     */
    public Factor(Pnode event, ArrayList<String> givens) {
        eventNode = new Pnode(event);
        id = event.getName();
        this.size_of_rows = event.cpt.size_of_rows;
        table = new ArrayList<HashMap>();
        for (int i = 0; i < event.cpt.size_of_rows; i++) {
            this.table.add(event.cpt.table.get(i));
        }
        for (int i = 0; i < event.cpt.size_of_rows; i++) {
            for (int j = 0; j < givens.size(); j++) {
                HashMap row = event.cpt.table.get(i);
                String[] quarry = givens.get(j).split("=");
                if (row.containsKey(quarry[0]) && row.get(quarry[0]).equals(quarry[1])) {
                    this.table.remove(i);
                    this.size_of_rows--;
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        String ret = "The id for the factor is:  " + this.id + "\n";
        for (int i = 0; i < this.table.size(); i++) {
            ret+= table.toString();
            ret+="\n";
        }
        ret += "The number of rows we have is : " + this.size_of_rows;
        return ret;
    }
    //function for eliminate will be in factor becuase in the same factor we are combining two rows of same factor
}
