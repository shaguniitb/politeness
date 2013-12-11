import csv
import string
import glob
import os

input_dir = "politeness/data/blogs";
output_file = "/home/shagun/blogs.csv";

os.chdir(input_dir)
firstLine = True
for input_file in glob.glob("*.txt"):
    print input_file
    fo = open(input_file, "rw+")
    lines = fo.readlines()
    request = ''.join(lines)
    with open(output_file, 'a') as csvfile:
        spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
        request = request.replace('\n', '')
        request = request.replace('\r', '')
        if firstLine:
            firstLine = False
            spamwriter.writerow(['text', '@@class@@'])
        spamwriter.writerow([request, '?'])
    fo.close()

