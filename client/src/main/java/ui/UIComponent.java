package ui;

import java.io.OutputStream;

public interface UIComponent {
    void displayLine(int lineNumber, int colStart, int colEnd, OutputStream stream);

    void widthHint(int width);
    void heightHint(int height);

    int getMinimumWidth();
    int getMinimumHeight();

    int getPreferredWidth();
    int getPreferredHeight();
}
