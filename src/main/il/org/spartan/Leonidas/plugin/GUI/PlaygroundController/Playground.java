package il.org.spartan.Leonidas.plugin.GUI.PlaygroundController;

import com.intellij.lang.java.JavaLanguage;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.plugin.Spartanizer;
import il.org.spartan.Leonidas.plugin.Toolbox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Anna Belozovsky
 * @since 22/04/2017
 */
public class Playground extends JFrame {
    private JPanel mainPanel;
    private JButton clearButton;
    private JButton spartanizeButton;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JLabel input;
    private JLabel output;
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JScrollPane inputScroll;
    private JScrollPane outputScroll;
    private JButton closeButton;

    private String before = "public class foo{ public void main(){\n";
    private String after = "\n}}";

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    public Playground() {
        super("Spartanizer Playground");
        setContentPane(mainPanel);
        setPreferredSize(new Dimension(600, 600));
        setResizable(false);
        pack();
        setVisible(true);
        outputArea.setEditable(false);
        spartanizeButton.addActionListener(e -> spartanizeButtonClicked());
        clearButton.addActionListener(e -> clearButtonClicked());
        closeButton.addActionListener(e -> closeButtonClicked());
    }

    private void spartanizeButtonClicked() {
        //JavaPsiFacade.getElementFactory(Utils.getProject()).createDummyHolder(inputArea.getText(), new IJavaElementType("banana"), null);
        // @TODO @AnnaBel7 the environment you've created will handle only tips apply to elements that can be inside a
        // method. A tip for method, won't work in the play ground.
        PsiFile file = PsiFileFactory.getInstance(Utils.getProject())
                .createFileFromText(JavaLanguage.INSTANCE, before + inputArea.getText() + after);
        Toolbox.getInstance().playground = true;
        Spartanizer.spartanizeFileOnePass(file);
        Toolbox.getInstance().playground = false;
        outputArea.setText(fixString(file.getText()));
    }

    private String fixString(String outputStr) {
        String temp = outputStr.substring(before.length());
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(temp.substring(0, temp.length() - after.length()).split("\n")));
        return lines.stream().map(line -> line.replaceFirst(" {4}", "")).collect(Collectors.joining("\n"));
    }

    private void clearButtonClicked() {
        outputArea.setText("");
    }

    private void closeButtonClicked() {
        dispose();
    }

    public void setInput(String newInput) {
        inputArea.setText(newInput);
    }

    public String getOutput() {
        return outputArea.getText();
    }

    public void doSpartanization() {
        spartanizeButton.doClick();
    }

    public void doClear() {
        clearButton.doClick();
    }

    public void doClose() {
        closeButton.doClick();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));
        textPanel = new JPanel();
        textPanel.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(textPanel, new GridConstraints(0, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(371, 24), null, 0, false));
        inputScroll = new JScrollPane();
        textPanel.add(inputScroll, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        inputArea = new JTextArea();
        inputArea.setText("");
        inputScroll.setViewportView(inputArea);
        outputScroll = new JScrollPane();
        textPanel.add(outputScroll, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        outputArea = new JTextArea();
        outputScroll.setViewportView(outputArea);
        final Spacer spacer1 = new Spacer();
        textPanel.add(spacer1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        input = new JLabel();
        input.setText("Input");
        textPanel.add(input, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        output = new JLabel();
        output.setText("Output");
        textPanel.add(output, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Clear");
        mainPanel.add(clearButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        buttonPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        closeButton = new JButton();
        closeButton.setText("Close");
        buttonPanel.add(closeButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spartanizeButton = new JButton();
        spartanizeButton.setText("Spartanize");
        mainPanel.add(spartanizeButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
