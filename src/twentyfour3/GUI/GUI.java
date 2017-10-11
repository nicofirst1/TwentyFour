/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twentyfour3.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowEvent;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author nicolo
 */
public class GUI extends JFrame {

    Iterator<player> iter;

    //array lists 
    public ArrayList<String> playersName;
    public ArrayList<player> playerList;
    public ArrayList<JButton> dices;
    public ArrayList<JTextArea> values;

    //ints
    public int playersNum = 0;
    public int turn = 0;
    public int resultInt = 0;
    public int isMakingDamageValue = 0;
    public int totalDamage = 0;

    //boolean
    public boolean canRollAgain = true;
    public boolean isMakingDamage = false;

    //strings
    public String gameMode = "rise";
    public String messageString = "";

    //swing components
    //<editor-fold>
    public JFrame app;
    public JProgressBar currentPlayerPf, nextPlayerPf; //player and computer life point
    public JTextArea currentPlayerName, nextPlayerName;
    public JButton dice1, dice2, dice3, dice4, dice5, roll;
    public JTextArea diceChoice1, diceChoice2, diceChoice3, diceChoice4, diceChoice5, result;
    public Color currentPlayerColor = new Color(255, 255, 0);
    public Color nextPlayerColor = new Color(102, 175, 255);
    public Font dicesFont = new Font("default", Font.BOLD, 20);
    public JPanel toRollDiceCont, rolledDiceCont, computerStuffCont, playerStuffCont;

    //intro menu
    public JFrame menu;
    public JButton playerVsPlayer, playerVsComputer;
    public JButton enterNum, enterName;
    public JTextArea introText, numText, nameText, namesChosen;
    public JPanel enterNumPanel, enterNamePanel, enterPanel, vsPanel;

    // messages to be displayed 
    public JTextPane messages;
    public StyledDocument doc;
    public Style damageTaken, damageDone, healingStyle, passingTurn, maybeDamage, normalText;
    public JScrollPane scroll;

    //</editor-fold>
    public GUI(int playerNum1, ArrayList<String> playersName1) {

        //creating player circular list
        //<editor-fold>
        playersName = playersName1;
        playersNum = playerNum1;

        playerList = new ArrayList<>();

        for (int i = 0; i < playersNum; i++) {
            playerList.add(new player(20, playersName.get(i), null, null));
        }

        playerList.get(0).setPrevious(playerList.get(playersNum - 1));
        playerList.get(playersNum - 1).setNext(playerList.get(0));

        for (int i = 0; i < playersNum - 1; i++) {
            playerList.get(i).setNext(playerList.get(i + 1));
            playerList.get(playersNum - 1 - i).setPrevious(playerList.get((playersNum - i - 2)));
        }

        //</editor-fold>
        //now swing components
        {

            // messages text area and scrollpane
            {
                //<editor-fold>
                messages = new JTextPane();
                doc = messages.getStyledDocument();

                scroll = new JScrollPane(messages);
                scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        messages.select(messages.getHeight() + 1000, 0);
                    }
                });

                damageTaken = messages.addStyle("damageTaken", null);
                damageDone = messages.addStyle("damageDone", null);
                healingStyle = messages.addStyle("healing", null);
                passingTurn = messages.addStyle("passingTurn", null);
                maybeDamage = messages.addStyle("maybeDamage", null);
                normalText = messages.addStyle("normalText", null);

                StyleConstants.setForeground(damageTaken, Color.red);
                StyleConstants.setForeground(damageDone, Color.orange);
                StyleConstants.setForeground(healingStyle, Color.green);
                StyleConstants.setForeground(passingTurn, Color.gray);
                StyleConstants.setForeground(maybeDamage, Color.blue);
                StyleConstants.setForeground(normalText, Color.black);
                //</editor-fold>
            }

            //main frame settings
            app = new JFrame("TwentyFour");
            app.setSize(700, 500);
            app.setLayout(new BorderLayout());
            //progress bar initialization
            {
                //<editor-fold>
                currentPlayerPf = new JProgressBar(0, 20);
                currentPlayerPf.setStringPainted(true);
                currentPlayerPf.setBackground(Color.green);
                currentPlayerPf.setValue(20);
                currentPlayerPf.setString("20");

                nextPlayerPf = new JProgressBar(0, 20);
                nextPlayerPf.setStringPainted(true);
                nextPlayerPf.setBackground(Color.green);
                nextPlayerPf.setValue(20);
                nextPlayerPf.setString("20");
                nextPlayerPf.setForeground(Color.GREEN);
            }
            //</editor-fold>

            //player's names
            { //<editor-fold>
                currentPlayerName = new JTextArea(playerList.get(0).name);

                nextPlayerName = new JTextArea(playerList.get(0).next.name);
                //we want to make this not editable
                currentPlayerName.setEnabled(false);
                nextPlayerName.setEnabled(false);

                // let's make them a little cooler
                currentPlayerName.setBackground(currentPlayerColor);
                currentPlayerName.setFont(new Font("default", Font.BOLD, 16));

                nextPlayerName.setBackground(nextPlayerColor);
                nextPlayerName.setFont(new Font("default", Font.BOLD, 16));
            } //</editor-fold>

            //rolled dice setting 
            //<editor-fold>
            {
                diceChoice1 = new JTextArea("0");
                diceChoice2 = new JTextArea("0");
                diceChoice3 = new JTextArea("0");
                diceChoice4 = new JTextArea("0");
                diceChoice5 = new JTextArea("0");
                result = new JTextArea("0");
                diceChoice1.setEnabled(false);
                diceChoice2.setEnabled(false);
                diceChoice3.setEnabled(false);
                diceChoice4.setEnabled(false);
                diceChoice5.setEnabled(false);
                result.setEnabled(false);

                diceChoice1.setFont(dicesFont);
                diceChoice2.setFont(dicesFont);
                diceChoice3.setFont(dicesFont);
                diceChoice4.setFont(dicesFont);
                diceChoice5.setFont(dicesFont);
                result.setFont(dicesFont);
            }
            //</editor-fold>

            //game modes
            // dice and roll
            {
                //<editor-fold>

                dice1 = new JButton("0");
                dice2 = new JButton("0");
                dice3 = new JButton("0");
                dice4 = new JButton("0");
                dice5 = new JButton("0");
                roll = new JButton("Roll");

                dice1.setFont(dicesFont);
                dice2.setFont(dicesFont);
                dice3.setFont(dicesFont);
                dice4.setFont(dicesFont);
                dice5.setFont(dicesFont);
                roll.setFont(dicesFont);

                roll.addActionListener(new rollAction());
                dice1.addActionListener(new dicePicker());
                dice2.addActionListener(new dicePicker());
                dice3.addActionListener(new dicePicker());
                dice4.addActionListener(new dicePicker());
                dice5.addActionListener(new dicePicker());

            }
            //</editor-fold>

            //panels 
            {
                //<editor-fold>

                //using gridLay with 2 rows and 3 cols
                toRollDiceCont = new JPanel(new GridLayout(3, 2));
                //need a gridLayout whit one row and 6 colums
                rolledDiceCont = new JPanel(new GridLayout(6, 1));
                //gridLayout 1 row 2 cols 
                //this two need a lowLayout which is default
                computerStuffCont = new JPanel();
                playerStuffCont = new JPanel();
                computerStuffCont.setBackground(nextPlayerColor);
                playerStuffCont.setBackground(currentPlayerColor);

                dices = new ArrayList<>();
                dices.add(dice1);
                dices.add(dice2);
                dices.add(dice3);
                dices.add(dice4);
                dices.add(dice5);

                values = new ArrayList<>();
                values.add(diceChoice1);
                values.add(diceChoice2);
                values.add(diceChoice3);
                values.add(diceChoice4);
                values.add(diceChoice5);

            }
            //</editor-fold>
        }

        iter = playerList.iterator();

        //putting components in right place
        {//<editor-fold>
            /*starting with computer and player stuff
            we want pf, name and victory to be on top of screen for computer
            contained in panel, on bottom for player*/

            computerStuffCont.add(nextPlayerPf);
            computerStuffCont.add(nextPlayerName);
            playerStuffCont.add(currentPlayerPf);
            playerStuffCont.add(currentPlayerName);

            app.add(BorderLayout.NORTH, computerStuffCont);
            app.add(BorderLayout.SOUTH, playerStuffCont);

            //now passing to rolled dices
            rolledDiceCont.add(diceChoice1);
            rolledDiceCont.add(diceChoice2);
            rolledDiceCont.add(diceChoice3);
            rolledDiceCont.add(diceChoice4);
            rolledDiceCont.add(diceChoice5);
            rolledDiceCont.add(result);

            app.add(BorderLayout.EAST, rolledDiceCont);

            //at last positioning dices and roll buttons in the center
            toRollDiceCont.add(dice1, 0);
            toRollDiceCont.add(dice2, 1);
            toRollDiceCont.add(dice3, 2);
            toRollDiceCont.add(dice4, 3);
            toRollDiceCont.add(dice5, 4);
            toRollDiceCont.add(roll, 5);

            app.add(BorderLayout.CENTER, toRollDiceCont);
            app.add(BorderLayout.WEST, scroll);

            //setting visible and default close action
            app.setVisible(true);
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }//</editor-fold>

        startGame();

    }

    public class dicePicker implements ActionListener {

        @Override
        public synchronized void actionPerformed(ActionEvent e) {
            JButton questo = (JButton) e.getSource();

            //prevent player to select first value zero
            if (questo.getText().equals("0")) {
                return;
            }

            //stop player from choosing incorrect dice
            if (isMakingDamage && !questo.getText().equals(isMakingDamageValue + "")) {
                return;
            }

            //write down dice value on left screen
            values.get(5 - dices.size()).setText(questo.getText());

            //removing selected dice
            questo.setEnabled(false);
            dices.remove(questo);

            //prevent player to roll more than once
            canRollAgain = true;
            roll.setEnabled(canRollAgain);

            //calculatin total
            resultInt += Integer.parseInt(questo.getText());
            result.setText("" + resultInt);

            if (dices.size() < 1) {
                SwingUtilities.invokeLater(new checkResultThread());
            }

        }
    }

    public class checkResultThread implements Runnable {

        @Override
        public void run() {

            checkResult();
        }

    }

    public void checkResult() {

        Integer finalResult = Integer.parseInt(result.getText());

        if (finalResult <= 17) {
            gameMode = "fall";
        } else {
            gameMode = "rise";
        }

        if (finalResult == 10 || finalResult == 24) {
            messageString = playerList.get(turn).name + " passes turn with no damage inflicted\n";
            try {
                doc.insertString(doc.getLength(), messageString, passingTurn);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            pass();
            return;
        }
        if (gameMode.equals("rise")) {
            if (finalResult < 24) //player inflic damage to itself
            {
                int selfDamage = 24 - finalResult;
                playerList.get(turn).subtractPf(selfDamage);
                messageString = playerList.get(turn).name + " take " + selfDamage + " damage, "
                        + "now at " + playerList.get(turn).pf + " pf\n";
                try {
                    doc.insertString(doc.getLength(), messageString, damageTaken);
                } catch (BadLocationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                pass();

            } else if (finalResult > 24)//player is making damage to next player
            {
                int damage = finalResult - 24;
                messageString = playerList.get(turn).name + " tries make " + damage + " damage to "
                        + "" + playerList.get(turn).next.name + "\n";
                try {
                    doc.insertString(doc.getLength(), messageString, maybeDamage);
                } catch (BadLocationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                isMakingDamage = true;
                isMakingDamageValue = damage;
                reset();

            }
        } else if (playerList.get(turn).pf <= 10 && finalResult < 10)//player is healing
        {
            int healing = (10 - finalResult) * 2;
            playerList.get(turn).addPf(healing);

            messageString = playerList.get(turn).name + " heals " + healing + " pf, "
                    + "now at " + playerList.get(turn).pf + "\n";
            try {
                doc.insertString(doc.getLength(), messageString, healingStyle);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            pass();

        } else if (finalResult > 10)// player inlifts damage to itself
        {
            int selfDamage = finalResult - 10;
            playerList.get(turn).subtractPf(selfDamage);
            messageString = playerList.get(turn).name + " takes " + selfDamage + " damage, "
                    + "now at " + playerList.get(turn).pf + " pf\n";
            try {
                doc.insertString(doc.getLength(), messageString, damageTaken);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            pass();

        } else {
            messageString = playerList.get(turn).name + " passes turn with no damage inflicted\n";
            try {
                doc.insertString(doc.getLength(), messageString, passingTurn);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            pass();
        }

    }

    public void makingDamage() {
        boolean shouldPass = true;

        for (JButton elem : dices) {
            if (elem.getText().equals(isMakingDamageValue + "")) {
                elem.setForeground(Color.green);
                shouldPass = false;
                totalDamage += Integer.parseInt(elem.getText());

                messageString = playerList.get(turn).name + " makes another " + elem.getText() + " damage to "
                        + playerList.get(turn).next.name + ", total is:" + totalDamage + "\n"
                        + playerList.get(turn).next.name + " life is now " + (playerList.get(turn).next.pf - totalDamage) + "\n";
                try {
                    doc.insertString(doc.getLength(), messageString, damageDone);
                } catch (BadLocationException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (dices.isEmpty()) {
                    reset();
                    isMakingDamage = true;
                }
            } else {
                elem.setForeground(Color.red);
            }
        }

        if (shouldPass) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {

                    messageString = "...sorry " + playerList.get(turn).name + ", no luck\n";
                    try {
                        doc.insertString(doc.getLength(), messageString, normalText);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    roll.setText("End Turn");
                    roll.setEnabled(true);
                    turn = (turn + 1) % playerList.size();
                    pass();
                }
            }
            );
        }

    }

    public void refresh() {

        //setting write pf and name
        currentPlayerPf.setValue(playerList.get(turn).pf);
        currentPlayerPf.setString(playerList.get(turn).pf + "");
        currentPlayerName.setText(playerList.get(turn).name);

        nextPlayerPf.setValue(playerList.get(turn).next.pf);
        nextPlayerPf.setString(playerList.get(turn).next.pf + "");
        nextPlayerName.setText(playerList.get(turn).next.name);

        if (isMakingDamage) {
            roll.setText("Kill Him!");
        } else {
            roll.setText("End Turn");
        }
        app.revalidate();

    }

    public void reset() {

        //stating game mode with rise
        gameMode = "rise";

        //adding, enabling and setting to zero dices, also setting foregroud to original
        dices.removeAll(dices);

        dices.add(dice1);
        dices.add(dice2);
        dices.add(dice3);
        dices.add(dice4);
        dices.add(dice5);

        dice1.setEnabled(true);
        dice2.setEnabled(true);
        dice3.setEnabled(true);
        dice4.setEnabled(true);
        dice5.setEnabled(true);

        dice1.setText("0");
        dice2.setText("0");
        dice3.setText("0");
        dice4.setText("0");
        dice5.setText("0");

        for (JButton elem : dices) {
            elem.setForeground(Color.BLACK);
        }
        //setting dices chosed and total to zero
        diceChoice1.setText("0");
        diceChoice2.setText("0");
        diceChoice3.setText("0");
        diceChoice4.setText("0");
        diceChoice5.setText("0");
        result.setText("0");
        resultInt = 0;

        //resetting roll button
        roll.setEnabled(true);
        if (isMakingDamage) {
            roll.setText("KILL HIM!");
        } else {
            roll.setText("Roll");
        }
        canRollAgain = true;

    }

    public class rollAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if (roll.getText().equals("Quit")) {
                app.dispatchEvent(new WindowEvent(app, WindowEvent.WINDOW_CLOSING));

            }

            if (roll.getText().equals("End Turn")) {
                reset();
                return;
            }

            if (canRollAgain) {

                for (JButton elem : dices) {
                    elem.setText(1 + (int) (Math.random() * 6) + "");

                }
                canRollAgain = false;
                roll.setEnabled(canRollAgain);
            }
            if (isMakingDamage) {
                makingDamage();
            }

        }

    }

    public final void startGame() {

        messageString = "Welcome to TwentyFour Game\n"
                + "First is " + playerList.get(turn).name + " "
                + "attacking " + playerList.get(turn).next.name + "\n";
        try {
            doc.insertString(doc.getLength(), messageString, normalText);
        } catch (BadLocationException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        app.setVisible(true);

    }

    public boolean linking(player deadP) {
        player first, second;

        first = deadP.previous;
        second = deadP.next;

        first.next = second;
        second.previous = first;

        playerList.remove(deadP);
        if (playerList.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void pass() {

        //current palyer is damaging next palyer
        {//<editor-fold>
            while (totalDamage > 0) {

                //if next player can survive damage just take it
                if (playerList.get(turn).next.pf > totalDamage) {
                    playerList.get(turn).next.subtractPf(totalDamage);
                    totalDamage = 0;

                } else if (playerList.size() > 1) {
                    /*If nextP can't support total damage we want this one to be subtracted
                to nextP life point, then the difference will be subracted to nextNextp
                and so on until dmage is zero*/

                    //subtract nextPlayer pf from total damage
                    totalDamage -= playerList.get(turn).pf;
                    //set mextPlayer pf to zero->dead is true now
                    playerList.get(turn).subtractPf(playerList.get(turn).pf);

                    if (linking(playerList.get(turn))) {
                        break;
                    }

                }
            }
        }//</editor-fold>

        //checking for evry dead player and removing them, linking previous with next
        //using iterator to remove elem to avoid concurrencyModificationException
        //<editor-fold>
        {

            for (iter = playerList.iterator(); iter.hasNext();) {
                player elem = iter.next();
                if (elem.isDead) {
                    messageString = "!!! " + elem.name + " is dead!!!\n";

                    try {
                        doc.insertString(doc.getLength(), messageString, normalText);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    elem.previous.next = elem.next;
                    iter.remove();
                }
            }
        }//</editor-fold>

        //if there is only one player left this last one wins!
        //<editor-fold>
        if (playerList.size() == 1) {

            messageString = playerList.get(0).name + " wins!\n"
                    + "Thanks for playing\n";
            try {
                doc.insertString(doc.getLength(), messageString, normalText);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            roll.setText("Quit");
            roll.setEnabled(true);
        } //</editor-fold>
        //incrementing turn with circullar buffer strategy
        else {
            turn = (turn + 1) % playerList.size();

            isMakingDamage = false;

            messageString = ">>>Now  " + playerList.get(turn).name + " attacks " + playerList.get(turn).next.name + "\n";
            try {
                doc.insertString(doc.getLength(), messageString, normalText);
            } catch (BadLocationException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            refresh();
        }

    }

}
