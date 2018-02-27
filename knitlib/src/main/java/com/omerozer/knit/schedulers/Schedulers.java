package com.omerozer.knit.schedulers;

import com.omerozer.knit.schedulers.heavy.HeavyTaskScheduler;

/**
 * Created by omerozer on 2/26/18.
 */

public class Schedulers implements SchedulerProvider{

    @Override
    public SchedulerInterface io(){
        return new IOScheduler();
    }

    @Override
    public SchedulerInterface main(){
        return new AndroidMainThreadScheduler();
    }

    @Override
    public SchedulerInterface immediate(){
        return new ImmediateScheduler();
    }

    @Override
    public SchedulerInterface heavy(){
        return new HeavyTaskScheduler();
    }
}