package com.quanlykhov.ui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;

/**
 * Hop thoai nhap lieu dung chung, sinh tu danh sach truong (Field).
 */
public class FormDialog extends JDialog {

    public enum Loai {
        TEXT, PASSWORD, COMBO, AREA, READONLY
    }

    /** Mo ta mot truong nhap lieu. */
    public static class Field {

        final String key;
        final String nhan;
        final Loai loai;
        final String giaTriBanDau;
        final boolean batBuoc;
        final LinkedHashMap<String, String> tuyChon; // key -> nhan hien thi
        final boolean soDienThoai;

        private Field(String key, String nhan, Loai loai, String giaTriBanDau,
                boolean batBuoc, LinkedHashMap<String, String> tuyChon, boolean soDienThoai) {
            this.key = key;
            this.nhan = nhan;
            this.loai = loai;
            this.giaTriBanDau = giaTriBanDau;
            this.batBuoc = batBuoc;
            this.tuyChon = tuyChon;
            this.soDienThoai = soDienThoai;
        }

        public static Field text(String key, String nhan, String giaTri, boolean batBuoc) {
            return new Field(key, nhan, Loai.TEXT, giaTri, batBuoc, null, false);
        }

        public static Field password(String key, String nhan, String giaTri, boolean batBuoc) {
            return new Field(key, nhan, Loai.PASSWORD, giaTri, batBuoc, null, false);
        }

        public static Field combo(String key, String nhan, LinkedHashMap<String, String> tuyChon,
                String giaTri, boolean batBuoc) {
            return new Field(key, nhan, Loai.COMBO, giaTri, batBuoc, tuyChon, false);
        }

        public static Field area(String key, String nhan, String giaTri) {
            return new Field(key, nhan, Loai.AREA, giaTri, false, null, false);
        }

        public static Field readonly(String key, String nhan, String giaTri) {
            return new Field(key, nhan, Loai.READONLY, giaTri, false, null, false);
        }

        public Field sdt() {
            return new Field(key, nhan, loai, giaTriBanDau, batBuoc, tuyChon, true);
        }
    }

    private final Map<String, JComponent> oNhap = new LinkedHashMap<String, JComponent>();
    private final List<Field> fields;
    private final JLabel lblLoi = new JLabel(" ");
    private Map<String, String> ketQua;

    private FormDialog(Component parent, String tieuDe, String phuDe, List<Field> fields) {
        super(lietKeWindow(parent), tieuDe, java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        this.fields = fields;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        khoiTaoGiaoDien(tieuDe, phuDe);
        pack();
        setMinimumSize(new Dimension(460, getHeight()));
        setLocationRelativeTo(parent);
    }

    /** Hien hop thoai. Tra ve map key->gia tri neu luu, null neu huy. */
    public static Map<String, String> hien(Component parent, String tieuDe,
            String phuDe, List<Field> fields) {
        FormDialog dialog = new FormDialog(parent, tieuDe, phuDe, fields);
        dialog.setVisible(true);
        return dialog.ketQua;
    }

    // ------------------------------------------------------------------
    // Cac ham tien ich tao truong (uy quyen cho Field)
    // ------------------------------------------------------------------

    public static Field text(String key, String nhan, String giaTri, boolean batBuoc) {
        return Field.text(key, nhan, giaTri, batBuoc);
    }

    public static Field password(String key, String nhan, String giaTri, boolean batBuoc) {
        return Field.password(key, nhan, giaTri, batBuoc);
    }

    public static Field combo(String key, String nhan, LinkedHashMap<String, String> tuyChon,
            String giaTri, boolean batBuoc) {
        return Field.combo(key, nhan, tuyChon, giaTri, batBuoc);
    }

    public static Field area(String key, String nhan, String giaTri) {
        return Field.area(key, nhan, giaTri);
    }

    public static Field readonly(String key, String nhan, String giaTri) {
        return Field.readonly(key, nhan, giaTri);
    }

    private void khoiTaoGiaoDien(String tieuDe, String phuDe) {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(20, 24, 18, 24));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(UiTheme.FONT_HEADING);
        lblTieuDe.setForeground(UiTheme.TEXT);
        lblTieuDe.setAlignmentX(LEFT_ALIGNMENT);
        header.add(lblTieuDe);
        if (phuDe != null && !phuDe.isEmpty()) {
            JLabel lblPhu = new JLabel(phuDe);
            lblPhu.setFont(UiTheme.FONT_SMALL);
            lblPhu.setForeground(UiTheme.TEXT_MUTED);
            lblPhu.setAlignmentX(LEFT_ALIGNMENT);
            header.add(Box.createVerticalStrut(4));
            header.add(lblPhu);
        }
        header.add(Box.createVerticalStrut(14));

        JPanel bang = new JPanel(new GridBagLayout());
        bang.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        int y = 0;
        for (Field f : fields) {
            gbc.gridy = y++;
            gbc.gridx = 0;
            JLabel lbl = new JLabel(f.nhan + (f.batBuoc ? " *" : ""));
            lbl.setFont(UiTheme.FONT_BOLD);
            lbl.setForeground(UiTheme.TEXT);
            bang.add(lbl, gbc);

            gbc.gridy = y++;
            JComponent comp = taoO(f);
            oNhap.put(f.key, comp);
            bang.add(comp, gbc);
        }

        lblLoi.setFont(UiTheme.FONT_SMALL);
        lblLoi.setForeground(UiTheme.DANGER);
        gbc.gridy = y++;
        gbc.insets = new Insets(4, 0, 0, 0);
        bang.add(lblLoi, gbc);

        JPanel noiDung = new JPanel(new BorderLayout());
        noiDung.setOpaque(false);
        noiDung.add(header, BorderLayout.NORTH);
        noiDung.add(bang, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        RoundedButton btnHuy = new RoundedButton("Huỷ", RoundedButton.Kieu.PHU);
        RoundedButton btnLuu = new RoundedButton("Lưu lại", RoundedButton.Kieu.CHINH);
        btnHuy.addActionListener(e -> {
            ketQua = null;
            dispose();
        });
        btnLuu.addActionListener(e -> xuLyLuu());
        footer.add(btnHuy);
        footer.add(btnLuu);

        root.add(noiDung, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);
        getRootPane().setDefaultButton(btnLuu);
    }

    private JComponent taoO(Field f) {
        switch (f.loai) {
            case PASSWORD: {
                JPasswordField pf = new JPasswordField(f.giaTriBanDau == null ? "" : f.giaTriBanDau);
                trangTriO(pf);
                return pf;
            }
            case COMBO: {
                LinkedHashMap<String, String> opts = f.tuyChon;
                JComboBox<String> cb = new JComboBox<String>();
                for (Map.Entry<String, String> e : opts.entrySet()) {
                    cb.addItem(e.getValue());
                }
                if (f.giaTriBanDau != null) {
                    String nhan = opts.get(f.giaTriBanDau);
                    if (nhan != null) {
                        cb.setSelectedItem(nhan);
                    }
                }
                cb.setFont(UiTheme.FONT_BODY);
                cb.setBackground(Color.WHITE);
                cb.setPreferredSize(new Dimension(10, 36));
                return cb;
            }
            case AREA: {
                JTextArea ta = new JTextArea(f.giaTriBanDau == null ? "" : f.giaTriBanDau, 3, 20);
                ta.setFont(UiTheme.FONT_BODY);
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                JScrollPane sp = new JScrollPane(ta);
                sp.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
                sp.setPreferredSize(new Dimension(10, 84));
                return sp;
            }
            case READONLY: {
                JTextField tf = new JTextField(f.giaTriBanDau == null ? "" : f.giaTriBanDau);
                trangTriO(tf);
                tf.setEditable(false);
                tf.setBackground(new Color(0xF1, 0xF5, 0xF9));
                tf.setForeground(UiTheme.TEXT_MUTED);
                return tf;
            }
            case TEXT:
            default: {
                JTextField tf = new JTextField(f.giaTriBanDau == null ? "" : f.giaTriBanDau);
                trangTriO(tf);
                return tf;
            }
        }
    }

    private void trangTriO(JTextField tf) {
        tf.setFont(UiTheme.FONT_BODY);
        tf.setPreferredSize(new Dimension(10, 38));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UiTheme.BORDER),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
    }

    private void xuLyLuu() {
        Map<String, String> values = new LinkedHashMap<String, String>();
        for (Field f : fields) {
            String giaTri = docGiaTri(f);
            if (f.batBuoc && (giaTri == null || giaTri.trim().isEmpty())) {
                lblLoi.setText("Vui lòng nhập: " + f.nhan);
                return;
            }
            values.put(f.key, giaTri);
        }
        String loi = kiemTraBoSung(values);
        if (loi != null) {
            lblLoi.setText(loi);
            return;
        }
        ketQua = values;
        dispose();
    }

    private String kiemTraBoSung(Map<String, String> values) {
        for (Field f : fields) {
            if (f.soDienThoai) {
                String v = values.get(f.key);
                if (v != null && !v.trim().isEmpty() && !v.trim().matches("0\\d{9}")) {
                    return "\"" + f.nhan + "\" phải gồm 10 chữ số và bắt đầu bằng 0.";
                }
            }
            if (f.loai == Loai.PASSWORD && f.batBuoc) {
                String v = values.get(f.key);
                if (v != null && v.length() < 6) {
                    return "Mật khẩu phải có ít nhất 6 ký tự.";
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private String docGiaTri(Field f) {
        JComponent comp = oNhap.get(f.key);
        if (comp instanceof JPasswordField) {
            return new String(((JPasswordField) comp).getPassword());
        }
        if (comp instanceof JTextField) {
            return ((JTextField) comp).getText().trim();
        }
        if (comp instanceof JTextArea) {
            return ((JTextArea) comp).getText().trim();
        }
        if (comp instanceof JScrollPane) {
            Component view = ((JScrollPane) comp).getViewport().getView();
            if (view instanceof JTextArea) {
                return ((JTextArea) view).getText().trim();
            }
        }
        if (comp instanceof JComboBox) {
            JComboBox<String> cb = (JComboBox<String>) comp;
            String nhan = (String) cb.getSelectedItem();
            for (Map.Entry<String, String> e : f.tuyChon.entrySet()) {
                if (e.getValue().equals(nhan)) {
                    return e.getKey();
                }
            }
            return nhan;
        }
        return null;
    }

    private static Window lietKeWindow(Component parent) {
        if (parent == null) {
            return null;
        }
        if (parent instanceof Window) {
            return (Window) parent;
        }
        return SwingUtilities.getWindowAncestor(parent);
    }

    /** Tien ich: tao LinkedHashMap tu cac cap key->nhan. */
    public static LinkedHashMap<String, String> options(String... cap) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (int i = 0; i + 1 < cap.length; i += 2) {
            map.put(cap[i], cap[i + 1]);
        }
        return map;
    }

    /** Tien ich: chuyen mot danh sach rong thanh list truong. */
    public static List<Field> ds(Field... f) {
        List<Field> list = new ArrayList<Field>();
        for (Field x : f) {
            list.add(x);
        }
        return list;
    }
}
