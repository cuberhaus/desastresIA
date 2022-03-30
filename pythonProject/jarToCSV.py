#!/usr/bin/env python
"""
This script executes automates the task of executing a jar file multiple times to obtain data and gather
that data into a csv
"""
from __future__ import annotations

__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import re
from subprocess import Popen, PIPE, STDOUT

import numpy as np
import pandas as pa
from pandas import DataFrame
from tqdm import tqdm

path_pol = '../Desastres/out/artifacts/Desastres_jar/Desastres.jar'
path_alejandro = r"../Desastres/src/out/artifacts/Desastres_jar/Desastres.jar"


def main():
    # regex = [("nodesExpanded", True), ("Heuristico final", False), ("Texec", True)]
    regex = [("Texec", True)]
    dataframe = get_data_hillclimbing_5(regex)
    # lambda_values = [1, 0.01, 0.0001]
    # k_values = [1, 5, 25, 125]
    # lambda_values = [1, 0.01]
    # k_values = [1, 5]
    # dataframe = get_data_simulated_annealing(regex, k_values, lambda_values)
    # regex = [("Texec", True), ("nodesExpanded", True), ("Heuristico final", False)]
    dataframe.to_csv("./data.csv", index=False, header=True, sep='\t')


# Python won't throw an exception if argument given is not the same as type hint
def get_data_simulated_annealing(regex: list[tuple[str, bool]],
                                 k_values: list,
                                 lambda_values: list,
                                 n_steps: int = 40000,
                                 n_stitter: int = 100,
                                 n_seeds: int = 5,
                                 n_times: int = 5) -> DataFrame:
    """
    Given a list of tuples (regex, True or False), a list of k_values and a list of lambda_values we execute a jar
    file which prints out values, and we retrieve those values and organize them.

    :param n_stitter:
    :param n_steps:
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
    for k in tqdm(k_values, desc="K:", leave=False):
        for l in tqdm(lambda_values, desc="Lambda:", leave=False):
            values = []
            for _ in regex:
                values.append([])
            for j in tqdm(range(n_seeds), desc="Seeds:", leave=False):
                seed = 1000 + j
                for _ in tqdm(range(n_times), desc="Times:", leave=False):
                    p = Popen(['java', '-jar', path_alejandro, str(seed), str(l), str(k), str(n_steps), str(n_stitter)],
                              stdout=PIPE, stderr=STDOUT)
                    output_to_values(p, regex, values)

            if counter == 0:
                n = len(regex)
                for i in range(n):
                    string = regex[i][0]
                    # print(string)
                    # print(np.asarray(values[i]))
                    dataframe[string] = np.asarray(values[i])
            elif counter > 0:
                n = len(regex)
                for i in range(n):
                    string = str(regex[i][0]) + "." + str(counter)
                    # print(string)
                    # print(np.asarray(values[i]))
                    dataframe[string] = np.asarray(values[i])
            counter = counter + 1

    return dataframe


def output_to_values(p: Popen, regex: list[tuple[str, bool]], values: list[list[int | float]]) -> None:
    """
    Takes output from p and for each line checks if for any i <= len(regex) if string from regex[i] matches,
    if it matches takes numbers in that line and appends it to values[i].
    A single regex[i] could potentially match for all lines, intended behaviour is for each regex[i] to only append
    values to values[i].

    :param p: pipe where output comes from
    :param regex: List with strings (regex) and a boolean True if it's a float False if it's an Int
    :param values: List of lists where each list i should be appended only by regex[i] (intended behaviour)
    """
    for line in p.stdout:
        # print(line)
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


def get_data_hillclimbing_5(regex: list[tuple[str, bool]], n_seeds: int = 10, n_times: int = 10) -> DataFrame:
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

    groups = [150, 200]
    for group in tqdm(groups, desc="Groups:"):
        for j in tqdm(range(n_seeds), desc="Seeds:"):
            seed = 1000 + j
            for _ in tqdm(range(n_times), desc="Times:", leave=False):
                p = Popen(['java', '-jar', path_pol, str(seed), str(group)], stdout=PIPE, stderr=STDOUT)
                output_to_values(p, regex, values)
        n = len(regex)
        for i in range(n):
            dataframe[regex[i][0] + str(group)] = np.asarray(values[i])
            # print(np.asarray(values[i]))
    return dataframe


def get_data_hillclimbing(regex: list[tuple[str, bool]], n_seeds: int = 10, n_times: int = 10) -> DataFrame:
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
        dataframe[regex[i][0]] = np.asarray(values[i])
        # print(np.asarray(values[i]))
    return dataframe


if __name__ == '__main__':
    main()
