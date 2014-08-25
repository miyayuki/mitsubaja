import gnu.io.*;
import java.io.*;
import java.util.*;

public class PortList {
    
    Enumeration portList;

    public PortList(){
        // �|�[�g�̃��X�g��擾
        portList = CommPortIdentifier.getPortIdentifiers();
    }

    public void showList(){
        CommPortIdentifier portID;

        while(portList.hasMoreElements()){
            // ���X�g����|�[�g����o��
            portID = (CommPortIdentifier)portList.nextElement();

            // �|�[�g�̖��O
            System.out.print("Port Name : " + portID.getName() + ",");

            // �|�[�g�̎g�p��
            if(portID.isCurrentlyOwned()){
                System.out.print(" Owned,");
            }else{
                System.out.print(" Not Owned,");
            }

            // �|�[�g�̃^�C�v (�V���A�� or �p������)
            switch(portID.getPortType()){
              case CommPortIdentifier.PORT_SERIAL:
                System.out.println(" Kind : Serial");
                break;
              case CommPortIdentifier.PORT_PARALLEL:
                System.out.println(" Kind : Parallel");
                break;
            }
        }
    }

    public static void main(String args[]){
        (new PortList()).showList();
    }
}