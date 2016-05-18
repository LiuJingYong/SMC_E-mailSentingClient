import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

/* $Id: MailClient.java,v 1.7 1999/07/22 12:07:30 kangasha Exp $ */

/**
 * A simple mail client with a GUI for sending mail.
 *
 * @author Jussi Kangasharju
 *
 * update by Liu Jingyong 2016/01/12
 */
public class MailClient extends Frame {
    /* The stuff for the GUI. */
    private Button btSend = new Button("Send");
    private Button btClear = new Button("Clear");
    private Button btQuit = new Button("Quit");
    private Label serverLabel = new Label("Destination mailserver:"
            + "(Please type \"nslookup -type=MX <domain of the recipient>\""
            + "in the terminal to find out)");
    private TextField serverField = new TextField("", 40);
    private Label fromLabel = new Label("From:(This simple program " +
            "only support to send e-mail from @mail2.sysu.edu.cn)");
    private TextField fromField = new TextField("", 40);
    private Label toLabel = new Label("To:(Destination mailbox " +
            "should match the mail server in \"Destination mailserver\")");
    private TextField toField = new TextField("", 40);
    private Label subjectLabel = new Label("Subject:");
    private TextField subjectField = new TextField("", 40);
    private Label messageLabel = new Label("Message:");
    private TextArea messageText = new TextArea(10, 40);

    /**
     * Create a new MailClient window with fields for entering all
     * the relevant information (From, To, Subject, and message).
     */
    public MailClient() {
        super("Java Mailclient");
	
	/* Create panels for holding the fields. To make it look nice,
	   create an extra panel for holding all the child panels. */
        Panel serverPanel1 = new Panel(new BorderLayout());
        Panel serverPanel2 = new Panel(new BorderLayout());
        Panel fromPanel1 = new Panel(new BorderLayout());
        Panel fromPanel2 = new Panel(new BorderLayout());
        Panel toPanel1 = new Panel(new BorderLayout());
        Panel toPanel2 = new Panel(new BorderLayout());
        Panel subjectPanel1 = new Panel(new BorderLayout());
        Panel subjectPanel2 = new Panel(new BorderLayout());
        Panel messagePanel = new Panel(new BorderLayout());
        serverPanel1.add(serverLabel, BorderLayout.WEST);
        serverPanel2.add(serverField, BorderLayout.WEST);
        fromPanel1.add(fromLabel, BorderLayout.WEST);
        fromPanel2.add(fromField, BorderLayout.WEST);
        toPanel1.add(toLabel, BorderLayout.WEST);
        toPanel2.add(toField, BorderLayout.WEST);
        subjectPanel1.add(subjectLabel, BorderLayout.WEST);
        subjectPanel2.add(subjectField, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageText, BorderLayout.CENTER);
        Panel fieldPanel = new Panel(new GridLayout(0, 1));
        fieldPanel.add(serverPanel1);
        fieldPanel.add(serverPanel2);
        fieldPanel.add(fromPanel1);
        fieldPanel.add(fromPanel2);
        fieldPanel.add(toPanel1);
        fieldPanel.add(toPanel2);
        fieldPanel.add(subjectPanel1);
        fieldPanel.add(subjectPanel2);

	/* Create a panel for the buttons and add listeners to the
	   buttons. */
        Panel buttonPanel = new Panel(new GridLayout(1, 0));
        btSend.addActionListener(new SendListener());
        btClear.addActionListener(new ClearListener());
        btQuit.addActionListener(new QuitListener());
        buttonPanel.add(btSend);
        buttonPanel.add(btClear);
        buttonPanel.add(btQuit);
	
	/* Add, pack, and show. */
        add(fieldPanel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    static public void main(String argv[]) {
        new MailClient();
    }

    /* Handler for the Send-button. */
    class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.out.println("Sending mail");
	    
	    /* Check that we have the local mailserver */
            if ((serverField.getText()).equals("")) {
                System.out.println("Need name of local mailserver!");
                return;
            }

	    /* Check that we have the sender and recipient. */
            if((fromField.getText()).equals("")) {
                System.out.println("Need sender!");
                return;
            }
            if((toField.getText()).equals("")) {
                System.out.println("Need recipient!");
                return;
            }

	    /* Create the message */
            Message mailMessage = new Message(fromField.getText(),
                    toField.getText(),
                    subjectField.getText(),
                    messageText.getText());

        /* Create the message */
//            Message mailMessage = new Message("liujy43@mail2.sysu.edu.cn",
//                    "33209812@163.com",
//                    "testMailClient",
//                    "wocaoaaaaaaaaa");

	    /* Check that the message is valid, i.e., sender and
	       recipient addresses look ok. */
            if(!mailMessage.isValid()) {
                return;
            }

	    /* Create the envelope, open the connection and try to send
	       the message. */
            try {
                Envelope envelope = new Envelope(mailMessage,
                        serverField.getText());
            } catch (UnknownHostException e) {
		/* If there is an error, do not go further */
                return;
            }
            try {
                Envelope envelope = new Envelope(mailMessage,
                        serverField.getText());
                SMTPConnection connection = new SMTPConnection(envelope);
                connection.send(envelope);
                connection.close();
            } catch (IOException error) {
                System.out.println("Sending failed: " + error);
                return;
            }
            System.out.println("Mail sent succesfully!");
        }
    }

    /* Clear the fields on the GUI. */
    class ClearListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clearing fields");
            fromField.setText("");
            toField.setText("");
            subjectField.setText("");
            messageText.setText("");
        }
    }

    /* Quit. */
    class QuitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}