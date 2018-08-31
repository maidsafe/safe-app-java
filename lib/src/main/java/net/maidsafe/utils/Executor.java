//package net.maidsafe.utils;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class Executor implements Cloneable {
//    private static Executor executor;
//    private ExecutorService executorService;
//
//    private Executor() {
//        executorService = Executors.newFixedThreadPool(20);
//    }
//
//    public static synchronized Executor getInstance() {
//        if (executor == null) {
//            executor = new Executor();
//        }
//        return executor;
//    }
//
//    public <T> Future<T> submit(Callable callable) {
//        return executorService.submit(callable);
//    }
//
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        throw new CloneNotSupportedException();
//    }
//}
