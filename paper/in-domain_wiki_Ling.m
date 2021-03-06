x=1:11;
p1 = [74.4485 74.7702 77.0221 76.3327];
p2 = [80.7904 80.239 80.4688 80.3309];
p3 = [72.7022 72.4265 75 73.3456];
p4 = [72.932 74.7702 76.7004 77.4357];
p5 = [79.9173 80.6066 80.1011 80.4228];
p6 = [64.6599 64.8438 71.4614 71.829];
p7 = [60.2022 59.6967 76.5165 76.7923];
p8 = [64.6599 64.8897 70.5423 70.5423];
p9 = [59.4669 59.5129 74.6783 74.9081];
p10 = [81.3879 80.3768 82.2151 81.0202];
plot(x, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
xlabel('Test Type');
ylabel('Correctly classified instances(%)');
legend('Naive Bayes', 'Naive Bayes Multinomial', 'J48', 'Random Forest (10 trees)', 'Random Forest (100 trees)', 'iBK (k=1, using Euclidean Distance)', 'iBK (k=10, using Euclidean Distance)', 'iBK (k=1, using Manhattan Distance)', 'iBK (k=10, using Manhattan Distance)', 'SMO');
figureHandle = gcf;
set(findall(figureHandle,'type','text'),'fontSize',26);
set(gca,'fontsize',20);set(h,'LineWidth',5);