package org.bluenautilus.gui;

import org.bluenautilus.data.SqlScriptFile;
import org.bluenautilus.script.PopOutScriptEvent;
import org.bluenautilus.script.ScriptPopOutEventListener;
import org.bluenautilus.script.ScriptType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: bluenautilus2
 * Date: 7/28/13
 * Time: 10:44 PM
 */
public class ScriptViewPanel extends ParentTextPanel {

	private JButton entireScriptButton = new JButton("Entire Script");
	private JButton rollbackScriptButton = new JButton("Rollback Script");

	private ArrayList<ScriptPopOutEventListener> popOutListeners = new ArrayList<ScriptPopOutEventListener>();

    public ScriptViewPanel() {
        super();
        clearText();

		this.add(this.getButtonPanel(), BorderLayout.SOUTH);
    }

    public void setText(SqlScriptFile theFile) throws IOException {
        textArea.setText("");

        InputStream fis;
        BufferedReader reader = null;
        String line;

        try {
            fis = new FileInputStream(theFile.getTheFile());
            reader = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));

            int count = 0;
            while (((line = reader.readLine()) != null) && count < 100) {
                textArea.append(line + "\n");
                count++;
            }
            if(count==100){
                textArea.append("\n*Script Truncated at 100 lines for GUI performance* \n");
				textArea.append("\n Click on \'Entire Script\' button to see the whole script --Beth\n");
            }
        } finally {
            if (null != reader) {
                reader.close();
            }
            reader = null;
            fis = null;
        }
        textArea.setCaretPosition(0);

    }

    public void clearText() {
		char[] ch = new char[48];
		Arrays.fill(ch, ' ');
		String fill = new String(ch);
        textArea.setText("Click a DB row to see the script" + fill);
    }

	private JPanel getButtonPanel() {

		this.entireScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ScriptPopOutEventListener listener : popOutListeners) {
					listener.popOutAScript(new PopOutScriptEvent(ScriptType.REGULAR));
				}
			}
		});

		this.rollbackScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (ScriptPopOutEventListener listener : popOutListeners) {
					listener.popOutAScript(new PopOutScriptEvent(ScriptType.ROLLBACK));
				}
			}
		});

		JPanel thispanel = new JPanel(new GridBagLayout());

		//int gridx, int gridy,int gridwidth, int gridheight,
		//double weightx, double weighty,
		// int anchor, int fill,
		//Insets insets, int ipadx, int ipady

		thispanel.add(this.entireScriptButton, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(4, 4, 4, 4), 2, 2));

		thispanel.add(this.rollbackScriptButton, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(4, 4, 4, 4), 2, 2));

		return thispanel;
	}

	public void addPopOutListener(ScriptPopOutEventListener listener){
		this.popOutListeners.add(listener);
	}

}
