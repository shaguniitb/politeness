
import glob
import os

# in - domain wiki-BOW
a = {}
folders = ['in-domain_wiki_BOW', 'in-domain_wiki_Ling', 'in-domain_stack_BOW', 'in-domain_stack_Ling']
for folder in folders:
    test_types = ["pre_alpha", "pre_word", "pre_alpha_with_attribute_selection", "pre_word_with_attribute_selection"]
    for test_type in test_types:
        input_dir = "/home/shagun/politeness/code/politeness/results/in-domain/" + folder + "/" + test_type;
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
    f = open('/home/shagun/politeness/paper/' + folder + '.m', 'w')
    count = 1
    f.write('x=1:11;\n')
    for classifier in classifiers:
        line = 'p' + str(count) + ' = ['
        line += a['pre_alpha'][classifier][:-2]
        line += ' ' 
        line += a['pre_word'][classifier][:-2]
        line += ' ' 
        line += a['pre_alpha_with_attribute_selection'][classifier][:-2]
        line += ' ' 
        line += a['pre_word_with_attribute_selection'][classifier][:-2]
        line += '];\n' 
        f.write(line)
        count = count + 1
    f.write('plot(x, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);\n')
    f.write("xlabel('Test Type');\n")
    f.write("ylabel('Correctly classified instances(%)');\n")
    f.write("legend('Naive Bayes', 'Naive Bayes Multinomial', 'J48', 'Random Forest (10 trees)', 'Random Forest (100 trees)', 'iBK (k=1, using Euclidean Distance)', 'iBK (k=10, using Euclidean Distance)', 'iBK (k=1, using Manhattan Distance)', 'iBK (k=10, using Manhattan Distance)', 'SMO');\n")
    f.write('figureHandle = gcf;\n')
    f.write("set(findall(figureHandle,'type','text'),'fontSize',26);\n")
    f.write("set(gca,'fontsize',20);")
    f.write("set(h,'LineWidth',5);")
    f.close()


