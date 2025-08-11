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
            UserData foundUser = cache.get(userId);
            if (foundUser != null) {
                return foundUser;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserData newUser = new UserData(userId, "Details for " + userId);
            cache.put(userId, newUser);
            return newUser;
        }, executor);
    }
}
