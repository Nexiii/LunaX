package io.github.nx.LunaX.engine.serialization;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Storage {

    private static Map<String, Object> data = new HashMap<>();
    private static String filePath;

    @SuppressWarnings("unchecked")
    public static void init(String path) {
        filePath = path;
        
        if (!Files.exists(Paths.get(filePath))) return;

        try {
            String base64String = new String(Files.readAllBytes(Paths.get(filePath)));
            
            byte[] bytes = Base64.getDecoder().decode(base64String);
            
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            data = (HashMap<String, Object>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            data = new HashMap<>();
        }
    }

    public static void add(String name, Object value) {
        data.put(name, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(String name, T defaultValue) {
        if (!data.containsKey(name)) return defaultValue;
        return (T) data.get(name);
    }

    public static void save() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.close();
            
            String base64String = Base64.getEncoder().encodeToString(baos.toByteArray());
            
            Files.write(Paths.get(filePath), base64String.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}