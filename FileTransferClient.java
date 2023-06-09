package com.yl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DataTruncation;

import javax.sound.sampled.SourceDataLine;

/**
 * Hello world!
 *
 */
public class FileTransferClient 
{
    public static void main( String[] args ) throws UnknownHostException, IOException
    {
        Path p = Paths.get(args[0]);
        File f = p.toFile();

        String fileName = f.getName();
        long fileSize = f.length();

        System.out.printf(">>> file name : %s, size: %d\n", fileName, fileSize);

        Socket sock = new Socket("localhost", 8080);
        try{
            OutputStream os = sock.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);


            
            //Send file metadata
            dos.writeUTF(fileName);
            dos.writeLong(fileSize);
            dos.flush();

            //4k buffer
            byte[] buff = new byte [4*1024];
            int size = 0;
            String msg = "";
            InputStream is = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            while((size = bis.read(buff)) > 0){
                dos.write(buff, 0, size);
                dos.flush();
            }

            msg = dis.readUTF();
            System.out.printf(">>> from server: %s\n", msg);
            

            dos.close();
            is.close();


        }finally{
            sock.close();
        }
    }
}
