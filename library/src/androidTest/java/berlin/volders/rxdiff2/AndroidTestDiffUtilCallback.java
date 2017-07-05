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

import android.support.v7.util.DiffUtil;

import io.reactivex.functions.Function;

class AndroidTestDiffUtilCallback<T> extends DiffUtil.Callback {

    final T oldData;
    final T newData;
    final Function<T, Integer> sizeOf;

    AndroidTestDiffUtilCallback(T oldData, T newData, Function<T, Integer> sizeOf) {
        this.oldData = oldData;
        this.newData = newData;
        this.sizeOf = sizeOf;
    }

    @Override
    public int getOldListSize() {
        try {
            return sizeOf.apply(oldData);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public int getNewListSize() {
        try {
            return sizeOf.apply(newData);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
