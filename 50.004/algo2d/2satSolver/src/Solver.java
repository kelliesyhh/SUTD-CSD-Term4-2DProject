import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Solver {

    private static int time = 0;
    private static Stack<Node> nodeStack = new Stack<>();
    private static Hashtable<Integer,Node> Graph = new Hashtable<>();
    private static Hashtable<Integer,Node> GraphInv = new Hashtable<>();
    private static ArrayList<ArrayList<Integer>> scc = new ArrayList<>();
    private static ArrayList<Integer> variableList = new ArrayList<>();

    public static void main(String[] args) {
        String filepath = args[0];

        try {
            // Read information in the file by using BufferedReader
            InputStream inputStream = new FileInputStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // NOTE: start parsing process
            // Stores the current line we are looking at
            String line;

            // Read until EOF
            while ((line = reader.readLine()) != null) {
                // Check that the line is a valid clause (not empty, not problem statement, not comment)
                if (line.length() > 0 && line.charAt(0) != 'p' && line.charAt(0) != 'c') {
                    // Remove any potential extra whitespace and split
                    String[] splitLine = line.trim().split(" ");

                    /*
                    - If the item has a 0, we consider it as EOL, and purge our literals LL to a clause
                    - Else if the item starts with a 0, we add a negative literal named after that term
                    - Else we add a positive literal named after that term
                     */
                    int firstLiteral = 0;
                    int secondLiteral = 0;
                    for (String item : splitLine) {
                        int literal = Integer.parseInt(item);
                        if (literal == 0){
                            //add the two nodes first, if it's not already there
                            if (!Graph.containsKey(firstLiteral)){
                                Graph.put(firstLiteral, new Node(firstLiteral));
                                Graph.put(-firstLiteral, new Node(-firstLiteral));
                                variableList.add(firstLiteral);
                            }
                            if (!Graph.containsKey(secondLiteral)) {
                                Graph.put(secondLiteral, new Node(secondLiteral));
                                Graph.put(-secondLiteral, new Node(-secondLiteral));
                                variableList.add(secondLiteral);
                            }
                            //draw the edges between the two added nodes
                            Graph.get(-firstLiteral).setOutNeighbour(secondLiteral);
                            Graph.get(-secondLiteral).setOutNeighbour(firstLiteral);

                            //reset values
                            firstLiteral = 0;
                            secondLiteral = 0;
                        } else {
                            if (firstLiteral == 0) {
                                firstLiteral = literal;
                            } else {
                                secondLiteral = literal;
                            }
                        }
                    }
                }
            }

            reader.close(); // Close BufferedReader stream and release all the system resources associated with the stream operations

            //do kosaraju's algorithm to find strongly connected components
            kosaraju(Graph);

            //finding 2-sat solution by checking in strongly connected components
            boolean satisfiable = true;
            for (int i : variableList){
                for (ArrayList<Integer> sccInstance : scc){
                    if (sccInstance.contains(i) & sccInstance.contains(-i)){
                        satisfiable = false;
                    }
                }
            }

            if (satisfiable){
                System.out.println("Satisfiable");
            } else {
                System.out.println("Not satisfiable");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transpose(){
        for (int i : Graph.keySet()){
            Node u = Graph.get(i);
            if (!GraphInv.containsKey(i)){
                GraphInv.put(i,new Node(u.getVar()));
            }
            for (int neighbourIndex : u.getOutNeighbour()){
                Node v = Graph.get(neighbourIndex);
                if (!GraphInv.containsKey(neighbourIndex)){
                    GraphInv.put(neighbourIndex,new Node(v.getVar()));
                }
                GraphInv.get(neighbourIndex).setOutNeighbour(i);
            }
        }
    }

    public static void kosaraju(Hashtable graph){
        //do dfs
        dfs(Graph,"first");
        System.out.println(Graph);
//        while (! nodeStack.isEmpty()){
//            Node node = nodeStack.pop();
//            System.out.println(node.getVar() + " , " + node.getEnd());
//        }
        //transpose the graph
        transpose(); // This transpose did not correctly set up the neighbours

        //do dfs again based on the post order
        System.out.println(GraphInv);
        dfs(GraphInv,"second");
        System.out.println(scc);

        //update the list of strongly connected components
    }

    public static void dfs (Hashtable<Integer,Node> graph,String state){
        for (int i : graph.keySet()){
            Node u = graph.get(i);
            u.setColour("white");
        }
        if (state.equals("first")){
            for (int i : graph.keySet()){
                Node u = graph.get(i);
                if (u.getColour().equals("white")){
                    dfsVisit(graph,i,"first");
                }
            }
        } else if (state.equals("second")) {
            while (!nodeStack.isEmpty()) {
                Node u = nodeStack.pop();
                Node uInv = graph.get(u.getVar());
                if (uInv.getColour().equals("white")){
                    ArrayList<Integer> sccItem = new ArrayList<>();
                    scc.add(sccItem);
                    dfsVisit(graph,u.getVar(), "second");
                }
            }
        }
    }

    public static void dfsVisit (Hashtable<Integer, Node> graph, int i, String state){
        if (state.equals("first")){
            time += 1;
            Node u = graph.get(i);
            u.setStart(time);
            u.setColour("grey");
            for(int nIndex : graph.get(i).getOutNeighbour()){
                Node v = graph.get(nIndex);
                if (v.getColour().equals("white")){
                    v.setParent(u);
                    dfsVisit(graph,nIndex,state);
                }
            }
            u.setColour("black");
            time += 1; //time might not be needed in this scenario
            u.setEnd(time);
            nodeStack.push(u);
        } else {
            Node u = graph.get(i);
            scc.get(scc.size()-1).add(i);
            u.setColour("grey");
            for (int nIndex : graph.get(i).getOutNeighbour()){
                Node v = graph.get(nIndex);
                if (v.getColour().equals("white")) {
                    dfsVisit(graph, nIndex, state);
                }
            }
        }

    }
}