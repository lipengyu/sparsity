package examples.lshtc;

import ru.algorithmist.sparsity.algorithms.RandomProjections;
import ru.algorithmist.sparsity.algorithms.TFIDF;
import ru.algorithmist.sparsity.data.DataFrame;
import ru.algorithmist.sparsity.data.MultiClassLabel;
import ru.algorithmist.sparsity.data.Vector;
import ru.algorithmist.sparsity.data.bit.BitVector;
import ru.algorithmist.sparsity.pipe.DataFrameProcessorDecorator;
import ru.algorithmist.sparsity.pipe.ParallelDataFrameProcessorDecorator;
import ru.algorithmist.sparsity.pipe.ParallelPipe;
import ru.algorithmist.sparsity.pipe.Pipe;
import ru.algorithmist.sparsity.text.FileProcessor;
import ru.algorithmist.sparsity.utils.StringFormat;
import ru.algorithmist.sparsity.utils.VectorUtils;

/**
 * @author Sergey Edunov
 */
public class RandomProjectionsAccuracy {

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.out.println("Usage: java " + RandomProjectionsAccuracy.class.getCanonicalName() + " train_file test_file");
            System.exit(1);
        }
        String trainFile = args[0];
        String testFile = args[1];

        Pipe<String, DataFrame<MultiClassLabel>> exactPipe = new Pipe<String, Iterable<String>>(new FileProcessor(true))
                .add(new DataProcessor(1000, 10))
                .add(new DataFrameProcessorDecorator<MultiClassLabel>(new TFIDF(3)));

        DataFrame<MultiClassLabel> trainExact = exactPipe.train(trainFile);
        DataFrame<MultiClassLabel> testExact = exactPipe.process(testFile);


        for(int proj = 3; proj < 10000; proj += proj) {

            ParallelPipe<DataFrame<MultiClassLabel>, DataFrame<MultiClassLabel>> rpPipe = new ParallelPipe<DataFrame<MultiClassLabel>, DataFrame<MultiClassLabel>>(4, new ParallelDataFrameProcessorDecorator<MultiClassLabel>(new RandomProjections(proj)));
            rpPipe.getContext().getProgressReporter().disable();

            DataFrame<MultiClassLabel> trainRP = rpPipe.train(trainExact);
            DataFrame<MultiClassLabel> testRP = rpPipe.process(testExact);

            rpPipe.close();

            double rmse = 0;
            double mean = 0;
            for(int r = 0; r<testExact.getRows(); r++) {
                Vector exact = testExact.getData(r);
                Vector rp = testRP.getData(r);
                for(int i=0; i<trainExact.getRows(); i++) {
                    double ex = VectorUtils.cosineSimilarity(trainExact.getData(i), exact);
                    double es = VectorUtils.cosineSimilarity((BitVector)trainRP.getData(i), (BitVector)rp);

                    rmse += (es-ex)*(es-ex);
                    mean += es-ex;
                }
            }

            rmse = Math.sqrt(rmse);

            System.out.println(proj + "," + StringFormat.format(rmse) + "," + StringFormat.format(mean));


        }
    }
}
