package com.surfilter.lpro.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

public class ExecutorServiceTest {

	private final static long MAX_CONSUMER = 1000;
	
	@Test
	public void testCyclicBarrier() {
		
	}
	
	@Test
	public void testFixedThreadPool() {
		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);
		long consumeTotal = 0;
		//����
		try {
			produce(queue);
			consumeTotal = consume(queue);
		} catch (InterruptedException | ExecutionException | RejectedExecutionException e) {
			System.out.println("�̳߳��Ѿ��رգ�");
		}
		Assert.assertFalse(queue.size()==0);
		Assert.assertTrue(consumeTotal==MAX_CONSUMER);
	}
	
	//����
	private void produce(final BlockingQueue<String> queue) throws InterruptedException, ExecutionException {
		final Random random = new Random();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				for(;;) {
					try {
						String value = String.valueOf(random.nextInt(Integer.MAX_VALUE));
						queue.put(value);
						System.out.format("����ֵ��%s��,��ǰ���д�С��%d��\n",value,queue.size());
						Thread.sleep(1L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	//����
	private long consume(final BlockingQueue<String> queue) throws InterruptedException, ExecutionException {
		AtomicLong atomicLong = new AtomicLong();
		ExecutorService fixedExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		long consumeTotal;
		for(;;) {
			Future<Long> future = fixedExecutor.submit(() -> {
				List<String> strs = new ArrayList<String>();
				int num = queue.drainTo(strs,1);
				atomicLong.addAndGet(Long.valueOf(num));
				System.out.format("��ǰ����Ԫ�ظ���%d,����������%d��\n", strs.size(),atomicLong.get());
				return atomicLong.get();
			});
			Thread.sleep(10L);
			consumeTotal = future.get();
			if(consumeTotal==MAX_CONSUMER) {
				fixedExecutor.shutdown();
				return consumeTotal;
			}
		}
	}
}
