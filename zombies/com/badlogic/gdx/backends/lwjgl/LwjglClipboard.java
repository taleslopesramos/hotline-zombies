/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.backends.lwjgl;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class LwjglClipboard
implements com.badlogic.gdx.utils.Clipboard,
ClipboardOwner {
    @Override
    public String getContents() {
        boolean hasTransferableText;
        String result = "";
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean bl = hasTransferableText = contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText) {
            try {
                result = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException unsupportedFlavorException) {
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        return result;
    }

    @Override
    public void setContents(String content) {
        StringSelection stringSelection = new StringSelection(content);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, this);
    }

    @Override
    public void lostOwnership(Clipboard arg0, Transferable arg1) {
    }
}

