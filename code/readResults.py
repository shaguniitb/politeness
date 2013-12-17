
import glob
import os

# in - domain wiki-BOW
a = {}
test_types = ["pre_alpha", "pre_word", "pre_alpha_with_attribute_selection", "pre_word_with_attribute_selection"]
for test_type in test_types:
    input_dir = "/home/shagun/politeness/code/politeness/results/cross-domain/cross-domain_stack_Ling/" + test_type;
    a[test_type] = {}
    os.chdir(input_dir)
    for input_file in glob.glob("*.txt"):
        classifier = input_file.split(".txt")[0]
        fo = open(input_file, "rw+")
        lines = fo.readlines()
        line = lines[1]
        words = line.split();
        error = words[-2] + '\%'
        print classifier, error
        a[test_type][classifier] = error

classifiers = ['naive_bayes', 'naive_bayes_multinomial', 'J48', 'random_forest_10_trees', 'random_forest_100_trees', 'ibk_1_KNN_EuclideanDistance', 'ibk_10_KNN_EuclideanDistance', 'ibk_1_KNN_ManhattanDistance', 'ibk_10_KNN_ManhattanDistance', 'SMO']
classifier_name = {'naive_bayes': 'Naive Bayes', 
                    'naive_bayes_multinomial': 'Naive Bayes Multinomial',
                    'J48': 'J48',
                    'random_forest_10_trees': 'Random Forest (10 trees)',
                    'random_forest_100_trees': 'Random Forest (100 trees)',
                    'ibk_1_KNN_EuclideanDistance': 'iBK (k=1, using Euclidean Distance)',
                    'ibk_10_KNN_EuclideanDistance': 'iBK (k=10, using Euclidean Distance)',
                    'ibk_1_KNN_ManhattanDistance': 'iBK (k=1, using Manhattan Distance)',
                    'ibk_10_KNN_ManhattanDistance':'iBK (k=1, using Manhattan Distance)',
                    'SMO': 'SMO'
                }   
f = open('/home/shagun/politeness/paper/temp_for_latex.txt', 'w')
for classifier in classifiers:
    f.write('\\textbf{' + classifier_name[classifier] + '}')
    f.write(' & ')
    f.write(a['pre_alpha'][classifier])
    f.write(' & ')
    f.write(a['pre_word'][classifier])
    f.write(' & ')
    f.write(a['pre_alpha_with_attribute_selection'][classifier])
    f.write(' & ')
    f.write(a['pre_word_with_attribute_selection'][classifier])
    f.write(' \\\ ')
    f.write('\n')
    f.write('\hline')
    f.write('\n')
