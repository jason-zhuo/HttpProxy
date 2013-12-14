package Thread;

public class Finish {
	private int     activeThreads = 0;
    private boolean started = false;

	/**
	 * 描述： wait until all the work is done
	 * 
	 */
    public synchronized void waitDone() {

        try {

            while (activeThreads > 0) {

                wait ();
            }
        }
        catch (InterruptedException e) {

        }
    }

    /**
     * 描述：wait until the work is begin
     */
    public synchronized void waitBegin() {

        try {

            while (!started) {

                wait ();
            }
        }
        catch (InterruptedException e) {

        }
    }

    /**
     * 描述：start new thread, increase the number of active thread by 1
     */
    public synchronized void workerBegin() {

        activeThreads++;
        started = true;
        notify ();
    }

    /**
     * 描述：end a thread, decrease the number of active thread by 1
     */
    public synchronized void workerEnd() {

        activeThreads--;
        notify ();
    }

    /**
     * 描述： reset the number of active thread to 0
     */
    public synchronized void reset() {

        activeThreads = 0;
    }
}
