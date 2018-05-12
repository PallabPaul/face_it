package org.faceit.demo;

import java.util.List;
import org.faceit.demo.Classifier.Recognition;

public interface ResultsView {
    void setResults(List<Recognition> list);
}
