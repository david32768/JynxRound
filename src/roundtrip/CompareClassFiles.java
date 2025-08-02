package roundtrip;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceClassVisitor;

import com.github.david32768.jynxfree.jynx.ClassUtil;
import com.github.david32768.jynxround.CompareStrings;

public class CompareClassFiles {

    public static CompareStrings usingASM(byte[] original, byte[] result) {
        String resultstr = textify(result, Short.MAX_VALUE);
        String originalstr = textify(original, resultstr.length());
        return new CompareStrings(originalstr, resultstr);
    }

    public static CompareStrings usingASM(String fname1, String fname2) throws IOException {
        byte[] ba1 = ClassUtil.getClassBytes(fname1);
        byte[] ba2 = ClassUtil.getClassBytes(fname2);
        return usingASM(ba1, ba2);
    }

    private static String textify(byte[] ba, int len) {
        ClassReader cr = new ClassReader(ba);
        return textify(cr, len);
    }
    
    private static String textify(ClassReader cr, int len) {
        ClassNode cn = new ClassNode();
        cr.accept(cn,ClassReader.EXPAND_FRAMES);
        Printer printer = new Textifier();
        StringWriter sw = new StringWriter(len);
        try (PrintWriter pw = new PrintWriter(sw)) {
            TraceClassVisitor tcv = new TraceClassVisitor(null, printer, pw);
            cn.accept(tcv);
        }
        return sw.toString();
    }
    
}
