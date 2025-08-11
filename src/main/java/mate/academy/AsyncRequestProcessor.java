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
        UserData foundUser = cache.get(userId);
        if (foundUser != null) {
            return CompletableFuture.completedFuture(foundUser);
        }
        return CompletableFuture.supplyAsync(() ->
            cache.computeIfAbsent(userId, details -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
                UserData newUser = new UserData(userId, "Details for " + userId);
                return newUser;
            }), executor);
    }
}
