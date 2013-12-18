import csv
import string
import glob
import os
import arff

input_dir = "politeness/data/blogs";
output_file = "/home/shagun/politeness/weka-3-6-10/blogs.arff";

os.chdir(input_dir)
firstLine = True
data = []

def checkInList(word_list, request):
    request = request.lower()
    count = 0
    for listItem in word_list:
        listItem = listItem.lower()
        if (listItem in request):
            count += 1
    return count

def checkInListLines(word_list, request):
    request = request.lower()
    lines = request.split("\\r?\\n")
    count = 0
    for line in lines:
        line = line.strip()
        for listItem in word_list:
            listItem = listItem.lower()
            if (line.startswith(listItem)):
                count += 1
    return count

def lexiconList(fileName):
    outputList = []
    fi = open(fileName, "r")
    for line in fi.readlines():
        line = line.strip()
        outputList += line
    return outputList

for input_file in glob.glob("*.txt"):
    fo = open(input_file, "rw+")
    lines = fo.readlines()
    request = '\n'.join(lines)

    gratitudeList = ["appreciate", "thankful", "grateful", "recognize", "indebted"]
    deferenceList = ["Nice work", "respect", "polite"]
    greetingList = ["Hey", "Hi", "Hello", "take care", "tc", "bye", "Good morning", "Good afternoon", "Good evening", "Good night", "gn", "Dear", "howdy", "ciao", "what's up", "wassup", "yo", "whassup", "welcome", "hail"]
    positiveList = lexiconList("/home/shagun/politeness/code/politeness/data/positive-words.txt")
    negativeList = lexiconList("/home/shagun/politeness/code/politeness/data/negative-words.txt")
    apologizingList = ["sorry", "pardon", "regret", "apologize", "ashamed", "regretful", "penitent"]

    numGratitude = checkInList(gratitudeList, request)
    numDeference = checkInList(deferenceList, request)
    numGreeting = checkInList(greetingList, request)
    inPosLexicon = checkInList(positiveList, request)
    inNegLexicon = checkInList(negativeList, request)

    numApologize = checkInList(apologizingList, request)
    pleaseCount = checkInList(["please"], request)
    pleaseStartCount = checkInListLines(["please"], request)
    numIndirect = checkInList(["by the way", "btw"], request)

    numDirectQuestion = checkInListLines(["wh"], request)
    numDirectStart = checkInListLines(["wh"], request)
    numCounterFactual = checkInListLines(["could", "would"], request)
    numIndicative = checkInListLines(["can", "will"], request)


    data += [[request, numGratitude, numDeference, numGratitude, inPosLexicon, inNegLexicon, numApologize, pleaseCount, pleaseStartCount, numIndirect, numDirectQuestion, numDirectStart, numCounterFactual, numIndicative, '?']]
    fo.close()

arff.dump(output_file, data, relation="testing", names=['request', 'numGratitude', 'numDeference', 'numGreeting', 'inPosLexicon', 'inNegLexicon', 'numApologize', 'pleaseCount', 'pleaseStartCount', 'numIndirect', 'numDirectQuestion', 'numDirectStart', 'numCounterFactual', 'numIndicative', 'class'])
