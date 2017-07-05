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

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

class OnApplyDiff<A extends Adapter, T> implements Consumer<OnCalculateDiffResult<A, T>> {

    @VisibleForTesting
    final BiConsumer<? super A, ? super T> onUpdate;

    OnApplyDiff(BiConsumer<? super A, ? super T> onUpdate) {
        this.onUpdate = onUpdate;
    }

    @Override
    public void accept(@NonNull OnCalculateDiffResult<A, T> result) throws Exception {
        result.applyDiff(onUpdate);
    }
}
