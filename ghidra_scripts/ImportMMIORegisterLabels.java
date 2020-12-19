// Create a label for each MMIO register in a namespace called "registers".
//@category ghidra-emotionengine

import java.util.List;

import ghidra.app.script.GhidraScript;
import ghidra.program.model.address.AddressSpace;
import ghidra.program.model.lang.Register;
import ghidra.program.model.symbol.Namespace;
import ghidra.program.model.symbol.SourceType;
import ghidra.program.model.symbol.Symbol;

public class ImportMMIORegisterLabels extends GhidraScript {

    public void run() throws Exception {
        Namespace registerNamespace = getNamespace(null, "registers");
        if(registerNamespace == null) {
            registerNamespace = currentProgram.getSymbolTable().createNameSpace(
                currentProgram.getGlobalNamespace(),
                "registers",
                SourceType.USER_DEFINED
            );
        }
        
        AddressSpace ram = currentProgram.getAddressFactory().getAddressSpace("ram");
        List<Register> registers = currentProgram.getLanguage().getRegisters();
        for(Register register : registers) {
            if(register.getAddressSpace() == ram) {
                Symbol[] oldSymbols = currentProgram.getSymbolTable().getSymbols(register.getAddress());
                for(Symbol symbol : oldSymbols) {
                    symbol.delete();
                }
                
                currentProgram.getSymbolTable().createLabel(
                    register.getAddress(),
                    register.getName(),
                    registerNamespace,
                    SourceType.USER_DEFINED
                );
            }
        }
    }

}
