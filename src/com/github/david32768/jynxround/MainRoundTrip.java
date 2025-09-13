package com.github.david32768.jynxround;

import java.io.PrintWriter;

import com.github.david32768.jynxfree.jynx.MainOption;
import com.github.david32768.jynxfree.jynx.MainOptionService;


public class MainRoundTrip implements MainOptionService {

    @Override
    public MainOption main() {
        return MainOption.ROUNDTRIP;
    }

    @Override
    public boolean call(PrintWriter pw, String fname) {
        return RoundTrip.roundTrip(fname);
    }
    
}
