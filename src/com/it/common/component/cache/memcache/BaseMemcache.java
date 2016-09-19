package com.it.common.component.cache.memcache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.it.common.component.log.LogPrint;
import com.it.common.share.GlobalName;

public class BaseMemcache {
	
	public static String memcacheServerAddress = "127.0.0.1:11211 ";
	
	protected static MemcachedClient memcachedClient;
	
	static{
		if(memcachedClient==null){
			String address = GlobalName.dbResources.getString("mem.cache_server");
			if(address!=null && address.trim().length()>0){
				memcacheServerAddress = address;
			}
			try {
				build();
				LogPrint.info("memcachedClient can be used ...");
			} catch (TimeoutException | MemcachedException e) {
				LogPrint.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public static MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}
	
	/**
	 * <T> T memcachedClient.get(String key,long timeout)
	 * boolean set(String key, int exp, Object value, long timeout)
	 * exp为过期时间，memcache的源码为：return (rel_time_t)(exptime + current_time); 即放入该对象后，不管有没有使用，超过exp后过期
	 * add--不存在时保存，replace--存在时保存，set--任何时候保存
	 * timeout是Xmemcache的等待应答的时间，exp是数据保存在memcached的过期时间
	 * */
	protected static void build() throws TimeoutException, MemcachedException{
		// 传入的memcached节点列表要求: host1:port1 host2:port2 …
		// 传入多个address，只要有一个能够操作成功，就认为可以操作成功，但是会等待第一个address操作timeout后，才会操作第二个，相应时间慢
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(memcacheServerAddress));
		//如果并发访问过高，一个memcachedClient无法满足，可以设置memcachedClient的连接池，推荐在0-30之间
		//builder.setConnectionPoolSize(5);
		builder.getConfiguration().setSessionIdleTimeout(10000);  // 如果连接超过10秒没有任何IO操作发生，即认为空闲并发起心跳检测，默认5秒;
		//关闭心跳检测
		//memcachedClient.setEnableHeartBeat(false);
		//客户端会去统计连接是否空闲，禁止统计可以通过：
		//builder.getConfiguration().setStatisticsServer(false);
		//builder.getConfiguration().setSoTimeout(soTimeout);
		//提高吞吐量，降低响应时间
		//builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32 * 1024); // 设置接收缓存区为32K，默认16K
        //builder.setSocketOption(StandardSocketOption.SO_SNDBUF, 16 * 1024); // 设置发送缓冲区为16K，默认为8K
        //builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false); // 启用nagle算法，提高吞吐量，默认关闭

		//builder.setBufferAllocator(arg0);
		//builder.setSessionLocator(arg0);
		//builder.setTranscoder(arg0);
		//builder.getConfiguration().setReadThreadCount(readThreadCount);
		//builder.getConfiguration().setWriteThreadCount(writeThreadCount);
		//builder.getConfiguration().setHandleReadWriteConcurrently(handleReadWriteConcurrently);
		//builder.getConfiguration().setSessionReadBufferSize(tcpHandlerReadBufferSize);
		
		try {
			//由于xmemcached的网络层实现是基于nio长连接的，因此你并不需要重复创建多个MemcachedClient对象，memcachedClient会在jvm关闭时，自动shutdown
			memcachedClient = builder.build();
			//请求超时：建立连接的超时时间
			memcachedClient.setConnectTimeout(GlobalName.CONNECTION_TIMEOUT);
			//响应超时：xmemcached的通讯层是基于非阻塞IO的，那么在请求发送给memcached之后，需要等待应答的到来，这个等待时间默认是1秒，如果超过1秒就抛出java.util.TimeoutExpcetion给用户
			memcachedClient.setOpTimeout(GlobalName.SO_TIMEOUT);
			
			//批量操作的buffer合并，关闭buffer合并或降低合并因子，提高响应时间，降低吞吐量
			//memcachedClient.setMergeFactor(50);   //合并因子，默认是150，缩小到50
		    //memcachedClient.setOptimizeMergeBuffer(false);  //关闭合并buffer的优化
			//memcachedClient.flushAll();  //清空之前的所有数据
		} catch (IOException e1) {
			System.err.println("build MemcachedClient fail");
			e1.printStackTrace();
		}
	}
	
	protected static void shutdown(){
		try {
			if(memcachedClient!=null) memcachedClient.shutdown();
			LogPrint.info("memcachedClient shutdown ...");
		} catch (IOException e) {
			System.err.println("Shutdown MemcachedClient fail");
			e.printStackTrace();    
		}
	}

}
