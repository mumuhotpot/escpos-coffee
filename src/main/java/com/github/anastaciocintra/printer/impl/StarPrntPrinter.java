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
 * StarPRNT printer implementation.
 */
public final class StarPrntPrinter extends Printer {

    /**
     * Creates an instance based on outputStream.
     *
     * @param outputStream can be one file, System.out or printer...
     */
    public StarPrntPrinter(OutputStream outputStream) {
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
        write(GS);
        write(')');
        write('U');
        write(2);
        write(0);
        write(FN);
        write(1);
        return this;
    }

    @Override
    public Printer setExternalDrawerPulse() throws IOException {
        write(ESC);
        write(BEL);
        write(20);
        write(20);
        return this;
    }

    @Override
    public Printer feed(int lines) throws IOException {
        write(ESC);
        write('a');
        write(lines);
        return this;
    }

    @Override
    public Printer cut(CutMode mode) throws IOException {
        write(ESC);
        write('d');
        write(mode.value + 2);
        return this;
    }

    @Override
    public Printer pulse() throws IOException {
        write(BEL);
        return this;
    }

    @Override
    protected byte[] getStyleCommands(Style style) {
        return style.toStarPrntCommands();
    }

}
