package io.github.nx.LunaX.engine.serialization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.github.nx.LunaX.engine.GameContainer;

public class SaveManager{
	
	public String directory = System.getProperty("user.dir")+"/saves/";
	
	public SaveManager(GameContainer gc) { 
		File dir = new File(directory);
		if(!dir.exists()) {
			dir.mkdir();
		}
	}
	
	public void saveInt(String obj, String name, int value) {
		String curDir = directory+"/"+obj+"/";
		File dir = new File(curDir);
		if(!dir.exists()) { dir.mkdir(); }
		File file = new File(curDir+name+".save");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(curDir+name+".save");
			String str = Integer.toString(value);
			byte[] strToBytes = str.getBytes();
		    outputStream.write(strToBytes);
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveFloat(String obj, String name, float value) {
		String curDir = directory+"/"+obj+"/";
		File dir = new File(curDir);
		if(!dir.exists()) { dir.mkdir(); }
		File file = new File(curDir+name+".dat");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(curDir+name+".dat");
			String str = Float.toString(value);
			byte[] strToBytes = str.getBytes();
		    outputStream.write(strToBytes);
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveString(String obj, String name, String value) {
		String curDir = directory+"/"+obj+"/";
		File dir = new File(curDir);
		if(!dir.exists()) { dir.mkdir(); }
		File file = new File(curDir+name+".dat");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(curDir+name+".dat");
			byte[] strToBytes = value.getBytes();
		    outputStream.write(strToBytes);
		    outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public int loadInt(String obj, String name) throws IOException {
		String curDir = directory+"/"+obj+"/";
		Path path = Paths.get(curDir+name+".save");

	    BufferedReader reader;
		reader = Files.newBufferedReader(path);
	    String line = reader.readLine();
	    int value = Integer.parseInt(line);
		return value;
		
	}
	
	@SuppressWarnings("resource")
	public float loadFloat(String obj, String name) throws IOException {
		String curDir = directory+"/"+obj+"/";
		Path path = Paths.get(curDir+name+".save");;

	    BufferedReader reader;
		reader = Files.newBufferedReader(path);
	    String line = reader.readLine();
	    float value = Float.parseFloat(line);
		return value;
		
	}
	
	@SuppressWarnings("resource")
	public String loadString(String obj, String name) throws IOException {
		String curDir = directory+"/"+obj+"/";
		Path path = Paths.get(curDir+name+".save");

	    BufferedReader reader;
		reader = Files.newBufferedReader(path);
	    String line = reader.readLine();
		return line;
	
	}
}
