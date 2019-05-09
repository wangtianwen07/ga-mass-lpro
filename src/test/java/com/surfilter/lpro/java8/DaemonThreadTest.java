package com.surfilter.lpro.java8;

import org.junit.Assert;
import org.junit.Test;


public class DaemonThreadTest {

	@Test
	public void testDaemonThread() {
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("daemon thread t1 is alive...");
				}
			}
		});
		t1.setDaemon(true);
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("user thread t2 over");
			}
		});
		
		t1.start();
		t2.start();
		
		try {
			t2.join();		//will block
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(t1.isDaemon());
		Assert.assertFalse(t2.isDaemon());
	}
}
