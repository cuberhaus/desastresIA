#!/usr/bin/env python
"""
Draws boxplots given some data in a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import matplotlib.pyplot as plot
import numpy as np
import pandas as pd
from pandas import DataFrame

path_pol = "/home/pol/Downloads/"
path_pol_mac = "/Users/pol/Downloads/"
experimento1_path = path_pol + "experimento1.tsv"
experimento2_path = path_pol + "experimento2.tsv"
experimento5_grupos_path = path_pol + "experimento5_grupos.tsv"
experimento5_centros_path = path_pol + "experimento5_centros.tsv"
experimento6_path = path_pol + "experimento6.tsv"
experimento4_HC_path = path_pol + "experimento4_HC.tsv"
experimento4_SA_path = path_pol + "experimento4_SA.tsv"
experimento7_HC_path = path_pol + "experimento7_HC.tsv"
experimento7_SA_path = path_pol + "experimento7_SA.tsv"


def main():
    experimento7()
    # data = pd.read_csv(experimento6_path, sep="\t", header=0, thousands=',')
    # print(data)
    # experimento6(data)
    # data = pd.read_csv(path_pol_mac, header=1, thousands=',')
    # csv_to_3d_plot(data, "Heurístico final", "Heurístico final", "3d.png")


def experimento7():
    data = pd.read_csv(experimento7_SA_path, sep="\t", header=1, thousands=',')
    x_labels = ["Mismo peso", "*2 prioritarios", "*4 prioritarios", "*8 prioritarios", "*16 prioritarios",
                "*32 prioritarios", "*64 prioritarios", "*128 prioritarios"]
    csv_to_boxplot_rename(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)
    csv_to_boxplot_rename(data, "Tiempo prio.", "Tiempo prio.", "tiempoPrio.png", x_labels)
    csv_to_boxplot_rename(data, "Suma todos", "Suma Todos", "sumaTodos.png", x_labels)


def experimento4():
    data = pd.read_csv(experimento4_HC_path, sep="\t", header=1, thousands=',')
    x_labels = ["5-100", "10-200", "15-300", "20-400", "25-500"]
    # x_labels = ["5-100", "10-200", "15-300"]
    csv_to_boxplot_rename(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)


def experimento6():
    data = pd.read_csv(experimento6_path, sep="\t", header=0, thousands=',')
    x_labels = list(map(str, np.arange(1, 16, 1)))
    csv_to_boxplot_xlabel(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)
    csv_to_boxplot_xlabel(data, "nodesExpanded", "Nodos Expandidos", "nodesExpanded.png", x_labels)
    csv_to_boxplot_xlabel(data, "Heuristico final", "Heurístico final", "heuristicoFinal.png", x_labels)


def experimento5_centros():
    data = pd.read_csv(experimento6_path, sep="\t", header=1, thousands=',')
    numbers = np.arange(5, 105, 5)
    x_labels = list(map(str, numbers))

    csv_to_boxplot_xlabel(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)
    csv_to_boxplot_xlabel(data, "nodesExpanded", "Nodos Expandidos", "nodesExpanded.png", x_labels)
    csv_to_boxplot_xlabel(data, "Heuristico final", "Heurístico final", "heuristicoFinal.png", x_labels)


def experimento5_grupos(data):
    x_labels = ["100", "150", "200", "250"]
    csv_to_boxplot_xlabel(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)
    csv_to_boxplot_xlabel(data, "nodesExpanded", "Nodos Expandidos", "nodesExpanded.png", x_labels)
    csv_to_boxplot_xlabel(data, "Heuristico final", "Heurístico final", "heuristicoFinal.png", x_labels)


def experimento1(data):
    x_labels = ["Swap", "Reasignar general", "Reasignar reducido", "Swap + general", "Swap + reducido"]
    texec_nodos_heuristic_boxplots(csv_to_boxplot_rename, x_labels, data)


def experimento2(data):
    x_labels = ["Random", "Todo a 1", "Greedy"]
    texec_nodos_heuristic_boxplots(csv_to_boxplot_rename, x_labels, data)


def texec_nodos_heuristic_boxplots(func, x_labels, data):
    func(data, "Texec", "Tiempo de ejecución", "texec.png", x_labels)
    func(data, "Nodos expandidos", "Nodos Expandidos", "nodesExpanded.png", x_labels)
    func(data, "Heurístico final", "Heurístico final", "heuristicoFinal.png", x_labels)


def csv_to_3d_plot(data: DataFrame, column_name: str, title: str, file_name: str) -> None:
    # plot.figure(figsize=(10, 6))
    fig = plot.figure()
    ax = fig.add_subplot(projection='3d')
    values = []
    x_values = []
    array = data[column_name]
    for i in range(array.size):
        x_values.append(float(data[column_name][i]))
    # print(array)
    x_values = []
    values.append(x_values)
    for j in range(4):
        column = j + 1
        array = data[column_name + "." + str(column)]
        for i in range(array.size):
            x_values.append(float(array[i]))
        # print(array)
        values.append(x_values)
        x_values = []
    # x_labels = ["Swap", "Reasignar general", "Reasignar reducido", "Swap + general", "Swap + reducido"]

    # Proves
    width = 1  # bar width in the x axis
    # height = [1, 2, 3, 4]  # this are the values for which given an "x" and "z" this values are the result
    print(values[0])
    height = values[0]
    depth = 1  # cube depth in the z axis
    # x = [1, 2, 3, 4]
    x = np.arange(1, 101)
    # y = [1, 2, 3, 4]
    y = np.arange(1, 101)
    z = np.zeros_like(height)  # generates a list with as many zeros as elements in parameter
    ax.bar3d(x, y, z, width, depth, height, shade=True)
    plot.title(title)
    plot.savefig("./" + file_name)
    plot.clf()


def csv_to_boxplot_xlabel(data: DataFrame, column_name: str, title: str, file_name: str, x_labels: list,
                          start: int = 0) -> None:
    """
    Given a DataFrame searches for all occurrences of column_name, extracts that data and creates a boxplots with
    label x_labels[i]
    :param start:
    :param data: DataFrame
    :param column_name: name of the columns to look for
    :param title: Title of the plot
    :param file_name: Name of the output file
    :param x_labels: Labels of each occurrence of the column
    :return:
    """

    plot.figure(figsize=(10, 6))
    values = []
    x_values = []
    for label in x_labels:
        array = data[column_name + "." + label]
        for i in range(array.size):
            x_values.append(float(array[i]))

        values.append(x_values)
        x_values = []
    plot.boxplot(values, labels=x_labels)
    plot.title(title)
    plot.savefig("./" + file_name)
    plot.clf()


def csv_to_boxplot_rename(data: DataFrame, column_name: str, title: str, file_name: str, x_labels: list,
                          start: int = 0) -> None:
    """
    Given a DataFrame searches for all occurrences of column_name, extracts that data and creates a boxplots with
    label x_labels[i]
    :param start:
    :param data: DataFrame
    :param column_name: name of the columns to look for
    :param title: Title of the plot
    :param file_name: Name of the output file
    :param x_labels: Labels of each occurrence of the column
    :return:
    """

    plot.figure(figsize=(12, 6))
    values = []
    x_values = []
    n = len(x_labels)
    for j in range(n):
        k = j + start
        if k == 0:
            array = data[column_name]
            for i in range(array.size):
                x_values.append(float(data[column_name][i]))
        else:
            column = k
            array = data[column_name + "." + str(column)]
            for i in range(array.size):
                x_values.append(float(array[i]))
        values.append(x_values)
        x_values = []
    plot.boxplot(values, labels=x_labels)
    plot.title(title)
    plot.savefig("./" + file_name)
    plot.clf()


if __name__ == '__main__':
    main()
