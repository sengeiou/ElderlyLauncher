package cn.colorfuline.base.rxbus;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * RxBus
 * Created by Administrator on 2016/4/25.
 */
public class RxBus {

    private static volatile RxBus defaultInstance;
    // 主题
    private final Subject bus;
    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }
    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }
    // 提供了一个新的事件
    public void post (Object o) {
        bus.onNext(o);
    }

    public final <T> Observable<T> ofType(final Class<T> klass) {
        return bus.filter(new Func1<T, Boolean>() {
            @Override
            public final Boolean call(T t) {
                return klass.isInstance(t);
            }
        }).cast(klass);
    }
}