/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgplot;

//import javax.swing.ProgressMonitor;

import dbgplot.evaluator.spi.Evaluator;
import dbgplot.evaluator.spi.Returner;
import javax.swing.ProgressMonitor;
import org.openide.util.lookup.ServiceProvider;

//import org.netbeans.api.debugger.DebuggerEngine;
//import org.netbeans.api.debugger.DebuggerManager;
//import org.netbeans.modules.cnd.debugger.gdb2.GdbDebuggerImpl;
//import org.netbeans.modules.cnd.debugger.gdb2.mi.MICommand;
//import org.netbeans.modules.cnd.debugger.gdb2.mi.MIRecord;
//import org.netbeans.modules.cnd.debugger.gdb2.mi.MIUserInteraction;

/**
 *
 * @author Will Shackleford<wshackle@gmail.com>
 */
@ServiceProvider(service = Evaluator.class)
public class CppGdbEvaluator implements Evaluator {

    @Override
    public void evaluate(String expr, 
            final String mapper, 
            ProgressMonitor pm, 
            final Returner r,
            boolean showGetters) {
        throw new java.lang.UnknownError("C++ / GDB still not supported.");
//        try {
//            final DebuggerManager dm = DebuggerManager.getDebuggerManager();
//            if (null == dm) {
//                System.err.println("DebuggerManager.getDebuggerManager() == null");
//                r.returnResult(null);
//                return;
//            }
//            DebuggerEngine currentEngine = dm.getCurrentEngine();
//            if (currentEngine == null) {
//                System.err.println("currentEngine == null");
//                r.returnResult(null);
//                return;
//            }
//            final GdbDebuggerImpl d = currentEngine.lookupFirst(null, GdbDebuggerImpl.class);
//            if (d == null) {
//                System.err.println("JPDADebugger == null");
//                r.returnResult(null);
//                return;
//            }
//            MICommand cmd = new MICommand(0, expr) {
//                @Override
//                protected void onDone(MIRecord record) {
//                    final String res;
//                    if (!record.isError()) {
//                        res = record.results().getConstValue("value"); //NOI18N
//                    } else {
//                        res = record.error();
//                    }
//                    r.returnResult(res);
//                }
//
//                @Override
//                protected void onRunning(MIRecord mir) {
//                }
//
//                @Override
//                protected void onError(MIRecord mir) {
//                }
//
//                @Override
//                protected void onExit(MIRecord mir) {
//                }
//
//                @Override
//                protected void onStopped(MIRecord mir) {
//                }
//
//                @Override
//                protected void onOther(MIRecord mir) {
//                }
//
//                @Override
//                protected void onUserInteraction(MIUserInteraction miui) {
//                }
//            };
//            
//            Gdb gdb = d.gdb();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return;

    }

    @Override
    public boolean isValid() {
        // TODO: implement this function
        return (boolean) false;
    }

}
