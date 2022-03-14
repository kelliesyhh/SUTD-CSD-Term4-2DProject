import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {

    ArrayList<Integer> outNeighbour = new ArrayList<>();
    String colour;
    int start;
    int end;
    Node parent;
    int var;

    public Node (int variable) {
        var = variable;
    }

    public int getVar (){
        return var;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public ArrayList<Integer> getOutNeighbour() {
        return outNeighbour;
    }

    public void setOutNeighbour(Integer index) {
        this.outNeighbour.add(index);
    }

}
