package net.maidsafe.utils;

import java.util.concurrent.Callable;

public class CallbackHelper<T> implements Callable<T> {

    private IFuncExecutor<T> funcExecutor;

    public CallbackHelper(IFuncExecutor<T> funcExecutor) {
        this.funcExecutor = funcExecutor;
    }

    @Override
    public T call() throws Exception {
        ResultBinder binder = new ResultBinder();
        Thread waitThread = new Thread(new Handler(binder, false));
        Thread execThread = new Thread(new Handler(binder, true));
        waitThread.start();
        execThread.start();
        waitThread.join();
        if (binder.exception != null) {
            throw binder.exception;
        }
        return binder.result;
    }

    public interface IFuncExecutor<T> {
        void exec(Binder<T> binder);
    }

    public interface Binder<T> {
        void onResult(T result);

        void onException(Exception e);
    }

    private class ResultBinder implements Binder<T> {
        private T result;
        private Exception exception;

        public synchronized void onResult(T result) {
            this.result = result;
            notify();
        }

        public synchronized void onException(Exception exception) {
            this.exception = exception;
            notify();
        }

        public synchronized void waitForResult() throws Exception {
            wait();
        }

        public synchronized void execute() {
            funcExecutor.exec(this);
        }
    }

    private class Handler implements Runnable {
        private ResultBinder resultBinder;
        private boolean isExec;

        public Handler(ResultBinder resultBinder, boolean isExec) {
            this.resultBinder = resultBinder;
            this.isExec = isExec;
        }

        public void run() {
            try {
                if (isExec) {
                    resultBinder.execute();
                } else {
                    resultBinder.waitForResult();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
