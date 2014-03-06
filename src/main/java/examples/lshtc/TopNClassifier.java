package examples.lshtc;

import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.procedure.TIntIntProcedure;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import ru.algorithmist.sparsity.data.MultiClassLabel;
import ru.algorithmist.sparsity.data.Predictors;
import ru.algorithmist.sparsity.pipe.AbstractProcessor;

import java.util.*;

/**
 * @author Sergey Edunov
 */
public class TopNClassifier extends AbstractProcessor<Predictors<Collection<MultiClassLabel>>, Predictors<MultiClassLabel>> {

    private int N;

    public TopNClassifier(int n){
        this.N = n;
    }

    @Override
    public Predictors<MultiClassLabel> train(Predictors<Collection<MultiClassLabel>> input) throws Exception {
        Predictors<MultiClassLabel> res = new Predictors<MultiClassLabel>(input.size());
        for(int i=0; i<input.size(); i++) {
            TIntSet ret = new TIntHashSet();
            Collection<MultiClassLabel> sets = input.get(i);
            for(MultiClassLabel set : sets){
                ret.addAll(set.getClasses());
            }
            res.set(i, new MultiClassLabel(ret.toArray()));
        }
        return res;
    }

    @Override
    public Predictors<MultiClassLabel> process(Predictors<Collection<MultiClassLabel>> input) throws Exception {
        Predictors<MultiClassLabel> res = new Predictors<MultiClassLabel>(input.size());
        for(int i = 0; i<input.size(); i++) {
            Collection<MultiClassLabel> sets = input.get(i);
            TIntIntMap counters = new TIntIntHashMap();
            for(MultiClassLabel set : sets){
                for(int v : set.getClasses()) {
                    counters.adjustOrPutValue(v, 1, 1);
                }
            }
            int[] values = counters.values();
            Arrays.sort(values);
            final int threshold = values.length > N ? values[values.length - N] : 0;
            if (threshold <= 1){
                getContext().getProgressReporter().reportWarning("Top three threshold is too low, not enough data? ");
            }
            final TIntSet r = new TIntHashSet();
            counters.forEachEntry(new TIntIntProcedure() {
                @Override
                public boolean execute(int a, int b) {
                    if (b >= threshold){
                        r.add(a);
                    }
                    return true;
                }
            });
            res.set(i,  new MultiClassLabel(r.toArray()));
        }
        return res;
    }
}
