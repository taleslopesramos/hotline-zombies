/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Checksum;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class PixmapIO {
    public static void writeCIM(FileHandle file, Pixmap pixmap) {
        CIM.write(file, pixmap);
    }

    public static Pixmap readCIM(FileHandle file) {
        return CIM.read(file);
    }

    public static void writePNG(FileHandle file, Pixmap pixmap) {
        try {
            PNG writer = new PNG((int)((float)(pixmap.getWidth() * pixmap.getHeight()) * 1.5f));
            try {
                writer.setFlipY(false);
                writer.write(file, pixmap);
            }
            finally {
                writer.dispose();
            }
        }
        catch (IOException ex) {
            throw new GdxRuntimeException("Error writing PNG: " + file, ex);
        }
    }

    public static class PNG
    implements Disposable {
        private static final byte[] SIGNATURE = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10};
        private static final int IHDR = 1229472850;
        private static final int IDAT = 1229209940;
        private static final int IEND = 1229278788;
        private static final byte COLOR_ARGB = 6;
        private static final byte COMPRESSION_DEFLATE = 0;
        private static final byte FILTER_NONE = 0;
        private static final byte INTERLACE_NONE = 0;
        private static final byte PAETH = 4;
        private final ChunkBuffer buffer;
        private final Deflater deflater;
        private ByteArray lineOutBytes;
        private ByteArray curLineBytes;
        private ByteArray prevLineBytes;
        private boolean flipY = true;
        private int lastLineLen;

        public PNG() {
            this(16384);
        }

        public PNG(int initialBufferSize) {
            this.buffer = new ChunkBuffer(initialBufferSize);
            this.deflater = new Deflater();
        }

        public void setFlipY(boolean flipY) {
            this.flipY = flipY;
        }

        public void setCompression(int level) {
            this.deflater.setLevel(level);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void write(FileHandle file, Pixmap pixmap) throws IOException {
            OutputStream output = file.write(false);
            try {
                this.write(output, pixmap);
            }
            finally {
                StreamUtils.closeQuietly(output);
            }
        }

        public void write(OutputStream output, Pixmap pixmap) throws IOException {
            byte[] lineOut;
            byte[] curLine;
            byte[] prevLine;
            DeflaterOutputStream deflaterOutput = new DeflaterOutputStream((OutputStream)this.buffer, this.deflater);
            DataOutputStream dataOutput = new DataOutputStream(output);
            dataOutput.write(SIGNATURE);
            this.buffer.writeInt(1229472850);
            this.buffer.writeInt(pixmap.getWidth());
            this.buffer.writeInt(pixmap.getHeight());
            this.buffer.writeByte(8);
            this.buffer.writeByte(6);
            this.buffer.writeByte(0);
            this.buffer.writeByte(0);
            this.buffer.writeByte(0);
            this.buffer.endChunk(dataOutput);
            this.buffer.writeInt(1229209940);
            this.deflater.reset();
            int lineLen = pixmap.getWidth() * 4;
            if (this.lineOutBytes == null) {
                this.lineOutBytes = new ByteArray(lineLen);
                lineOut = this.lineOutBytes.items;
                this.curLineBytes = new ByteArray(lineLen);
                curLine = this.curLineBytes.items;
                this.prevLineBytes = new ByteArray(lineLen);
                prevLine = this.prevLineBytes.items;
            } else {
                lineOut = this.lineOutBytes.ensureCapacity(lineLen);
                curLine = this.curLineBytes.ensureCapacity(lineLen);
                prevLine = this.prevLineBytes.ensureCapacity(lineLen);
                int n = this.lastLineLen;
                for (int i = 0; i < n; ++i) {
                    prevLine[i] = 0;
                }
            }
            this.lastLineLen = lineLen;
            ByteBuffer pixels = pixmap.getPixels();
            int oldPosition = pixels.position();
            boolean rgba8888 = pixmap.getFormat() == Pixmap.Format.RGBA8888;
            int h = pixmap.getHeight();
            for (int y = 0; y < h; ++y) {
                int py;
                int n = py = this.flipY ? h - y - 1 : y;
                if (rgba8888) {
                    pixels.position(py * lineLen);
                    pixels.get(curLine, 0, lineLen);
                } else {
                    int x = 0;
                    for (int px = 0; px < pixmap.getWidth(); ++px) {
                        int pixel = pixmap.getPixel(px, py);
                        curLine[x++] = (byte)(pixel >> 24 & 255);
                        curLine[x++] = (byte)(pixel >> 16 & 255);
                        curLine[x++] = (byte)(pixel >> 8 & 255);
                        curLine[x++] = (byte)(pixel & 255);
                    }
                }
                lineOut[0] = (byte)(curLine[0] - prevLine[0]);
                lineOut[1] = (byte)(curLine[1] - prevLine[1]);
                lineOut[2] = (byte)(curLine[2] - prevLine[2]);
                lineOut[3] = (byte)(curLine[3] - prevLine[3]);
                for (int x = 4; x < lineLen; ++x) {
                    int pb;
                    int pc;
                    int a = curLine[x - 4] & 255;
                    int b = prevLine[x] & 255;
                    int c = prevLine[x - 4] & 255;
                    int p = a + b - c;
                    int pa = p - a;
                    if (pa < 0) {
                        pa = - pa;
                    }
                    if ((pb = p - b) < 0) {
                        pb = - pb;
                    }
                    if ((pc = p - c) < 0) {
                        pc = - pc;
                    }
                    if (pa <= pb && pa <= pc) {
                        c = a;
                    } else if (pb <= pc) {
                        c = b;
                    }
                    lineOut[x] = (byte)(curLine[x] - c);
                }
                deflaterOutput.write(4);
                deflaterOutput.write(lineOut, 0, lineLen);
                byte[] temp = curLine;
                curLine = prevLine;
                prevLine = temp;
            }
            pixels.position(oldPosition);
            deflaterOutput.finish();
            this.buffer.endChunk(dataOutput);
            this.buffer.writeInt(1229278788);
            this.buffer.endChunk(dataOutput);
            output.flush();
        }

        @Override
        public void dispose() {
            this.deflater.end();
        }

        static class ChunkBuffer
        extends DataOutputStream {
            final ByteArrayOutputStream buffer;
            final CRC32 crc;

            ChunkBuffer(int initialSize) {
                this(new ByteArrayOutputStream(initialSize), new CRC32());
            }

            private ChunkBuffer(ByteArrayOutputStream buffer, CRC32 crc) {
                super(new CheckedOutputStream(buffer, crc));
                this.buffer = buffer;
                this.crc = crc;
            }

            public void endChunk(DataOutputStream target) throws IOException {
                this.flush();
                target.writeInt(this.buffer.size() - 4);
                this.buffer.writeTo(target);
                target.writeInt((int)this.crc.getValue());
                this.buffer.reset();
                this.crc.reset();
            }
        }

    }

    private static class CIM {
        private static final int BUFFER_SIZE = 32000;
        private static final byte[] writeBuffer = new byte[32000];
        private static final byte[] readBuffer = new byte[32000];

        private CIM() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static void write(FileHandle file, Pixmap pixmap) {
            DataOutputStream out = null;
            try {
                DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(file.write(false));
                out = new DataOutputStream(deflaterOutputStream);
                out.writeInt(pixmap.getWidth());
                out.writeInt(pixmap.getHeight());
                out.writeInt(Pixmap.Format.toGdx2DPixmapFormat(pixmap.getFormat()));
                ByteBuffer pixelBuf = pixmap.getPixels();
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
                int remainingBytes = pixelBuf.capacity() % 32000;
                int iterations = pixelBuf.capacity() / 32000;
                byte[] arrby = writeBuffer;
                synchronized (arrby) {
                    for (int i = 0; i < iterations; ++i) {
                        pixelBuf.get(writeBuffer);
                        out.write(writeBuffer);
                    }
                    pixelBuf.get(writeBuffer, 0, remainingBytes);
                    out.write(writeBuffer, 0, remainingBytes);
                }
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
            }
            catch (Exception e) {
                throw new GdxRuntimeException("Couldn't write Pixmap to file '" + file + "'", e);
            }
            finally {
                StreamUtils.closeQuietly(out);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public static Pixmap read(FileHandle file) {
            DataInputStream in = null;
            try {
                in = new DataInputStream(new InflaterInputStream(new BufferedInputStream(file.read())));
                int width = in.readInt();
                int height = in.readInt();
                Pixmap.Format format = Pixmap.Format.fromGdx2DPixmapFormat(in.readInt());
                Pixmap pixmap = new Pixmap(width, height, format);
                ByteBuffer pixelBuf = pixmap.getPixels();
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
                Object object = readBuffer;
                synchronized (object) {
                    int readBytes = 0;
                    while ((readBytes = in.read(readBuffer)) > 0) {
                        pixelBuf.put(readBuffer, 0, readBytes);
                    }
                }
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
                object = pixmap;
                return object;
            }
            catch (Exception e) {
                throw new GdxRuntimeException("Couldn't read Pixmap from file '" + file + "'", e);
            }
            finally {
                StreamUtils.closeQuietly(in);
            }
        }
    }

}

