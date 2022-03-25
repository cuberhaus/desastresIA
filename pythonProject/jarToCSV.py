#!/usr/bin/env python
"""
This script executes automates the task of executing a jar file multiple times to obtain data and gather
that data into a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

from subprocess import Popen, PIPE, STDOUT
import re


def main():
    t_exec = []
    nodes_expanded = []
    heuristico_final = []
    for i in range(10):
        p = Popen(['java', '-jar', '/Users/pol/desastresIA/Desastres/out/artifacts/Desastres_jar/Desastres.jar'],
                  stdout=PIPE, stderr=STDOUT)
        for line in p.stdout:
            print(line)
            if re.search(".*nodesExpanded.*", str(line)):
                number = [int(i) for i in line.split() if i.isdigit()]
                nodes_expanded.append(number[0])
            if re.search(".*Heuristico final.*", str(line)):
                line = str(line)
                number = re.findall("\d+\.\d+", line)
                heuristico_final.append(number[0])
            if re.search(".*Texec.*", str(line)):
                number = [int(i) for i in line.split() if i.isdigit()]
                t_exec.append(number[0])
    print("T_exec: " + str(t_exec))
    print("nodesExpanded: " + str(nodes_expanded))
    print("Heuristico final: " + str(heuristico_final))


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()
