package com.github.liert;

import com.github.liert.Config.Settings;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;


public class Ws extends WebSocketClient {
    private final CountDownLatch latch;
    private static final long HEARTBEAT_INTERVAL = 20000;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> futureTask;
    private final Runnable command = Main::reconnect;

    public Ws(URI serverUri, CountDownLatch latch) {
        super(serverUri);
        this.latch = latch;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Bukkit.getConsoleSender().sendMessage("连接成功");
        latch.countDown();
    }

    @Override
    public void onMessage(String message) {
        if (message.equals("PONG")) {
            this.handlePongMessage();
        } else {
            boolean bool = Main.getInstance().getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), message);
            if (bool) {
                Main.getWs().send("执行成功");
            } else {
                Main.getWs().send("执行失败");
            }
        }
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Closed with exit code " + code + ", additional info: " + reason);
    }
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
    public static void startHeartbeat() {
        Main.getWs().send("PING");
        Timer heartBeatTimer = new Timer();
        heartBeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Main.getWs().send("PING");
                } catch (WebsocketNotConnectedException e) {
                    Bukkit.getConsoleSender().sendMessage("连接断开正在尝试重连...");
                }
            }
        }, Ws.HEARTBEAT_INTERVAL - 10000, Ws.HEARTBEAT_INTERVAL - 10000);
    }
    public void startOrResetTask(long delay, TimeUnit timeUnit) {
        if (futureTask != null && !futureTask.isDone()) {
            futureTask.cancel(false);
        }
        futureTask = scheduler.schedule(command, delay, timeUnit);
    }
    private void handlePongMessage() {
        this.startOrResetTask(Ws.HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
    }
    public static Ws connectKoishi() {
        final CountDownLatch latch = new CountDownLatch(1);
        try {
            Ws client = new Ws(new URI(Settings.I.KoishiWsUrl), latch);
            client.connect();
            latch.await();
            return client;
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
