# A simple file encryption and transfer protocol


This is a multi-module maven project. It consists out of three modules

1. Server - a simple server whose purpose is to store files on upload request and retrieve files on get file request,
2. Client - console app which allows users to upload/download files to/from the server,
3. A separate library - it provides the API for encryption/decryption of the files, as well as a couple of common functions used for file manipulation.


## File transfer flow

In order to transfer files between client and server, a simple custom protocol is implemented on top of Java Web Sockets.  

The upload file flow has following steps:
1. The client sends UPLOAD_OPERATION request,
2. The server responds with SERVER_READY message,
3. The client sends message that contains file metadata and file data.

   The first 4 bytes of the message represent the size of a file name, after that the file name is sent. The next 8 bytes are reserverd for file size, which is followed by file content.

The download file flow consists of the following steps:
1. The client sends DOWNLOAD_OPERATION request,
2. The server responds with SERVER_READY message,
3. The client sends file name message,
4. The server responds with FILE_FOUND_SUCCESSFULLY/FILE_NOT_FOUND message,
5. The server sends meesage that contains file metadata and file data.

   The first 8 bytes of this message are reserved for file size, followed by file content.


## File encryption

Before uploading files to the server, client encrypts them using separate library's API. Furthermore, when a file is downloaded from the server it is decrypted using the same API.
On client startup it generates AES key which is used for encryption/decryption of the files. Notice that this key is only present in memory, hence once client is stoped the key is lost.


# Running project

The only dependency that is used is:
```
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcprov-jdk15on</artifactId>
  <version>1.68</version>
</dependency>
```

The first thing to do is to build all modules by running:
```
mvn clean install
```
in the root directory of the project.

After that, start the server buy positioning into server directory and run command:
```
mvn compile exec:java -Dexec.mainClass="org.challenge.App"
```

Furthermore, start the client by positioning into cleint directory and run the same command.

On startup of both, the server and the client, you will be prompted to enter absoulute path of working directories, which will later be used as file storage.


## Future improvements

1. In the real case scenario the key which is used for encryption/decryption of the files should be encrypted using private/public key pair, and then shared between sender and receiver. 
This key should not be stored only in memory and should exist as long as the files which are encrypted using it exist. 
2. Similarly, in the real case scenario, the system should not use a file system of a host as a primary storage.
3. The encryption/decryption key should be unique per file.
4. The current implementation uses file names as identificators, instead it should use file ids.
5. Exception handling should be improved.
6. Java documentation and logging should be added.


