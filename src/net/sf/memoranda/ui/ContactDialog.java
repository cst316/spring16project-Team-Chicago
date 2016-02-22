package net.sf.memoranda.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

import net.sf.memoranda.Contact;
import net.sf.memoranda.util.Local;
import oracle.jrockit.jfr.parser.ParseException;

import org.apache.commons.validator.routines.EmailValidator;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class ContactDialog extends JDialog implements WindowListener {	
	private static PhoneNumberUtil _phoneNumberUtil = PhoneNumberUtil.getInstance();
	private static EmailValidator _emailValidator = EmailValidator.getInstance();
    public boolean CANCELLED = false;
    boolean ignoreStartChanged = false;
    boolean ignoreEndChanged = false;
    JPanel topPanel = new JPanel(new BorderLayout());
    JPanel bottomPanel = new JPanel(new BorderLayout());
    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    public JLabel header = new JLabel();
    JPanel contactPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc;
    JLabel lblFirstName = new JLabel();
    JLabel lblLastName = new JLabel();
    JLabel lblEmailAddress = new JLabel();
    JLabel lblTelephone = new JLabel();
    JLabel lblOrganization = new JLabel();
    JLabel lblAddToProject = new JLabel();
    public JTextField txtFirstName = new JTextField();
    public JTextField txtLastName = new JTextField();
    public EmailField txtEmailAddress = new EmailField();
    public PhoneNumberField txtTelephone = new PhoneNumberField();
    public JTextField txtOrganization = new JTextField();
    JCheckBox cbAddToProject = new JCheckBox();
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    JButton okB = new JButton();
    JButton cancelB = new JButton();
    
    public ContactDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();
            pack();
        }
        catch (Exception ex) {
            new ExceptionDialog(ex);
        }
        super.addWindowListener(this);
    }

    void jbInit() throws Exception {
    	this.setResizable(false);
        // Build headerPanel
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(Local.getString("Contact"));
        header.setIcon(new ImageIcon(net.sf.memoranda.ui.EventDialog.class.getResource(
            "resources/icons/contact48.png")));
        headerPanel.add(header);
        
        // Build eventPanel
        lblFirstName.setText(Local.getString("First Name"));
        lblFirstName.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        contactPanel.add(lblFirstName, gbc);
        lblLastName.setText(Local.getString("Last Name"));
        lblLastName.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        contactPanel.add(lblLastName, gbc);
        lblEmailAddress.setText(Local.getString("Email"));
        lblEmailAddress.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        contactPanel.add(lblEmailAddress, gbc);
        lblTelephone.setText(Local.getString("Telephone"));
        lblTelephone.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        contactPanel.add(lblTelephone, gbc);
        lblOrganization.setText(Local.getString("Organization"));
        lblOrganization.setMinimumSize(new Dimension(60, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        contactPanel.add(lblOrganization, gbc);
        cbAddToProject.setText(Local.getString("Add to current project"));
        cbAddToProject.setMinimumSize(new Dimension(375, 24));
        cbAddToProject.setSelected(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(cbAddToProject, gbc);

        txtFirstName.setMinimumSize(new Dimension(375, 24));
        txtFirstName.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(txtFirstName, gbc);
        txtLastName.setMinimumSize(new Dimension(375, 24));
        txtLastName.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(txtLastName, gbc);
        txtEmailAddress.setMinimumSize(new Dimension(375, 24));
        txtEmailAddress.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(txtEmailAddress, gbc);
        txtTelephone.setMinimumSize(new Dimension(375, 24));
        txtTelephone.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(txtTelephone, gbc);
        txtOrganization.setMinimumSize(new Dimension(375, 24));
        txtOrganization.setPreferredSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 6;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(txtOrganization, gbc);
        /*lblAddToProject.setText(Local.getString("Add to current project"));
        lblAddToProject.setMinimumSize(new Dimension(375, 24));
        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.gridy = 5;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactPanel.add(lblAddToProject, gbc);*/
        
        // Build ButtonsPanel
        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText(Local.getString("Ok"));
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        this.getRootPane().setDefaultButton(okB);
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelB_actionPerformed(e);
            }
        });
        cancelB.setText(Local.getString("Cancel"));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setMaximumSize(new Dimension(100, 26));
        buttonsPanel.add(okB);
        buttonsPanel.add(cancelB);
        
        // Finally build the Dialog
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(contactPanel, BorderLayout.SOUTH);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    void okB_actionPerformed(ActionEvent e) {
    	if (txtTelephone.isValidNumber() && txtEmailAddress.isValidEmail()) {
    		this.dispose();
    	}
    }

    void cancelB_actionPerformed(ActionEvent e) {
        CANCELLED = true;
        this.dispose();
    }
    
    public void windowOpened( WindowEvent e ) {}

    public void windowClosing( WindowEvent e ) {
        CANCELLED = true;
        this.dispose();
    }
	
    public void windowClosed( WindowEvent e ) {}

	public void windowIconified( WindowEvent e ) {}

	public void windowDeiconified( WindowEvent e ) {}

	public void windowActivated( WindowEvent e ) {}

	public void windowDeactivated( WindowEvent e ) {}
	
	
	/**
	 * A JTextField which only accepts valid phone numbers based on what <code>Contact</code> defines as valid. 
	 * @author Jonathan Hinkle
	 *
	 */
	class PhoneNumberField extends JTextField {
		
		private boolean _isValidNumber = true;
		private PhoneNumber _phoneNumber = null;
		
		/**
		 * The constructor for the PhoneNumberField
		 */
		public PhoneNumberField() {
			super();

			AbstractDocument doc = (AbstractDocument)this.getDocument();
			
			doc.setDocumentFilter(new DocumentFilter() {
				/* (non-Javadoc)
				 * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter.FilterBypass, int, java.lang.String, javax.swing.text.AttributeSet)
				 */
				public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) {
					string.replaceAll("\\s", "");
					if(offset == 0) {
						if(!string.matches("(\\d|\\+)(\\d)*")) {
							string = "";
						}
					}
					else {
						if(!string.matches("(\\d)*")) {
							string = "";
						}
					}
				}
			});
			
			this.addFocusListener(new FocusListener() {

				/* (non-Javadoc)
				 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
				 */
				@Override
				public void focusGained(FocusEvent event) {
					setBackground(Color.white);
				}

				/* (non-Javadoc)
				 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
				 */
				@Override
				public void focusLost(FocusEvent event) {
					final String phoneNumber = getText();
					if (!phoneNumber.isEmpty()) {
						_isValidNumber = Contact.isValidPhoneNumber(phoneNumber);
						if(_isValidNumber) {
							setText(Contact.parsePhoneNumber(phoneNumber));
							setBackground(Color.white);
						}
						else {
							setBackground(Color.pink);
						}
					}
					else {
						setBackground(Color.white);
						_isValidNumber = true;
					}
				}
				
			});
		}
		
		public boolean isValidNumber() {
			return _isValidNumber;
		}
	}
	
	/**
	 * A JTextField which only accepts valid emails based on what <code>Contact</code> defines as valid. 
	 * @author Jonathan Hinkle
	 *
	 */
	class EmailField extends JTextField {
		private boolean _isValidEmail = true;
		
		/**
		 * The constructor for the EmailField
		 */
		public EmailField() {
			super();
			
			this.addFocusListener(new FocusListener() {

				/* (non-Javadoc)
				 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
				 */
				@Override
				public void focusGained(FocusEvent event) {
					setBackground(Color.white);
				}

				/* (non-Javadoc)
				 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
				 */
				@Override
				public void focusLost(FocusEvent event) {
					if (!getText().isEmpty()) {
						_isValidEmail = Contact.isValidEmail(getText());
						if(_isValidEmail) {
							setBackground(Color.white);
						}
						else {
							setBackground(Color.pink);
						}
					}
					else {
						setBackground(Color.white);
						_isValidEmail = true;
					}
				}
				
			});
		}
		
		public boolean isValidEmail() {
			return _isValidEmail;
		}
	}
}

