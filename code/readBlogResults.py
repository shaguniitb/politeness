
import glob
import os

# wiki-Ling with blogs
a = {}
input_dir = "/home/shagun/Acads/Fall_2013/politeness/code/politeness/results/kblogs_stack_Ling/pre_alpha_with_attribute_selection"
os.chdir(input_dir)
for input_file in glob.glob("*.txt"):
    classifier = input_file.split(".txt")[0]
    a[classifier] = {}
    fo = open(input_file, "rw+")
    lines = fo.readlines()
    for line in lines:
        words = line.split();
        blog = 'Blog ' + words[1]
        a[classifier][blog] = {'polite': words[3], 'impolite': words[5]}

classifiers = ['naive_bayes', 'naive_bayes_multinomial', 'J48', 'random_forest_10_trees', 'random_forest_100_trees', 'ibk_1_KNN_EuclideanDistance', 'ibk_10_KNN_EuclideanDistance', 'ibk_1_KNN_ManhattanDistance', 'ibk_10_KNN_ManhattanDistance', 'SMO']
classifier_name = {'naive_bayes': 'Naive Bayes', 
                    'naive_bayes_multinomial': 'Naive Bayes Multinomial',
                    'J48': 'J48',
                    'random_forest_10_trees': 'Random Forest (10 trees)',
                    'random_forest_100_trees': 'Random Forest (100 trees)',
                    'ibk_1_KNN_EuclideanDistance': 'iBK (k=1, using Euclidean Distance)',
                    'ibk_10_KNN_EuclideanDistance': 'iBK (k=10, using Euclidean Distance)',
                    'ibk_1_KNN_ManhattanDistance': 'iBK (k=1, using Manhattan Distance)',
                    'ibk_10_KNN_ManhattanDistance':'iBK (k=10, using Manhattan Distance)',
                    'SMO': 'SMO'
                }   
f = open('/home/shagun/Acads/Fall_2013/politeness/paper/temp_for_latex.txt', 'w')
for classifier in classifiers:
    f.write('\\textbf{' + classifier_name[classifier] + '}')
    for i in range(6,11):
        key = 'Blog ' + str(i)
        f.write(' & ')
        f.write(a[classifier][key]['polite'])
        f.write(' & ')
        f.write(a[classifier][key]['impolite'])
    f.write(' \\\ ')
    f.write('\n')
    f.write('\hline')
    f.write('\n')
