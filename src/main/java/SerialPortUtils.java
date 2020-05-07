import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SerialPortUtils {

    /**
     * 串口开关标记
     */
    private boolean portOpenTag = false;
    private SerialPort ser;
    private InputStream inputStream;
    private OutputStream outputStream;

    /**
     * 打开串口
     *
     * @param comName
     */
    public void openCom(String comName) throws IOException {

        if (!portOpenTag) {

            try {
                CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(comName);
                ser = (SerialPort) commPortIdentifier.open("czk", 2000);
                ser.setRTS(true);  //打开RTS
                ser.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);


            } catch (NoSuchPortException e) {
                e.printStackTrace();
                System.out.println("串口号错误，错误的串口号：{}" + comName);
            } catch (PortInUseException e) {
                e.printStackTrace();
                System.out.println("串口被占用");
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
                System.out.println("不支持的串口参数");

            }

            inputStream = ser.getInputStream();
            outputStream = ser.getOutputStream();
            portOpenTag = true;

        } else {

            //一个串口操作工具只能操作一个串口,如果想要操作多个串口就创建多个对象
            throw new RuntimeException("该串口操作对象已经打开了一个串口");
        }

    }

    /**
     * 向串口写入字节数组
     * @param data
     * @throws IOException
     */
    public void writeByteData(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }
    /**
     * 向串口写入文本数据
     * @param data
     */
    public void writeTextData(String data) throws IOException {
        //串口通信要求的字符集是ASCII
        byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * 从串口读取字节数组
     * @return
     */
    public byte[] readByteData() throws IOException {
        // 通过输入流对象的available方法获取数组字节长度
        int i = inputStream.available();
        if (i < 0){
            return new byte[0];
        }
        byte[] readBuffer = new byte[i];
        int readed = inputStream.read(readBuffer);
        return readBuffer;
    }
    /**
     * 从串口读取文本信息
     * @return
     */
    public String readTextData() throws IOException {
        byte[] bytes = readByteData();
        return new String(bytes, StandardCharsets.US_ASCII);
    }
    public void closePort(){
        //将串口开关状态设为false
        portOpenTag = false;
        try {
            if (outputStream != null){
                outputStream.close();
            }
            if (inputStream != null){
                inputStream.close();
            }
            if (ser != null){
                ser.close();
            }
        } catch (IOException e) {
            System.out.println("串口关闭异常");
        }
    }

    /**
     * 获取该工具对象是否已经打开了一个串口
     * @return
     */
    public boolean isPortOpen() {
        return portOpenTag;
    }


}
