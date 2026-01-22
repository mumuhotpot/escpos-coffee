/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.printer;

import java.io.ByteArrayOutputStream;
import com.github.anastaciocintra.printer.impl.EscPosStyleConfig;

/**
 * Supply text style commands
 */
public class Style implements Commands {

    /**
     * Values of font name.
     */
    public enum FontName {
        Font_A_Default(0),
        Font_B(1),
        Font_C(2);
        public int value;

        private FontName(int value) {
            this.value = value;
        }
    }

    /**
     * Values of font size.
     */
    public enum FontSize {
        _1(0),
        _2(1),
        _3(2),
        _4(3);
        public int value;

        private FontSize(int value) {
            this.value = value;
        }
    }

    /**
     * Values of underline style.
     */
    public enum Underline {
        None_Default(0),
        OneDotThick(1),
        TwoDotThick(2);
        public int value;

        private Underline(int value) {
            this.value = value;
        }
    }

    /**
     * Values for print justification.
     */
    public enum Justification {
        Left_Default(0),
        Center(1),
        Right(2);
        public int value;

        private Justification(int value) {
            this.value = value;
        }
    }

    /**
     * Values of color mode background / foreground reverse.
     */
    public enum ColorMode {
        BlackOnWhite_Default(0),
        WhiteOnBlack(1);
        public int value;

        private ColorMode(int value) {
            this.value = value;
        }
    }

    private FontName fontName;
    private FontSize fontWidth;
    private FontSize fontHeight;
    private boolean bold;
    private Underline underline;
    private Justification justification;
    private ColorMode colorMode;

    /**
     * Creates Style object with default values.
     */
    public Style() {
        reset();
    }

    /**
     * Creates Style object with another Style instance values.
     *
     * @param another value to be copied.
     */
    public Style(Style another) {
        setFontName(another.fontName);
        setFontSize(another.fontWidth, another.fontHeight);
        setBold(another.bold);
        setUnderline(another.underline);
        setJustification(another.justification);
        setColorMode(another.colorMode);
    }

    /**
     * Resets values to default.
     */
    public void reset() {
        fontName = FontName.Font_A_Default;
        fontWidth = FontSize._1;
        fontHeight = FontSize._1;
        bold = false;
        underline = Underline.None_Default;
        justification = Justification.Left_Default;
        colorMode = ColorMode.BlackOnWhite_Default;
    }

    /**
     * Sets character font name.
     *
     * @param fontName character font name
     * @return this object
     */
    public Style setFontName(FontName fontName) {
        this.fontName = fontName;
        return this;
    }

    /**
     * Sets font size.
     *
     * @param fontWidth font width
     * @param fontHeight font height
     * @return this object
     */
    public Style setFontSize(FontSize fontWidth, FontSize fontHeight) {
        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;
        return this;
    }

    /**
     * Sets emphasized mode on/off.
     *
     * @param bold bold mode
     * @return this object
     */
    public Style setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    /**
     * Sets underline mode.
     *
     * @param underline underline mode
     * @return this object
     */
    public Style setUnderline(Underline underline) {
        this.underline = underline;
        return this;
    }

    /**
     * Sets Justification for text.
     *
     * @param justification justification mode
     * @return this object
     */
    public Style setJustification(Justification justification) {
        this.justification = justification;
        return this;
    }

    /**
     * Sets color mode background / foreground reverse.
     *
     * @param colorMode color mode
     * @return this object
     */
    public Style setColorMode(ColorMode colorMode) {
        this.colorMode = colorMode;
        return this;
    }

    /**
     * TODO move this method to the impl class
     *
     * Gets ESC/POS commands for current style.
     *
     * @return ESC/POS commands as byte array
     */
    public byte[] toEscPosCommands(EscPosStyleConfig styleConfig) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bytes.write(ESC);
        bytes.write('M');
        bytes.write(fontName.value);

        bytes.write(GS);
        bytes.write('!');
        bytes.write(fontWidth.value << 4 | fontHeight.value);

        bytes.write(ESC);
        bytes.write('3');
        bytes.write((fontHeight.value + 1) * styleConfig.getLineSpacingDot() - 1);

        bytes.write(ESC);
        bytes.write('E');
        bytes.write(bold ? 1 : 0);

        bytes.write(ESC);
        bytes.write('-');
        bytes.write(underline.value);

        bytes.write(ESC);
        bytes.write('a');
        bytes.write(justification.value);

        bytes.write(GS);
        bytes.write('B');
        bytes.write(colorMode.value);

        return bytes.toByteArray();
    }

    /**
     * Gets StarPRNT commands for current style.
     *
     * @return StarPRNT commands as byte array
     */
    public byte[] toStarPrntCommands() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bytes.write(ESC);
        bytes.write(RS);
        bytes.write('F');
        bytes.write(fontName.value);

        bytes.write(ESC);
        bytes.write('i');
        bytes.write(fontHeight.value);
        bytes.write(fontWidth.value);

        bytes.write(ESC);
        bytes.write(bold ? 'E' : 'F');

        bytes.write(ESC);
        bytes.write('-');
        bytes.write(underline.value > 0 ? 1 : 0);

        bytes.write(ESC);
        bytes.write(GS);
        bytes.write('a');
        bytes.write(justification.value);

        bytes.write(ESC);
        bytes.write(colorMode.value == 1 ? '4' : '5');

        return bytes.toByteArray();
    }

}
