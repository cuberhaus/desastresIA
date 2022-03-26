#!/usr/bin/env python
"""
"""
__author__ = "Pol Casacuberta Gil"
__email__ = "pol.casacuberta@estudiantat.upc.edu"

import matplotlib.pyplot as plot
import pandas as pd

path_pol = "/home/pol/Downloads/plot.csv"


def main():
    x_values = []
    data = pd.read_csv(path_pol, header=1, thousands=',')
    array = data["Texec"]
    for i in range(array.size):
        x_values.append(float(data["Texec"][i]))
    print(array)
    plot.boxplot(x_values)
    plot.show()


if __name__ == '__main__':
    main()
