package com.github.david32768.jynxround;

import java.io.IOException;
import java.io.PrintWriter;

import static com.github.david32768.jynxfree.jynx.Global.LOG;

import com.github.david32768.jynxfree.jynx.MainOption;
import com.github.david32768.jynxfree.jynx.MainOptionService;

import roundtrip.CompareClassFiles;


public class MainCompare implements MainOptionService {

    @Override
    public MainOption main() {
        return MainOption.COMPARE;
    }

    @Override
    public boolean call(PrintWriter pw, String fname1, String fname2) {
        try {
            var strings = CompareClassFiles.usingASM(fname1, fname2);
            return strings.compareString();
        } catch (IOException ex) {
            LOG(ex);
            return false;
        }
    }
    
}
