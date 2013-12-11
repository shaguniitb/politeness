import csv
import string
input_file = "/home/shagun/stack-exchange.annotated.csv";
output_file = "/home/shagun/output.csv";
alist = []
firstLine = True
with open(input_file, 'rb') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',', quotechar='"')
    for row in spamreader:
        if firstLine:
            firstLine = False
        else:
            community = row[0]
            request_id = row[1]
            request = row[2]
            request = filter(lambda x: x in string.printable, request)
            request = request.replace('\n', '')
            request = request.replace(',', '')
            request = request.replace("'", "")
            request = request.replace('"', '')
            request = request.replace('\r', '')
            print row[13]
            score = float(row[13])
            if ('Stack Overflow' in community):
                alist += [{'request_id': request_id, 'score':score, 'request': request}]

print len(alist)
alist = sorted(alist, key = lambda k: k['score'])
count = len(alist)

with open(output_file, 'wb') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
    for element in alist[0:count/4]:
        spamwriter.writerow([element['request'], 'impolite'])
    for element in alist[count*3/4: count]:
        spamwriter.writerow([element['request'], 'polite'])
