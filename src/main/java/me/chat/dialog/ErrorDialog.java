package me.chat.dialog;

import lombok.SneakyThrows;
import me.chat.common.IOHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDialog {
    public static Font FONT = new Font("Verdana", 0, 13);
    private static boolean isSystemLaf = false;

    public static void load() {
        try {
            ErrorDialog.class.getClassLoader().loadClass(Frame.class.getName());
        } catch (ClassNotFoundException ex) {
        }
    }

    public static void show(final String title, final String... msg) {
        setLAF();
        new Frame(title, implode(msg)).setVisible(true);
    }

    public static void show(final String title, final Throwable th, final String... msg) {
        setLAF();
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        th.printStackTrace(pw);
        String header;
        if (msg.length == 0) {
            header = "\n      Щось пішло не так...\n";
        } else {
            header = implode(msg);
        }
        new Frame(title, header + "Інформація для ітівців:\n\n" + sw).setVisible(true);
    }

    private static String implode(final String... msg) {
        final StringBuilder sb = new StringBuilder();
        for (final String s : msg) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    private static void setLAF() {
        if (ErrorDialog.isSystemLaf) {
            return;
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        ErrorDialog.isSystemLaf = true;
    }

    private static class Frame extends JFrame {
        @SneakyThrows
        public Frame(final String title, final String message) {
            super(title);
            this.setDefaultCloseOperation(3);
            this.getRootPane().setLayout(new BorderLayout());
            this.setSize(300, 200);
            this.setLocationRelativeTo(null);
            this.setIconImage(ImageIO.read(IOHelper.getResourceURL("assets/img/logo.png")));
            final JTextArea text = new JTextArea();
            text.setTabSize(2);
            text.setEditable(false);
            text.setText(message);
            text.setFont(ErrorDialog.FONT);
            text.setWrapStyleWord(true);
            text.setLineWrap(true);
            final JScrollPane scroll = new JScrollPane(text);
            this.getRootPane().add(scroll, "Center");
        }
    }
}
