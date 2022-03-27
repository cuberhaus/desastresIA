#!/usr/bin/env python
"""
Draws boxplots given some data in a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import matplotlib.pyplot as plot
import numpy as np
import pandas as pd

path_pol = "/home/pol/Downloads/plot.csv"
path_pol_mac = "/Users/pol/Downloads/plot.csv"


def main():
    data = pd.read_csv(path_pol_mac, header=1, thousands=',')

    csv_to_boxplot(data, "Texec", "Tiempo de ejecución", "texec.png")
    csv_to_boxplot(data, "Nodos expandidos", "Nodos Expandidos", "nodesExpanded.png")
    csv_to_boxplot(data, "Heurístico final", "Heurístico final", "heuristicoFinal.png")
    # csv_to_3d_plot(data, "Heurístico final", "Heurístico final", "3d.png")


def csv_to_3d_plot(data, column_name, title, file_name):
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


def csv_to_boxplot(data, column_name, title, file_name):
    plot.figure(figsize=(10, 6))
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
    x_labels = ["Swap", "Reasignar general", "Reasignar reducido", "Swap + general", "Swap + reducido"]
    plot.boxplot(values, labels=x_labels, )
    plot.title(title)
    plot.savefig("./" + file_name)
    plot.clf()


if __name__ == '__main__':
    main()
