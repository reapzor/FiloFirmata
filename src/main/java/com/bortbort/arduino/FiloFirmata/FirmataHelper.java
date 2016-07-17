package com.bortbort.arduino.FiloFirmata;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Created by chuck on 1/4/2016.
 */
public class FirmataHelper {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHexString(byte... bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String decodeTwoSevenBitByteString(byte[] bytes) throws UnsupportedEncodingException {
        return decodeTwoSevenBitByteString(bytes, 0, bytes.length);
    }

    public static String decodeTwoSevenBitByteString(byte[] bytes, int offset, int size)
            throws UnsupportedEncodingException {
        byte[] decodedBytes = decodeTwoSevenBitByteSequence(bytes, offset, size);
        return new String(decodedBytes, "UTF-8");
    }

    public static byte[] decodeTwoSevenBitByteSequence(byte[] encodedBytes) {
        return decodeTwoSevenBitByteSequence(encodedBytes, 0, encodedBytes.length);
    }

    public static byte[] decodeTwoSevenBitByteSequence(byte[] encodedBytes, int offset, int size) {
        size = size >>> 1;
        ByteBuffer byteBuffer = ByteBuffer.allocate(size);

        for (int x = 0; x < size; x++) {
            byteBuffer.put(
                    decodeTwoSevenBitByteSequence(
                            encodedBytes[offset++],
                            encodedBytes[offset++]));
        }

        return byteBuffer.array();
    }

    public static byte decodeTwoSevenBitByteSequence(byte byte1, byte byte2) {
        return (byte) (byte1 + (byte2 << 7));
    }


    public static byte[] encodeTwoSevenBitByteSequence(String string) {
        return encodeTwoSevenBitByteSequence(string.getBytes());
    }

    public static byte[] encodeTwoSevenBitByteSequence(byte... bytes) {
        return encodeTwoSevenBitByteSequence(bytes, 0, bytes.length);
    }

    public static byte[] encodeTwoSevenBitByteSequence(byte[] bytes, int offset, int size) {
        int encodedSize = size * 2;
        ByteBuffer byteBuffer = ByteBuffer.allocate(encodedSize);

        for (int x = offset; x < size; x++) {
            // Mask by 01111111 as we only want to preserve the first 7 bits only
            byteBuffer.put((byte) (bytes[x] & 0x7F));
            // Shift the byte over by 7 bits
            byteBuffer.put((byte) (bytes[x] >>> 7 & 0x7f));
        }

        return byteBuffer.array();
    }


    public static Boolean fastReadBytesWithTimeout(InputStream inputStream,
                                                   byte[] buffer, long timeout) throws IOException {
        return fastReadBytesWithTimeout(inputStream, buffer, buffer.length, timeout);
    }

    public static Boolean fastReadBytesWithTimeout(InputStream inputStream,
                                                   byte[] buffer, int length,
                                                   long timeout) throws IOException {
        int readByteCount = 0;
        long timeoutTime = System.currentTimeMillis() + timeout;

        while (System.currentTimeMillis() < timeoutTime && readByteCount < length) {
            int readByte = inputStream.read();
            if (readByte == -1) {
                return false;
            }
            else {
                buffer[readByteCount] = (byte) readByte;
                readByteCount += 1;
            }
        }

        return true;
    }

}
