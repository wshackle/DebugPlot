/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbgplot;

import static dbgplot.DebugPlotPrint.printString;
import dbgplot.ui.PlotterTopComponent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.text.StyledDocument;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.text.NbDocument;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Debug",
        id = "dbgplot.NewPlot"
)
@ActionRegistration(
        iconBase = "dbgplot/ploticon.png",
        displayName = "#CTL_NewPlot"
)
@ActionReference(path = "Menu/RunProject", position = 0)
@Messages("CTL_NewPlot=New Plot")
public final class NewPlot extends AbstractAction {

    public NewPlot() {
//        super((Object) "NewPlot",false);
//        putValue("noIconInMenu", Boolean.TRUE);
    }


    // The next few functions were shamelessly copied from
    // org.netbeans.modules.debugger.ui.Utils.java
    // to implement getIdentifier()
    // The module they are in does not export these, so the copy
    // here prevents version changes from breaking this code.
    private static String getIdentifier() {
        EditorCookie e = getCurrentEditorCookie();
        if (e == null) {
            return null;
        }
        JEditorPane ep = getCurrentEditor(e);
        if (ep == null) {
            return null;
        }
        return getIdentifier(
                e.getDocument(),
                ep,
                ep.getCaret().getDot()
        );
    }

    private static String getIdentifier(
            StyledDocument doc,
            JEditorPane ep,
            int offset
    ) {
        String t = null;
        if ((ep.getSelectionStart() <= offset)
                && (offset <= ep.getSelectionEnd())) {
            t = ep.getSelectedText();
        }
        if (t != null) {
            return t;
        }

        int line = NbDocument.findLineNumber(
                doc,
                offset
        );
        int col = NbDocument.findLineColumn(
                doc,
                offset
        );
        try {
            javax.swing.text.Element lineElem
                    = org.openide.text.NbDocument.findLineRootElement(doc).
                    getElement(line);

            if (lineElem == null) {
                return null;
            }
            int lineStartOffset = lineElem.getStartOffset();
            int lineLen = lineElem.getEndOffset() - lineStartOffset;
            t = doc.getText(lineStartOffset, lineLen);
            int identStart = col;
            while (identStart > 0
                    && (Character.isJavaIdentifierPart(
                            t.charAt(identStart - 1)
                    )
                    || (t.charAt(identStart - 1) == '.'))) {
                identStart--;
            }
            int identEnd = col;
            while (identEnd < lineLen
                    && Character.isJavaIdentifierPart(t.charAt(identEnd))) {
                identEnd++;
            }

            if (identStart == identEnd) {
                return null;
            }
            return t.substring(identStart, identEnd);
        } catch (javax.swing.text.BadLocationException e) {
            return null;
        }
    }

    /**
     * Returns current editor component instance.
     *
     * @return current editor component instance
     */
    private static JEditorPane getCurrentEditor(EditorCookie e) {
        JEditorPane[] op = e.getOpenedPanes();
        if ((op == null) || (op.length < 1)) {
            return null;
        }
        return op[0];
    }

    /**
     * Returns current editor component instance.
     *
     * @return current editor component instance
     */
    private static EditorCookie getCurrentEditorCookie() {
        Node[] nodes = TopComponent.getRegistry().getActivatedNodes();
        if ((nodes == null) || (nodes.length != 1)) {
            return null;
        }
        Node node = nodes[0];
        DataObject dob = node.getLookup().lookup(DataObject.class);
        if (dob != null && !dob.isValid()) {
            return null;
        }
        return node.getLookup().lookup(EditorCookie.class);
    }

    private void plotCurrentId() {
        String id = getIdentifier();
        printString("id = " + id);
        PlotterTopComponent tc = new PlotterTopComponent();
        if (null != id && id.length() > 0) {
            tc.setDisplayName(tc.getDisplayName() + " " + id);
        }
        tc.open();
        tc.requestActive();
        tc.evaluateAndPlot(id, null);
    }

//    @Override
//    protected boolean asynchronous() {
//        return false;
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.plotCurrentId();
    }

//    @Override
//    public void performAction() {
//        this.plotCurrentId();
//    }
//
//    @Override
//    public String getName() {
//        return NbBundle.getMessage(
//                NewPlot.class,
//                "CTL_NewPlot"
//        );
//    }
//
//    @Override
//    public HelpCtx getHelpCtx() {
//        return new HelpCtx(NewPlot.class);
//    }
}
