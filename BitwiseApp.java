import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class BitwiseApp extends JFrame {

    // =========================
    // COLORS
    // =========================

    private static final Color BG = new Color(0x17181C);
    private static final Color SCREEN = new Color(0x0F1013);
    private static final Color KEY = new Color(0x2B2D34);
    private static final Color OP = new Color(0x3D4A7A);
    private static final Color CLEAR = new Color(0x8E3B3B);
    private static final Color ACCENT = new Color(0xFF9F0A);
    private static final Color BIT_ON = new Color(0x4F8CFF);
    private static final Color DIM_TXT = new Color(0x8A8D98);

    // =========================
    // UI
    // =========================

    private final JLabel exprLabel =
            label("", 14, DIM_TXT, false);

    private final JLabel display =
            label("0", 38, Color.WHITE, true);

    private final JLabel hexLabel =
            label("HEX  00000000", 13, DIM_TXT, false);

    private final JButton[] bitButtons =
            new JButton[32];

    private final JComboBox<String> baseSelector =
            new JComboBox<>(
                    new String[]{"DEC", "HEX", "BIN", "OCT"}
            );

    // =========================
    // CALCULATOR STATE
    // =========================

    private String entry = "0";

    private String currentBase = "DEC";

    private final StringBuilder expression =
            new StringBuilder();

    private boolean newEntry = true;

    private boolean justCalculated = false;


    // =========================
    // CONSTRUCTOR
    // =========================

    public BitwiseApp() {

        super("Bitwise Calculator");

        setDefaultCloseOperation(
                EXIT_ON_CLOSE
        );


        JPanel root =
                new JPanel(
                        new BorderLayout(0, 10)
                );

        root.setBackground(BG);

        root.setBorder(
                BorderFactory.createEmptyBorder(
                        14, 14, 14, 14
                )
        );


        // =========================
        // SCREEN
        // =========================

        JPanel screen =
                new JPanel(
                        new BorderLayout(8, 8)
                );

        screen.setBackground(SCREEN);

        screen.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 14, 12, 14
                )
        );


        // =========================
        // BASE DROPDOWN
        // =========================

        setupBaseSelector();

        screen.add(
                baseSelector,
                BorderLayout.WEST
        );


        // =========================
        // DISPLAY
        // =========================

        JPanel displayPanel =
                new JPanel(
                        new BorderLayout(0, 2)
                );

        displayPanel.setOpaque(false);

        displayPanel.add(
                exprLabel,
                BorderLayout.NORTH
        );

        displayPanel.add(
                display,
                BorderLayout.CENTER
        );

        hexLabel.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        displayPanel.add(
                hexLabel,
                BorderLayout.SOUTH
        );

        screen.add(
                displayPanel,
                BorderLayout.CENTER
        );

        root.add(
                screen,
                BorderLayout.NORTH
        );


        // =========================
        // 32-BIT GRID
        // =========================

        JPanel bitContainer =
                new JPanel(
                        new BorderLayout(0, 4)
                );

        bitContainer.setOpaque(false);

        bitContainer.add(
                label(
                        "32-BIT VALUE  •  CLICK A BIT TO TOGGLE",
                        11,
                        DIM_TXT,
                        false
                ),
                BorderLayout.NORTH
        );


        JPanel bits =
                new JPanel(
                        new GridLayout(
                                4,
                                8,
                                3,
                                3
                        )
                );

        bits.setOpaque(false);


        for (int i = 31; i >= 0; i--) {

            final int bit = i;

            JButton b =
                    button(
                            "0",
                            KEY,
                            DIM_TXT,
                            11,
                            () -> toggleBit(bit)
                    );

            b.setToolTipText(
                    "Bit " + bit
            );

            bitButtons[i] = b;

            bits.add(b);
        }


        bitContainer.add(
                bits,
                BorderLayout.CENTER
        );


        JPanel center =
                new JPanel(
                        new BorderLayout(0, 8)
                );

        center.setOpaque(false);

        center.add(
                bitContainer,
                BorderLayout.NORTH
        );

        center.add(
                createKeypad(),
                BorderLayout.CENTER
        );

        root.add(
                center,
                BorderLayout.CENTER
        );


        // =========================
        // FINISH
        // =========================

        setContentPane(root);

        setupKeyboard();

        refresh();

        setSize(
                430,
                780
        );

        setMinimumSize(
                new Dimension(
                        390,
                        700
                )
        );

        setLocationRelativeTo(null);
    }


    // =========================================================
    // BASE SELECTOR
    // =========================================================

    private void setupBaseSelector() {

        baseSelector.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        baseSelector.setForeground(
                Color.WHITE
        );

        baseSelector.setBackground(
                KEY
        );

        baseSelector.setOpaque(true);

        baseSelector.setFocusable(false);


        // Prevent Windows from making the box white
        baseSelector.setUI(
                new BasicComboBoxUI() {

                    @Override
                    protected JButton createArrowButton() {

                        JButton arrow =
                                new JButton("▼");

                        arrow.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        10
                                )
                        );

                        arrow.setForeground(
                                Color.WHITE
                        );

                        arrow.setBackground(
                                KEY
                        );

                        arrow.setOpaque(true);

                        arrow.setBorderPainted(
                                false
                        );

                        arrow.setFocusPainted(
                                false
                        );

                        return arrow;
                    }
                }
        );


        // Dropdown options
        baseSelector.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component
                    getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {

                        JLabel item =
                                (JLabel)
                                        super
                                                .getListCellRendererComponent(
                                                        list,
                                                        value,
                                                        index,
                                                        isSelected,
                                                        cellHasFocus
                                                );

                        item.setFont(
                                new Font(
                                        "SansSerif",
                                        Font.BOLD,
                                        14
                                )
                        );

                        item.setOpaque(true);

                        item.setBackground(
                                isSelected
                                        ? OP
                                        : KEY
                        );

                        item.setForeground(
                                Color.WHITE
                        );

                        item.setBorder(
                                BorderFactory.createEmptyBorder(
                                        6,
                                        10,
                                        6,
                                        10
                                )
                        );

                        return item;
                    }
                }
        );


        baseSelector.addActionListener(
                e -> changeBase()
        );
    }


    // =========================================================
    // KEYPAD
    // =========================================================

    private JPanel createKeypad() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout(0, 5)
                );

        wrapper.setOpaque(false);


        JPanel keypad =
                new JPanel(
                        new GridLayout(
                                7,
                                4,
                                5,
                                5
                        )
                );

        keypad.setOpaque(false);


        String[][] rows = {

                {"AC", "DEL", "(", ")"},

                {"~", "AND", "OR", "XOR"},

                {"<<", ">>", ">>>", "÷"},

                {"7", "8", "9", "×"},

                {"4", "5", "6", "−"},

                {"1", "2", "3", "+"},

                {"0", "A", "B", "="}
        };


        for (String[] row : rows) {

            for (String s : row) {

                keypad.add(
                        makeKey(s)
                );
            }
        }


        wrapper.add(
                keypad,
                BorderLayout.CENTER
        );


        // C D E F
        JPanel hexRow =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                5,
                                5
                        )
                );

        hexRow.setOpaque(false);


        for (
                String s :
                new String[]{
                        "C",
                        "D",
                        "E",
                        "F"
                }
        ) {

            hexRow.add(
                    makeKey(s)
            );
        }


        wrapper.add(
                hexRow,
                BorderLayout.SOUTH
        );


        return wrapper;
    }


    // =========================================================
    // BUTTON CREATION
    // =========================================================

    private JButton makeKey(
            String text
    ) {

        Color bg = KEY;

        int size = 18;


        if (text.equals("AC")) {

            bg = CLEAR;

            size = 17;

        } else if (text.equals("=")) {

            bg = ACCENT;

            size = 22;

        } else if (isOperator(text)) {

            bg = OP;

            size = 17;

        } else if (isHexDigit(text)) {

            bg = new Color(
                    0x454B62
            );

            size = 17;
        }


        return button(
                text,
                bg,
                Color.WHITE,
                size,
                () -> handleKey(text)
        );
    }


    private JButton button(
            String text,
            Color bg,
            Color fg,
            int size,
            Runnable action
    ) {

        JButton b =
                new JButton(text);

        b.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        size
                )
        );

        b.setBackground(bg);

        b.setForeground(fg);

        b.setOpaque(true);

        b.setContentAreaFilled(true);

        b.setBorderPainted(false);

        b.setFocusPainted(false);

        b.setFocusable(false);

        b.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        b.addActionListener(
                e -> action.run()
        );

        return b;
    }


    // =========================================================
    // OPERATOR CHECKS
    // =========================================================

    private boolean isHexDigit(
            String s
    ) {

        return s.matches(
                "[A-F]"
        );
    }


    private boolean isOperator(
            String s
    ) {

        return s.equals("~") ||
                s.equals("AND") ||
                s.equals("OR") ||
                s.equals("XOR") ||
                s.equals("<<") ||
                s.equals(">>") ||
                s.equals(">>>") ||
                s.equals("+") ||
                s.equals("−") ||
                s.equals("×") ||
                s.equals("÷");
    }


    // =========================================================
    // HANDLE KEYS
    // =========================================================

    private void handleKey(
            String text
    ) {

        if (text.equals("AC")) {

            clearAll();

        } else if (text.equals("DEL")) {

            backspace();

        } else if (text.equals("=")) {

            calculate();

        } else if (text.equals("~")) {

            bitNot();

        } else if (
                text.equals("(") ||
                text.equals(")")
        ) {

            parenthesis(text);

        } else if (isOperator(text)) {

            addOperator(text);

        } else if (
                text.matches("[0-9A-F]")
        ) {

            digit(text);
        }
    }


    // =========================================================
    // DIGITS
    // =========================================================

    private void digit(
            String d
    ) {

        if (!validDigitForBase(d)) {

            return;
        }


        if (justCalculated) {

            expression.setLength(0);

            entry = "0";

            justCalculated = false;

            newEntry = true;

            exprLabel.setText("");
        }


        if (
                newEntry ||
                entry.equals("0")
        ) {

            entry = d;

            newEntry = false;

        } else {

            entry += d;
        }


        refresh();
    }


    private boolean validDigitForBase(
            String d
    ) {

        switch (currentBase) {

            case "BIN":

                return d.equals("0") ||
                        d.equals("1");

            case "OCT":

                return d.matches(
                        "[0-7]"
                );

            case "DEC":

                return d.matches(
                        "[0-9]"
                );

            case "HEX":

                return d.matches(
                        "[0-9A-F]"
                );

            default:

                return false;
        }
    }


    // =========================================================
    // ADD OPERATOR
    // =========================================================

    private void addOperator(
            String displayOp
    ) {

        if (
                expression.length() == 0
        ) {

            expression.append(
                    entry
            );

        } else if (!newEntry) {

            expression.append(
                    entry
            );

        } else if (
                isExpressionOperatorAtEnd()
        ) {

            expression.setLength(
                    expression.length()
                            - lastOperatorLength()
            );
        }


        String op =
                toInternalOperator(
                        displayOp
                );


        expression.append(op);


        entry = "0";

        newEntry = true;

        justCalculated = false;


        exprLabel.setText(
                toDisplayExpression(
                        expression.toString()
                )
        );
    }


    // =========================================================
    // PARENTHESES
    // =========================================================

    private void parenthesis(
            String p
    ) {

        if (p.equals("(")) {

            if (!newEntry) {

                expression
                        .append(entry)
                        .append("*");
            }

            expression.append("(");

            entry = "0";

            newEntry = true;

        } else {

            if (!newEntry) {

                expression.append(entry);
            }

            expression.append(")");

            entry = "0";

            newEntry = true;
        }


        exprLabel.setText(
                toDisplayExpression(
                        expression.toString()
                )
        );

        refresh();
    }


    // =========================================================
    // CONVERT DISPLAY OPERATOR
    // =========================================================

    private String toInternalOperator(
            String s
    ) {

        switch (s) {

            case "−":
                return "-";

            case "×":
                return "*";

            case "÷":
                return "/";

            case "AND":
                return "&";

            case "OR":
                return "|";

            case "XOR":
                return "^";

            default:
                return s;
        }
    }


    // =========================================================
    // DISPLAY EXPRESSION
    // =========================================================

    private String toDisplayExpression(
            String s
    ) {

        return s
                .replace(
                        ">>>",
                        " >>> "
                )
                .replace(
                        ">>",
                        " >> "
                )
                .replace(
                        "<<",
                        " << "
                )
                .replace(
                        "&",
                        " AND "
                )
                .replace(
                        "^",
                        " XOR "
                )
                .replace(
                        "|",
                        " OR "
                )
                .replace(
                        "*",
                        " × "
                )
                .replace(
                        "/",
                        " ÷ "
                )
                .replace(
                        "-",
                        " − "
                )
                .replace(
                        "+",
                        " + "
                )
                .replaceAll(
                        "\\s+",
                        " "
                )
                .trim();
    }


    // =========================================================
    // OPERATOR AT END
    // =========================================================

    private boolean isExpressionOperatorAtEnd() {

        if (
                expression.length() == 0
        ) {

            return false;
        }


        char c =
                expression.charAt(
                        expression.length() - 1
                );


        return "+-*/&|^".indexOf(c) >= 0
                || c == '>'
                || c == '<';
    }


    private int lastOperatorLength() {

        if (
                expression.length() >= 3 &&
                expression.substring(
                        expression.length() - 3
                ).equals(">>>")
        ) {

            return 3;
        }


        if (
                expression.length() >= 2 &&
                (
                        expression.substring(
                                expression.length() - 2
                        ).equals(">>")
                        ||
                        expression.substring(
                                expression.length() - 2
                        ).equals("<<")
                )
        ) {

            return 2;
        }


        return 1;
    }


    // =========================================================
    // CALCULATE
    // =========================================================

    private void calculate() {

        String full =
                expression.toString();


        if (!newEntry) {

            full += entry;
        }


        if (full.isEmpty()) {

            return;
        }


        try {

            int result =
                    evaluate(full);


            exprLabel.setText(
                    toDisplayExpression(full)
                            + " ="
            );


            entry =
                    formatValue(result);


            expression.setLength(0);

            justCalculated = true;

            newEntry = true;

            refresh();

        } catch (
                ArithmeticException |
                IllegalArgumentException ex
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Calculation Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // EXPRESSION EVALUATOR
    // =========================================================

    private int evaluate(
            String s
    ) {

        List<String> tokens =
                tokenize(s);


        Stack<Integer> values =
                new Stack<>();

        Stack<String> ops =
                new Stack<>();


        for (String token : tokens) {

            if (token.equals("(")) {

                ops.push(token);

            } else if (token.equals(")")) {

                while (
                        !ops.empty() &&
                        !ops.peek().equals("(")
                ) {

                    applyTop(
                            values,
                            ops.pop()
                    );
                }


                if (ops.empty()) {

                    throw new IllegalArgumentException(
                            "Mismatched parentheses."
                    );
                }


                ops.pop();

            } else if (
                    isOperatorToken(token)
            ) {

                while (
                        !ops.empty() &&
                        !ops.peek().equals("(") &&
                        precedence(
                                ops.peek()
                        ) >= precedence(token)
                ) {

                    applyTop(
                            values,
                            ops.pop()
                    );
                }


                ops.push(token);

            } else {

                values.push(
                        parseNumber(token)
                );
            }
        }


        while (!ops.empty()) {

            if (
                    ops.peek().equals("(")
            ) {

                throw new IllegalArgumentException(
                        "Mismatched parentheses."
                );
            }


            applyTop(
                    values,
                    ops.pop()
            );
        }


        if (
                values.size() != 1
        ) {

            throw new IllegalArgumentException(
                    "Invalid expression."
            );
        }


        return values.pop();
    }


    // =========================================================
    // TOKENIZE
    // =========================================================

    private List<String> tokenize(
            String s
    ) {

        List<String> out =
                new ArrayList<>();


        int i = 0;


        while (i < s.length()) {

            char c =
                    s.charAt(i);


            if (
                    Character.isWhitespace(c)
            ) {

                i++;

                continue;
            }


            if (
                    Character.isDigit(c) ||
                    (
                            currentBase.equals("HEX") &&
                            c >= 'A' &&
                            c <= 'F'
                    )
            ) {

                int start = i++;

                while (
                        i < s.length()
                ) {

                    char n =
                            s.charAt(i);


                    if (
                            Character.isDigit(n) ||
                            (
                                    currentBase.equals("HEX") &&
                                    n >= 'A' &&
                                    n <= 'F'
                            )
                    ) {

                        i++;

                    } else {

                        break;
                    }
                }


                out.add(
                        s.substring(
                                start,
                                i
                        )
                );

            } else if (
                    c == '>' &&
                    i + 2 < s.length() &&
                    s.startsWith(
                            ">>>",
                            i
                    )
            ) {

                out.add(">>>");

                i += 3;

            } else if (
                    (c == '>' || c == '<') &&
                    i + 1 < s.length() &&
                    s.charAt(i + 1) == c
            ) {

                out.add(
                        s.substring(
                                i,
                                i + 2
                        )
                );

                i += 2;

            } else if (
                    "+-*/&|^()".indexOf(c) >= 0
            ) {

                out.add(
                        String.valueOf(c)
                );

                i++;

            } else {

                throw new IllegalArgumentException(
                        "Invalid character: " + c
                );
            }
        }


        return out;
    }


    // =========================================================
    // OPERATOR TOKEN
    // =========================================================

    private boolean isOperatorToken(
            String s
    ) {

        return "+-*/&|^<>"
                .contains(
                        s.substring(0, 1)
                );
    }


    // =========================================================
    // PRECEDENCE
    // =========================================================

    private int precedence(
            String op
    ) {

        switch (op) {

            case "*":
            case "/":
                return 7;

            case "+":
            case "-":
                return 6;

            case ">>":
            case "<<":
            case ">>>":
                return 5;

            case "&":
                return 4;

            case "^":
                return 3;

            case "|":
                return 2;

            default:
                return 0;
        }
    }


    // =========================================================
    // APPLY OPERATION
    // =========================================================

    private void applyTop(
            Stack<Integer> values,
            String op
    ) {

        if (
                values.size() < 2
        ) {

            throw new IllegalArgumentException(
                    "Invalid expression."
            );
        }


        int b =
                values.pop();

        int a =
                values.pop();

        int r;


        switch (op) {

            case "+":

                r = a + b;

                break;

            case "-":

                r = a - b;

                break;

            case "*":

                r = a * b;

                break;

            case "/":

                if (b == 0) {

                    throw new ArithmeticException(
                            "Cannot divide by zero."
                    );
                }

                r = a / b;

                break;

            case "&":

                r = a & b;

                break;

            case "|":

                r = a | b;

                break;

            case "^":

                r = a ^ b;

                break;

            case "<<":

                r =
                        a << (b & 31);

                break;

            case ">>":

                r =
                        a >> (b & 31);

                break;

            case ">>>":

                r =
                        a >>> (b & 31);

                break;

            default:

                throw new IllegalArgumentException(
                        "Unknown operator: " + op
                );
        }


        values.push(r);
    }


    // =========================================================
    // PARSE NUMBER
    // =========================================================

    private int parseNumber(
            String n
    ) {

        switch (currentBase) {

            case "HEX":

                return (int)
                        Long.parseLong(
                                n,
                                16
                        );

            case "BIN":

                return (int)
                        Long.parseLong(
                                n,
                                2
                        );

            case "OCT":

                return (int)
                        Long.parseLong(
                                n,
                                8
                        );

            default:

                return Integer.parseInt(
                        n
                );
        }
    }


    // =========================================================
    // NOT
    // =========================================================

    private void bitNot() {

        int value =
                getEntryValue();


        entry =
                formatValue(
                        ~value
                );


        newEntry = false;

        refresh();
    }


    // =========================================================
    // DELETE
    // =========================================================

    private void backspace() {

        if (newEntry) {

            return;
        }


        if (
                entry.length() <= 1
        ) {

            entry = "0";

            newEntry = true;

        } else {

            entry =
                    entry.substring(
                            0,
                            entry.length() - 1
                    );
        }


        refresh();
    }


    // =========================================================
    // CLEAR
    // =========================================================

    private void clearAll() {

        entry = "0";

        expression.setLength(0);

        newEntry = true;

        justCalculated = false;

        exprLabel.setText("");

        refresh();
    }


    // =========================================================
    // GET ENTRY VALUE
    // =========================================================

    private int getEntryValue() {

        try {

            return parseNumber(
                    entry
            );

        } catch (
                NumberFormatException e
        ) {

            return 0;
        }
    }


    // =========================================================
    // FORMAT VALUE
    // =========================================================

    private String formatValue(
            int value
    ) {

        switch (currentBase) {

            case "HEX":

                return Integer
                        .toHexString(value)
                        .toUpperCase();

            case "BIN":

                return Integer
                        .toBinaryString(value);

            case "OCT":

                return Integer
                        .toOctalString(value);

            default:

                return Integer
                        .toString(value);
        }
    }


    // =========================================================
    // CHANGE BASE
    // =========================================================

    private void changeBase() {

        int value =
                getEntryValue();


        currentBase =
                (String)
                        baseSelector
                                .getSelectedItem();


        entry =
                formatValue(value);


        expression.setLength(0);

        exprLabel.setText("");

        newEntry = false;

        justCalculated = false;

        refresh();
    }


    // =========================================================
    // TOGGLE BIT
    // =========================================================

    private void toggleBit(
            int bit
    ) {

        int value =
                getEntryValue();


        value ^=
                (1 << bit);


        entry =
                formatValue(value);


        newEntry = false;

        refresh();
    }


    // =========================================================
    // REFRESH
    // =========================================================

    private void refresh() {

        display.setText(
                entry
        );


        int value =
                getEntryValue();


        hexLabel.setText(
                "HEX  "
                        + String.format(
                                "%08X",
                                value
                        )
        );


        for (int i = 0; i < 32; i++) {

            boolean on =
                    (value &
                            (1 << i)) != 0;


            bitButtons[i].setText(
                    on ? "1" : "0"
            );


            bitButtons[i].setBackground(
                    on
                            ? BIT_ON
                            : KEY
            );


            bitButtons[i].setForeground(
                    on
                            ? Color.WHITE
                            : DIM_TXT
            );
        }


        updateHexButtons();
    }


    // =========================================================
    // ENABLE HEX BUTTONS ONLY IN HEX MODE
    // =========================================================

    private void updateHexButtons() {

        setHexButtonState(
                getContentPane(),
                currentBase.equals("HEX")
        );
    }


    private void setHexButtonState(
            Container container,
            boolean enabled
    ) {

        for (
                Component component :
                container.getComponents()
        ) {

            if (
                    component instanceof JButton
            ) {

                JButton b =
                        (JButton) component;


                if (
                        isHexDigit(
                                b.getText()
                        )
                ) {

                    b.setEnabled(
                            enabled
                    );
                }
            }


            if (
                    component instanceof Container
            ) {

                setHexButtonState(
                        (Container)
                                component,
                        enabled
                );
            }
        }
    }


    // =========================================================
    // KEYBOARD
    // =========================================================

    private void setupKeyboard() {

        JRootPane root =
                getRootPane();


        InputMap input =
                root.getInputMap(
                        JComponent
                                .WHEN_IN_FOCUSED_WINDOW
                );


        ActionMap actions =
                root.getActionMap();


        for (
                char c = '0';
                c <= '9';
                c++
        ) {

            final String key =
                    String.valueOf(c);


            input.put(
                    KeyStroke.getKeyStroke(c),
                    "digit_" + c
            );


            actions.put(
                    "digit_" + c,
                    new AbstractAction() {

                        @Override
                        public void actionPerformed(
                                java.awt.event.ActionEvent e
                        ) {

                            digit(key);
                        }
                    }
            );
        }


        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_BACK_SPACE,
                        0
                ),
                "backspace"
        );


        actions.put(
                "backspace",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        backspace();
                    }
                }
        );


        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_ENTER,
                        0
                ),
                "equals"
        );


        actions.put(
                "equals",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        calculate();
                    }
                }
        );


        input.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_ESCAPE,
                        0
                ),
                "clear"
        );


        actions.put(
                "clear",
                new AbstractAction() {

                    @Override
                    public void actionPerformed(
                            java.awt.event.ActionEvent e
                    ) {

                        clearAll();
                    }
                }
        );
    }


    // =========================================================
    // LABEL
    // =========================================================

    private static JLabel label(
            String text,
            int size,
            Color color,
            boolean bold
    ) {

        JLabel l =
                new JLabel(text);


        l.setForeground(
                color
        );


        l.setFont(
                new Font(
                        "SansSerif",
                        bold
                                ? Font.BOLD
                                : Font.PLAIN,
                        size
                )
        );


        return l;
    }


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    BitwiseApp app =
                            new BitwiseApp();

                    app.setVisible(true);
                }
        );
    }
}