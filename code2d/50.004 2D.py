with open("example2.cnf") as f:
    cnf_list = []
    
    for line in f:
        if line.startswith("p"):
            variables_num = int(line.lstrip("p cnf ").split()[0])
            clauses_num = int(line.lstrip("p cnf ").split()[1])  

        if not (line.startswith("c") or line.startswith("p") or line.startswith("\n")):
            cnf_list.append([int(y) for y in line.rstrip("0").rstrip("0\n").split()])

    


list1 = []
list2 = []
for i in cnf_list:
    list1.append(i[0])
    list2.append(i[1])

print(list1)




