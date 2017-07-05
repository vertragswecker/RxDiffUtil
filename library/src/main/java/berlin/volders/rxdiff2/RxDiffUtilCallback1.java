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
import android.support.annotation.VisibleForTesting;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView.Adapter;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import berlin.volders.rxdiff2.RxDiffUtil.Callback2;
import io.reactivex.functions.Function;

/**
 * {@link Callback} wrapping a {@link Callback2} with a {@code Func0} providing the old value.
 *
 * @param <T> type of the data set
 */
class RxDiffUtilCallback1<A extends Adapter, T> implements Callback<A, T> {

    @VisibleForTesting
    final Function<? super A, ? extends T> o;
    @VisibleForTesting
    final Callback2<T> cb;

    RxDiffUtilCallback1(Function<? super A, ? extends T> o, Callback2<T> cb) {
        this.o = o;
        this.cb = cb;
    }

    @Override
    @NonNull
    public DiffUtil.Callback diffUtilCallback(@NonNull A adapter, @Nullable T o) {
        try {
            return cb.diffUtilCallback(this.o.apply(adapter), o);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
