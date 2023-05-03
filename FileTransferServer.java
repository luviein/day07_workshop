package com.yl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class FileTransferServer {
    public static void main (String[] args) throws IOException{
        ServerSocket server = new ServerSocket(8080);
        System.out.printf("Staring file transfer on port 8080\n");
        Socket client = server.accept();
        
        

        try {
            InputStream is = client.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            System.out.printf("Transferring file %s (%d) ", fileName, fileSize);


            byte[] buffer = new byte[4*1024];
            long totalReceived = 0;
            int size = 0;
            OutputStream os = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            while((size = dis.read(buffer)) > 0){
                bos.write(buffer, 0, size);
                totalReceived += size;
                System.out.printf("total bytes received: %d\n", totalReceived);
            }

            
            
            System.out.printf("final bytes received: %d\n", totalReceived);
            
            if(fileSize == totalReceived){
                System.out.println("File Size Match");
            }
            bos.flush();
            bos.close();
            os.close();

        }finally{
            client.close();

        }
        server.close();
    }
}
