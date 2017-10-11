/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twentyfour3.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author nicolo
 */
public class Menu {

    //intro menu
    public JFrame menu, playerVsPlayerFrame;
    public JButton playerVsPlayer, playerVsComputer;
    public JTextArea introText, namesChosen;
    public JTextField numText, nameText;
    public JPanel enterPanel, vsPanel;

    // integers
    public int playersNum;
    int i = 0;

    //array lists
    public ArrayList<String> playerNames;

    public Menu() {
        //Generating menu window
        menu = new JFrame("menu");
        menu.setSize(400, 200);

        introText = new JTextArea("Choose a game option");
        introText.setEnabled(false);
        introText.setFont(new Font("default", Font.BOLD, 20));

        vsPanel = new JPanel();
        vsPanel.setLayout(new BoxLayout(vsPanel, BoxLayout.Y_AXIS));

        playerVsPlayer = new JButton("Player vs Player");
        playerVsPlayer.addActionListener(new playerVsPlayerListener());
        playerVsComputer = new JButton("Player vs Computer (not supported yet)");

        vsPanel.add(playerVsPlayer);
        vsPanel.add(playerVsComputer);

        menu.add(BorderLayout.NORTH, introText);
        menu.add(BorderLayout.CENTER, vsPanel);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public class playerVsPlayerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.dispatchEvent(new WindowEvent(menu, WindowEvent.WINDOW_CLOSING));

            playerVsPlayerFrame = new JFrame("Player Vs Player");
            playerVsPlayerFrame.setSize(500, 200);

            numText = new JTextField("How many players are there (min 2)?\n"
                    + "Hit enter to confirm");
            numText.addActionListener(new enterNumList());
            nameText = new JTextField("Insert one name at the time.\n"
                    + "To save it hit enter");
            nameText.addActionListener(new enterNameList());
            nameText.addFocusListener(new nameFocusList());

            enterPanel = new JPanel();
            enterPanel.setLayout(new BoxLayout(enterPanel, BoxLayout.Y_AXIS));
            enterPanel.add(numText);
            enterPanel.add(nameText);

            namesChosen = new JTextArea();

            playerVsPlayerFrame.add(BorderLayout.CENTER, enterPanel);
            playerVsPlayerFrame.add(BorderLayout.EAST, namesChosen);

            playerVsPlayerFrame.setVisible(true);

        }

    }
    
    public class nameFocusList implements FocusListener
    {

        @Override
        public void focusGained(FocusEvent e) {
            nameText.setText("");
        }

        @Override
        public void focusLost(FocusEvent e) {
            nameText.setText("Insert one name at the time.\n"
                    + "To save it hit enter");
        }
        
    }

    public class enterNameList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (nameText.getText().isEmpty()) {
                nameText.setText("Empty string input!");
                nameText.setBackground(Color.red);
                return;

            } else {
                nameText.setBackground(Color.green);

                i++;
                playerNames.add(nameText.getText());
                namesChosen.append(i + ") " + nameText.getText() + "\n");
                nameText.setText("");
                
                if (i == playersNum) {
                    GUI gui = new GUI(playersNum, playerNames);
                    playerVsPlayerFrame.dispatchEvent(new WindowEvent(playerVsPlayerFrame, WindowEvent.WINDOW_CLOSING));

                }
            }
            nameText.setBackground(Color.white);

        }

    }

    public class enterNumList implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
               if( (playersNum = Integer.parseInt(numText.getText()))<2)
                   throw new NumberFormatException();
                numText.setBackground(Color.white);
                playerNames = new ArrayList<>();
            } catch (NumberFormatException ex) {
                numText.setText("Invalid number input!");
                numText.setBackground(Color.red);
                return;
            }
            numText.setBackground(Color.green);

        }

    }

}
