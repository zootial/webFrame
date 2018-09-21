package com.jonly.frame.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SingleTransactionService {

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public <T> T performTransaction(Executor<T> executor){
		return executor.execute();
	}
	
	public static interface Executor<T> {
		T execute();
	}
}
