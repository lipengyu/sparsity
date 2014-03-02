package ru.algorithmist.sparsity.text;

import org.xml.sax.InputSource;
import ru.algorithmist.sparsity.data.SparseMatrix;
import ru.algorithmist.sparsity.pipe.AbstractProcessor;
import ru.algorithmist.sparsity.pipe.Processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Vectorizer extends AbstractProcessor<Iterator<Iterator<String>>, SparseMatrix> {

    private Map<String, Integer> vectorizerMap = new HashMap<String, Integer>();

    public SparseMatrix vectorize(Iterator<Iterator<String>> source) {
        SparseMatrix res = new SparseMatrix();
        int row = 0;
        while(source.hasNext()) {
            Iterator<String> lemmas = source.next();
            for(; lemmas.hasNext(); ) {
                res.set(row, column(lemmas.next()), 1);
            }
            row++;
        }
        return res;
    }

    private int column(String lemma) {
        Integer res = vectorizerMap.get(lemma);
        if (res == null) {
            vectorizerMap.put(lemma, vectorizerMap.size());
            res = vectorizerMap.get(lemma);
        }
        return res;
    }

    @Override
    public SparseMatrix train(Iterator<Iterator<String>> input) {
        return vectorize(input);
    }

    @Override
    public SparseMatrix process(Iterator<Iterator<String>> input) {
        return vectorize(input);
    }
}
