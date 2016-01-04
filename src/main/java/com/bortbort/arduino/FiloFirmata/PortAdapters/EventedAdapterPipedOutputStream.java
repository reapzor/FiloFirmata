package com.bortbort.arduino.FiloFirmata.PortAdapters;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

/**
 * Created by chuck on 1/4/2016.
 */
public class EventedAdapterPipedOutputStream extends PipedOutputStream {
    private ArrayList<AdapterInputStreamListener> inputStreamListeners = new ArrayList<>();

    public EventedAdapterPipedOutputStream(PipedInputStream snk) throws IOException {
        super(snk);
    }

    public EventedAdapterPipedOutputStream() {
        super();
    }

    public void addInputStreamListener(AdapterInputStreamListener inputStreamListener) {
        if (!inputStreamListeners.contains(inputStreamListener)) {
            inputStreamListeners.add(inputStreamListener);
        }
    }

    public void removeInputStreamListener(AdapterInputStreamListener inputStreamListener) {
        inputStreamListeners.remove(inputStreamListener);
    }

    @Override
    public void write(int b) throws IOException {
        super.write(b);
        fireEvent(1);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        fireEvent(len-off);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        fireEvent(b.length);
    }

    private void fireEvent(Integer byteCount) {
        for (AdapterInputStreamListener inputStreamListener : inputStreamListeners) {
            inputStreamListener.inputStreamAvailable(new AdapterInputStreamEvent(byteCount));
        }
    }
}
