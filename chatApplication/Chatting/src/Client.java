import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Client implements ActionListener {
    JPanel panel;
    static JFrame f = new JFrame();
    static JPanel panel2;
    JTextField msg;
    JButton send;
    static Box vertical = Box.createVerticalBox();
    JScrollPane scrollPane;
    static DataOutputStream dout;

    public Client() {
        f.setSize(450, 600);
        f.setLayout(null);
        f.setLocation(800, 50);
        f.setUndecorated(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(7, 130, 84));
        panel.setBounds(0, 0, 450, 60);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/back.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 15, 25, 25);
        panel.add(back);

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("images\\dpp.png"));
        Image i5 = i4.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel dp1 = new JLabel(i6);
        dp1.setBounds(30, 5, 50, 65);
        panel.add(dp1);
        f.add(panel);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("images/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(300, 15, 30, 30);
        panel.add(video);
        f.add(panel);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("images/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel call = new JLabel(i12);
        call.setBounds(350, 15, 35, 30);
        panel.add(call);
        f.add(panel);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("images/threedot.png"));
        Image i14 = i13.getImage().getScaledInstance(15, 35, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JButton threeDot = new JButton(i15);
        threeDot.setBounds(400, 10, 15, 35);
        panel.add(threeDot);
        f.add(panel);

        JLabel name = new JLabel("Rajat");
        name.setFont(new Font("SAN_SARIF", Font.HANGING_BASELINE, 14));
        name.setForeground(Color.WHITE);
        name.setBounds(85, 10, 100, 18);
        panel.add(name);

        JLabel status = new JLabel("Active Now");
        status.setFont(new Font("SAN_SARIF", Font.HANGING_BASELINE, 14));
        status.setForeground(Color.WHITE);
        status.setBounds(85, 28, 100, 18);
        panel.add(status);

        msg = new JTextField();
        msg.setBounds(5, 555, 350, 40);
        msg.setFont(new Font("SAN_SARIF", Font.PLAIN, 14));
        f.add(msg);

        send = new JButton("Send");
        send.setFont(new Font("Arial", Font.PLAIN, 18));
        send.setBackground(new Color(7, 130, 84));
        send.setForeground(Color.WHITE);
        send.setBounds(360, 555, 80, 40);
        f.add(send);
        send.addActionListener(this);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setBounds(0, 60, 450, 490);
        panel2.setBackground(Color.WHITE);
        panel2.add(vertical, BorderLayout.PAGE_START);
        scrollPane = new JScrollPane(panel2);
        scrollPane.setBounds(0, 60, 450, 490);
        f.add(scrollPane);

        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = msg.getText();
        if (str.isEmpty()) {
            return;
        }
        JPanel p = DesignMsg(str);
        JPanel right = new JPanel(new BorderLayout());
        right.add(p, BorderLayout.LINE_END);
        vertical.add(right);
        vertical.add(Box.createVerticalStrut(15));

        try {
            dout.writeUTF(str);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        panel2.revalidate();
        panel2.repaint();

        msg.setText("");
    }

    public static JPanel DesignMsg(String str) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel(str);
        output.setFont(new Font("Arial", Font.CENTER_BASELINE, 16));
        output.setBackground(new Color(37, 211, 107));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15, 15, 15, 50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);
        return panel;
    }

    public static void main(String[] args) {
        new Client();
        try {
            try (Socket s = new Socket("127.0.0.1", 6001)) {
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                while (true) {
                    String msg = din.readUTF();
                    JPanel panel = DesignMsg(msg);
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    vertical.add(Box.createVerticalStrut(15));
                    panel2.add(vertical, BorderLayout.PAGE_START);
                    f.validate();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
