# Filo Firmata Client Library
Filo Firmata is a Java client library designed to allow object based communication over a serial port using the Firmata protocol. 

#### Firmata Protocol
Firmata is a duplication of the standard MIDI protocol that has been adapted to work with hardware project boards such as Arduino for controlling and reading values from sensors or custom code embedded within the project board. The Firmata project expands the capibilities of an Arduino or similar device by allowing you to do deeper processing from another platform, and programming language, such as a Raspberry Pi or a laptop computer. The Firmata protocol  has standard support for reading pins, writing to pins, reading analog sensors, etc, but also allows for you to integrate custom commands so that you can implement the library to suit your communication needs. FiloFirmata is a client for this protocol, supporting the base command structure within Firmata, with a design pattern that allows you to quickly define custom commands and data that can communicate with your Firmata library implementation within your project board.

#### Firmata Messages
Firmata messages are a simple object that contains values, indexes, and other sets of data that was passed either to or from your project board. Messages that get sent to the project board get serialized into a stream of bytes and are then sent to the project board over a serial communications port. Messages that come from the project board are parsed as a byte stream from the serial port, and built into a message object representing the data that the message contained as a series of Java values or objects for reading and handling within your Java application.

FiloFirmata is an event driven library. Every message passed up or down the Arduino device counts as an event.

For more information on the Firmata Protocol and its use on an Ardiuno project board, see https://github.com/firmata/protocol and https://github.com/firmata/arduino

## Installation
Maven:
```xml
<dependency>
    <groupId>com.bortbort.arduino</groupId>
    <artifactId>filo-firmata</artifactId>
    <version>0.1.1</version>
</dependency>
```

Gradle:
```gradle
compile 'com.bortbort.arduino:filo-firmata:0.1.1'
```

## Integrating With Your Project
The core of this library uses a pure java serial port communications driver. You do not need to install any platform specific software to run this library. See https://github.com/nyholku/purejavacomm for details on the serial port interface. 

FiloFirmata requires a configuration. The configuration class provides the ability to customize the data port, bit rates, protocol verification, and more. A default configuration can be generated by simply creating a new object instance of FiloFirmataConfiguration.
For basic usage you will need to only supply a port identifier (COM3, /dev/..).
Please note that FiloFirmata creates a copy of the configuration fed to instantiate the library. Any changes to your configuration object after creating the library will be ignored by the library instance.
```java
// No configuration needed for COM3 MS Windows clients
Firmata firmata = new Firmata();
```
```java
// Configuration for an arduino device running on an Apple computer.
Firmata firmata = new Firmata("/dev/tty.usbmodemXXXXXX");
```
```java
//Configuration for an arduino device running on an Apple computer with a non default baudrate.
Firmata firmata = new Firmata("/dev/tty.usbmodemXXXXXX", 9600);
```
```java
// Configuration for an arduino device running on an Apple computer that disables
//   the communications to to see if the serial connected device supports our firmata protocol.
FirmataConfiguration firmataConfiguration = new FirmataConfiguration("/dev/tty.usbmodemXXXXXX");
// We dont want to test communications, because we always know this port has a firmata device. 
firmataConfiguration.setTestProtocolCommunication(false);
// Create a new firmata instance using the custom configuration.
//   (A copy of the configuration will be generated and used by the library)
Firmata firmata = new Firmata(firmataConfiguration);
// This change ignored by the implemented Firmata library above.
firmataConfiguration.setSerialPortBaudRate(57600);
```


## Basic Usage
```java
// Somewhere in your application you generate a new firmata object (Can be restarted as needed)
Firmata firmata = new Firmata();

// Somewhere in your application you start up the library and begin communications through the serial port.
firmata.start();


/* SYNCHRONOUS (Send with expected instant reply)*/
// You want to ask the project board what firmware it is running. Send a firmware query and populate
//    the firmwareResponse parameter with the firmware response message.
SysexReportFirmwareMessage firmwareMessage =
    firmata.sendMessageSynchronous<SysexReportFirmwareMessage>(new SysexReportFirmwareQueryMessage());
System.out.println(firmwareMessage.getFirmwareName());
System.out.println(firmwareMessage.getMajorVersion());
System.out.println(firmwareMessage.getMinorVersion());

/* ASYNCHRONOUS (Send with no expected reply, or potentially many replies) */
// You want to print out to console the firmware name and version of your project board whenever it is sent up
//   So you create a listener that will fire every time a the specific message is received.
MessageListener<SysexReportFirmwareMessage> firmwareListener =
        new MessageListener<SysexReportFirmwareMessage>() {
    @Override
    public void messageReceived(SysexReportFirmwareMessage message) {
        System.out.println(message.getFirmwareName());
        System.out.println(message.getMajorVersion());
        System.out.println(message.getMinorVersion());
    }
};

// Somewhere in your application you decide to tell the Firmata library that it should rout the firmware messages to your new listener.
//   Listeners can be added or removed while the library is started or stopped.
firmata.addMessageListener(firmwareListener);

// Somewhere in your application you ask the project board to send its firmware name and version to us.
//   You do this by sending a ReportFirmware 'sysex' firmata message to the board.
//   Its response will be handled by your new listener above.
firmata.sendMessage(new SysexReportFirmwareQueryMessage());

// At some point you do not care to respond to or handle firmware name messages being passed by the project board.
//   So you remove your listener to tell the library you wish to ignore these messages now.
firmata.removeMessageListener(firmwareListener);



// At some point you are done talking over the serial port, so you decide to shut down the Firmata library.
firmata.stop();
```


## Sending Messages
Any message that implements the abstract TransmittableMessage class can be transmitted through the serial port. This base class supports the serialization methods needed to convert your data message into a byte stream. For the base Firmata Protocol, these messages have been predefined for you.
```java
// Ask the project board to start eventing the analog (adc) values of analog pin 2. (0 indexed)
firmata.sendMessage(new ReportAnalogPinMessage(2, true));
// Ask the project board to stop eventing the analog (adc) values of analog pin 2. (0 indexed)
firmata.sendMessage(new ReportAnalogPinMessage(2, false));
// Ask the project board to reboot.
firmata.sendMessage(new SystemResetMessage());
```

## Receiving Messages
FiloFirmata has a parsing system that builds message objects up from a byte stream sent by the serial port. When a message is identified and built up, there is a system in place to then route this message to any bit of code that is interested in reading and handling its data. This is done through an event listening design. To handle a message sent up by the project board, add an event listener implementation for the message. The listener can be added and removed as necessary.
```java
// Example listener that reads protocol version values from a Firmata Message that was sent by the project board.
private final MessageListener<ProtocolVersionMessage> versionListener = new MessageListener<ProtocolVersionMessage>() {
    @Override
    public void messageReceived(ProtocolVersionMessage message) {
        // Log the major and minor firmata firmware version reported to us by the Arduino / project board.
        log.info("Detected Firmata device protocol version: {}.{}",
              message.getMajorVersion(), message.getMinorVersion());
    }
};

// Register the ProtocolVersionMessage listener so it will pick up all ProtocolVersion messages.
firmata.addMessageListener(versionListener);
```

Some messages use a 'Channel' byte to identify the pin the message represents (See the analog/digital pin reporting mechanism in the Firmata protocol). To listen to messages for a specific channel, add your listener with an identifier indicating witch pin or port you want the listener to handle. To listen to messages from all channels that the message type supports, do not provide an identifier.
```java
// Handle analog messages from pin 2 evented to us from the project board (pin 2 request is handled below);
MessageListener<AnalogMessage> analogListener = MessageListener<AnalogMessage>() {
    @Override
    public void messageReceived(AnalogMessage message) {
        assertTrue(message.getChannelInt() == 2);
        if (message.getAnalogValue() > 100) {
            System.out.println("Pin ADC value is greater than 100!");
        }
    }
};

// Only listen to analog message events that correspond to analog pin/channel 2 on the project board (0 indexed).
firmata.addMessageListener(2, analogListener);
```

If you want to listen to all or any messages, you can implement a generic message listener.
```java
// Create a generic listener that will fire whenever any message is received.
MessageListener<Message> messageListener = MessageListener<Message>() {
    @Override
    public void messageReceived(Message message) {
        System.out.println(message.getClass().getSimpleName());
    }
};

// Register the listener for all messages coming in from the board.
firmata.addMessageListener(messageListener);

// OR Register the listener for all messages coming in for "Channel 2" (Pin or port depending on command) (0 indexed)
firmata.addMessageListener(2, messageListener);

// OR Register the listener for a few specific message types
// All sysex report firmaware types
firmata.addMessageListener(SysexReportFirmwareMessage.class, messageListener);
// All protocol version types
firmata.addMessageListener(ProtocolVersionMessage.class, messageListener);
// All ports of a digital port message type
firmata.addMessageListener(DigitalPortMessage.class, messageListener);
// Only port 2 of an analog message type
firmata.addMessageListener(2, AnalogMessage.class, messageListener);
```

## Synchronous Messages
Some parts of the Firmata library use a simple request & response pattern where if you send a message, you will get a specific response about your request. For these types of message, using the asynchronous listener pattern may be a little overkill. A method has been created that will allow you to specific the transmission message as well as the expected message type to be returned and defined, all within one line. You must supply the expected message response type in the method.
```java
// We want to send a firmware query request to the project board. Send the query message synchronously.
//   The board will reply with its firmware details immediately to this request (or, at least, we expect it to)
//   Since the response will be a SysexReportFirmwareMessage object, tell the method this is the class we are
//     expecting to receive as a response.
//   Wait for the expected reply message, then return the response message, populating the firmwareMessage parameter.
SysexReportFirmwareMessage firmwareMessage =
    firmata.sendMessageSynchronous<SysexReportFirmwareMessage>(new SysexReportFirmwareQueryMessage());

// We can now identify the firmware details that the project board sent up from our request.
System.out.println(firmwareMessage.getFirmwareName());
```

## Transmitting Custom Messages
Implementing your own custom Firmata messages into the FiloFirmata library is quite simple, and desired! Simply pick the base class you need for your type of message and implement the serialize callback to translate your message into a byte stream.

For example, if we want to implement some custom Firmata command that sends two separate strings of data to the Arduino / project board, we need to write the class and we need to pick a command byte that the Firmata protocol is not using. For this example, lets pick the byte "0x20". Please be weary of Firmata/MIDI protocol design requirements when picking your command byte. The two strings will be separated by a proprietary separator byte that when the project board sees, will know to start building the second string. Lets use "0xFF", since the strings can only be within 0x00-0x7f by using the 'two seven bit byte' Firmata base design for transmitting complex data. 
```java
public class TwoStringTransmitMessage extends TransmittableMessage {
    private String string1;
    private String string2;

    public TwoStringTransmitMessage(String string1, String string2) {
        // Tell the FiloFirmata library that our custom message is using Firmata command byte "0x20"
        super((byte) 0x20);
        this.string1 = string1;
        this.string2 = string2;
    }

    @Override
    protected Boolean serialize(ByteArrayOutputStream outputStream) {
        try {
            // Convert the java strings to a "two 7 bit byte" array
            //   See Firmata Protocol documentation.
            //   (avoids accidentally sending Firmata commands in data)
            byte[] string1Bytes = DataTypeHelpers.encodeTwoSevenBitByteSequence(string1);
            byte[] string2Bytes = DataTypeHelpers.encodeTwoSevenBitByteSequence(string2);
            outputStream.write(string1Bytes);
            outputString.write((byte) 0xFF);
            outputStream.write(string2Bytes);
            outputString.write((byte) 0xFF);
            return true;
        } catch (IOException e) {
            System.out.println("Message not built! Unable to convert strings to byte array!");
            return false;
        }
    }
}

// Send a Two String Message to our project board
firmata.sendMessage(new TwoStringTransmitMessage("This is String 1", "This is String 2"));
```

## Receiving Custom Messages
To implement a handler and custom message that is coming from your Arduino or project board, several classes must be built up and then registered within the FiloFirmata library. Whenever a registered command byte is identified over the serial port, a message builder for the command will be called which will parse the stream into a Message object. To handle, parse, and pass a custom message from the board, you will need to write 2 classes: a Message containing the data that was parsed from the custom command, and a Builder to parse the message up from the serial port byte stream. The builder class must be registered with the corresponding command byte to let the FiloFirmata library know that any message passed up from the project board with your custom command byte needs to be parsed by your custom message parser.


For the example, we will use the same Two String Message command above, however now we will be reading two strings from the project board, instead of sending them.

* Define a TwoStringReceieve message that represents two string values sent by the project board.
```java
public class TwoStringReceiveMessage implements Message {
    private String string1;
    private String string2;

    public TwoStringReceiveMessage(String string1, String string2) {
        this.string1 = string1;
        this.string2 = string2;
    }

    public String getString1() {
        return string1;
    }

    public String getString2() {
        return string2;
    }
}
```

* Create a MessageBuilder that will be called whenever the the command byte (0x20) for the message is identified by the FiloFirmata library.
```java
public class TwoStringReceiveBuilder extends MessageBuilder {

    // Handle all messages sent by the project board using command byte "0x20".
    public TwoStringReceiveBuilder() {
        super((byte) 0x20);
    }

    // Note: Do not use scanners or read more from the stream than needed to parse your message,
    //   else you may corrupt the next message in the stream.
    @Override
    public Message buildMessage(Byte pinByte, InputStream inputStream) {
        // pinByte ignored as this is not a channel / pin based Firmata message

        // Allocate a byte buffer to build the two strings up.
        ByteBuffer stringBuffer = ByteBuffer.allocate(32);

        // Strings array to hold the two strings once they have been built up.
        String[] stringsArray = new String[2];

        // Two iterations for two strings
        for (int x = 0; x < 2; x++) {
            int currentByte;
            // Read every byte and only break if the strings are done, or if the stream closes.
            while (true) {
                try {
                    currentByte = inputStream.read();
                } catch (IOException e) {
                    // Error reading from stream
                    return null;
                }

                if (currentByte == 0xFF) {
                    try {
                        // String is done being built. Convert and save as a Java String object.
                        stringsArray[x] = DataTypeHelpers.decodeTwoSevenBitByteString(stringBuffer.array());
                    } catch (UnsupportedEncodingException e) {
                        // Error converting string from 'two 7 bit byte' Firmata protocol design.
                        return null;
                    }
                    // Reset the buffer to prepare for the next string.
                    stringBuffer.clear();
                    // Start building the other string, or break out of the final loop.
                    break;
                } else if (currentByte == -1) {
                    // Stream read error
                    return null;
                } else {
                    // The byte is part of string 1 or string 2. Add to buffer.
                    stringBuffer.put((byte) currentByte);
                }
            }
        }

        // Create a new instance of a TwoStringReceive message and return it to the FiloFirmata library.
        return new TwoStringReceiveMessage(stringsArray[0], stringsArray[1]);
    }
}
```

* Register the TwoStringReceieve message builder statically in our project (or anywhere you wish)
```java
static {
    Firmata.addCustomCommandParser(new TwoStringReceiveBuilder());
}
```

## Transmitting & Receiving Custom SYSEX Messages
Designing a custom SYSEX message is very similar to designing a custom message above. It is suggested you use SYSEX for custom messages in general as it has a dedicated set of bytes for custom messages that the Firmata protocol has promised to not implement internally.

Please see the code base for examples of implementing your own custom SYSEX message. 



### Contributions Welcome!
