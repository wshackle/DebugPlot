DebugPlot
=========

A netbeans plugin for plotting variables while debugging.

To install from source:

        1) Open directory as a project in Netbeans.
        2) Right-click on the project in the Projects window and choose
            "Install/Reload in Development IDE"

To use:

	1) Start a debugging session
	2) Select the menu option Window/Debugging/Plot
	3) Paste and enter the name of a variable that is into the textfield at the bottom.
		
The variable must be in scope and be an array or List<?>.
The elements in the array need to either be convertible to double  or have a field that is convertable to double.
It may be helpful to resize. 
One can zoom in by dragging a selection rectangle in the plot panel.
After zooming use the fit button to see the full plot again.
If multiple variables are plotted the split button toggles between having
the plots overlaid on each other or split into vertically stacked panels.
It has only been tested when debugging Java. It might work when debugging 
other languages but only if that debugger was implemented via JDPA Debugger.
The map text field optionally modifies each element of the array or list before 
plotting. If no "_" variable occurs in the string the string is appended to the element.

Examples
=======

Map String | Effect
---------- | ---------
    .pz | just get pz element of objects in list

    + 5 | add 5 to every element in list

    Math.abs(_) | take the absolute value of each element in the list

    < 0.5 | plot 1.0 for values less than 0.5 and 0.0 otherwise

    _ < 0.5 ? _ : null | plot the original value if it less than 0.5 otherwise skip




There is an extension point by adding an Evaluator service so that other modules 
could add support for other languages/debuggers.

