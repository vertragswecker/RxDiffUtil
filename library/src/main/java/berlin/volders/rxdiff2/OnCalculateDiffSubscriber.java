/*
 * Copyright (C) 2017 volders GmbH with <3 in Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package berlin.volders.rxdiff2;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView.Adapter;

import org.reactivestreams.Subscriber;

import java.lang.ref.WeakReference;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.DisposableSubscriber;

class OnCalculateDiffSubscriber<A extends Adapter, T> extends DisposableSubscriber<T> {

    private final Consumer<Long> p = new Consumer<Long>() {
        @Override
        public void accept(Long n) {
            OnCalculateDiffSubscriber.this.request(n);
        }
    };

    private final Subscriber<? super OnCalculateDiffResult<A, T>> observer;
    @VisibleForTesting
    final WeakReference<A> adapter;
    @VisibleForTesting
    final Callback<A, T> cb;
    @VisibleForTesting
    final boolean dm;

    OnCalculateDiffSubscriber(Subscriber<? super OnCalculateDiffResult<A, T>> subscriber,
                              WeakReference<A> adapter, Callback<A, T> cb, boolean dm) {
        super();
        this.observer = subscriber;
        this.adapter = adapter;
        this.cb = cb;
        this.dm = dm;
    }

    @Override
    public void onStart() {
        request(1);
    }

    @Override
    public void onNext(T o) {
        if (!isDisposed()) {
            try {
                OnCalculateDiffResult<A, T> result = new OnCalculateDiffResult<>(adapter, o, cb, dm, p);
                if (!isDisposed()) {
                    observer.onNext(result);
                }
            } catch (Throwable throwable) {
                onError(throwable);
            }
        }
    }

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            observer.onComplete();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (!isDisposed()) {
            observer.onError(e);
        }
    }
}
