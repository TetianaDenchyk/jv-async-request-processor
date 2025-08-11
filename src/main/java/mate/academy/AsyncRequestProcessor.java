package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AsyncRequestProcessor {
    private static Map<String, UserData> cache = new java.util.concurrent.ConcurrentHashMap<>();
    private final Executor executor;

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserData foundUser = cache.get(userId);
            if (foundUser != null) {
                return foundUser;
            }
            UserData newUser = new UserData(userId, "Details for " + userId);
            storeInCache(userId, newUser);
            return newUser;
        }, executor);
    }

    private void storeInCache(String userId, UserData userData) {
        executor.execute(() -> cache.put(userId, userData));
    }
}
