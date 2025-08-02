module com.github.david32768.JynxRound {
    requires com.github.david32768.JynxFree;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.objectweb.asm.util;
    provides com.github.david32768.jynxfree.jynx.MainOptionService 
            with com.github.david32768.jynxround.MainRoundTrip,
            com.github.david32768.jynxround.MainCompare;
}
