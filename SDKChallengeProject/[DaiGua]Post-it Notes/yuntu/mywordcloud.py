# coding:utf-8

from os import path
import plotWordcloud
import sys

if __name__ == '__main__':
    arg1 = sys.argv[1]
    arg2 = sys.argv[2]

    plotWordcloud.generate_wordcloud(arg1, arg2)

    print(arg1)
