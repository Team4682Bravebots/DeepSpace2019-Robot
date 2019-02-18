package frc.robot;

public class Timer {
  private long _startTime;
  private boolean _isRunning;

  public Timer() {
    reset();
  }

  public void start() {
    _startTime = getTime_ms();
    _isRunning = true;
  }

  public void stop() {
    _isRunning = false;
  }

  public void reset() {
    stop();
    _startTime = 0L;
  }

  public long get() {
    return getTime_ms() - _startTime;
  }

  public boolean isRunning() {
    return _isRunning;
  }

  public static void delay(final double seconds) {
    try {
      Thread.sleep((long) (seconds * 1e3));
    } catch (final InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  private long getTime_ms() {
    return System.currentTimeMillis();
  }
}