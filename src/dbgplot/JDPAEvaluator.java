/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgplot;

import static dbgplot.DebugPlotPrint.printThrowable;
import dbgplot.evaluator.spi.Evaluator;
import dbgplot.evaluator.spi.Returner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ProgressMonitor;
import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.jpda.JPDADebugger;
import org.netbeans.api.debugger.jpda.ObjectVariable;
import org.netbeans.api.debugger.jpda.Variable;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Will Shackleford<wshackle@gmail.com>
 */
@ServiceProvider(service = Evaluator.class)
public class JDPAEvaluator implements Evaluator {

    
    private JPDADebugger getDebugger() {
        try {
            final DebuggerManager dm = DebuggerManager.getDebuggerManager();
            if (null == dm) {
                System.err.println("DebuggerManager.getDebuggerManager() == null");
                return null;
            }
            DebuggerEngine currentEngine = dm.getCurrentEngine();
            if (currentEngine == null) {
                System.err.println("currentEngine == null");
                return null;
            }
            final JPDADebugger d = currentEngine.lookupFirst(null, JPDADebugger.class);
            if (d == null) {
                System.err.println("JPDADebugger == null");
                return null;
            }
            return d;
        } catch (Exception e) {
            printThrowable(e);
        }
        return null;
    }

    private static String cleanName(String input) {
        input = input.trim();
        while (input.startsWith("\"")) {
            input = input.substring(1).trim();
        }
        while (input.endsWith("\"")) {
            input = input.substring(0, input.length() - 1).trim();
        }
        return input;
    }

    @Override
    public void evaluate(
            final String expr,
            final String mapper,
            ProgressMonitor pm,
            Returner r,
            boolean showGetters) {
        try {
            final JPDADebugger d = this.getDebugger();
            if (null == d) {
                r.returnResult(null);
                return;
            }

            Variable ov = d.evaluate(expr);
            if (ov.getValue().equals("null")) {
                r.returnResult(null);
                return;
            }
            //System.out.println("v = " + v);
            //System.out.println(v.getType());
            //System.out.println(v.getValue());
            final Object mirror = ov.createMirrorObject();
            //System.out.println("mirror = " + mirror);
            if (mirror != null && (mapper == null || mapper.length() < 1)) {
                r.returnResult(mirror);
                return;
            }
            final List<Object> fakeMirror = new ArrayList<Object>();
//            org.netbeans.api.debugger.jpda.Field ovfa[] = ov.getFields(0, ov.getFieldsCount());
//            System.out.println("ovfa = " + ovfa);
            final boolean is_array = ov.getType().endsWith("[]");
            final int n = is_array
                    ? Integer.valueOf(d.evaluate(expr + ".length").getValue())
                    : Integer.valueOf(d.evaluate(expr + ".size()").getValue());
            List<String> mapperparts = null;
            if (mapper != null && mapper.length() > 1) {
                mapperparts = new ArrayList<String>();
                String part = "";
                for (int ci = 0; ci < mapper.length(); ci++) {
                    char c = mapper.charAt(ci);
                    if (c == '_'
                            && (ci == 0 || !Character.isJavaIdentifierPart(mapper.charAt(ci - 1)))
                            && (ci == mapper.length() - 1 || !Character.isJavaIdentifierPart(mapper.charAt(ci + 1)))) {
                        mapperparts.add(part);
                        part = "";
                    } else {
                        part += mapper.substring(ci, ci + 1);
                    }
                }
                if (part.length() > 0) {
                    mapperparts.add(part);
                }
            }
            //System.out.println("n = " + n);
//            org.netbeans.api.debugger.jpda.Field elementData = ov.getField("elementData");
//            Object o = elementData.createMirrorObject();
            pm.setMaximum(n);
            for (int i = 0; i < n; i++) {
                pm.setProgress(i);
                if (pm.isCanceled()) {
                    r.returnResult(null);
                    return;
                }
                final String pre_mapped_elem_name
                        = is_array
                                ? expr + "[" + i + "]"
                                : expr + ".get(" + i + ")";
                String mapped_elem_name = pre_mapped_elem_name;
                if (mapperparts != null) {
                    mapped_elem_name
                            = mapperparts.size() < 2
                                    ? pre_mapped_elem_name + mapperparts.get(0)
                                    : mapperparts.get(0);
                    for (int mpi = 1; mpi < mapperparts.size(); mpi++) {
                        mapped_elem_name
                                += pre_mapped_elem_name
                                + mapperparts.get(mpi);
                    }
                }
                final Variable elem_v = d.evaluate(mapped_elem_name);
                if (elem_v.getValue().equals("null")) {
                    fakeMirror.add(null);
                    continue;
                }
                Object elem_mirror = elem_v.createMirrorObject();
                if (elem_mirror != null) {
                    fakeMirror.add(elem_mirror);
                    continue;
                }
//                final String fn_expr = expr + ".get(" + i + ").getClass().getFields().length";
//                final int fn = Integer.valueOf(d.evaluate(fn_expr).getValue());
                ObjectVariable elem_ov = null;
                if (elem_v instanceof ObjectVariable) {
                    elem_ov = (ObjectVariable) elem_v;
                }
                final int fn = elem_ov.getFieldsCount();
                final Map<String, Object> map = new HashMap<String, Object>();
                org.netbeans.api.debugger.jpda.Field fa[] = elem_ov.getFields(0, fn);
                for (int j = 0; j < fa.length; j++) {
                    try {
                        final String name = fa[j].getName();
//                    final String name_expr = expr + ".get(" + i + ").getClass().getFields()[" + j + "].getName()";
//                    final String name = cleanName(d.evaluate(name_expr).getValue());
                        //System.out.println("name = " + name);
                        final Double D = Double.valueOf(fa[j].getValue());
//                    final Variable fv = d.evaluate(expr + ".get(" + i + ")." + name);
//                    //System.out.println("fv = " + fv);
//                    final Object eov = fv.createMirrorObject();
                        //System.out.println("ov = " + ov);
                        map.put(name, D);
                    } catch (Exception exception) {
                        // ignore
                    }
                }
                org.netbeans.api.debugger.jpda.Field ifa[] = elem_ov.getInheritedFields(0, 100);
                for (int j = 0; j < ifa.length; j++) {
                    try {
                        final String name = ifa[j].getName();
//                    final String name_expr = expr + ".get(" + i + ").getClass().getFields()[" + j + "].getName()";
//                    final String name = cleanName(d.evaluate(name_expr).getValue());
                        //System.out.println("name = " + name);
                        final Double D = Double.valueOf(ifa[j].getValue());
//                    final Variable fv = d.evaluate(expr + ".get(" + i + ")." + name);
//                    //System.out.println("fv = " + fv);
//                    final Object eov = fv.createMirrorObject();
                        //System.out.println("ov = " + ov);
                        map.put(name, D);
                    } catch (Exception exception) {
                        // ignore
                    }
                }
                if (showGetters) {
                    final String mn_expr = mapped_elem_name + ".getClass().getMethods().length";
                    final int mn = Integer.valueOf(d.evaluate(mn_expr).getValue());
                    for (int k = 0; k < mn; k++) {
                        final String name_expr = mapped_elem_name + ".getClass().getMethods()[" + k + "].getName()";
                        final String name = cleanName(d.evaluate(name_expr).getValue());
                        //System.out.println("name = " + name);
                        String propName = null;
                        if (name.startsWith("get") && name.length() > 3) {
                            propName = name.substring(3, 4).toLowerCase()
                                    + (name.length() > 4
                                            ? name.substring(4)
                                            : "");
                        } else if (name.startsWith("is") && name.length() > 2) {
                            propName = name.substring(2, 3).toLowerCase()
                                    + (name.length() > 3
                                            ? name.substring(3)
                                            : "");
                        }
                        if (null == propName) {
                            continue;
                        }
                        if (map.containsKey(propName)) {
                            continue;
                        }
                        final String param_count_expr
                                = expr + ".get(" + i + ").getClass().getMethods()[" + k + "].getParameterTypes().length";
                        final int param_count = Integer.valueOf(d.evaluate(param_count_expr).getValue());
                        if (param_count != 0) {
                            continue;
                        }
                        //System.out.println("name = " + name);
                        final Variable return_fv = d.evaluate(mapped_elem_name + "." + name + "()");
                        //System.out.println("fv = " + fv);
                        final Object return_ov = return_fv.createMirrorObject();
                        //System.out.println("ov = " + ov);
                        map.put(propName, return_ov);
                    }
                }
                fakeMirror.add(map);
            }
            //System.out.println("fakeMirror = " + fakeMirror);
            r.returnResult(fakeMirror);
            return;
        } catch (Exception exception) {
            printThrowable(exception);
        }
        r.returnResult(null);
        return;
    }

    @Override
    public boolean isValid() {
        return this.getDebugger() != null;
    }
}
