/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.printer.impl;

import java.io.IOException;
import java.io.OutputStream;

import com.github.anastaciocintra.printer.Printer;
import com.github.anastaciocintra.printer.Style;

/**
 * ESC/POS printer implementation.
 */
public final class EscPosPrinter extends Printer {

    /**
     * Creates an instance based on outputStream.
     *
     * @param outputStream can be one file, System.out or printer...
     */
    public EscPosPrinter(OutputStream outputStream) {
        super(outputStream);
    }

    @Override
    public Printer initialize() throws IOException {
        write(ESC);
        write('@');
        return this;
    }

    @Override
    public Printer setChineseCharacterSupport() throws IOException {
        write(ESC);
        write('R');
        write(15);
        return this;
    }

    @Override
    public Printer setExternalDrawerPulse() throws IOException {
        return this;
    }

    @Override
    public Printer feed(int lines) throws IOException {
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
        return style.toEscPosCommands();
    }

}
