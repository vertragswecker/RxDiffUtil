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

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

class AndroidTestFunction<T> implements Function<Flowable<T>, Completable> {

    final AndroidTestAdapter<T> adapter;
    private final Function<AndroidTestAdapter<T>, T> object;

    AndroidTestFunction(AndroidTestAdapter<T> adapter, Function<AndroidTestAdapter<T>, T> object) {
        this.adapter = adapter;
        this.object = object;
    }

    @Override
    public Completable apply(Flowable<T> o) {
        return o.to(RxDiffUtil.<AndroidTestAdapter<T>, T>with(adapter))
                .calculateDiff(object, adapter)
                .applyDiff(adapter);
    }
}
