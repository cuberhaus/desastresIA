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
path_alejandro = r"../Desastres/src/out/artifacts/Desastres_jar/Desastres.jar"


def main():
    regex = [("nodesExpanded", True), ("Heuristico final", False), ("Texec", True)]
    # dataframe = get_data_hillclimbing(regex)
    # lambda_values = [1, 0.01, 0.0001]
    # k_values = [1, 5, 25, 125]
    lambda_values = [1, 0.01]
    k_values = [1, 5]
    dataframe = get_data_simulated_annealing(regex, k_values, lambda_values)
    # regex = [("Texec", True), ("nodesExpanded", True), ("Heuristico final", False)]
    dataframe.to_csv("./data.csv", index=False, header=True, sep='\t')


# TODO: separate csv values by k and lambda
def get_data_simulated_annealing(regex, k_values, lambda_values, n_seeds=2, n_times=2):
    """
    Given a list of tuples (regex, True or False), a list of k_values and a list of lambda_values we execute a jar
    file which prints out values, and we retrieve those values and organize them.

    :param k_values: k values which will be used in simulated annealing
    :param lambda_values: lambda values which will be used in simulated annealing
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    dataframe = pa.DataFrame()
    counter = 0
    for k in k_values:
        for l in lambda_values:
            values = []
            for _ in regex:
                values.append([])
            for j in tqdm(range(n_seeds), desc="Seeds:"):
                seed = 1000 + j
                for _ in tqdm(range(n_times), desc="Times:", leave=False):
                    p = Popen(['java', '-jar', path_alejandro, str(seed), str(l), str(k)], stdout=PIPE, stderr=STDOUT)
                    output_to_values(p, regex, values)

            if counter == 0:
                n = len(regex)
                for i in range(n):
                    string = regex[i][0]
                    print(string)
                    print(np.asarray(values[i]))
                    dataframe[string] = np.asarray(values[i])
            elif counter > 0:
                n = len(regex)
                for i in range(n):
                    string = str(regex[i][0]) + "." + str(counter)
                    # if regex[i][0] == "Heuristico final":
                    #     string = "heur." + str(counter)
                    print(string)
                    print(np.asarray(values[i]))
                    dataframe[string] = np.asarray(values[i])
            counter = counter + 1

    return dataframe


def output_to_values(p, regex, values):
    for line in p.stdout:
        print(line)
        n = len(regex)
        for i in range(n):
            attribute = regex[i]
            if re.search(".*" + attribute[0] + ".*", str(line)):
                if not attribute[1]:
                    line = str(line)
                    number = re.findall(r"\d+\.\d+", line)
                    values[i].append(number[0])
                elif attribute[1]:
                    number = [int(i) for i in line.split() if i.isdigit()]
                    values[i].append(number[0])


def get_data_hillclimbing(regex, n_seeds=10, n_times=10):
    """
    Given a list of tuples we execute a jar file which prints out values, and we retrieve those values and organize them
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    values = []
    dataframe = pa.DataFrame()
    for _ in regex:
        values.append([])
    for j in tqdm(range(n_seeds), desc="Seeds:"):
        seed = 1000 + j
        for _ in tqdm(range(n_times), desc="Times:", leave=False):
            p = Popen(['java', '-jar', path_alejandro, str(seed)], stdout=PIPE, stderr=STDOUT)
            output_to_values(p, regex, values)
    n = len(regex)
    for i in range(n):
        dataframe[regex[i]] = np.asarray(values[i])
        print(np.asarray(values[i]))
    return dataframe


if __name__ == '__main__':
    main()
