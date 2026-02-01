package io.github.nx.LunaX.engine.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList; // WICHTIG!

public class Network {

    private static Socket clientSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    public static boolean isConnected = false;
    public static int ping = 0;
    private static boolean isServerRunning = false;
    private static List<ClientHandler> serverClients = new CopyOnWriteArrayList<>();
    private static ServerSocket serverSocket; 

    private static HashMap<Integer, NetObject> objects = new HashMap<>();
    private static GamePacketListener gameListener;

    public static void register(NetObject obj) {
        objects.put(obj.getNetID(), obj);
    }
    
    public static void setListener(GamePacketListener listener) {
        gameListener = listener;
    }

    public static void host(int port) {
        System.out.println("[Net] Starte Host...");
        startServer(port);
        try { Thread.sleep(200); } catch (Exception e) {}
        join("localhost", port);
    }

    public static void join(String ip, int port) {
        isConnected = false; 
        System.out.println("[Net] Versuche zu verbinden: " + ip + ":" + port);
        
        new Thread(() -> {
            try {
                clientSocket = new Socket(ip, port);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush(); 
                in = new ObjectInputStream(clientSocket.getInputStream());
                
                isConnected = true;
                System.out.println("[Net] VERBUNDEN! Streams stehen.");

                while (isConnected) {
                    try {
                        Object obj = in.readObject();
                        if (obj != null && obj instanceof Packet) {
                            handlePacketClientSide((Packet) obj);
                        }
                    } catch (Exception e) {
                        System.err.println("[Client] Kritischer Lesefehler:");
                        e.printStackTrace();
                        isConnected = false;
                    }
                }
            } catch (Exception e) {
                System.err.println("[Client] Verbindung gescheitert: " + e.getMessage());
                isConnected = false;
            } finally {
                try { if(clientSocket != null) clientSocket.close(); } catch(Exception e) {}
            }
        }).start();
    }

    public static void disconnect() {
        if (isServerRunning) {
            broadcast(new Packet(-1, new Object[]{"SERVER_CLOSED"}));
            isServerRunning = false;
            try { if(serverSocket != null) serverSocket.close(); } catch(Exception e) {}
            serverClients.clear();
        }
        isConnected = false;
        try { if (clientSocket != null) clientSocket.close(); } catch (Exception e) {}
    }

    public static void sync(int netID, Object... data) {
        if (!isConnected || out == null) return;
        synchronized (out) {
            try {
                out.writeObject(new Packet(netID, data));
                out.flush();
                out.reset();
            } catch (Exception e) { 
                isConnected = false; 
            }
        }
    }
    
    public static void updatePing() {
        if (!isConnected) return;
        sync(-1, "PING", System.currentTimeMillis());
    }

    private static void handlePacketClientSide(Packet p) {
        if (p == null) return;

        if (p.netID == -1) {
            String type = (String) p.data[0];
            
            if (type.equals("PONG")) {
                ping = (int) (System.currentTimeMillis() - (long) p.data[1]);
            }
            else if (type.equals("SERVER_CLOSED")) {
                isConnected = false;
            }
            else {
                if (gameListener != null) {
                    gameListener.onSystemPacket(type, p.data);
                }
            }
        } 
        else if (objects.containsKey(p.netID)) {
            objects.get(p.netID).onNetworkUpdate(p.data);
        } 
    }
    
    private static void startServer(int port) {
        isServerRunning = true;
        serverClients.clear();
        
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("[Server] Läuft auf Port " + port);

                while (isServerRunning) {
                    try {
                        Socket cs = serverSocket.accept();
                        ClientHandler handler = new ClientHandler(cs);
                        serverClients.add(handler);
                        handler.start();
                    } catch (Exception e) { }
                }
            } catch (Exception e) {
                isServerRunning = false;
            }
        }).start();
    }

    static class ClientHandler extends Thread {
        Socket socket;
        ObjectOutputStream sOut;
        ObjectInputStream sIn;

        public ClientHandler(Socket socket) { this.socket = socket; }

        @Override
        public void run() {
            try {
                sOut = new ObjectOutputStream(socket.getOutputStream());
                sOut.flush();
                sIn = new ObjectInputStream(socket.getInputStream());
                
                while (isServerRunning) {
                    Object obj = sIn.readObject();
                    
                    if (obj != null && obj instanceof Packet) {
                        Packet p = (Packet) obj;
                        
                        if (p.netID == -1 && "PING".equals(p.data[0])) {
                            send(new Packet(-1, new Object[]{"PONG", p.data[1]}));
                        } else {
                            broadcast(p);
                        }
                    }
                }
            } catch (Exception e) {
            } finally {
                serverClients.remove(this);
                try { socket.close(); } catch(Exception e) {}
            }
        }

        public void send(Packet p) {
            if (sOut == null) return;
            synchronized (sOut) {
                try {
                    sOut.writeObject(p);
                    sOut.flush();
                    sOut.reset();
                } catch (Exception e) {}
            }
        }
    }

    private static void broadcast(Packet p) {
        for (ClientHandler c : serverClients) c.send(p);
    }
}