/*
 * Decompiled with CFR 0_122.
 */
package com.badlogic.gdx.utils;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SharedLibraryLoader {
    public static boolean isWindows = System.getProperty("os.name").contains("Windows");
    public static boolean isLinux = System.getProperty("os.name").contains("Linux");
    public static boolean isMac = System.getProperty("os.name").contains("Mac");
    public static boolean isIos = false;
    public static boolean isAndroid = false;
    public static boolean isARM = System.getProperty("os.arch").startsWith("arm");
    public static boolean is64Bit = System.getProperty("os.arch").equals("amd64") || System.getProperty("os.arch").equals("x86_64");
    public static String abi = System.getProperty("sun.arch.abi") != null ? System.getProperty("sun.arch.abi") : "";
    private static final HashSet<String> loadedLibraries;
    private String nativesJar;

    public SharedLibraryLoader() {
    }

    public SharedLibraryLoader(String nativesJar) {
        this.nativesJar = nativesJar;
    }

    public String crc(InputStream input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null.");
        }
        CRC32 crc = new CRC32();
        byte[] buffer = new byte[4096];
        try {
            int length;
            while ((length = input.read(buffer)) != -1) {
                crc.update(buffer, 0, length);
            }
        }
        catch (Exception ex) {
            StreamUtils.closeQuietly(input);
        }
        return Long.toString(crc.getValue(), 16);
    }

    public String mapLibraryName(String libraryName) {
        if (isWindows) {
            return libraryName + (is64Bit ? "64.dll" : ".dll");
        }
        if (isLinux) {
            return "lib" + libraryName + (isARM ? new StringBuilder().append("arm").append(abi).toString() : "") + (is64Bit ? "64.so" : ".so");
        }
        if (isMac) {
            return "lib" + libraryName + (is64Bit ? "64.dylib" : ".dylib");
        }
        return libraryName;
    }

    public synchronized void load(String libraryName) {
        if (isIos) {
            return;
        }
        if (loadedLibraries.contains(libraryName = this.mapLibraryName(libraryName))) {
            return;
        }
        try {
            if (isAndroid) {
                System.loadLibrary(libraryName);
            } else {
                this.loadFile(libraryName);
            }
        }
        catch (Throwable ex) {
            throw new GdxRuntimeException("Couldn't load shared library '" + libraryName + "' for target: " + System.getProperty("os.name") + (is64Bit ? ", 64-bit" : ", 32-bit"), ex);
        }
        loadedLibraries.add(libraryName);
    }

    private InputStream readFile(String path) {
        if (this.nativesJar == null) {
            InputStream input = SharedLibraryLoader.class.getResourceAsStream("/" + path);
            if (input == null) {
                throw new GdxRuntimeException("Unable to read file for extraction: " + path);
            }
            return input;
        }
        try {
            ZipFile file = new ZipFile(this.nativesJar);
            ZipEntry entry = file.getEntry(path);
            if (entry == null) {
                throw new GdxRuntimeException("Couldn't find '" + path + "' in JAR: " + this.nativesJar);
            }
            return file.getInputStream(entry);
        }
        catch (IOException ex) {
            throw new GdxRuntimeException("Error reading '" + path + "' in JAR: " + this.nativesJar, ex);
        }
    }

    public File extractFile(String sourcePath, String dirName) throws IOException {
        try {
            File extractedFile;
            String sourceCrc = this.crc(this.readFile(sourcePath));
            if (dirName == null) {
                dirName = sourceCrc;
            }
            if ((extractedFile = this.getExtractedFile(dirName, new File(sourcePath).getName())) == null && (extractedFile = this.getExtractedFile(UUID.randomUUID().toString(), new File(sourcePath).getName())) == null) {
                throw new GdxRuntimeException("Unable to find writable path to extract file. Is the user home directory writable?");
            }
            return this.extractFile(sourcePath, sourceCrc, extractedFile);
        }
        catch (RuntimeException ex) {
            File file = new File(System.getProperty("java.library.path"), sourcePath);
            if (file.exists()) {
                return file;
            }
            throw ex;
        }
    }

    public void extractFileTo(String sourcePath, File dir) throws IOException {
        this.extractFile(sourcePath, this.crc(this.readFile(sourcePath)), new File(dir, new File(sourcePath).getName()));
    }

    private File getExtractedFile(String dirName, String fileName) {
        File file22;
        File idealFile = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + dirName, fileName);
        if (this.canWrite(idealFile)) {
            return idealFile;
        }
        try {
            file22 = File.createTempFile(dirName, null);
            if (file22.delete() && this.canWrite(file22 = new File(file22, fileName))) {
                return file22;
            }
        }
        catch (IOException file22) {
            // empty catch block
        }
        file22 = new File(System.getProperty("user.home") + "/.libgdx/" + dirName, fileName);
        if (this.canWrite(file22)) {
            return file22;
        }
        file22 = new File(".temp/" + dirName, fileName);
        if (this.canWrite(file22)) {
            return file22;
        }
        if (System.getenv("APP_SANDBOX_CONTAINER_ID") != null) {
            return idealFile;
        }
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean canWrite(File file) {
        File testFile;
        File parent = file.getParentFile();
        if (file.exists()) {
            if (!file.canWrite() || !this.canExecute(file)) {
                return false;
            }
            testFile = new File(parent, UUID.randomUUID().toString());
        } else {
            parent.mkdirs();
            if (!parent.isDirectory()) {
                return false;
            }
            testFile = file;
        }
        try {
            new FileOutputStream(testFile).close();
            if (!this.canExecute(testFile)) {
                boolean bl = false;
                return bl;
            }
            boolean bl = true;
            return bl;
        }
        catch (Throwable ex) {
            boolean bl = false;
            return bl;
        }
        finally {
            testFile.delete();
        }
    }

    private boolean canExecute(File file) {
        try {
            Method canExecute = File.class.getMethod("canExecute", new Class[0]);
            if (((Boolean)canExecute.invoke(file, new Object[0])).booleanValue()) {
                return true;
            }
            Method setExecutable = File.class.getMethod("setExecutable", Boolean.TYPE, Boolean.TYPE);
            setExecutable.invoke(file, true, false);
            return (Boolean)canExecute.invoke(file, new Object[0]);
        }
        catch (Exception canExecute) {
            return false;
        }
    }

    private File extractFile(String sourcePath, String sourceCrc, File extractedFile) throws IOException {
        String extractedCrc = null;
        if (extractedFile.exists()) {
            try {
                extractedCrc = this.crc(new FileInputStream(extractedFile));
            }
            catch (FileNotFoundException fileNotFoundException) {
                // empty catch block
            }
        }
        if (extractedCrc == null || !extractedCrc.equals(sourceCrc)) {
            try {
                int length;
                InputStream input = this.readFile(sourcePath);
                extractedFile.getParentFile().mkdirs();
                FileOutputStream output = new FileOutputStream(extractedFile);
                byte[] buffer = new byte[4096];
                while ((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
                input.close();
                output.close();
            }
            catch (IOException ex) {
                throw new GdxRuntimeException("Error extracting file: " + sourcePath + "\nTo: " + extractedFile.getAbsolutePath(), ex);
            }
        }
        return extractedFile;
    }

    private void loadFile(String sourcePath) {
        String sourceCrc = this.crc(this.readFile(sourcePath));
        String fileName = new File(sourcePath).getName();
        File file = new File(System.getProperty("java.io.tmpdir") + "/libgdx" + System.getProperty("user.name") + "/" + sourceCrc, fileName);
        Throwable ex = this.loadFile(sourcePath, sourceCrc, file);
        if (ex == null) {
            return;
        }
        try {
            file = File.createTempFile(sourceCrc, null);
            if (file.delete() && this.loadFile(sourcePath, sourceCrc, file) == null) {
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        file = new File(System.getProperty("user.home") + "/.libgdx/" + sourceCrc, fileName);
        if (this.loadFile(sourcePath, sourceCrc, file) == null) {
            return;
        }
        file = new File(".temp/" + sourceCrc, fileName);
        if (this.loadFile(sourcePath, sourceCrc, file) == null) {
            return;
        }
        file = new File(System.getProperty("java.library.path"), sourcePath);
        if (file.exists()) {
            System.load(file.getAbsolutePath());
            return;
        }
        throw new GdxRuntimeException(ex);
    }

    private Throwable loadFile(String sourcePath, String sourceCrc, File extractedFile) {
        try {
            System.load(this.extractFile(sourcePath, sourceCrc, extractedFile).getAbsolutePath());
            return null;
        }
        catch (Throwable ex) {
            return ex;
        }
    }

    static {
        String vm = System.getProperty("java.runtime.name");
        if (vm != null && vm.contains("Android Runtime")) {
            isAndroid = true;
            isWindows = false;
            isLinux = false;
            isMac = false;
            is64Bit = false;
        }
        if (!(isAndroid || isWindows || isLinux || isMac)) {
            isIos = true;
            is64Bit = false;
        }
        loadedLibraries = new HashSet();
    }
}

