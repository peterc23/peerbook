package ece454p1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class FileUtils {

	public static String getAbsolutePath(String fullRelativePath) {
		String path = fullRelativePath;
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		path = Config.SHARE_PATH + path;
		return path;
	}
	public static void deleteChuckFiles(String absolutePath) {
		int pos = absolutePath.lastIndexOf('/') + 1;
		String filename = absolutePath.substring(pos);
		File folder = new File(absolutePath.substring(0, pos));
		File [] listOfFiles = folder.listFiles();
		if (listOfFiles == null) return;
		for (File file : listOfFiles) {
			if (file.getName().startsWith(filename+".part")) {
				file.delete();
			}
		}
	}
	
	public static void deleteTorrentAndFiles(String relativePath) {
		int pos = relativePath.lastIndexOf('/') + 1;
		String filename = relativePath.substring(pos);
		File folder = new File(Config.SHARE_PATH + relativePath.substring(0, pos));
		File[] files = folder.listFiles();
		if (files == null) return;
		for (File file : files) {
			if (file.getName().startsWith(filename)) {
				file.delete();
			}
		}
	}
	
	public static void writeFile(String filePath, String fileContent) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		out.write(fileContent);
		out.close();
	}
	
	public static String readFile( String file ) throws IOException {
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    return stringBuilder.toString();
	}
	
	public static void copyFile(String sourceFile, String destFile) throws IOException {
		copyFile(new File(sourceFile), new File(destFile));
	}
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
			destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	public static String mapToJSON(Object object) {
		ObjectMapper oMapper = new ObjectMapper();
		String result = null;
		try {
			result = oMapper.writeValueAsString(object);
		} catch (Exception e) {
		}
		return result;
	}
	
	public static Object mapToObject(String json, Class<?> t) {
		ObjectMapper oMapper = new ObjectMapper();
		Object result = null;
		
		try {
			result = oMapper.readValue(json, t);
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}
	
	public static ArrayList<File> divideFiles(File file){

		ArrayList<File> fileList = new ArrayList<File>();
		long fileSize = file.length();
		int read = 0;
		int i =1;
		int readLength = Config.CHUNK_SIZE;
		if (file.length() > Config.CHUNK_SIZE) {
			try{
				InputStream in = new FileInputStream(file);
				while(fileSize > 0){
					if (fileSize <= readLength){
						readLength = (int)fileSize;
					}
					byte [] byteChunk = new byte[readLength];
					read = in.read(byteChunk, 0, readLength);
					fileSize -= read;
					File destFile = new File(file.getAbsoluteFile()+".part"+i);
					i++;
					OutputStream out = new FileOutputStream(destFile);
					out.write(byteChunk);
					out.flush();
					out.close();
					fileList.add(destFile);
				}
				in.close();
			}catch (Exception e){
				System.out.println(e);
			}
		}else{
			fileList.add(file);
		}
		
		// bug fix, hack
		if (file.length() < Config.CHUNK_SIZE) {
			try {
				FileUtils.copyFile(file.getAbsolutePath(), file.getAbsolutePath()+".part1");
			} catch (Exception e) {
				// do nothing
			}
		}
			
		return fileList;
	}
	
	public static File combineFiles(ArrayList<File> fileList, String fileName){
		File ofile = new File(fileName);
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		try {
		    fos = new FileOutputStream(ofile,true);             
		    for (File file : fileList) {
		        fis = new FileInputStream(file);
		        fileBytes = new byte[(int) file.length()];
		        bytesRead = fis.read(fileBytes, 0,(int)  file.length());
		        assert(bytesRead == fileBytes.length);
		        assert(bytesRead == (int) file.length());
		        fos.write(fileBytes);
		        fos.flush();
		        fileBytes = null;
		        fis.close();
		        fis = null;
		    }
		    fos.close();
		    fos = null;
		}catch(Exception e){
			System.out.println("I am in combine funciton");
			System.out.println(e);
		}
		return ofile;
	}
	
	//Call generateHeaderFile on insert.
	public static File generateHeaderFile(File file, ArrayList<File> fileList, int id){
		
		File returnFile = new File(file.getAbsolutePath() +Config.HEADER_FILE_EXT);
		Writer output = null;
		HeaderFile headFile = new HeaderFile();
		ObjectMapper oMapper = new ObjectMapper();
		
		
		headFile.localChunksPresent = new boolean[fileList.size()];
		for(int i=0; i<fileList.size(); i++){
			headFile.localChunksPresent[i] = true;
		}
		headFile.fileId = id;
		

		try {
			output = new BufferedWriter(new FileWriter(returnFile));
			output.write(oMapper.writeValueAsString(headFile));
			output.close();
		} catch (Exception e) {
			System.out.println("generateHeaderFile");
			System.out.println(e);
		}
		 return returnFile;
	}

	public static File generateHeaderFile(File file, ArrayList<File> fileList, Status status){
		
		File returnFile = new File(file.getAbsolutePath() +Config.HEADER_FILE_EXT);
		Writer output = null;
		HeaderFile headFile = new HeaderFile();
		ObjectMapper oMapper = new ObjectMapper();
		
		
		headFile.localChunksPresent = new boolean[fileList.size()];
		for(int i=0; i<fileList.size(); i++){
			headFile.localChunksPresent[i] = true;
		}
		status.numFiles++;
		headFile.fileId = status.numFiles;
		

		try {
			output = new BufferedWriter(new FileWriter(returnFile));
			output.write(oMapper.writeValueAsString(headFile));
			output.close();
		} catch (Exception e) {
			System.out.println("generateHeaderFile");
			System.out.println(e);
		}
		 return returnFile;
	}
	
	public static synchronized ArrayList<HeaderFile> getHeaderFiles() {
		return getHeaderFiles(Config.SHARE_PATH, "/");
	}
	public static synchronized ArrayList<HeaderFile> getHeaderFiles(String absolutePath, String fullRelativePath) {
		File file = new File(absolutePath);
		File [] fileList = file.listFiles();
		ArrayList<HeaderFile> files = new ArrayList<HeaderFile>();

		for (int i=0; i< fileList.length; i++){
			if (fileList[i].isFile()) {
				HeaderFile headFile = FileUtils.readHeaderFile(fileList[i]);	
				if(headFile != null){
					headFile.fullRelativePath = fullRelativePath+fileList[i].getName();
					files.add(headFile);
				}
			} else {
				ArrayList<HeaderFile> subFiles = getHeaderFiles(fileList[i].getAbsolutePath()+"/", fullRelativePath+fileList[i].getName()+"/");
				if (subFiles != null) {
					files.addAll(subFiles);
				}
			}
		}
		return files;
	}
	
	public static String getHeadPathFromId(ArrayList<HeaderFile> headers, int fileId) {
		
		for (HeaderFile head : headers) {
			if (head.fileId == fileId) {
				return head.fullRelativePath;
			}
		}
		
		return null;
	}
	
	public static String getFileNameFromId(int fileId){
		File folder = new File(Config.SHARE_PATH);
		File[] files = folder.listFiles();
		for (File file : files) {
			HeaderFile headfile = readHeaderFile(file);
			if (headfile != null && headfile.fileId == fileId){
				String fileName = file.getName();
				int pos = fileName.lastIndexOf('.');
				String result = Config.SHARE_PATH+fileName.substring(0, pos);
				return result;
			}
		}
		return null;
	}
	
	public static String getChunkPathFromId(ArrayList<HeaderFile> headers, int headId, int chunk) {
		for (HeaderFile head : headers) {
			if (head.fileId == headId) {
				int pos = head.fullRelativePath.lastIndexOf('.');
				String result = head.fullRelativePath.substring(0, pos)+".part"+chunk;
				return result; 
			}
		}
		return null;
	}
	
	public synchronized static HeaderFile readHeaderFile(File file){
		ObjectMapper oMapper = new ObjectMapper();
		HeaderFile headFile = null;
		
		if(isHeaderFile(file)){
			try{
				FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder str = new StringBuilder();
				String strLine;
				while((strLine = br.readLine()) != null){
					str.append(strLine);
				}
				in.close();
				headFile = oMapper.readValue(str.toString(), HeaderFile.class);
				
			}catch(Exception e){
				System.out.println("read header file");
				System.out.println(e);
			}
		}
		return headFile;
	}
	
	public static HeaderFile updateGlobalHeader(HeaderFile globalHead, HeaderFile peerHead, Status status){
		if (peerHead == null){
			
		}else if(globalHead == null){
			globalHead = peerHead;
			updateChunkReplication(status, peerHead);
		}else if (globalHead.fileId == peerHead.fileId){
				updateChunkReplication(status, peerHead);
				globalHead.localChunksPresent = orChunks(globalHead.localChunksPresent, peerHead.localChunksPresent);
		}
		
		return globalHead;
	}
	
	public static void updateChunkReplication(Status status, HeaderFile peerHead){
		ArrayList<Integer> result;
		if(status.chunkReplication.get(peerHead.fileId).size() != peerHead.localChunksPresent.length){
			result = new ArrayList<Integer>();
			for(int i=0; i< peerHead.localChunksPresent.length;i++){
				result.add(i, 0);
			}
			status.chunkReplication.set(peerHead.fileId, result);
		}
		
		for(int i=0; i<peerHead.localChunksPresent.length;i++){
			if(peerHead.localChunksPresent[i] == true){

				int current = status.chunkReplication.get(peerHead.fileId).get(i);
				current++;
				status.chunkReplication.get(peerHead.fileId).set(i, current);				
			}
		}
	}
	
	public static boolean [] orChunks(boolean [] global, boolean [] peer){
		if(global.length != peer.length) return global;
		boolean [] resultarray = new boolean[global.length];
		
		for(int i=0; i< global.length; i++){
			resultarray[i] = global[i] | peer[i];
		}
		return resultarray;
	}
	
	public static float chunkPercentage(boolean [] chunks){
		int chunksCount = 0;
		for(int i=0; i<chunks.length;i++){
			if (chunks[i] == true){
				chunksCount++;
			}
		}
		return (float)chunksCount/(float)chunks.length;
	}
	
	public static boolean isHeaderFile(File file){
		if (!file.isFile()) return false;
		String fileName = file.getName();
		int pos = file.getName().lastIndexOf('.');
		if (fileName.substring(pos).equalsIgnoreCase(Config.HEADER_FILE_EXT)){
			return true;
		}else{
			return false;
		}
	}
	
	public synchronized static File updateChunkToHeaderFile(File chunkFile) {
		String chunkPath = chunkFile.getPath();
		int dot = chunkPath.lastIndexOf(".");
		StringBuilder path = new StringBuilder(chunkPath.substring(0, dot));
		path.append(Config.HEADER_FILE_EXT);
		File abc = new File (path.toString());
		return updateHeaderFile(abc);
		
	}
	
	public static synchronized File downloadAndCleanHeaderFile(InputStream is, int size, String fullRelativePath) {
		// download header file
		File file = FileUtils.saveFileFromByte(is, size, fullRelativePath);
		// update header file
	    FileUtils.updateHeaderFile(file);
	    
	    return file;
	}
	
	public static File saveFileFromByte(InputStream is, int size, String fullRelativePath) {
		File file = null;
		FileOutputStream os = null;
		BufferedOutputStream bos = null;
		try {
		  byte[] bytearray = new byte[size];
		  int bytesRead = is.read(bytearray, 0, bytearray.length);
		  int current = bytesRead;
		  do {
			  bytesRead = is.read(bytearray, current, bytearray.length-current);
			  if (bytesRead >= 0) current += bytesRead;
		  } while (bytesRead > 0);
		  file = new File(FileUtils.getAbsolutePath(fullRelativePath));
		  if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
		  os = new FileOutputStream(file);
		  bos = new BufferedOutputStream(os);
		  bos.write(bytearray, 0, bytearray.length);
		  bos.flush();
		}
		catch (Exception e) {
			System.out.println(e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
		return file;
	}
	
	public static synchronized void updateHeaderFileId(String absolutePath, int id) {
		ObjectMapper oMapper = new ObjectMapper();
		File file = new File(absolutePath);
		if (!file.exists()) return;
		
		HeaderFile headFile = null;
		Writer output = null;
		headFile = readHeaderFile(file);
		
		headFile.fileId = id;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(oMapper.writeValueAsString(headFile));
			output.close();
		} catch (Exception e) {
			System.out.println("output header file");
			System.out.println(e);
		}
	}
	
	public static synchronized void updateHeaderFileForWrite(String relativePath, HeaderFile file, ArrayList<File> chunks) {
		ObjectMapper oMapper = new ObjectMapper();
		if(relativePath.charAt(0) == '/'){
			relativePath = relativePath.substring(1);
		}
		File returnFile = new File(Config.SHARE_PATH + relativePath + ".torrent");
		HeaderFile headFile = null;
		Writer output = null;
		
		headFile = file;
		
		headFile.localChunksPresent = new boolean[chunks.size()];
		for(int i=0; i<chunks.size(); i++){
			headFile.localChunksPresent[i] = true;
		}
		
		
		try {
			output = new BufferedWriter(new FileWriter(returnFile));
			output.write(oMapper.writeValueAsString(headFile));
			output.close();
		} catch (Exception e) {
			System.out.println("output header file");
			System.out.println(e);
		}
		//return returnFile;
	}
	
	// takes in torrent file
	public static synchronized File updateHeaderFile(File file){
		ObjectMapper oMapper = new ObjectMapper();
		File returnFile = new File(file.getPath());
		HeaderFile headFile = null;
		Writer output = null;
		
		headFile = readHeaderFile(file);
		int pos = file.getName().lastIndexOf('.');
		String partsname = file.getName().substring(0, pos);
		
		for(int i=0; i< headFile.localChunksPresent.length; i++){
			headFile.localChunksPresent[i] = false;
		}
		File folder = file.getParentFile();
		File [] listOfFiles = folder.listFiles();
		if (listOfFiles == null) return file;
		
		for(int i=0; i<listOfFiles.length; i++){
			if(listOfFiles[i].isFile()){
				String fileName = listOfFiles[i].getName();
				if(fileName.startsWith(partsname)){
					int filepos = fileName.lastIndexOf('.');
					String ext = fileName.substring(filepos);
					if (ext.matches(".part[\\d]+")){
						int fileNum = Integer.parseInt(ext.substring(5));
						headFile.localChunksPresent[fileNum-1] = true;
					}
				}
			}
		}
		
		try {
			output = new BufferedWriter(new FileWriter(returnFile));
			output.write(oMapper.writeValueAsString(headFile));
			output.close();
		} catch (Exception e) {
			System.out.println("output header file");
			System.out.println(e);
		}
		return returnFile;
		
		
	}
}