/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.printer;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Write some usual commands to the OutputStream.
 */
public abstract class Printer implements Closeable, Flushable, Commands {

    /**
     * Values for CutMode.
     */
    public enum CutMode {
        FULL(0),
        PART(1);
        public int value;

        private CutMode(int value) {
            this.value = value;
        }
    }

    private OutputStream outputStream;
    private Style defaultStyle;
    private Charset charset;

    /**
     * Creates an instance based on outputStream.
     *
     * @param outputStream can be one file, System.out or printer...
     * @see java.io.OutputStream
     */
    public Printer(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.defaultStyle = new Style();
        this.charset = StandardCharsets.UTF_8;
    }

    /**
     * Calls outputStrem.flush().
     *
     * @exception IOException if an I/O error occurs
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    /**
     * Calls outputStream.close().
     *
     * @exception IOException if an I/O error occurs
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    /**
     * Each write will be send to output Stream.
     *
     * @param outputStream value to be used on writes
     * @return this object
     */
    public Printer setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    /**
     * Gets output stream of this object.
     *
     * @return actual value of output stream
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Sets style of actual instance.
     *
     * @param defaultStyle to be used on writes.
     * @return this object
     */
    public Printer setDefaultStyle(Style defaultStyle) {
        this.defaultStyle = defaultStyle;
        return this;
    }

    /**
     * Gets actual style of this object.
     *
     * @return actual value
     */
    public Style getDefaultStyle() {
        return defaultStyle;
    }

    /**
     * Sets charset to encode message.
     *
     * @param charset to encode the message.
     * @return this object
     */
    public Printer setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    /**
     * Gets actual charset to encode message.
     *
     * @return actual value
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Resets printer to default state.
     *
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public Printer reset() throws IOException {
        defaultStyle.reset();
        initialize();
        setChineseCharacterSupport();
        setExternalDrawerPulse();
        return this;
    }

    /**
     * Sends initialize command to printer.
     *
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer initialize() throws IOException;

    /**
     * Sends command to enable Chinese character support.
     *
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer setChineseCharacterSupport() throws IOException;

    /**
     * Sends command to set external drawer pulse timing.
     *
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer setExternalDrawerPulse() throws IOException;

    /**
     * Feeds the specified number of lines.
     *
     * @param lines number of lines to feed
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer feed(int lines) throws IOException;

    /**
     * Cuts the paper.
     *
     * @param mode cut mode (full or partial)
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer cut(CutMode mode) throws IOException;

    /**
     * Sends pulse to open the cash drawer.
     *
     * @return this object
     * @throws IOException if an I/O error occurs
     */
    public abstract Printer pulse() throws IOException;

    /**
     * Gets the style commands to be sent to printer.
     *
     * @param style text style to be used
     * @return the style commands as byte array
     */
    protected abstract byte[] getStyleCommands(Style style);

    /**
     * Writes one byte directly to outputStream.
     *
     * @param b the byte to be written
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see java.io.OutputStream#write(int)
     */
    public Printer write(int b) throws IOException {
        this.outputStream.write(b);
        return this;
    }

    /**
     * Writes bytes directly to outputStream.
     *
     * @param b the bytes to be written
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see java.io.OutputStream#write(byte[])
     */
    public Printer write(byte b[]) throws IOException {
        this.outputStream.write(b);
        return this;
    }

    /**
     * Writes bytes directly to outputStream.
     *
     * @param b the bytes to be written
     * @param off the start offset in the data
     * @param len the number of bytes to write
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public Printer write(byte b[], int off, int len) throws IOException {
        this.outputStream.write(b, off, len);
        return this;
    }

    /**
     * Writes String to outputStream.
     *
     * @param style text style to be used
     * @param text content to be encoded and write to outputStream
     * @return this object
     * @exception IOException if an I/O error occurs
     */
    public Printer write(Style style, String text) throws IOException {
        write(getStyleCommands(style));
        write(text.getBytes(charset));
        return this;
    }

    /**
     * Calls write with default style.
     *
     * @param text content to be send
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see #write(Style, String)
     */
    public Printer write(String text) throws IOException {
        return write(defaultStyle, text);
    }

    /**
     * Calls write and feed on end.
     *
     * @param style text style to be used
     * @param text content to be send
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see #write(Style, String)
     */
    public Printer writeLF(Style style, String text) throws IOException {
        write(style, text);
        write(LF);
        return this;
    }

    /**
     * Calls write and feed on end with default style.
     *
     * @param text content to be send
     * @return this object
     * @exception IOException if an I/O error occurs
     * @see #writeLF(Style, String)
     */
    public Printer writeLF(String text) throws IOException {
        return writeLF(defaultStyle, text);
    }

}
