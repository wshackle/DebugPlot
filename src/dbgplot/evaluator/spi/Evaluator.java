/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgplot.evaluator.spi;

import javax.swing.ProgressMonitor;

/**
 *
 * @author Will Shackleford<wshackle@gmail.com>
 */
public interface Evaluator {
    
    public void evaluate(String expr, 
            String mapper,
            ProgressMonitor pm, 
            Returner r,
            boolean showGetters);
    
    public boolean isValid();
}
