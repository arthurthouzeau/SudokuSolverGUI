package gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements how to display a delayed busy cursor.
 */
public class CursorController {
    public static final Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
    public static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    public static final int delay = 500; // in milliseconds
    
    /**
     * Constructor
     */
    private CursorController() {}
    
    /**
     * Accepts an ActionListener as input and returns a modified ActionListener
     * as its output. The modified ActionListener takes care of cursor control,
     * but also performs the same action processing as the original ActionListener.
     * 
     * @param component the component on which to set the busy cursor
     * @param mainActionListener the ActionListener to modify
     * @return the modified ActionListener now taking care of cursor control
     */
    public static ActionListener createListener(final Component component, final ActionListener mainActionListener) {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                
                TimerTask timerTask = new TimerTask() {
                    public void run() {
                        component.setCursor(busyCursor);
                    }
                };
                Timer timer = new Timer(); 
                
                try {   
                    timer.schedule(timerTask, delay);
                    mainActionListener.actionPerformed(ae);
                } finally {
                    timer.cancel();
                    component.setCursor(defaultCursor);
                }
            }
        };
        return actionListener;
    }
}