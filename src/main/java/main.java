import java.io.IOException;

public class main {
    
    
    public static void main(String [] args){
        System.out.println("hello world");

        SerialPortUtils utils=new SerialPortUtils();
        try {
            utils.openCom("COM4");
            while(!utils.isPortOpen()){ }
            while(true){
                byte[] bytes = utils.readByteData();

                String str=BinaryUtils.bytesToString(bytes);
                if (!str.isEmpty()) {
                    System.out.println(str);
                    utils.writeTextData(str+"Bear");
                }

            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
