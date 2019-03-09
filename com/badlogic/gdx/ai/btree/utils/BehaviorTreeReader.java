/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.ai.btree.utils;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class BehaviorTreeReader {
    private static final String LOG_TAG = "BehaviorTreeReader";
    protected boolean debug = false;
    protected int lineNumber;
    protected boolean reportsComments;
    private static final byte[] _btree_actions = BehaviorTreeReader.init__btree_actions_0();
    private static final short[] _btree_key_offsets = BehaviorTreeReader.init__btree_key_offsets_0();
    private static final char[] _btree_trans_keys = BehaviorTreeReader.init__btree_trans_keys_0();
    private static final byte[] _btree_single_lengths = BehaviorTreeReader.init__btree_single_lengths_0();
    private static final byte[] _btree_range_lengths = BehaviorTreeReader.init__btree_range_lengths_0();
    private static final short[] _btree_index_offsets = BehaviorTreeReader.init__btree_index_offsets_0();
    private static final byte[] _btree_indicies = BehaviorTreeReader.init__btree_indicies_0();
    private static final byte[] _btree_trans_targs = BehaviorTreeReader.init__btree_trans_targs_0();
    private static final byte[] _btree_trans_actions = BehaviorTreeReader.init__btree_trans_actions_0();
    private static final byte[] _btree_eof_actions = BehaviorTreeReader.init__btree_eof_actions_0();
    static final int btree_start = 26;
    static final int btree_first_final = 26;
    static final int btree_error = 0;
    static final int btree_en_main = 26;

    protected abstract void startLine(int var1);

    protected abstract void startStatement(String var1, boolean var2, boolean var3);

    protected abstract void attribute(String var1, Object var2);

    protected abstract void endStatement();

    protected abstract void endLine();

    protected void comment(String text) {
    }

    public BehaviorTreeReader() {
        this(false);
    }

    public BehaviorTreeReader(boolean reportsComments) {
        this.reportsComments = reportsComments;
    }

    public void parse(String string) {
        char[] data = string.toCharArray();
        this.parse(data, 0, data.length);
    }

    public void parse(Reader reader) {
        try {
            int length;
            char[] data = new char[1024];
            int offset = 0;
            while ((length = reader.read(data, offset, data.length - offset)) != -1) {
                if (length == 0) {
                    char[] newData = new char[data.length * 2];
                    System.arraycopy(data, 0, newData, 0, data.length);
                    data = newData;
                    continue;
                }
                offset += length;
            }
            this.parse(data, 0, offset);
        }
        catch (IOException ex) {
            throw new SerializationException(ex);
        }
        finally {
            StreamUtils.closeQuietly(reader);
        }
    }

    public void parse(InputStream input) {
        try {
            this.parse(new InputStreamReader(input, "UTF-8"));
        }
        catch (IOException ex) {
            throw new SerializationException(ex);
        }
        finally {
            StreamUtils.closeQuietly(input);
        }
    }

    public void parse(FileHandle file) {
        try {
            this.parse(file.reader("UTF-8"));
        }
        catch (Exception ex) {
            throw new SerializationException("Error parsing file: " + file, ex);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void parse(char[] data, int offset, int length) {
        block103 : {
            p = offset;
            eof = pe = length;
            s = 0;
            indent = 0;
            taskIndex = -1;
            isGuard = false;
            isSubtreeRef = false;
            statementName = null;
            taskProcessed = false;
            needsUnescape = false;
            stringIsUnquoted = false;
            parseRuntimeEx = null;
            attrName = null;
            this.lineNumber = 1;
            try {
                cs = 26;
                _trans = 0;
                _goto_targ = 0;
                block45 : do {
                    switch (_goto_targ) {
                        case 0: {
                            if (p == pe) {
                                _goto_targ = 4;
                                continue block45;
                            }
                            if (cs == 0) {
                                _goto_targ = 5;
                                continue block45;
                            }
                        }
                        case 1: {
                            _keys = BehaviorTreeReader._btree_key_offsets[cs];
                            _trans = BehaviorTreeReader._btree_index_offsets[cs];
                            _klen = BehaviorTreeReader._btree_single_lengths[cs];
                            if (_klen <= 0) ** GOTO lbl49
                            _lower = _keys;
                            _upper = _keys + _klen - 1;
                            do {
                                if (_upper >= _lower) ** GOTO lbl40
                                _keys += _klen;
                                _trans += _klen;
                                ** GOTO lbl49
lbl40: // 1 sources:
                                _mid = _lower + (_upper - _lower >> 1);
                                if (data[p] < BehaviorTreeReader._btree_trans_keys[_mid]) {
                                    _upper = _mid - 1;
                                    continue;
                                }
                                if (data[p] <= BehaviorTreeReader._btree_trans_keys[_mid]) break;
                                _lower = _mid + 1;
                            } while (true);
                            _trans += _mid - _keys;
                            ** GOTO lbl64
lbl49: // 2 sources:
                            if ((_klen = BehaviorTreeReader._btree_range_lengths[cs]) <= 0) ** GOTO lbl64
                            _lower = _keys;
                            _upper = _keys + (_klen << 1) - 2;
                            do {
                                if (_upper >= _lower) ** GOTO lbl56
                                _trans += _klen;
                                ** GOTO lbl64
lbl56: // 1 sources:
                                _mid = _lower + (_upper - _lower >> 1 & -2);
                                if (data[p] < BehaviorTreeReader._btree_trans_keys[_mid]) {
                                    _upper = _mid - 2;
                                    continue;
                                }
                                if (data[p] <= BehaviorTreeReader._btree_trans_keys[_mid + 1]) break;
                                _lower = _mid + 2;
                            } while (true);
                            _trans += _mid - _keys >> 1;
lbl64: // 4 sources:
                            _trans = BehaviorTreeReader._btree_indicies[_trans];
                            cs = BehaviorTreeReader._btree_trans_targs[_trans];
                            if (BehaviorTreeReader._btree_trans_actions[_trans] == 0) ** GOTO lbl201
                            _acts = BehaviorTreeReader._btree_trans_actions[_trans];
                            _nacts = BehaviorTreeReader._btree_actions[_acts++];
                            while (_nacts-- > 0) {
                                switch (BehaviorTreeReader._btree_actions[_acts++]) {
                                    case 0: {
                                        value = new String(data, s, p - s);
                                        s = p;
                                        if (needsUnescape) {
                                            value = BehaviorTreeReader.unescape(value);
                                        }
                                        if (stringIsUnquoted) {
                                            if (this.debug) {
                                                GdxAI.getLogger().info("BehaviorTreeReader", "string: " + attrName + "=" + value);
                                            }
                                            if (value.equals("true")) {
                                                if (this.debug) {
                                                    GdxAI.getLogger().info("BehaviorTreeReader", "boolean: " + attrName + "=true");
                                                }
                                                this.attribute(attrName, Boolean.TRUE);
                                            } else if (value.equals("false")) {
                                                if (this.debug) {
                                                    GdxAI.getLogger().info("BehaviorTreeReader", "boolean: " + attrName + "=false");
                                                }
                                                this.attribute(attrName, Boolean.FALSE);
                                            } else if (value.equals("null")) {
                                                this.attribute(attrName, null);
                                            } else {
                                                try {
                                                    if (BehaviorTreeReader.containsFloatingPointCharacters(value)) {
                                                        if (this.debug) {
                                                            GdxAI.getLogger().info("BehaviorTreeReader", "double: " + attrName + "=" + Double.parseDouble(value));
                                                        }
                                                        this.attribute(attrName, new Double(value));
                                                    }
                                                    if (this.debug) {
                                                        GdxAI.getLogger().info("BehaviorTreeReader", "double: " + attrName + "=" + Double.parseDouble(value));
                                                    }
                                                    this.attribute(attrName, new Long(value));
                                                }
                                                catch (NumberFormatException nfe) {
                                                    throw new GdxRuntimeException("Attribute value must be a number, a boolean, a string or null");
                                                }
                                            }
                                        } else {
                                            if (this.debug) {
                                                GdxAI.getLogger().info("BehaviorTreeReader", "string: " + attrName + "=\"" + value + "\"");
                                            }
                                            this.attribute(attrName, value);
                                        }
                                        stringIsUnquoted = false;
                                        break;
                                    }
                                    case 1: {
                                        if (this.debug) {
                                            GdxAI.getLogger().info("BehaviorTreeReader", "unquotedChars");
                                        }
                                        s = p;
                                        needsUnescape = false;
                                        stringIsUnquoted = true;
                                        do {
                                            switch (data[p]) {
                                                case '\\': {
                                                    needsUnescape = true;
                                                    break;
                                                }
                                                case '\t': 
                                                case '\n': 
                                                case '\r': 
                                                case ' ': 
                                                case '(': 
                                                case ')': {
                                                    ** break;
                                                }
                                            }
                                        } while (++p != eof);
                                        ** break;
lbl124: // 2 sources:
                                        --p;
                                        break;
                                    }
                                    case 2: {
                                        if (this.debug) {
                                            GdxAI.getLogger().info("BehaviorTreeReader", "quotedChars");
                                        }
                                        s = ++p;
                                        needsUnescape = false;
                                        do {
                                            switch (data[p]) {
                                                case '\\': {
                                                    needsUnescape = true;
                                                    ++p;
                                                    break;
                                                }
                                                case '\"': {
                                                    ** break;
                                                }
                                            }
                                        } while (++p != eof);
                                        ** break;
lbl141: // 2 sources:
                                        --p;
                                        break;
                                    }
                                    case 3: {
                                        indent = 0;
                                        taskIndex = -1;
                                        isGuard = false;
                                        isSubtreeRef = false;
                                        statementName = null;
                                        taskProcessed = false;
                                        ++this.lineNumber;
                                        if (!this.debug) break;
                                        GdxAI.getLogger().info("BehaviorTreeReader", "****NEWLINE**** " + this.lineNumber);
                                        break;
                                    }
                                    case 4: {
                                        ++indent;
                                        break;
                                    }
                                    case 5: {
                                        if (taskIndex >= 0) {
                                            this.endStatement();
                                        }
                                        taskProcessed = true;
                                        if (statementName != null) {
                                            this.endLine();
                                        }
                                        if (!this.debug) break;
                                        GdxAI.getLogger().info("BehaviorTreeReader", "endLine: indent: " + indent + " taskName: " + statementName + " data[" + p + "] = " + (p >= eof ? "EOF" : new StringBuilder().append("\"").append(data[p]).append("\"").toString()));
                                        break;
                                    }
                                    case 6: {
                                        s = p;
                                        break;
                                    }
                                    case 7: {
                                        if (this.reportsComments) {
                                            this.comment(new String(data, s, p - s));
                                            break;
                                        }
                                        if (!this.debug) break;
                                        GdxAI.getLogger().info("BehaviorTreeReader", "# Comment");
                                        break;
                                    }
                                    case 8: {
                                        if (taskIndex++ < 0) {
                                            this.startLine(indent);
                                        } else {
                                            this.endStatement();
                                        }
                                        statementName = new String(data, s, p - s);
                                        this.startStatement(statementName, isSubtreeRef, isGuard);
                                        isGuard = false;
                                        break;
                                    }
                                    case 9: {
                                        attrName = new String(data, s, p - s);
                                        break;
                                    }
                                    case 10: {
                                        isSubtreeRef = false;
                                        break;
                                    }
                                    case 11: {
                                        isSubtreeRef = true;
                                        break;
                                    }
                                    case 12: {
                                        isGuard = true;
                                        break;
                                    }
                                    case 13: {
                                        isGuard = false;
                                    }
                                }
                            }
                            ** GOTO lbl-1000
                        }
lbl201: // 2 sources:
                        case 2: lbl-1000: // 2 sources:
                        {
                            if (cs == 0) {
                                _goto_targ = 5;
                                continue block45;
                            }
                            if (++p != pe) {
                                _goto_targ = 1;
                                continue block45;
                            }
                        }
                        case 4: {
                            if (p != eof) break block45;
                            __acts = BehaviorTreeReader._btree_eof_actions[cs];
                            __nacts = BehaviorTreeReader._btree_actions[__acts++];
                            while (__nacts-- > 0) {
                                switch (BehaviorTreeReader._btree_actions[__acts++]) {
                                    case 0: {
                                        value = new String(data, s, p - s);
                                        s = p;
                                        if (needsUnescape) {
                                            value = BehaviorTreeReader.unescape(value);
                                        }
                                        if (stringIsUnquoted) {
                                            if (this.debug) {
                                                GdxAI.getLogger().info("BehaviorTreeReader", "string: " + attrName + "=" + value);
                                            }
                                            if (value.equals("true")) {
                                                if (this.debug) {
                                                    GdxAI.getLogger().info("BehaviorTreeReader", "boolean: " + attrName + "=true");
                                                }
                                                this.attribute(attrName, Boolean.TRUE);
                                            } else if (value.equals("false")) {
                                                if (this.debug) {
                                                    GdxAI.getLogger().info("BehaviorTreeReader", "boolean: " + attrName + "=false");
                                                }
                                                this.attribute(attrName, Boolean.FALSE);
                                            } else if (value.equals("null")) {
                                                this.attribute(attrName, null);
                                            } else {
                                                try {
                                                    if (BehaviorTreeReader.containsFloatingPointCharacters(value)) {
                                                        if (this.debug) {
                                                            GdxAI.getLogger().info("BehaviorTreeReader", "double: " + attrName + "=" + Double.parseDouble(value));
                                                        }
                                                        this.attribute(attrName, new Double(value));
                                                    }
                                                    if (this.debug) {
                                                        GdxAI.getLogger().info("BehaviorTreeReader", "double: " + attrName + "=" + Double.parseDouble(value));
                                                    }
                                                    this.attribute(attrName, new Long(value));
                                                }
                                                catch (NumberFormatException nfe) {
                                                    throw new GdxRuntimeException("Attribute value must be a number, a boolean, a string or null");
                                                }
                                            }
                                        } else {
                                            if (this.debug) {
                                                GdxAI.getLogger().info("BehaviorTreeReader", "string: " + attrName + "=\"" + value + "\"");
                                            }
                                            this.attribute(attrName, value);
                                        }
                                        stringIsUnquoted = false;
                                        break;
                                    }
                                    case 5: {
                                        if (taskIndex >= 0) {
                                            this.endStatement();
                                        }
                                        taskProcessed = true;
                                        if (statementName != null) {
                                            this.endLine();
                                        }
                                        if (!this.debug) break;
                                        GdxAI.getLogger().info("BehaviorTreeReader", "endLine: indent: " + indent + " taskName: " + statementName + " data[" + p + "] = " + (p >= eof ? "EOF" : new StringBuilder().append("\"").append(data[p]).append("\"").toString()));
                                        break;
                                    }
                                    case 6: {
                                        s = p;
                                        break;
                                    }
                                    case 7: {
                                        if (this.reportsComments) {
                                            this.comment(new String(data, s, p - s));
                                            break;
                                        }
                                        if (!this.debug) break;
                                        GdxAI.getLogger().info("BehaviorTreeReader", "# Comment");
                                        break;
                                    }
                                    case 8: {
                                        if (taskIndex++ < 0) {
                                            this.startLine(indent);
                                        } else {
                                            this.endStatement();
                                        }
                                        statementName = new String(data, s, p - s);
                                        this.startStatement(statementName, isSubtreeRef, isGuard);
                                        isGuard = false;
                                        break;
                                    }
                                    case 10: {
                                        isSubtreeRef = false;
                                        break;
                                    }
                                    case 11: {
                                        isSubtreeRef = true;
                                    }
                                }
                            }
                            break block103;
                        }
                    }
                    break;
                } while (true);
            }
            catch (RuntimeException ex) {
                parseRuntimeEx = ex;
            }
        }
        if (p < pe) throw new SerializationException("Error parsing behavior tree on line " + this.lineNumber + " near: " + new String(data, p, pe - p), parseRuntimeEx);
        if (statementName != null && !taskProcessed) {
            throw new SerializationException("Error parsing behavior tree on line " + this.lineNumber + " near: " + new String(data, p, pe - p), parseRuntimeEx);
        }
        if (parseRuntimeEx == null) return;
        throw new SerializationException("Error parsing behavior tree: " + new String(data), parseRuntimeEx);
    }

    private static byte[] init__btree_actions_0() {
        return new byte[]{0, 1, 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 9, 1, 12, 1, 13, 2, 0, 5, 2, 0, 13, 2, 5, 3, 2, 7, 5, 2, 10, 8, 2, 11, 8, 3, 0, 5, 3, 3, 6, 7, 5, 3, 7, 5, 3, 3, 10, 8, 5, 3, 10, 8, 13, 3, 11, 8, 5, 3, 11, 8, 13, 4, 6, 7, 5, 3, 4, 10, 8, 5, 3, 4, 11, 8, 5, 3};
    }

    private static short[] init__btree_key_offsets_0() {
        return new short[]{0, 0, 1, 6, 16, 21, 33, 37, 47, 59, 63, 72, 73, 77, 82, 86, 99, 108, 120, 124, 133, 137, 138, 142, 146, 151, 155, 167, 172, 174, 176, 189, 194, 208, 218, 223, 228};
    }

    private static char[] init__btree_trans_keys_0() {
        return new char[]{'\n', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', '$', ')', '_', 'A', 'Z', 'a', 'z', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ')', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ')', '\t', '\r', ' ', '$', '(', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ':', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ':', '\t', '\n', '\r', ' ', '\"', '#', ':', '(', ')', '\"', '\t', '\r', ' ', ':', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ')', '\t', '\r', ' ', ')', '.', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ')', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ':', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ':', '\t', '\n', '\r', ' ', '\"', '#', ':', '(', ')', '\t', '\r', ' ', ')', '\"', '\t', '\r', ' ', ')', '\t', '\r', ' ', ':', '_', 'A', 'Z', 'a', 'z', '\t', '\r', ' ', ')', '\t', '\n', '\r', ' ', '#', '$', '(', '_', 'A', 'Z', 'a', 'z', '\t', '\n', '\r', ' ', '#', '\n', '\r', '\n', '\r', '\t', '\n', '\r', ' ', '#', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\n', '\r', ' ', '#', '\t', '\n', '\r', ' ', '#', '.', '?', '_', '0', '9', 'A', 'Z', 'a', 'z', '\t', '\n', '\r', ' ', '#', '_', 'A', 'Z', 'a', 'z', '\t', '\n', '\r', ' ', '#', '\t', '\n', '\r', ' ', '#', '\t', '\n', '\r', ' ', '#', '\u0000'};
    }

    private static byte[] init__btree_single_lengths_0() {
        return new byte[]{0, 1, 1, 6, 1, 6, 4, 6, 6, 4, 7, 1, 4, 1, 4, 7, 5, 6, 4, 7, 4, 1, 4, 4, 1, 4, 8, 5, 2, 2, 7, 5, 8, 6, 5, 5, 5};
    }

    private static byte[] init__btree_range_lengths_0() {
        return new byte[]{0, 0, 2, 2, 2, 3, 0, 2, 3, 0, 1, 0, 0, 2, 0, 3, 2, 3, 0, 1, 0, 0, 0, 0, 2, 0, 2, 0, 0, 0, 3, 0, 3, 2, 0, 0, 0};
    }

    private static short[] init__btree_index_offsets_0() {
        return new short[]{0, 0, 2, 6, 15, 19, 29, 34, 43, 53, 58, 67, 69, 74, 78, 83, 94, 102, 112, 117, 126, 131, 133, 138, 143, 147, 152, 163, 169, 172, 175, 186, 192, 204, 213, 219, 225};
    }

    private static byte[] init__btree_indicies_0() {
        return new byte[]{0, 1, 2, 2, 2, 1, 3, 3, 3, 4, 5, 6, 6, 6, 1, 7, 7, 7, 1, 8, 8, 8, 9, 11, 10, 10, 10, 10, 1, 12, 12, 12, 5, 1, 13, 13, 13, 14, 15, 16, 16, 16, 1, 17, 17, 17, 19, 20, 18, 18, 18, 18, 1, 21, 21, 21, 22, 1, 22, 1, 22, 22, 24, 1, 1, 1, 23, 25, 1, 17, 17, 17, 19, 1, 26, 26, 26, 1, 8, 8, 8, 9, 1, 27, 27, 27, 28, 29, 31, 30, 30, 30, 30, 1, 32, 32, 32, 5, 33, 33, 33, 1, 34, 34, 34, 36, 37, 35, 35, 35, 35, 1, 38, 38, 38, 39, 1, 39, 1, 39, 39, 41, 1, 1, 1, 40, 42, 42, 42, 43, 1, 44, 1, 32, 32, 32, 5, 1, 34, 34, 34, 36, 1, 30, 30, 30, 1, 27, 27, 27, 28, 1, 45, 46, 47, 45, 48, 14, 15, 16, 16, 16, 1, 47, 46, 47, 47, 48, 1, 50, 51, 49, 53, 54, 52, 55, 56, 55, 55, 57, 59, 58, 58, 58, 58, 1, 55, 56, 55, 55, 57, 1, 60, 61, 60, 60, 62, 63, 64, 26, 26, 26, 26, 1, 65, 46, 65, 65, 48, 66, 66, 66, 1, 67, 68, 67, 67, 69, 1, 65, 46, 65, 65, 48, 1, 60, 61, 60, 60, 62, 1, 0};
    }

    private static byte[] init__btree_trans_targs_0() {
        return new byte[]{26, 0, 30, 3, 4, 7, 15, 5, 6, 7, 5, 14, 6, 7, 2, 3, 32, 9, 8, 10, 12, 9, 10, 34, 11, 35, 32, 16, 7, 24, 15, 25, 16, 17, 18, 17, 19, 23, 18, 19, 20, 21, 16, 7, 22, 26, 26, 27, 28, 29, 26, 1, 29, 26, 1, 27, 26, 28, 30, 31, 33, 26, 28, 13, 36, 33, 8, 33, 26, 28};
    }

    private static byte[] init__btree_trans_actions_0() {
        return new byte[]{7, 0, 13, 0, 0, 19, 13, 13, 36, 63, 0, 0, 0, 0, 0, 17, 13, 15, 0, 15, 0, 0, 0, 3, 5, 1, 0, 33, 55, 0, 0, 0, 0, 13, 15, 0, 15, 0, 0, 0, 3, 5, 1, 24, 1, 9, 27, 0, 0, 13, 67, 43, 0, 47, 30, 36, 77, 36, 0, 0, 33, 72, 33, 0, 0, 0, 13, 1, 39, 1};
    }

    private static byte[] init__btree_eof_actions_0() {
        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 11, 43, 30, 59, 59, 51, 11, 21, 11, 51};
    }

    private static boolean containsFloatingPointCharacters(String value) {
        int n = value.length();
        for (int i = 0; i < n; ++i) {
            switch (value.charAt(i)) {
                case '.': 
                case 'E': 
                case 'e': {
                    return true;
                }
            }
        }
        return false;
    }

    private static String unescape(String value) {
        int length = value.length();
        StringBuilder buffer = new StringBuilder(length + 16);
        int i = 0;
        while (i < length) {
            char c;
            if ((c = value.charAt(i++)) != '\\') {
                buffer.append(c);
                continue;
            }
            if (i == length) break;
            if ((c = value.charAt(i++)) == 'u') {
                buffer.append(Character.toChars(Integer.parseInt(value.substring(i, i + 4), 16)));
                i += 4;
                continue;
            }
            switch (c) {
                case '\"': 
                case '/': 
                case '\\': {
                    break;
                }
                case 'b': {
                    c = '\b';
                    break;
                }
                case 'f': {
                    c = '\f';
                    break;
                }
                case 'n': {
                    c = '\n';
                    break;
                }
                case 'r': {
                    c = '\r';
                    break;
                }
                case 't': {
                    c = '\t';
                    break;
                }
                default: {
                    throw new SerializationException("Illegal escaped character: \\" + c);
                }
            }
            buffer.append(c);
        }
        return buffer.toString();
    }
}

