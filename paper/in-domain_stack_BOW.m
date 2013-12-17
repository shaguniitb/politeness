x=1:11;
p1 = [68.4 67.7 71.8 71.2];
p2 = [71.8 71.55 72.35 71.4];
p3 = [67.15 65.9 69.05 69.75];
p4 = [69.5 68.75 70.15 69.95];
p5 = [73.6 73.45 72.35 72.45];
p6 = [57.7 58.85 65.75 65.6];
p7 = [53.2 53.15 66.95 66.35];
p8 = [56.9 56.85 65.9 63.55];
p9 = [51.35 51.95 64.6 63.1];
p10 = [74.55 73.5 74.8 75.05];
plot(x, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
xlabel('Test Type');
ylabel('Correctly classified instances(%)');
legend('Naive Bayes', 'Naive Bayes Multinomial', 'J48', 'Random Forest (10 trees)', 'Random Forest (100 trees)', 'iBK (k=1, using Euclidean Distance)', 'iBK (k=10, using Euclidean Distance)', 'iBK (k=1, using Manhattan Distance)', 'iBK (k=10, using Manhattan Distance)', 'SMO');
figureHandle = gcf;
set(findall(figureHandle,'type','text'),'fontSize',26);
set(gca,'fontsize',20);set(h,'LineWidth',5);