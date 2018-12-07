/**
 *
// * @Title MainTask.java
 * @author wanglu
 * @date 2018年6月25日
 */
package com.edupay.commons.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.edupay.cache.redis.service.RedisDistributedLockService;
import com.edupay.pay.service.CheckBillService;
import com.edupay.pay.service.PushPayResultService;

/**  
 *
 * Title MainTask 
 * @author wanglu  
 * @date 2018年6月25日  
 */

public class MainTask {
	
	@Autowired
	private CheckBillService checkBillSerivce;
	@Autowired
	private PushPayResultService pushPayResultSerivce;
	@Autowired
    private RedisDistributedLockService redisDistributedLockService;
	
	/**
	 * 银商 T+1 对账(POS支付)
	 */
	public void checkBill(){
		String taskName="checkBill";
		//对账计划锁
		String redisKey = taskName + "_task_lock";
		//校验锁
		if(!redisDistributedLockService.setLock(redisKey,"true",60*2*60L)){
			return;
		}
		try {
			//执行任务
			checkBillSerivce.checkBill();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//Redis解锁
			redisDistributedLockService.releaseLock(redisKey, "true");
		}
	}
	
	/**
	 * 补推支付通知给业务系统
	 */
	public void pushPayResult(){
		String taskName="pushPayResults";
		//推送支付通知计划锁
		String redisKey = taskName + "_task_lock";
		if(!redisDistributedLockService.setLock(redisKey,"true",60*4L)){
			return;
		}
		try {
			//执行任务
			pushPayResultSerivce.pushPayResultTask();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//Redis解锁
			redisDistributedLockService.releaseLock(redisKey, "true");
		}
	}
	
}
