package examples.lshtc;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import ru.algorithmist.sparsity.algorithms.KNN;
import ru.algorithmist.sparsity.algorithms.TFIDF;
import ru.algorithmist.sparsity.algorithms.similarity.CosineSimilarity;
import ru.algorithmist.sparsity.data.MultiClassLabel;
import ru.algorithmist.sparsity.data.Predictors;
import ru.algorithmist.sparsity.pipe.*;
import ru.algorithmist.sparsity.text.FileProcessor;

/**
 * @author Sergey Edunov
 */
public class Main {
    //TFIDF 100, RP 500, CV 10000 = 0.05683133461363336
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java " + Main.class.getCanonicalName() + " train_file test_file");
            System.exit(1);
        }
        String trainFile = args[0];
        String testFile = args[1];
        ParallelPipe<String, Predictors<MultiClassLabel>> pipe = new ParallelPipe<String, Iterable<String>>(4, new FileProcessor(true))
                .add(new NFoldCVProcessor<String>(1, 10))
                .add(new DataProcessor(-1, 10000))
                .add(new DataFrameProcessorDecorator<MultiClassLabel>(new TFIDF(100)))
//                .add(new ParallelDataFrameProcessorDecorator<MultiClassLabel>(new RandomProjections(500)))
//                .add(new KNN<MultiClassLabel>(5, new RPCosineSimilarity()))
                .add(new KNN<MultiClassLabel>(5, new CosineSimilarity()))
                .add(new TopNClassifier(3));


        pipe.train(trainFile);
        Predictors<MultiClassLabel> data = pipe.process(testFile);
//        BufferedWriter out = new BufferedWriter(new FileWriter("out.csv"));
//        for(int i = 0; i<data.size(); i++) {
//            out.write("" + (i+1) + ",");
//            for(int v : data.get(i)) {
//                out.write(v + " ");
//            }
//            out.write("\n");
//        }
//        out.flush();
//        out.close();
        Predictors<MultiClassLabel> cv = pipe.cv(testFile);
        pipe.close();
        for(MultiClassLabel r : cv) {
            System.out.println(r);
        }

        System.out.println(maf(cv, data));
    }


    private static double maf(Predictors<MultiClassLabel> correct, Predictors<MultiClassLabel> answer) {
        TIntIntMap tp = new TIntIntHashMap();
        TIntIntMap fp = new TIntIntHashMap();
        TIntIntMap fn = new TIntIntHashMap();
        TIntSet classes = new TIntHashSet();

        for(int r=0; r < correct.size(); r++) {
            classes.addAll(correct.get(r).getClasses());
            classes.addAll(answer.get(r).getClasses());
            TIntSet cor = new TIntHashSet();
            cor.addAll(correct.get(r).getClasses());
            TIntSet ans = new TIntHashSet();
            ans.addAll(answer.get(r).getClasses());

            for(int a : ans.toArray()) {
                if (cor.contains(a)) {
                    tp.adjustOrPutValue(a, 1, 1);
                } else {
                    fp.adjustOrPutValue(a, 1, 1);
                }
            }
            for(int c : cor.toArray()) {
                if (!ans.contains(c)) {
                    fn.adjustOrPutValue(c, 1, 1);
                }
            }
        }

        double map  = 0;
        double mar = 0;
        for (int c : classes.toArray()) {
            if (tp.get(c) != 0) {
                map += tp.get(c) / (tp.get(c) + fp.get(c));
                mar += tp.get(c) / (tp.get(c) + fn.get(c));
            }
        }
        map /= classes.size();
        mar /= classes.size();
        return 2*map*mar / (map + mar);
    }
}
