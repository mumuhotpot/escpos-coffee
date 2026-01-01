/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */
package com.github.anastaciocintra.printer;

/**
 * Character constants used in printer commands.
 */
public interface Commands {

    /** Null character */
    int NUL = 0;

    /** Bell character */
    int BEL = 7;

    /** Horizontal tab */
    int HT = 9;

    /** Line feed */
    int LF = 10;

    /** Escape */
    int ESC = 27;

    /** File separator */
    int FS = 28;

    /** Group separator */
    int GS = 29;

    /** Record separator */
    int RS = 30;

    /** Function code */
    int FN = 48;
}
