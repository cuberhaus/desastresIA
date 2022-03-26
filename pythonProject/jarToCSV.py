#!/usr/bin/env python
"""
This script executes automates the task of executing a jar file multiple times to obtain data and gather
that data into a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import re
from subprocess import Popen, PIPE, STDOUT

import numpy as np
import pandas as pa
from tqdm import tqdm

path_pol = '../Desastres/out/artifacts/Desastres_jar/Desastres.jar'
path_alejandro = r"D:\UNI\6o quadri\IA\Pràcticas\Práctica1\git\desastresIA\Desastres\src\out\artifacts\Desastres_jar\Desastres.jar"


def main():
    regex = [("nodesExpanded", True), ("Heuristico final", False), ("Texec", True)]
    dataframe = get_data(regex)
    dataframe.to_csv("./data.csv", index=False, header=False, sep='\t')


def get_data(regex):
    values = []
    dataframe = pa.DataFrame()
    for i in regex:
        values.append([])
    for j in tqdm(range(1), desc="Seeds:"):
        seed = 1007 + j
        for i in tqdm(range(3), desc="Times:", leave=False):
            p = Popen(['java', '-jar', path_pol, str(seed)], stdout=PIPE, stderr=STDOUT)
            for line in p.stdout:
                print(line)
                n = len(regex)
                for k in range(n):
                    attribute = regex[k]
                    if re.search(".*" + attribute[0] + ".*", str(line)):
                        if not attribute[1]:
                            line = str(line)
                            number = re.findall("\d+\.\d+", line)
                            values[k].append(number[0])
                        elif attribute[1]:
                            number = [int(i) for i in line.split() if i.isdigit()]
                            values[k].append(number[0])
    n = len(regex)
    for i in range(n):
        dataframe[regex[i]] = np.asarray(values[i])
        print(np.asarray(values[i]))
    return dataframe


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main()
