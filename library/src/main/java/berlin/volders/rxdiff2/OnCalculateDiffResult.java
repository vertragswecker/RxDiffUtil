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
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.DiffResult;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

class OnCalculateDiffResult<A extends Adapter, T> extends AdapterDataObserver {

    @VisibleForTesting
    boolean invalidated = false;

    @VisibleForTesting
    final Consumer<Long> p;
    @VisibleForTesting
    final WeakReference<A> adapter;
    @VisibleForTesting
    final T o;
    @VisibleForTesting
    final DiffResult diff;

    OnCalculateDiffResult(WeakReference<A> adapter, T o, Callback<A, T> cb, boolean dm, Consumer<Long> p) {
        this.adapter = adapter;
        A a = nonLeaking(adapter);
        a.registerAdapterDataObserver(this);
        DiffUtil.Callback callback = cb.diffUtilCallback(a, o);
        this.o = o;
        this.diff = DiffUtil.calculateDiff(callback, dm);
        this.p = p;
    }

    void applyDiff(BiConsumer<? super A, ? super T> onUpdate) throws Exception {
        A adapter = nonLeaking(this.adapter);
        checkConcurrency(adapter);
        onUpdate.accept(adapter, o);
        diff.dispatchUpdatesTo(adapter);
        p.accept(1L);
    }

    @VisibleForTesting
    static <A extends Adapter> A nonLeaking(WeakReference<A> adapter) {
        A a = adapter.get();
        if (a != null) {
            return a;
        }
        throw new RxDiffUtil.SubscriptionLeak();
    }

    @VisibleForTesting
    void checkConcurrency(Adapter adapter) {
        if (invalidated) {
            throw new ConcurrentModificationException("adapter data changed - diff outdated");
        }
        invalidated = true;
        adapter.unregisterAdapterDataObserver(this);
    }

    @Override
    public void onChanged() {
        invalidated = true;
        nonLeaking(adapter).unregisterAdapterDataObserver(this);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        onChanged();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        onChanged();
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        onChanged();
    }
}
