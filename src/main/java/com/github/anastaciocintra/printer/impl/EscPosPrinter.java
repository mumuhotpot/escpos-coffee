/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.printer.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.github.anastaciocintra.printer.Printer;
import com.github.anastaciocintra.printer.Style;

/**
 * ESC/POS printer implementation.
 */
public final class EscPosPrinter extends Printer {

    private EscPosStyleConfig styleConfig;

    /**
     * Creates an instance based on outputStream.
     *
     * @param outputStream can be one file, System.out or printer...
     * @param styleConfig config related to style in ESC/POS printer
     */
    public EscPosPrinter(OutputStream outputStream, EscPosStyleConfig styleConfig) {
        super(outputStream, Charset.forName("GB18030"));
        this.styleConfig = styleConfig;
    }

    public EscPosPrinter(OutputStream outputStream) {
        this(outputStream, new EscPosStyleConfig());
    }

    @Override
    public Printer initialize() throws IOException {
        write(ESC);
        write('@');
        return this;
    }

    @Override
    public Printer setLineSpacing() throws IOException {
        write(GS);
        write('P');
        write(0);
        write(0);
        return this;
    }

    @Override
    public Printer setChineseCharacterSupport() throws IOException {
        write(FS);
        write('&');
        return this;
    }

    @Override
    public Printer setExternalDrawerPulse() throws IOException {
        return this;
    }

    @Override
    public Printer feed(int lines) throws IOException {
        // Set it default style, otherwise the line height would be affected by the font height in last style
        Style defaultStyle = this.getDefaultStyle();
        write(this.getStyleCommands(defaultStyle));

        write(ESC);
        write('d');
        write(lines);
        return this;
    }

    @Override
    public Printer cut(CutMode mode) throws IOException {
        write(GS);
        write('V');
        write(mode.value);
        return this;
    }

    @Override
    public Printer pulse() throws IOException {
        write(ESC);
        write('p');
        write(0);
        write(16);
        write(20);
        return this;
    }

    @Override
    protected byte[] getStyleCommands(Style style) {
        return style.toEscPosCommands(this.styleConfig);
    }

}
