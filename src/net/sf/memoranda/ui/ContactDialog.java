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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.sf.memoranda.util.Local;

@SuppressWarnings("serial")
public class ContactDialog extends JDialog implements WindowListener {

	private boolean _cancelled = false;
	private JPanel _topPanel = new JPanel(new BorderLayout());
	private JPanel _bottomPanel = new JPanel(new BorderLayout());
	private JPanel _headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel _header = new JLabel();
	private JPanel _contactPanel = new JPanel(new GridBagLayout());
	private GridBagConstraints _gbc;
	private JLabel _lblFirstName = new JLabel();
	private JLabel _lblLastName = new JLabel();
	private JLabel _lblEmailAddress = new JLabel();
	private JLabel _lblTelephone = new JLabel();
	private JLabel _lblOrganization = new JLabel();
	private JTextField _txtFirstName = new JTextField();
	private JTextField _txtLastName = new JTextField();
	private JTextField _txtEmailAddress = new JTextField();
	private JTextField _txtTelephone = new JTextField();
	private JTextField _txtOrganization = new JTextField();
	private JCheckBox _cbAddToProject = new JCheckBox();
	private JPanel _buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
	private JButton _okB = new JButton();
	private JButton _cancelB = new JButton();

	/**
	 * Constructor for the ContactDialog.
	 * 
	 * @param frame The owner frame.
	 * @param title The title of the dialog box.
	 * @see javax.swing.JDialog
	 */
	public ContactDialog(Frame frame, String title) {
		super(frame, title, true);
		_jbInit();
		pack();
		super.addWindowListener(this);
	}

	public boolean isCancelled() {
		return _cancelled;
	}

	public JCheckBox getCBAddToProject() {
		return _cbAddToProject;
	}

	public JTextField getTxtFirstName() {
		return _txtFirstName;
	}

	public JTextField getTxtLastName() {
		return _txtLastName;
	}

	public JTextField getTxtEmailAddress() {
		return _txtEmailAddress;
	}

	public JTextField getTxtTelephone() {
		return _txtTelephone;
	}

	public JTextField getTxtOrganization() {
		return _txtOrganization;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		_cancelled = true;
		this.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.
	 * WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.
	 * WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent event) {}

	private void _jbInit() {
		this.setResizable(false);
		// Build headerPanel
		_headerPanel.setBackground(Color.WHITE);
		_headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		_header.setFont(new java.awt.Font("Dialog", 0, 20));
		_header.setForeground(new Color(0, 0, 124));
		_header.setText(Local.getString("Contact"));
		_header.setIcon(
				new ImageIcon(net.sf.memoranda.ui.EventDialog.class.getResource("resources/icons/contact48.png")));
		_headerPanel.add(_header);

		// Build eventPanel
		_lblFirstName.setText(Local.getString("First Name"));
		_lblFirstName.setMinimumSize(new Dimension(60, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 0;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_contactPanel.add(_lblFirstName, _gbc);
		_lblLastName.setText(Local.getString("Last Name"));
		_lblLastName.setMinimumSize(new Dimension(60, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 1;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_contactPanel.add(_lblLastName, _gbc);
		_lblEmailAddress.setText(Local.getString("Email"));
		_lblEmailAddress.setMinimumSize(new Dimension(60, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 2;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_contactPanel.add(_lblEmailAddress, _gbc);
		_lblTelephone.setText(Local.getString("Telephone"));
		_lblTelephone.setMinimumSize(new Dimension(60, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 3;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_contactPanel.add(_lblTelephone, _gbc);
		_lblOrganization.setText(Local.getString("Organization"));
		_lblOrganization.setMinimumSize(new Dimension(60, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 4;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_contactPanel.add(_lblOrganization, _gbc);
		_cbAddToProject.setText(Local.getString("Add to current project"));
		_cbAddToProject.setMinimumSize(new Dimension(375, 24));
		_cbAddToProject.setSelected(true);
		_gbc = new GridBagConstraints();
		_gbc.gridx = 0;
		_gbc.gridy = 5;
		_gbc.insets = new Insets(10, 10, 5, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_cbAddToProject, _gbc);

		_txtFirstName.setMinimumSize(new Dimension(375, 24));
		_txtFirstName.setPreferredSize(new Dimension(375, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 1;
		_gbc.gridy = 0;
		_gbc.gridwidth = 6;
		_gbc.insets = new Insets(5, 10, 10, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_txtFirstName, _gbc);
		_txtLastName.setMinimumSize(new Dimension(375, 24));
		_txtLastName.setPreferredSize(new Dimension(375, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 1;
		_gbc.gridy = 1;
		_gbc.gridwidth = 6;
		_gbc.insets = new Insets(5, 10, 10, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_txtLastName, _gbc);
		_txtEmailAddress.setMinimumSize(new Dimension(375, 24));
		_txtEmailAddress.setPreferredSize(new Dimension(375, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 1;
		_gbc.gridy = 2;
		_gbc.gridwidth = 6;
		_gbc.insets = new Insets(5, 10, 10, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_txtEmailAddress, _gbc);
		_txtTelephone.setMinimumSize(new Dimension(375, 24));
		_txtTelephone.setPreferredSize(new Dimension(375, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 1;
		_gbc.gridy = 3;
		_gbc.gridwidth = 6;
		_gbc.insets = new Insets(5, 10, 10, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_txtTelephone, _gbc);
		_txtOrganization.setMinimumSize(new Dimension(375, 24));
		_txtOrganization.setPreferredSize(new Dimension(375, 24));
		_gbc = new GridBagConstraints();
		_gbc.gridx = 1;
		_gbc.gridy = 4;
		_gbc.gridwidth = 6;
		_gbc.insets = new Insets(5, 10, 10, 10);
		_gbc.anchor = GridBagConstraints.WEST;
		_gbc.fill = GridBagConstraints.HORIZONTAL;
		_contactPanel.add(_txtOrganization, _gbc);
		/*
		 * lblAddToProject.setText(Local.getString("Add to current project"));
		 * lblAddToProject.setMinimumSize(new Dimension(375, 24)); gbc = new
		 * GridBagConstraints(); gbc.gridx = 1; gbc.gridy = 5; gbc.insets = new
		 * Insets(10, 10, 5, 10); gbc.anchor = GridBagConstraints.WEST; gbc.fill
		 * = GridBagConstraints.HORIZONTAL; contactPanel.add(lblAddToProject,
		 * gbc);
		 */

		// Build ButtonsPanel
		_okB.setMaximumSize(new Dimension(100, 26));
		_okB.setMinimumSize(new Dimension(100, 26));
		_okB.setPreferredSize(new Dimension(100, 26));
		_okB.setText(Local.getString("Ok"));
		_okB.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent event) {
				okActionPerformed(event);
			}
		});
		this.getRootPane().setDefaultButton(_okB);
		_cancelB.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent event) {
				cancelActionPerformed(event);
			}
		});
		_cancelB.setText(Local.getString("Cancel"));
		_cancelB.setPreferredSize(new Dimension(100, 26));
		_cancelB.setMinimumSize(new Dimension(100, 26));
		_cancelB.setMaximumSize(new Dimension(100, 26));
		_buttonsPanel.add(_okB);
		_buttonsPanel.add(_cancelB);

		// Finally build the Dialog
		_topPanel.add(_headerPanel, BorderLayout.NORTH);
		_topPanel.add(_contactPanel, BorderLayout.SOUTH);
		_bottomPanel.add(_buttonsPanel, BorderLayout.SOUTH);
		this.getContentPane().add(_topPanel, BorderLayout.NORTH);
		this.getContentPane().add(_bottomPanel, BorderLayout.SOUTH);
	}

	private void okActionPerformed(ActionEvent event) {
		this.dispose();
	}

	private void cancelActionPerformed(ActionEvent event) {
		_cancelled = true;
		this.dispose();
	}
}
