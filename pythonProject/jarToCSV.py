#!/usr/bin/env python
"""
This script executes automates the task of executing a jar file multiple times to obtain data and gather
that data into a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

from subprocess import Popen, PIPE, STDOUT
import re

import pandas as pa
import numpy as np
from tqdm import tqdm

# data = {'t_exec': [],
#         'nodes_expanded': [],
#         'heuristico_final': []}

import locale


def main():
    t_exec = []
    nodes_expanded = []
    heuristico_final = []

    dataframe = pa.DataFrame()
    for j in tqdm(range(3), desc="Seeds:"):
        seed = 1007 + j
        for i in tqdm(range(10), desc="Times:", leave=False):
            p = Popen(['java', '-jar',
                       r'D:\UNI\6o quadri\IA\Pràcticas\Práctica1\git\desastresIA\Desastres\src\out\artifacts\Desastres_jar\Desastres.jar',
                       str(seed)],
                      stdout=PIPE, stderr=STDOUT)

            for line in tqdm(p.stdout, desc="Lines", leave=False):
                # print(line)
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
    # print("T_exec: " + str(t_exec))
    # print("nodesExpanded: " + str(nodes_expanded))
    # print("Heuristico final: " + str(heuristico_final))

    dataframe['t_exec'] = np.asarray(t_exec)
    dataframe['nodesExpanded'] = np.asarray(nodes_expanded)
    dataframe['heuristicoFinal'] = np.asarray(heuristico_final)
    dataframe.to_csv("./data.csv", index=False, header=False, sep='\t')


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()