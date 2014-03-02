package ru.algorithmist.sparsity.text;

import ru.algorithmist.sparsity.pipe.AbstractProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Sergey Edunov
 */
public class FileProcessor extends AbstractProcessor<String, Iterable<String>> {

    private boolean skipHeader;

    public FileProcessor(boolean skipHeader) {
        this.skipHeader = skipHeader;
    }

    @Override
    public Iterable<String> train(String input) throws IOException {
        return processInput(input, skipHeader);
    }

    @Override
    public Iterable<String> process(String input) throws IOException {
        return processInput(input, skipHeader);
    }

    public Iterable<String> processInput(String input, boolean skipHeader) throws IOException {
        final BufferedReader br = new BufferedReader(new FileReader(input));
        if (skipHeader) {
            br.readLine();
        }


        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    String line;

                    @Override
                    public boolean hasNext() {
                        if (line != null) {
                            return true;
                        }
                        try {
                            line = br.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return line != null;
                    }

                    @Override
                    public String next() {
                        if (line != null) {
                            String res = line;
                            line = null;
                            return res;
                        }
                        try {
                            return br.readLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Removing from file stream is not supported");
                    }
                };
            }
        };

    }
}