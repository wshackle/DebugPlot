/* 
The NIST RCS (Real-time Control Systems) 
 library is public domain software, however it is preferred
 that the following disclaimers be attached.

Software Copywrite/Warranty Disclaimer

   This software was developed at the National Institute of Standards and
Technology by employees of the Federal Government in the course of their
official duties. Pursuant to title 17 Section 105 of the United States
Code this software is not subject to copyright protection and is in the
public domain. NIST Real-Time Control System software is an experimental
system. NIST assumes no responsibility whatsoever for its use by other
parties, and makes no guarantees, expressed or implied, about its
quality, reliability, or any other characteristic. We would appreciate
acknowledgement if the software is used. This software can be
redistributed and/or modified freely provided that any derivative works
bear some notice that they are derived from it, and any modified
versions bear some notice that they have been modified.



*/ 


package dbgplot.utils;

/**
 * Common interface to the Swing and AWT Panels for displaying progress while loading
 * a URL/File.
 * @author Will Shackleford <shackle@nist.gov>
 */
public interface URLLoadInfoPanelInterface
{

    /**
     *
     * @param _bytes_read
     * @throws Exception
     */
    public void set_bytes_read(int _bytes_read) throws Exception;

    /**
     *
     * @param _bytes_read_inc
     * @throws Exception
     */
    public void inc_bytes_read(int _bytes_read_inc) throws Exception;

    /**
     *
     * @return
     */
    public int get_bytes_read();

    /**
     *
     * @param _content_length
     */
    public void set_content_length(int _content_length);

    /**
     *
     * @return
     */
    public int get_content_length();

    /**
     *
     * @param _URLName
     */
    public void set_URLname(String _URLName);

    /**
     *
     * @return
     */
    public String get_URLname();

    /**
     *
     */
    public void updateDisplay();

    /**
     *
     * @param i
     */
    public void force_repaint(int i);

    /**
     *
     */
    public void repaint();
}
