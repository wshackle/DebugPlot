/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgplot;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 *
 * @author Will Shackleford<wshackle@gmail.com>
 */
public class DebugPlotPrint {

    public static void printString(String s) {
        InputOutput io = IOProvider.getDefault().getIO("DebugPlot", false);
//        io.select();
        OutputWriter writer = io.getOut();
        writer.println(s);
        writer.close();
        System.err.println(s);
    }

    public static void printThrowable(Throwable t) {
        InputOutput io = IOProvider.getDefault().getIO("DebugPlot", false);
        io.select();
        OutputWriter writer = io.getOut();
        t.printStackTrace(writer);
        writer.close();
        t.printStackTrace();
    }
}
