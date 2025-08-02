package roundtrip;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;

import static com.github.david32768.jynxfree.jynx.Global.ADD_OPTIONS;
import static com.github.david32768.jynxfree.jynx.Global.ADD_RELEVENT_OPTIONS;
import static com.github.david32768.jynxfree.jynx.Global.LOG;
import static com.github.david32768.jynxfree.jynx.Global.LOGGER;
import static com.github.david32768.jynxfree.jynx.Global.OPTIONS;
import static com.github.david32768.jynxround.my.Message.M417;
import static com.github.david32768.jynxround.my.Message.M87;

import com.github.david32768.jynxfree.jynx.ClassUtil;
import com.github.david32768.jynxfree.jynx.Global;
import com.github.david32768.jynxfree.jynx.GlobalOption;
import com.github.david32768.jynxfree.jynx.MainOption;
import com.github.david32768.jynxfree.jynx.SevereError;

public class RoundTrip {

    public static boolean roundTrip(String classname) {
        if (classname.endsWith(".txt")) {
            return roundTripList(classname, OPTIONS());
        } else {
            return roundTripClass(classname, OPTIONS());
        }
    }

    private static boolean roundTripList(String listfile, EnumSet<GlobalOption> options) {
        int ct = 0;
        int okct = 0;
        int errct = 0;
        int parmct = 0;
        long start = System.currentTimeMillis();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(listfile))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith(";")
                        || line.isEmpty()
                        || line.endsWith("/")
                        || line.endsWith("\\")
                        || line.endsWith(".MF")) {
                    continue;
                }
                LOGGER().setLine(line);
                ++ct;
                var args = line.split(" ");
                MainOption main = MainOption.ROUNDTRIP;
                Global.newGlobal(main);
                ADD_OPTIONS(options);
                String[] mainargs = Global.setOptions(args);
                if (mainargs.length != 1) {
                    ++parmct;
                    // "main args size (after removing options) is not %d: main args = %s"
                    LOG(M417, 1, Arrays.asList(mainargs));
                    continue;
                }
                var classname = mainargs[0];
                boolean success;
                try {
                    success = roundTripClass(classname, OPTIONS());
                } catch (SevereError ex) {
                    success = false;
                }
                if (success) {
                    ++okct;
                } else {
                    System.out.format("roundtrip for %s failed%n", classname);
                    ++errct;
                }
            }
        } catch (IOException ex) {
            LOG(ex);
            return false;
        }
        long end = System.currentTimeMillis();
        double minutes = (end - start)/60000.;
        System.out.format("classes = %d (ok = %d) (%.2f mins)%n", ct, okct, minutes);
        if (ct != okct) {
            System.out.format("    %6d failed  %6d mainargs%n", errct, parmct);
        }
        return errct == 0;
    }
    
    private static boolean roundTripClass(String classname, EnumSet<GlobalOption> options) {
        if (Global.OPTION(GlobalOption.SKIP_FRAMES) && Global.OPTION(GlobalOption.USE_STACK_MAP)) {
            LOG(M87,GlobalOption.SKIP_FRAMES,GlobalOption.USE_STACK_MAP); // "options %s and %s conflict"
            return false;
        }
        byte[] originalbytes = getBytes(classname);
        if (originalbytes == null) {
            return false;
        }
               
        String disasm = disassemble(originalbytes, options);
        if (disasm == null) {
            System.out.format("disassembly of %s failed%n", classname);
            return false;
        }
        var mainoption = MainOption.ASSEMBLY;
        Global.newGlobal(mainoption);
        var main = mainoption.mainOptionService();
        byte[] resultbytes = main.callFromString(classname, disasm);
        if (resultbytes == null) {
            System.out.format("assembly of %s failed%n", classname);
            return false;
        }
        Global.newGlobal(MainOption.COMPARE);
        ADD_RELEVENT_OPTIONS(options);
        var comparator = CompareClassFiles.usingASM(originalbytes, resultbytes);
        return comparator.compareString();
    }

    private static byte[] getBytes(String classname) {
        byte[] bytes;
        try {
            bytes = ClassUtil.getClassBytes(classname);
        } catch (IOException ex) {
            LOG(ex);
            return null;
        }
        return bytes;
    }

    private static String disassemble(byte[] bytes, EnumSet<GlobalOption> options) {
        var mainoption = MainOption.DISASSEMBLY;
        Global.newGlobal(mainoption);
        ADD_OPTIONS(options);
        var main = mainoption.mainOptionService();
        return main.callToString(bytes);        
    }
}
