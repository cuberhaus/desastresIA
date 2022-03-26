#!/usr/bin/env python
"""
Draws boxplots given some data in a csv
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import matplotlib.pyplot as plot
import pandas as pd

path_pol = "/home/pol/Downloads/plot.csv"
path_pol_mac = "/Users/pol/Downloads/plot.csv"



def main():
    data = pd.read_csv(path_pol_mac, header=1, thousands=',')

    csv_to_boxplot(data, "Texec", "Tiempo de ejecución", "texec.png")
    csv_to_boxplot(data, "Nodos expandidos", "Nodos Expandidos", "nodesExpanded.png")
    csv_to_boxplot(data, "Heurístico final", "Heurístico final", "heuristicoFinal.png")


def csv_to_boxplot(data, column_name, title, file_name):
    plot.figure(figsize=(10, 6))
    values = []
    x_values = []
    array = data[column_name]
    for i in range(array.size):
        x_values.append(float(data[column_name][i]))
    print(array)
    x_values = []
    values.append(x_values)
    for j in range(4):
        column = j + 1
        array = data[column_name + "." + str(column)]
        for i in range(array.size):
            x_values.append(float(array[i]))
        print(array)
        values.append(x_values)
        x_values = []
    x_labels = ["Swap", "Reasignar general", "Reasignar reducido", "Swap + general", "Swap + reducido"]
    plot.boxplot(values, labels=x_labels, )
    plot.title(title)
    plot.savefig("./" + file_name)
    plot.clf()


if __name__ == '__main__':
    main()
