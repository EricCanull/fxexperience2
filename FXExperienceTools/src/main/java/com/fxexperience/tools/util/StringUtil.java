/*
 * Permissions of this free software license are conditioned on making available
 * complete source code of licensed works and modifications under the same 
 * license or the GNU GPLv3. Copyright and license notices must be preserved.
 * Contributors provide an express grant of patent rights. However, a larger 
 * work using the licensed work through interfaces provided by the licensed 
 * work may be distributed under different terms and without source code 
 * for the larger work.
 */
package com.fxexperience.tools.util;

public class StringUtil {

    public static String padWithSpaces(String s, boolean newLine, int length) {

        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() < length) {
            stringBuilder.append(" ");
        }

        stringBuilder.append(s).toString();

        if (newLine) {
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
