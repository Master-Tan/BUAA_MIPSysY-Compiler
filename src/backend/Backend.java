package backend;

import backend.Parser.MipsModule;
import midend.ir.values.Module;

public class Backend {

    public Backend() {
        IRParser irParser = new IRParser();
        irParser.parserModule();
    }

    public String getMIPS() {
        return MipsModule.getInstance().toString();
    }

}
