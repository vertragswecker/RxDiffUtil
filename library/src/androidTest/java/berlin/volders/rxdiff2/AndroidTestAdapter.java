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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.Observer;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.ReplaySubject;

import static android.support.test.InstrumentationRegistry.getContext;

class AndroidTestAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        Function<AndroidTestAdapter<T>, T>, BiConsumer<AndroidTestAdapter<T>, T>,
        RxDiffUtil.Callback<AndroidTestAdapter<T>, T>, RxDiffUtil.Callback2<T> {

    final ReplaySubject<T> ts = ReplaySubject.create();
    final Function<T, Integer> sizeOf;

    AndroidTestAdapter(Function<T, Integer> sizeOf, T emptyState) {
        this.ts.onNext(emptyState);
        this.sizeOf = sizeOf;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(new View(getContext())) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        try {
            return sizeOf.apply(apply(this));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public void accept(AndroidTestAdapter<T> adapter, T t) {
        adapter.ts.onNext(t);
    }

    @Override
    public T apply(AndroidTestAdapter<T> adapter) {
        int last = adapter.ts.getValues().length - 1;
        return adapter.ts.skip(last).blockingFirst();
    }

    @NonNull
    @Override
    public DiffUtil.Callback diffUtilCallback(@NonNull AndroidTestAdapter<T> adapter, @Nullable T newData) {
        return diffUtilCallback(apply(adapter), newData);
    }

    @NonNull
    @Override
    public DiffUtil.Callback diffUtilCallback(@Nullable T oldData, @Nullable T newData) {
        return new AndroidTestDiffUtilCallback<>(oldData, newData, sizeOf);
    }

    void subscribe(Observer<T> subscriber) {
        ts.skip(1).subscribe(subscriber);
    }

    Function<AndroidTestAdapter<T>, T> notifyOnGet() {
        return new Function<AndroidTestAdapter<T>, T>() {
            @Override
            public T apply(AndroidTestAdapter<T> adapter) {
                adapter.notifyDataSetChanged();
                return adapter.apply(adapter);
            }
        };
    }
}
