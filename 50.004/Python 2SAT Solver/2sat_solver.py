import sys

def parse_cnf(filename):
    """
    Parse the CNF file with the given filename and return:
    - A list of its literals
    - Its adjacency list representation
    - Its transpose adjacency list representation
    """
    with open(filename) as fp:
        # Store all our clauses in one continuous stream of data
        data = []
        # Read all the lines of our file
        for line in fp.readlines():
            # If the line doesn't start with c (comment) or p (problem statement), then
            if line[0] not in ["c", "p"]:
                # Add the clause to our data
                data.extend(line.strip().split(" "))

    adj_list = {}
    transpose_adj_list = {}
    vars = set()

    for i in range(0, len(data), 3):
        lit1 = int(data[i])
        lit2 = int(data[i + 1])

        # Check if the literals we are currently parsing (or their negation)
        # are already in our adjacency lists and list of variables
        # If not, add them
        if lit1 not in vars and -lit1 not in vars:
            vars.add(abs(lit1))
            adj_list[lit1] = set()
            adj_list[-lit1] = set()
            transpose_adj_list[lit1] = set()
            transpose_adj_list[-lit1] = set()

        if lit2 not in vars and -lit2 not in vars:
            vars.add(abs(lit2))
            adj_list[lit2] = set()
            adj_list[-lit2] = set()
            transpose_adj_list[lit2] = set()
            transpose_adj_list[-lit2] = set()

        # Add their respective edges
        adj_list[-lit1].add(lit2)
        adj_list[-lit2].add(lit1)

        # Add our transpose edges
        transpose_adj_list[lit2].add(-lit1)
        transpose_adj_list[lit1].add(-lit2)

    return vars, adj_list, transpose_adj_list

def kosaraju(graph, transpose_graph):
    """Kosajaru's algorithm to find strongly-connected components (SCCs)"""
    visited = {i : 0 for i in graph}                # Keep track of whether we have visited a node before
    ordered_vertices = []                           # Keep track of the exit order of the vertices of our DFS
    roots = {i : None for i in graph}               # roots[i] stores the root node of the SCC node i is in
    scc = {}                                        # Keep track of SCCs; key corresponds to root node of that SCC

    # Perform initial DFS on graph
    for vertex in graph:
        if not visited[vertex]:
            dfs(graph, vertex, visited, ordered_vertices)
    
    # Perform next pass of DFS on tranpose graph
    for vertex in ordered_vertices:
        transpose_dfs(transpose_graph, vertex, vertex, roots, scc)

    return visited, ordered_vertices, roots, scc

def dfs(graph, vertex, visited, ordered_vertices):
    # Set this node to visited, so no other nodes visit it
    visited[vertex] = 1

    for neighbour in graph[vertex]:
        # For each of this node's neighbours, visit them if they haven't already been visited
        if not visited[neighbour]:
            dfs(graph, neighbour, visited, ordered_vertices)
    
    # Insert at the front of our list of vertices
    ordered_vertices.insert(0, vertex)

def transpose_dfs(graph, vertex, root, roots, scc):
    # If the vertex we are currently looking at doesn't yet have a root, set its root to the current root
    # and append to scc[root]
    if roots[vertex] is None:
        # If root is not yet a key in scc, add it now
        if root not in scc:
            scc[root] = []
        roots[vertex] = root
        scc[root].append(vertex)

        # Recursively call on the neighbours of the current vertex
        for neighbour in graph[vertex]:
            transpose_dfs(graph, neighbour, root, roots, scc)

def boolsat(filename):
    # Parse our file
    vars, adj_list, transpose_adj_list = parse_cnf(filename)

    # Call Kosaraju's algorithm on our graph to find our SCCs
    visited, ordered_vertices, roots, scc = kosaraju(adj_list, transpose_adj_list)

    scc_adj_list = {}
    for i in scc:
        # Initialize the data structure we will use to store the adjacency list of the condensation of our implication graph
        scc_adj_list[scc[i][0]] = set()
        # We iterate through each SCC, and check for each literal in it, whether its negation is contained too
        # If it is, then we know that our boolean formula is unsatisfiable
        # Display result and return early
        for var in scc[i]:
            if -var in scc[i]:
                print("UNSATISFIABLE")
                return

    # If we passed the previous loop, it means our boolean formula is satisfiable; now we need to find the assignments
    print("SATISFIABLE")

    # We want to construct the condensation of our implication graph
    # where each vertex corresponds to the root node of each SCC
    # and there is an edge between vertices if an edge exists between vertices of different SCCs
    for var in vars:
        for neighbour in adj_list[var]:
            if roots[var] != roots[neighbour]:
                scc_adj_list[roots[var]].add(roots[neighbour])

    truths = {i : None for i in vars}

    # We set all the vertices of a SCC (in reverse topological order of the condensation of the implication graph)
    # to True if they have not yet been assigned a truth value
    # This means setting their negation to False
    for vertex in ordered_vertices[::-1]:
        if roots[vertex] == vertex:
            for current_scc_vertex in scc[vertex]:
                if current_scc_vertex > 0:
                    if truths[current_scc_vertex] is None:
                        truths[current_scc_vertex] = True
                else:
                    if truths[abs(current_scc_vertex)] is None:
                        truths[abs(current_scc_vertex)] = False

    # Print our result and return
    print(truths)

if __name__ == "__main__":
    filename = "sat.cnf"
    if len(sys.argv) > 1:
        filename = sys.argv[1]
    boolsat(filename)