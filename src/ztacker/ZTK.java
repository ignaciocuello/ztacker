package ztacker;

import java.awt.AWTException;
import java.awt.Toolkit;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import ztacker.chooser.MoveChooser;
import ztacker.chooser.pf.PlayingForeverMoveChooser;
import ztacker.chooser.zt.ZTStackingMoveChooser;
import ztacker.display.DisplayPanel;

import ztacker.environment.MainEnvironment;
import ztacker.framework.Dynamic;
import ztacker.framework.MainFrameWork;
import ztacker.robot.in.GridCapturerDisplay;
import ztacker.robot.in.InputWrapper;
import ztacker.robot.in.modes.SprintInputWrapper;
import ztacker.robot.in.modes.UltraInputWrapper;

public final class ZTK {

    private static final String TITLE = "ZTK 0.5.2";
    
    public static void main(String[] args) throws AWTException,
            FileNotFoundException {        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame(TITLE);
        frame.setSize(GridCapturerDisplay.WIDTH, GridCapturerDisplay.HEIGHT);
        frame.setLocation(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()
                - frame.getWidth()), 0);

        InputWrapper mode = promptSelectMode(frame);
        MoveChooser playstyle = promptSelectPlaystyle(frame);

        DisplayPanel display = new DisplayPanel();
        Dynamic environment = new MainEnvironment(mode, playstyle, display);

        MainFrameWork frameWork = new MainFrameWork(environment, display);

        frame.add(display);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setVisible(true);

        frameWork.start();
    }

    /**
     * Prompts the user to choose the game mode that they are going to be playing on.
     * Game modes currently supported are "Ultra" and "Sprint". The user needs to choose
     * a game mode because the grid UI is different for different game modes, which alters
     * how ztacker analyzes the game window. 
     * @param frame the main frame of the application 
     * @return an <tt>InputWrapper</tt> object corresponding to the game mode chosen
     */
    private static InputWrapper promptSelectMode(JFrame frame) {
        //only allow user to choose sprint or ultra, as these are the
    	//only modes the bot supports on Nullpomino
    	InputWrapper[] modes = {
            new SprintInputWrapper(),
            new UltraInputWrapper()
        };
    	
        InputWrapper selectedMode = (InputWrapper) JOptionPane.showInputDialog(
                frame,
                "Select a game mode", TITLE,
                JOptionPane.INFORMATION_MESSAGE, null,
                modes, modes[modes.length - 1]);
        
        return selectedMode;
    }

    private static MoveChooser promptSelectPlaystyle(JFrame frame) {
        MoveChooser[] playstyles = {
            new PlayingForeverMoveChooser(),
            new ZTStackingMoveChooser()
        };
        MoveChooser selectedPlaystyle = (MoveChooser) JOptionPane.showInputDialog(
                frame,
                "Select a playstyle", TITLE,
                JOptionPane.INFORMATION_MESSAGE, null,
                playstyles, playstyles[playstyles.length - 1]);
        return selectedPlaystyle;
    }
}
