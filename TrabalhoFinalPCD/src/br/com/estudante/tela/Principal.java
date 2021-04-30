package br.com.estudante.tela;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import br.com.estudante.Aldeao;
import br.com.estudante.Fazenda;
import br.com.estudante.MinaOuro;
import br.com.estudante.Status;
import br.com.estudante.Vila;

import java.awt.Toolkit;

public class Principal extends JFrame {
	// Minhas alteracoes

	private Vila vila;

	// Edson alteracoes
	private static final long serialVersionUID = 1L;
	private JLabel lblJogador;
	private JTable tblAldeoes;
	private DefaultTableModel tmAldeoes;
	private JComboBox<String> cbFazenda;
	private JComboBox<String> cbMinaOuro;
	private JTable tblFazendas;
	private DefaultTableModel tmFazendas;
	private JTable tblMinasOuro;
	private DefaultTableModel tmMinasOuro;
	private JLabel lblComida;
	private JLabel lblOuro;
	private JTextField tfPrefeitura;
	private JPanel pnTemplo;
	private JPanel pnOferenda;
	private JLabel lblOferenda;
	private JTextField tfTemplo;
	private JComboBox<String> cbTEmploEvolucoes;
	private JButton btnTemploEvoluir;
	private JComboBox<String> cbTemploLancamentos;
	private JComboBox<String> cbTemploInimigo;
	private JButton btnTemploLancar;
	private JPanel pnMaravilha;
	private JLabel lblMaravilha;
	private JProgressBar pbMaravilha;
	private String nomeJogador;
	private String civilizacao;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Principal window = new Principal();
			window.setVisible(true);
		});
	}

	public Principal() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/tela/img/icone.png")));
		initialize();
		nomeJogador = JOptionPane.showInputDialog(null, "Informe seu nome", "Jogador", JOptionPane.QUESTION_MESSAGE);
		String[] civilizacoes = { "Acádia", "Babilônia", "Helenística", "Mesopotâmica", "Persa", "Sumária" };
		this.civilizacao = (String) JOptionPane.showInputDialog(null, "Escolha sua civilização", "Jogador",
				JOptionPane.QUESTION_MESSAGE, null, civilizacoes, civilizacoes[0]);
		this.lblJogador.setText(nomeJogador + " - " + this.civilizacao);

		/*
		 * Minhas alteracoes
		 */

		this.vila = new Vila(this);
		this.vila.getPrefeitura().start();

		// FIM
		// ********************************************************************
		// *** Testar - Depois pode apagar ************************************
		this.testar();
		// ********************************************************************
	}

	@SuppressWarnings("serial")
	private void initialize() {
		this.setTitle("Jogo de Estratégia em Tempo Real");
		this.setResizable(false);
		this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		this.setBounds(100, 100, 886, 720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		// *** Configuracoes **************************************************

		DefaultTableCellRenderer dtcrCentralizado = new DefaultTableCellRenderer();
		dtcrCentralizado.setHorizontalAlignment(SwingConstants.CENTER);

		DefaultTableCellRenderer dtcrAldeaoAcao = new DefaultTableCellRenderer() {
			public void setValue(Object valor) {
				String v = valor.toString();
				if (v.equals("Parado"))
					setBackground(Color.YELLOW);
				else if (v.equals("Orando"))
					setBackground(new Color(135, 206, 235));
				else if (v.equals("Sacrificado"))
					setBackground(Color.RED);
				else if (v.contains("Cultivando"))
					setBackground(Color.GREEN);
				else if (v.contains("Minerando"))
					setBackground(Color.ORANGE);
				else if (v.contains("Construindo"))
					setBackground(Color.LIGHT_GRAY);
				else
					setBackground(Color.ORANGE);
				super.setValue(valor);
			}
		};

		// *** Componentes ****************************************************

		JTabbedPane tpJogo = new JTabbedPane(JTabbedPane.TOP);
		tpJogo.setBounds(10, 10, 850, 665);
		this.getContentPane().add(tpJogo);

		JPanel pnTP_Inicio = new JPanel();
		pnTP_Inicio.setLayout(null);
		tpJogo.addTab("Início", null, pnTP_Inicio, null);

		JPanel pnJogador = new JPanel();
		pnJogador.setBorder(new TitledBorder(null, "Jogador", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnJogador.setBounds(10, 10, 250, 51);
		pnTP_Inicio.add(pnJogador);

		lblJogador = new JLabel("Jogador");
		lblJogador.setFont(new Font("Tahoma", Font.BOLD, 12));
		pnJogador.add(lblJogador);

		JPanel pnTP_Vila = new JPanel();
		pnTP_Vila.setLayout(null);
		tpJogo.addTab("Vila", null, pnTP_Vila, null);

		JPanel pnAldeao = new JPanel();
		pnAldeao.setLayout(null);
		pnAldeao.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Alde\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnAldeao.setBounds(10, 10, 270, 620);
		pnTP_Vila.add(pnAldeao);

		String[] colunasAldeoes = { "Nº", "Ação" };
		this.tmAldeoes = (new DefaultTableModel(null, colunasAldeoes) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		this.tblAldeoes = new JTable(this.tmAldeoes);
		this.tblAldeoes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.tblAldeoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.tblAldeoes.getColumn("Ação").setCellRenderer(dtcrAldeaoAcao);
		this.tblAldeoes.getColumnModel().getColumn(0).setResizable(false);
		this.tblAldeoes.getColumnModel().getColumn(0).setCellRenderer(dtcrCentralizado);
		this.tblAldeoes.getColumnModel().getColumn(0).setPreferredWidth(30);
		this.tblAldeoes.getColumnModel().getColumn(1).setResizable(false);
		this.tblAldeoes.getColumnModel().getColumn(1).setPreferredWidth(202);

		JScrollPane spAldeoes = new JScrollPane(this.tblAldeoes);
		spAldeoes.setBounds(10, 20, 250, 460);
		spAldeoes.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pnAldeao.add(spAldeoes);

		JButton btnAldeaoParar = new JButton("Parar");
		btnAldeaoParar.setBounds(10, 485, 120, 21);
		pnAldeao.add(btnAldeaoParar);

		JButton btnAldeaoConstruir = new JButton("Construir");
		btnAldeaoConstruir.setBounds(10, 510, 120, 21);
		pnAldeao.add(btnAldeaoConstruir);

		JComboBox<String> cbConstruir = new JComboBox<String>();
		cbConstruir.setBounds(140, 510, 119, 21);
		cbConstruir.addItem("Fazenda");
		cbConstruir.addItem("Mina de ouro");
		cbConstruir.addItem("Templo");
		cbConstruir.addItem("Maravilha");
		pnAldeao.add(cbConstruir);

		JButton btnAldeaoCultivar = new JButton("Cultivar");
		btnAldeaoCultivar.setBounds(10, 535, 120, 21);
		pnAldeao.add(btnAldeaoCultivar);

		this.cbFazenda = new JComboBox<String>();
		cbFazenda.setBounds(140, 535, 119, 21);
		pnAldeao.add(cbFazenda);

		JButton btnAldeaoMinerar = new JButton("Minerar");
		btnAldeaoMinerar.setBounds(10, 560, 120, 21);
		pnAldeao.add(btnAldeaoMinerar);

		this.cbMinaOuro = new JComboBox<String>();
		cbMinaOuro.setBounds(140, 560, 119, 21);
		pnAldeao.add(cbMinaOuro);

		JButton btnAldeaoOrar = new JButton("Orar");
		btnAldeaoOrar.setBounds(10, 585, 120, 21);
		pnAldeao.add(btnAldeaoOrar);

		JButton btnAldeaoSacrificar = new JButton("Sacrificar");
		btnAldeaoSacrificar.setBounds(140, 585, 120, 21);
		pnAldeao.add(btnAldeaoSacrificar);

		JPanel pnFazenda = new JPanel();
		pnFazenda.setLayout(null);
		pnFazenda.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Fazendas",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnFazenda.setBounds(290, 10, 270, 305);
		pnTP_Vila.add(pnFazenda);

		String[] colunasFazendas = { "Nº", "Aldeões" };
		this.tmFazendas = (new DefaultTableModel(null, colunasFazendas) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		this.tblFazendas = new JTable(this.tmFazendas);
		this.tblFazendas.setRowSelectionAllowed(false);
		this.tblFazendas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.tblFazendas.getColumnModel().getColumn(0).setResizable(false);
		this.tblFazendas.getColumnModel().getColumn(0).setCellRenderer(dtcrCentralizado);
		this.tblFazendas.getColumnModel().getColumn(0).setPreferredWidth(30);
		this.tblFazendas.getColumnModel().getColumn(1).setResizable(false);
		this.tblFazendas.getColumnModel().getColumn(1).setPreferredWidth(202);

		JScrollPane spFazendas = new JScrollPane(this.tblFazendas);
		spFazendas.setBounds(10, 20, 250, 275);
		spFazendas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pnFazenda.add(spFazendas);

		JPanel pnMinaOuro = new JPanel();
		pnMinaOuro.setLayout(null);
		pnMinaOuro.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Minas de ouro", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnMinaOuro.setBounds(290, 325, 270, 305);
		pnTP_Vila.add(pnMinaOuro);

		String[] colunasMinas = { "Nº", "Aldeões" };
		this.tmMinasOuro = (new DefaultTableModel(null, colunasMinas) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});

		this.tblMinasOuro = new JTable(this.tmMinasOuro);
		this.tblMinasOuro.setRowSelectionAllowed(false);
		this.tblMinasOuro.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.tblMinasOuro.getColumnModel().getColumn(0).setResizable(false);
		this.tblMinasOuro.getColumnModel().getColumn(0).setCellRenderer(dtcrCentralizado);
		this.tblMinasOuro.getColumnModel().getColumn(0).setPreferredWidth(30);
		this.tblMinasOuro.getColumnModel().getColumn(1).setResizable(false);
		this.tblMinasOuro.getColumnModel().getColumn(1).setPreferredWidth(202);

		JScrollPane spMinas = new JScrollPane(this.tblMinasOuro);
		spMinas.setBounds(10, 20, 250, 275);
		spMinas.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		pnMinaOuro.add(spMinas);

		JPanel pnPrefeitura = new JPanel();
		pnPrefeitura.setLayout(null);
		pnPrefeitura.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Prefeitura", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnPrefeitura.setBounds(570, 10, 270, 175);
		pnTP_Vila.add(pnPrefeitura);

		JPanel pnComida = new JPanel();
		((FlowLayout) pnComida.getLayout()).setAlignment(FlowLayout.LEFT);
		pnComida.setBorder(new TitledBorder(null, "Comida", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnComida.setBounds(8, 15, 127, 45);
		pnPrefeitura.add(pnComida);

		this.lblComida = new JLabel("0");
		this.lblComida.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pnComida.add(this.lblComida);

		JPanel pnOuro = new JPanel();
		((FlowLayout) pnOuro.getLayout()).setAlignment(FlowLayout.LEFT);
		pnOuro.setBorder(new TitledBorder(null, "Ouro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnOuro.setBounds(135, 15, 128, 45);
		pnPrefeitura.add(pnOuro);

		this.lblOuro = new JLabel("0");
		this.lblOuro.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pnOuro.add(this.lblOuro);

		this.tfPrefeitura = new JTextField();
		tfPrefeitura.setBounds(10, 65, 250, 20);
		this.tfPrefeitura.setEditable(false);
		pnPrefeitura.add(tfPrefeitura);

		JButton btnPrefeituraCriarAldeao = new JButton("Criar aldeão");
		btnPrefeituraCriarAldeao.setBounds(10, 90, 128, 21);
		pnPrefeitura.add(btnPrefeituraCriarAldeao);

		JComboBox<String> cbPrefeituraEvolucoes = new JComboBox<String>();
		cbPrefeituraEvolucoes.setBounds(10, 115, 248, 21);
		cbPrefeituraEvolucoes.addItem("Evolução de aldeão");
		cbPrefeituraEvolucoes.addItem("Evolução de fazenda");
		cbPrefeituraEvolucoes.addItem("Evolução de mina de ouro");
		pnPrefeitura.add(cbPrefeituraEvolucoes);

		JButton btnPrefeituraEvoluir = new JButton("Evoluir");
		btnPrefeituraEvoluir.setBounds(131, 140, 128, 21);
		pnPrefeitura.add(btnPrefeituraEvoluir);

		this.pnTemplo = new JPanel();
		this.pnTemplo.setLayout(null);
		this.pnTemplo.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Templo",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.pnTemplo.setBounds(570, 195, 270, 225);
		this.pnTemplo.setEnabled(false);
		pnTP_Vila.add(this.pnTemplo);

		this.pnOferenda = new JPanel();
		((FlowLayout) this.pnOferenda.getLayout()).setAlignment(FlowLayout.LEFT);
		this.pnOferenda.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Oferendas de fé", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.pnOferenda.setBounds(8, 15, 255, 45);
		this.pnOferenda.setEnabled(false);
		this.pnTemplo.add(this.pnOferenda);

		this.lblOferenda = new JLabel("0");
		this.lblOferenda.setHorizontalAlignment(SwingConstants.LEFT);
		this.lblOferenda.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.lblOferenda.setEnabled(false);
		this.pnOferenda.add(this.lblOferenda);

		this.tfTemplo = new JTextField();
		this.tfTemplo.setEditable(false);
		this.tfTemplo.setBounds(10, 65, 250, 20);
		this.pnTemplo.add(this.tfTemplo);

		this.cbTEmploEvolucoes = new JComboBox<String>();
		this.cbTEmploEvolucoes.setBounds(10, 90, 248, 21);
		this.cbTEmploEvolucoes.addItem("Nuvem de gafanhotos");
		this.cbTEmploEvolucoes.addItem("Morte dos primogênitos");
		this.cbTEmploEvolucoes.addItem("Chuva de pedras");
		this.cbTEmploEvolucoes.addItem("Proteção contra nuvem de gafanhotos");
		this.cbTEmploEvolucoes.addItem("Proteção contra morte dos primogênitos");
		this.cbTEmploEvolucoes.addItem("Proteção contra chuva de pedras");
		this.cbTEmploEvolucoes.setEnabled(false);
		this.pnTemplo.add(this.cbTEmploEvolucoes);

		this.btnTemploEvoluir = new JButton("Evoluir");
		this.btnTemploEvoluir.setBounds(131, 115, 128, 21);
		this.btnTemploEvoluir.setEnabled(false);
		this.pnTemplo.add(this.btnTemploEvoluir);

		this.cbTemploLancamentos = new JComboBox<String>();
		this.cbTemploLancamentos.setBounds(10, 140, 248, 21);
		this.cbTemploLancamentos.setEnabled(false);
		this.pnTemplo.add(this.cbTemploLancamentos);

		this.cbTemploInimigo = new JComboBox<String>();
		this.cbTemploInimigo.setEnabled(false);
		this.cbTemploInimigo.setBounds(10, 165, 248, 21);
		this.pnTemplo.add(this.cbTemploInimigo);

		this.btnTemploLancar = new JButton("Lançar");
		this.btnTemploLancar.setBounds(131, 190, 128, 21);
		this.btnTemploLancar.setEnabled(false);
		this.pnTemplo.add(this.btnTemploLancar);

		this.pnMaravilha = new JPanel();
		this.pnMaravilha.setLayout(null);
		this.pnMaravilha.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Maravilha",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		this.pnMaravilha.setBounds(570, 430, 270, 200);
		this.pnMaravilha.setEnabled(false);
		pnTP_Vila.add(this.pnMaravilha);

		this.lblMaravilha = new JLabel();
		this.lblMaravilha.setBounds(10, 20, 215, 170);
		this.lblMaravilha.setIcon(new ImageIcon(Principal.class.getResource("/tela/img/maravilha.png")));
		this.lblMaravilha.setEnabled(false);
		this.pnMaravilha.add(this.lblMaravilha);

		this.pbMaravilha = new JProgressBar();
		this.pbMaravilha.setOrientation(SwingConstants.VERTICAL);
		this.pbMaravilha.setBounds(225, 20, 30, 170);
		this.pbMaravilha.setMaximum(100000);
		this.pbMaravilha.setStringPainted(true);
		this.pbMaravilha.setEnabled(false);
		this.pnMaravilha.add(pbMaravilha);

		tpJogo.setSelectedIndex(1);

		// *** Eventos ********************************************************

		btnAldeaoParar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoParar(tblAldeoes.getSelectedRow());
			}
		});

		btnAldeaoConstruir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoConstruir(tblAldeoes.getSelectedRow(), cbConstruir.getSelectedItem().toString());
			}
		});

		btnAldeaoCultivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoCultivar(tblAldeoes.getSelectedRow(), cbFazenda.getSelectedIndex());
			}
		});

		btnAldeaoMinerar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoMinerar(tblAldeoes.getSelectedRow(), cbMinaOuro.getSelectedIndex());
			}
		});

		btnAldeaoOrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoOrar(tblAldeoes.getSelectedRow());
			}
		});

		btnAldeaoSacrificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoAldeaoSacrificar(tblAldeoes.getSelectedRow());
			}
		});

		btnPrefeituraCriarAldeao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoPrefeituraCriarAldeao();
			}
		});

		btnPrefeituraEvoluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoPrefeituraEvoluir(cbPrefeituraEvolucoes.getSelectedItem().toString());
			}
		});

		btnTemploEvoluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoTemploEvoluir(cbTEmploEvolucoes.getSelectedItem().toString());
			}
		});

		btnTemploLancar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comandoTemploLancar();
			}
		});

	}

	// ************************************************************************
	// *** Testar - Depois pode apagar ****************************************
	// ************************************************************************
	public void testar() {
		this.mostrarPrefeitura("Vila de " + this.nomeJogador, Color.ORANGE);
		// this.habilitarMaravilha();
		// this.mostrarMaravilha(444);

	}
	// ************************************************************************
	// ************************************************************************
	// ************************************************************************

	// *** Entrada=Apresentação - altera valores dos componentes **************

	public void mostrarMensagemErro(String titulo, String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
	}

	public void adicionarAldeao(String numero, String acao) {
		String[] linha = { String.valueOf(Integer.valueOf(numero) + 1), acao };
		this.tmAldeoes.addRow(linha);
	}

	public void mostrarAldeao(int aldeao, String acao) {
		this.tblAldeoes.setValueAt(acao, aldeao, 1);
	}

	public void adicionarFazenda(String numero, String aldeoes) {
		String[] linha = { String.valueOf(Integer.valueOf(numero) + 1), aldeoes };
		this.tmFazendas.addRow(linha);
		this.cbFazenda.addItem(String.valueOf(Integer.valueOf(numero) + 1));
	}

	public void mostrarFazenda(int fazenda, String aldeoes) {
		this.tblFazendas.setValueAt(aldeoes, fazenda, 1);
	}

	public synchronized void mostrarComida(int qtd) {
		this.lblComida.setText(NumberFormat.getNumberInstance().format(qtd));
	}

	public void adicionarMinaOuro(String numero, String aldeoes) {
		String[] linha = { String.valueOf(Integer.valueOf(numero) + 1), aldeoes };
		this.tmMinasOuro.addRow(linha);
		this.cbMinaOuro.addItem(String.valueOf(Integer.valueOf(numero) + 1));
	}

	public void mostrarMinaOuro(int minaOuro, String aldeoes) {
		this.tblMinasOuro.setValueAt(aldeoes, minaOuro, 1);
	}

	public synchronized void mostrarOuro(int qtd) {
		this.lblOuro.setText(NumberFormat.getNumberInstance().format(qtd));
	}

	public synchronized void mostrarOferendaFe(int qtd) {
		this.lblOferenda.setText(NumberFormat.getNumberInstance().format(qtd));
	}

	public void mostrarPrefeitura(String acao, Color cor) {
		this.tfPrefeitura.setText(acao);
		this.tfPrefeitura.setBackground(cor);
	}

	public void habilitarTemplo() {
		this.pnTemplo.setEnabled(true);
		this.pnOferenda.setEnabled(true);
		this.lblOferenda.setEnabled(true);
		this.cbTEmploEvolucoes.setEnabled(true);
		this.btnTemploEvoluir.setEnabled(true);
	}

	public void habilitarMaravilha() {
		this.pnMaravilha.setEnabled(true);
		this.lblMaravilha.setEnabled(true);
		this.pbMaravilha.setEnabled(true);
	}

	public void mostrarMaravilha(int tijolos) {
		this.pbMaravilha.setValue(tijolos);
	}

	public void mostrarAtaques(List<String> evolucoes) {
		this.cbTemploLancamentos.setEnabled(true);
		this.cbTemploInimigo.setEnabled(true);
		this.btnTemploLancar.setEnabled(true);
		this.cbTemploLancamentos.removeAllItems();
		for (String evolucao : evolucoes) {
			switch (evolucao) {
			case "Nuvem de gafanhotos":
				this.cbTemploLancamentos.addItem("Nuvem de gafanhotos");
				break;
			case "Morte dos primogênitos":
				this.cbTemploLancamentos.addItem("Morte dos primogênitos");
				break;
			case "Chuva de pedras":
				this.cbTemploLancamentos.addItem("Chuva de pedras");
			}
		}
	}

	public void mostrarTemplo(String acao, Color cor) {
		this.tfTemplo.setText(acao);
		this.tfTemplo.setBackground(cor);
	}

	// *** Saída=Ações/comandos - informa ação do usuário *********************

	public void comandoAldeaoParar(int aldeao) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao aldeaoSelecionado = this.vila.getAldeao(aldeao);
			if (aldeaoSelecionado.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				if (aldeaoSelecionado.getFazenda() != null) {
					Fazenda fazenda = aldeaoSelecionado.getFazenda();
					fazenda.removeFazendeiro(aldeaoSelecionado);
				} else if (aldeaoSelecionado.getMinaOuro() != null) {
					MinaOuro minaOuro = aldeaoSelecionado.getMinaOuro();
					minaOuro.removeMinerador(aldeaoSelecionado);
				} else if (this.getVila().getMaravilha() != null) {
					this.getVila().getMaravilha().procuraRemoveConstrutor(aldeaoSelecionado);
					aldeaoSelecionado.setTipoConstrucao("");
				} else if (this.getVila().getTemplo() != null) {
					this.getVila().getTemplo().procuraremoveReligioso(aldeaoSelecionado);
				}
				aldeaoSelecionado.setStatus(Status.PARADO);
				this.mostrarAldeao(aldeao, aldeaoSelecionado.getStatus());
			}
		}

	}

	public void comandoAldeaoConstruir(int aldeao, String qual) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao aldeaoSelecionado = this.vila.getAldeao(aldeao);
			if (aldeaoSelecionado.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				if (qual.equals("Templo") && this.getVila().getTemplo() != null) {

					this.mostrarMensagemErro("Erro", "Já existe um templo");

				} else {
					if (!aldeaoSelecionado.getStatus().equals(Status.PARADO.getDescription())
							&& !aldeaoSelecionado.getStatus().equals(Status.CONSTRUINDO.getDescription())) {
						aldeaoSelecionado.interrupt();
						switch (aldeaoSelecionado.getStatus()) {
						case "Minerando":
							aldeaoSelecionado.getMinaOuro().removeMinerador(aldeaoSelecionado);
							break;

						case "Cultivando":
							aldeaoSelecionado.getFazenda().removeFazendeiro(aldeaoSelecionado);
							break;
						case "Orando":
							this.getVila().getTemplo().removeReligioso(aldeaoSelecionado);
							break;
						}
					}
					aldeaoSelecionado.setTipoConstrucao(qual);
					aldeaoSelecionado.setStatus(Status.CONSTRUINDO);
					this.mostrarAldeao(aldeao, aldeaoSelecionado.getStatus());
					if (this.getVila().getTemplo() != null)
						this.habilitarTemplo();

				}
			}
		}

	}

	public void comandoAldeaoCultivar(int aldeao, int numeroFazenda) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao novoFazendeiro = this.vila.getAldeao(aldeao);
			if (novoFazendeiro.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				Fazenda fazenda = this.vila.getFazenda(numeroFazenda);
				synchronized (fazenda) {
					if (fazenda.getQtdFazendeiros() < fazenda.getCapacidade()) {
						if (!novoFazendeiro.getStatus().equals(Status.PARADO.getDescription())
								&& !novoFazendeiro.getStatus().equals(Status.CULTIVANDO.getDescription())) {
							novoFazendeiro.interrupt();
							switch (novoFazendeiro.getStatus()) {
							case "Minerando":
								novoFazendeiro.getMinaOuro().removeMinerador(novoFazendeiro);
								break;

							case "Orando":
								this.getVila().getTemplo().removeReligioso(novoFazendeiro);
								break;
							}
						}
						novoFazendeiro.setFazenda(fazenda);
						fazenda.addFazendeiro(novoFazendeiro);
						novoFazendeiro.setStatus(Status.CULTIVANDO);
						this.mostrarAldeao(aldeao, novoFazendeiro.getStatus());
					} else
						mostrarMensagemErro("Erro", "Quantidade máxima de aldeões na fazenda " + (numeroFazenda + 1));
				}
			}
		}
	}

	public void comandoAldeaoMinerar(int aldeao, int numeroMinaOuro) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao novoMinerador = this.vila.getAldeao(aldeao);
			if (novoMinerador.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				MinaOuro minaOuro = this.vila.getMinaOuro(numeroMinaOuro);
				synchronized (minaOuro) {
					if (minaOuro.getQtdMineradores() < minaOuro.getCapacidade()) {
						if (!novoMinerador.getStatus().equals(Status.PARADO.getDescription())
								&& !novoMinerador.getStatus().equals(Status.MINERANDO.getDescription())) {
							novoMinerador.interrupt();
							switch (novoMinerador.getStatus()) {
							case "Cultivando":
								novoMinerador.getFazenda().removeFazendeiro(novoMinerador);
								break;

							case "Orando":
								this.getVila().getTemplo().removeReligioso(novoMinerador);
								break;
							}
						}
						novoMinerador.setMinaOuro(minaOuro);
						minaOuro.addMinerador(novoMinerador);
						novoMinerador.setStatus(Status.MINERANDO);
						this.mostrarAldeao(aldeao, novoMinerador.getStatus());
					} else
						mostrarMensagemErro("Erro",
								"Quantidade máxima de aldeões na mina de ouro " + (numeroMinaOuro + 1));
				}
			}
		}
	}

	public void comandoAldeaoOrar(int aldeao) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao novoReligioso = this.vila.getAldeao(aldeao);
			if (novoReligioso.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				if (this.getVila().getTemplo() != null) {
					if (!novoReligioso.getStatus().equals(Status.PARADO.getDescription())
							&& !novoReligioso.getStatus().equals(Status.ORANDO.getDescription())) {
						novoReligioso.interrupt();
						switch (novoReligioso.getStatus()) {
						case "Cultivando":
							novoReligioso.getFazenda().removeFazendeiro(novoReligioso);
							break;

						case "Minerando":
							novoReligioso.getMinaOuro().removeMinerador(novoReligioso);
							break;
						}
					}
					this.getVila().getTemplo().addReligioso(novoReligioso);
					novoReligioso.setStatus(Status.ORANDO);
					this.mostrarAldeao(aldeao, novoReligioso.getStatus());
				} else
					this.mostrarMensagemErro("Erro", "O templo ainda não foi criado");
			}
		}
	}

	public void comandoAldeaoSacrificar(int aldeao) {
		if (aldeao == -1)
			mostrarMensagemErro("Erro", "Escolha um aldeão");
		else {
			Aldeao novoSacrificado = this.vila.getAldeao(aldeao);
			if (novoSacrificado.getStatus().equals(Status.SACRIFICADO.getDescription())) {
				this.mostrarMensagemErro("Erro", "Aldeão já foi sacrificado");
			} else {
				if (novoSacrificado.getStatus().equals(Status.ORANDO.getDescription())) {
					novoSacrificado.interrupt();
					int oferendaGerada = this.getVila().getTemplo().Sacrificar(novoSacrificado);
					this.getVila().getPrefeitura().addOferendasFe(oferendaGerada);
					this.mostrarOferendaFe(this.getVila().getPrefeitura().getOferendasFe());
					novoSacrificado.setStatus(Status.SACRIFICADO);
					this.mostrarAldeao(aldeao, novoSacrificado.getStatus());
				} else
					this.mostrarMensagemErro("Erro", "Somente aldeões religiosos podem ser sacrificados");
			}
			System.out.println("Status 4: " + novoSacrificado.getStatus());
		}
	}

	public void comandoPrefeituraCriarAldeao() {
		Aldeao novo = this.vila.getPrefeitura().criarAldeao();
		if (novo != null)
			this.vila.addAldeao(novo);

	}

	public void comandoPrefeituraEvoluir(String strEvolucao) {
		this.getVila().getPrefeitura().setTipoEvolucao(strEvolucao);
	}

	public void comandoTemploEvoluir(String strEvolucao) {
		this.getVila().getTemplo().setTipoEvolucao(strEvolucao);
	}

	public void comandoTemploLancar() {
		System.out.println("comandoTemploLancar();");
	}

	// Getters and Setters

	public Vila getVila() {
		return vila;
	}

	public String getCivilizacao() {
		return this.civilizacao;
	}
}