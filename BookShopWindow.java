import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.BevelBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class BookShopWindow {

	private JFrame frame;
	private JTextField nameInput;
	private JTextField editionInput;
	private JTextField priceInput;
	private JTextField isbnInput;
	private static Connection con;
	private JTable bookTable;
	private JTextField isbnSearch;
	private JTextPane returnBook;
	private DefaultTableModel model;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sbutextbookshop", "root", "PASSWORD");
					ShopGUI window = new ShopGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookShopWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 14));
		frame.setBounds(100, 100, 710, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("SBU Textbook Database");
		lblNewLabel.setForeground(new Color(199, 84, 71));
		lblNewLabel.setBackground(new Color(234, 216, 208));
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 25));
		lblNewLabel.setBounds(29, 11, 358, 23);
		frame.getContentPane().add(lblNewLabel);
		
		JPanel panel = new JPanel();
		panel.setForeground(new Color(255, 255, 255));
		panel.setBackground(new Color(234, 216, 208));
		panel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Registration", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(199, 84, 71)));
		panel.setBounds(29, 57, 221, 155);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Book Name");
		lblNewLabel_1.setBounds(23, 21, 63, 14);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNewLabel_1);
		
		nameInput = new JTextField();
		nameInput.setBounds(101, 18, 110, 20);
		panel.add(nameInput);
		nameInput.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Edition");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_1.setBounds(23, 55, 63, 14);
		panel.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Price");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_1_1.setBounds(23, 90, 63, 14);
		panel.add(lblNewLabel_1_1_1);
		
		editionInput = new JTextField();
		editionInput.setColumns(10);
		editionInput.setBounds(101, 55, 110, 20);
		panel.add(editionInput);
		
		priceInput = new JTextField();
		priceInput.setColumns(10);
		priceInput.setBounds(101, 87, 110, 20);
		panel.add(priceInput);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("ISBN");
		lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_1_1_1.setBounds(23, 124, 63, 14);
		panel.add(lblNewLabel_1_1_1_1);
		
		isbnInput = new JTextField();
		isbnInput.setColumns(10);
		isbnInput.setBounds(101, 121, 110, 20);
		panel.add(isbnInput);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(270, 57, 396, 279);
		frame.getContentPane().add(scrollPane);
		
		bookTable = new JTable();
		bookTable.setFillsViewportHeight(true);
		model = new DefaultTableModel();
		Object[] column = {"ISBN", "Name", "Price", "Edition"};
		Object[] row = new Object[4];
		model.setColumnIdentifiers(column);
		bookTable.setModel(model);
		scrollPane.setViewportView(bookTable);
		bookTable.setBorder(null);
		bookTable.setBackground(new Color(234, 216, 208));
		
		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from textbookinfo");
			while(resultSet.next()) {
				row[0] = resultSet.getString("isbn");
				row[1] = resultSet.getString("name");
				row[2] = resultSet.getString("price");
				row[3] = resultSet.getString("edition");
				model.addRow(row);
			}
		} catch(Exception err) {
			err.printStackTrace();
		}
		
		JButton saveBtn = new JButton("SAVE");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Grab 4 fields --> name, isbn, price, edition and put it into insert statement
				String name = nameInput.getText();
				String isbn = isbnInput.getText();
				int price = 0;
				int edition = 0;
				
				if(nameInput.getText().equals("") || isbnInput.getText().equals("") || priceInput.getText().equals("") || editionInput.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Fill In Required Fields");
				} else {
					price = Integer.parseInt(priceInput.getText());
					edition = Integer.parseInt(editionInput.getText());
					
					try {
						Statement statement = con.createStatement();
			            statement.execute("insert into textbookinfo(isbn, name, edition, price) values ('" + isbn + "', '" + name + "', " + edition + ", " + price + ")");
					} catch(Exception err) {
						err.printStackTrace();
					}
					
					row[0] = isbnInput.getText();
					row[1] = nameInput.getText();
					row[2] = priceInput.getText();
					row[3] = editionInput.getText();
					model.addRow(row);
					
					nameInput.setText("");
					isbnInput.setText("");
					priceInput.setText("");
					editionInput.setText("");
				}
			}
		});
		saveBtn.setForeground(new Color(199, 84, 71));
		saveBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		saveBtn.setBounds(29, 217, 105, 23);
		frame.getContentPane().add(saveBtn);
		
		JButton clearBtn = new JButton("CLEAR");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nameInput.setText("");
				isbnInput.setText("");
				priceInput.setText("");
				editionInput.setText("");
			}
		});
		clearBtn.setForeground(new Color(199, 84, 71));
		clearBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		clearBtn.setBounds(145, 217, 105, 23);
		frame.getContentPane().add(clearBtn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setForeground(new Color(199, 84, 71));
		panel_1.setBackground(new Color(234, 216, 208));
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Search", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(199, 84, 71)));
		panel_1.setBounds(29, 261, 221, 47);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("ISBN");
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1_1_2.setBounds(23, 19, 63, 14);
		panel_1.add(lblNewLabel_1_1_2);
		
		isbnSearch = new JTextField();
		isbnSearch.setColumns(10);
		isbnSearch.setBounds(101, 16, 110, 20);
		panel_1.add(isbnSearch);
		
		JButton returnBtn = new JButton("RETURN");
		returnBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Grab inputSearch field
				String isbnToQuery = isbnSearch.getText();
				
				// Select query + return contents into returnField
				try {
					Statement statement = con.createStatement();
					ResultSet resultSet = statement.executeQuery("select * from textbookinfo where isbn='"+ isbnToQuery + "'");
					while(resultSet.next()) {
						returnBook.setText(resultSet.getString("name"));
					      // System.out.println(resultSet.getString("name"));
					}
				} catch(Exception err) {
					err.printStackTrace();
				}
			}
		});
		returnBtn.setForeground(new Color(199, 84, 71));
		returnBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		returnBtn.setBounds(29, 314, 105, 23);
		frame.getContentPane().add(returnBtn);
		
		returnBook = new JTextPane();
		returnBook.setBounds(29, 348, 221, 20);
		frame.getContentPane().add(returnBook);
		
		JButton deleteBtn = new JButton("DELETE");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Update GUI table
				int i=bookTable.getSelectedRow();
				
				if(i>=0) {
					String isbnToDelete = (String) model.getValueAt(i, 0);
					model.removeRow(i);
					
					// Update database
					try {
						Statement statement = con.createStatement();
						statement.execute("delete from textbookinfo where isbn='"+ isbnToDelete + "'");
					} catch(Exception err) {
						err.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please select a row first");
				}
			}
		});
		deleteBtn.setForeground(new Color(199, 84, 71));
		deleteBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		deleteBtn.setBounds(355, 347, 105, 23);
		frame.getContentPane().add(deleteBtn);
		
		JButton clearAllBtn = new JButton("CLEAR ALL");
		clearAllBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Update Database
				try {
					Statement statement = con.createStatement();
					statement.execute("delete from textbookinfo");
				} catch(Exception err) {
					err.printStackTrace();
				}
				
				// Update GUI
				model = new DefaultTableModel();
				Object[] row = new Object[4];
				model.setColumnIdentifiers(column);
				bookTable.setModel(model);
			}
		});
		clearAllBtn.setForeground(new Color(199, 84, 71));
		clearAllBtn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
		clearAllBtn.setBounds(472, 347, 105, 23);
		frame.getContentPane().add(clearAllBtn);
	}
}
