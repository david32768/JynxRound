package com.github.david32768.jynxround.my;

import static com.github.david32768.jynxfree.jynx.LogMsgType.*;

import com.github.david32768.jynxfree.jynx.JynxMessage;
import com.github.david32768.jynxfree.jynx.LogMsgType;

public enum Message implements JynxMessage {

    M87(ERROR,"options %s and %s conflict"),
    M414(WARNING,"difference output exceeds %d lines and has been truncated"),
    M415(BLANK,"string comparison succeeded"),
    M416(ERROR,"string comparison failed"),
    M417("main args size (after removing options) is not %d: main args = %s"),
    M990(BLANK,"%s"),
    ;

    private final LogMsgType logtype;
    private final String format;
    private final String msg;

    private Message(LogMsgType logtype, String format) {
        this.logtype = logtype;
        this.format = logtype.prefix(name()) + format;
        this.msg = format;
    }
    
    
    private Message(String format) {
        this(ERROR,format);
    }

    @Override
    public String format(Object... objs) {
        return String.format(format,objs);
    }

    @Override
    public String getFormat() {
        return msg;
    }
    
    @Override
    public LogMsgType getLogtype() {
        return logtype;
    }
    
}
