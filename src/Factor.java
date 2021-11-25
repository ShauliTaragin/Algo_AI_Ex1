import java.util.ArrayList;
import java.util.HashMap;

public class Factor implements Comparable<Factor> {
    public String id;
    public Pnode eventNode;
    public ArrayList<HashMap<String, String>> table; //similar table to cpt.
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
        table = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < event.cpt.size_of_rows; i++) {
            this.table.add(event.cpt.table.get(i));
        }
        for (int i = 0; i < event.cpt.size_of_rows; i++) {
            for (int j = 0; j < givens.size(); j++) {
                HashMap row = event.cpt.table.get(i);
                String[] quarry = givens.get(j).split("=");
                if (row.containsKey(quarry[0]) && !row.get(quarry[0]).equals(quarry[1])) {
                    this.table.remove(i);
                    this.size_of_rows--;
                    break;
                }
            }
        }
    }

    /**
     * an empty constructor for Factor. We will use it when doing the actual join and eliminate
     */
    public Factor(){
        this.id="";
        this.size_of_rows=0;
        this.eventNode=null;
        this.table= new ArrayList<HashMap<String,String>>();
    }

    @Override
    public String toString() {
        String ret = "The id for the factor is:  " + this.id + "\n";
        for (int i = 0; i < this.table.size(); i++) {
            ret += table.get(i).toString();
            ret += "\n";
        }
        ret += "The number of rows we have is : " + this.size_of_rows;
        return ret;
    }

    /**
     * Finds the ASCII value of our factor.
     *
     * @return the ASCII value.
     */
    private int ascii_value() {
        int asciiV = 0;
        for (String key : this.table.get(0).keySet()) {
            for (int j = 0; j < key.length(); j++) {
                asciiV += (int) key.charAt(j);
            }
        }
        return asciiV;
    }
    public void add_row(String probability){
        HashMap<String,String> new_row = new HashMap<>();
        new_row.put("Pr" , probability);
        this.table.add(new_row);
        this.size_of_rows++;
    }

    /**
     * A compare to ,  in order to sort factors.
     * @param other - we get another factor and compare it to our current factor.
     * @return 1->factor is bigger -1 ->other is bigger
     */
    @Override
    public int compareTo(Factor other) {
        if (this.size_of_rows > other.size_of_rows)
            return 1;
        else if (this.size_of_rows < other.size_of_rows)
            return -1;
        else {
            if(ascii_value()> other.ascii_value()){
                return 1;
            }
            else if (ascii_value()< other.ascii_value())
                return -1;
            else
                return 1; //arbitrary because they have the same ascii value
        }
    }
    //function for eliminate will be in factor because in the same factor we are combining two rows of same factor
}
