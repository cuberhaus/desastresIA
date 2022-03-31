#!/usr/bin/env python
"""
This script executes automates the task of executing a jar file multiple times to obtain data and gather
that data into a csv
"""
from __future__ import annotations

__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import re
from datetime import datetime
from subprocess import Popen, PIPE, STDOUT

import numpy as np
import pandas as pa
from pandas import DataFrame
from tqdm import tqdm


def main():
    path_pol = '../Desastres/out/artifacts/Desastres_jar/Desastres.jar'
    path_alejandro = r"../Desastres/src/out/artifacts/Desastres_jar/Desastres.jar"

    regex = [("Texec", True), ("nodesExpanded", True), ("Heuristico final", False), ]
    # regex = [("Texec", True)]
    groups = [100, 150, 200, 250]
    # dataframe = get_data_hillclimbing_5(regex, groups, path_pol)
    helicopters = [1, 2, 3, 4, 5]
    dataframe = get_data_hillclimbing_6(regex, helicopters, path_pol)
    # lambda_values = [0.0001, 0.01, 1]
    # k_values = [1, 5, 25, 125]
    # lambda_values = [1, 0.01]
    # k_values = [1, 5]
    # dataframe = get_data_simulated_annealing(regex, k_values, lambda_values, path_alejandro)
    # regex = [("Texec", True), ("nodesExpanded", True), ("Heuristico final", False)]
    dataframe.to_csv("./data" + str(datetime.now()) + ".csv", index=False, header=True, sep='\t')


# Python won't throw an exception if argument given is not the same as type hint
def get_data_simulated_annealing(regex: list[tuple[str, bool]],
                                 k_values: list,
                                 lambda_values: list,
                                 path_jar: str,
                                 n_steps: int = 60000,
                                 n_stitter: int = 5,
                                 n_seeds: int = 15,
                                 n_times: int = 1
                                 ) -> DataFrame:
    """
    Given a list of tuples (regex, True or False), a list of k_values and a list of lambda_values we execute a jar
    file which prints out values, and we retrieve those values and organize them.

    :param path_jar: path to jar
    :param n_stitter: value for stitter
    :param n_steps: number of steps
    :param k_values: k values which will be used in simulated annealing
    :param lambda_values: lambda values which will be used in simulated annealing
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    dataframe = pa.DataFrame()
    for k in tqdm(k_values, desc="K:", leave=False):
        for lambda_value in tqdm(lambda_values, desc="Lambda:", leave=False):
            values = []
            for _ in regex:
                values.append([])
            for j in tqdm(range(n_seeds), desc="Seeds:", leave=False):
                seed = 1000 + j
                for _ in tqdm(range(n_times), desc="Times:", leave=False):
                    p = Popen(
                        ['java', '-jar', path_jar, str(seed), str(lambda_value), str(k), str(n_steps), str(n_stitter)],
                        stdout=PIPE, stderr=STDOUT)
                    output_to_values(p, regex, values)

            # n = len(regex)
            n = len(values)
            for i in range(n):
                string = str(regex[i][0]) + "_" + str(lambda_value) + "_" + str(k)
                # print(string)
                print(np.asarray(values[i]))
                dataframe[string] = pa.Series(values[i])
                # dataframe[string] = np.asarray(values[i])

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


def get_data_hillclimbing_6(regex: list[tuple[str, bool]],
                            n_helicopters: list[int],
                            path_jar: str,
                            n_grupos: int = 100,
                            n_seeds: int = 10,
                            n_times: int = 1) -> DataFrame:
    """
    Experiment number 5
    Given a list of tuples we execute a jar file which prints out values, and we retrieve those values and organize them
    :param n_grupos: número de grupos
    :param n_helicopters: número de helicópteros
    :param path_jar: path to jar
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    dataframe = pa.DataFrame()
    for n_helicopter in tqdm(n_helicopters, desc="Groups:", leave=False):
        values = []
        for _ in regex:
            values.append([])
        for j in tqdm(range(n_seeds), desc="Seeds:", leave=False):
            seed = 1000 + j
            for _ in tqdm(range(n_times), desc="Times:", leave=False):
                p = Popen(['java', '-jar', path_jar, str(seed), str(n_grupos), str(n_helicopter), str(n_helicopters)],
                          stdout=PIPE, stderr=STDOUT)
                output_to_values(p, regex, values)
        n = len(regex)
        for i in range(n):
            dataframe[regex[i][0] + "." + str(n_helicopter)] = np.asarray(values[i])
            # print(np.asarray(values[i]))
    return dataframe


def get_data_hillclimbing_5_centers(regex: list[tuple[str, bool]],
                                    centers: list[int],
                                    path_jar: str,
                                    n_grupos: int = 100,
                                    n_seeds: int = 10,
                                    n_times: int = 1) -> DataFrame:
    """
    Experiment number 5
    Given a list of tuples we execute a jar file which prints out values, and we retrieve those values and organize them
    :param n_grupos: número de grupos
    :param centers: groups to try
    :param path_jar: path to jar
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    dataframe = pa.DataFrame()
    for center in tqdm(centers, desc="Groups:", leave=False):
        values = []
        for _ in regex:
            values.append([])
        for j in tqdm(range(n_seeds), desc="Seeds:", leave=False):
            seed = 1000 + j
            for _ in tqdm(range(n_times), desc="Times:", leave=False):
                p = Popen(['java', '-jar', path_jar, str(seed), str(n_grupos), str(center)], stdout=PIPE, stderr=STDOUT)
                output_to_values(p, regex, values)
        n = len(regex)
        for i in range(n):
            dataframe[regex[i][0] + "." + str(center)] = np.asarray(values[i])
            # print(np.asarray(values[i]))
    return dataframe


def get_data_hillclimbing_5(regex: list[tuple[str, bool]],
                            groups: list[int],
                            path_jar: str,
                            n_seeds: int = 10,
                            n_times: int = 1) -> DataFrame:
    """
    Experiment number 5
    Given a list of tuples we execute a jar file which prints out values, and we retrieve those values and organize them
    :param groups: groups to try
    :param path_jar: path to jar
    :param n_times: number of times to execute each seed
    :param n_seeds: number of seeds
    :param regex: list of tuples of which the first element indicates which regex value to look for, second element
    is True if the value we look for is an int, if It's False then the value we look for is a float
    :return: dataframe
    """
    dataframe = pa.DataFrame()
    for group in tqdm(groups, desc="Groups:", leave=False):
        values = []
        for _ in regex:
            values.append([])
        for j in tqdm(range(n_seeds), desc="Seeds:", leave=False):
            seed = 1000 + j
            for _ in tqdm(range(n_times), desc="Times:", leave=False):
                p = Popen(['java', '-jar', path_jar, str(seed), str(group)], stdout=PIPE, stderr=STDOUT)
                output_to_values(p, regex, values)
        n = len(regex)
        for i in range(n):
            dataframe[regex[i][0] + "." + str(group)] = np.asarray(values[i])
            # print(np.asarray(values[i]))
    return dataframe


def get_data_hillclimbing(regex: list[tuple[str, bool]],
                          path_jar: str,
                          n_seeds: int = 10,
                          n_times: int = 10) -> DataFrame:
    """
    Given a list of tuples we execute a jar file which prints out values, and we retrieve those values and organize them
    :param path_jar: path to jar
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
            p = Popen(['java', '-jar', path_jar, str(seed)], stdout=PIPE, stderr=STDOUT)
            output_to_values(p, regex, values)
    n = len(regex)
    for i in range(n):
        dataframe[regex[i][0]] = np.asarray(values[i])
        # print(np.asarray(values[i]))
    return dataframe


if __name__ == '__main__':
    main()
