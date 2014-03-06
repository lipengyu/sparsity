package examples.lshtc;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import ru.algorithmist.sparsity.data.DataFrame;
import ru.algorithmist.sparsity.data.MultiClassLabel;
import ru.algorithmist.sparsity.data.Predictors;
import ru.algorithmist.sparsity.data.huge.OffHeapSparseMatrix;
import ru.algorithmist.sparsity.pipe.AbstractProcessor;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Sergey Edunov
 */
public class DataProcessor extends AbstractProcessor<Iterable<String>, DataFrame<MultiClassLabel>> {

    private int readFirstNTrainRows;
    private int readFirstNTestRows;

    private static final Pattern SPLIT_PATTERN = Pattern.compile("[\\,\\s]+");

    public DataProcessor(int readFirstNTrainRows, int readFirstNTestRows){
        this.readFirstNTestRows = readFirstNTestRows;
        this.readFirstNTrainRows = readFirstNTrainRows;
    }

    @Override
    public  DataFrame<MultiClassLabel> train(Iterable<String> input) throws IOException {
        return processInput(input, true);
    }

    @Override
    public  DataFrame<MultiClassLabel> process(Iterable<String> input) throws IOException {
        return processInput(input, false);
    }

    public  DataFrame<MultiClassLabel> processInput(Iterable<String> input, boolean train) throws IOException {
        int readNFirstRows = train? readFirstNTrainRows : readFirstNTestRows;
        int row = 0;
        OffHeapSparseMatrix words = new OffHeapSparseMatrix(readNFirstRows>0?readNFirstRows:6000000, 100000);
//        SparseMatrix words = new SparseMatrix();
        Predictors<MultiClassLabel> predictors = new Predictors<MultiClassLabel>();
        for(String line : input) {
            if (readNFirstRows >0 && row > readNFirstRows) break;
            if (row % 1000 == 0) {
                getContext().getProgressReporter().reportProgress("DataProcessor", row);
            }

            String[] spl = SPLIT_PATTERN.split(line);

            TIntSet classes = new TIntHashSet();
            boolean initialized = false;
            for(int i=0; i<spl.length; i++) {
                if (spl[i].contains(":")){
                    if (!initialized){
                        words.init(row, spl.length-i);
                        initialized = true;
                    }
                    int idx = spl[i].indexOf(':');
                    words.set(row, Integer.parseInt(spl[i].substring(0, idx)), Integer.parseInt(spl[i].substring(idx + 1)));
                } else {
                    classes.add(Integer.parseInt(spl[i]));
                }
            }

            predictors.add(new MultiClassLabel(classes.toArray()));
            row++;

        }
        return new DataFrame<MultiClassLabel>(predictors, words);
    }
}
