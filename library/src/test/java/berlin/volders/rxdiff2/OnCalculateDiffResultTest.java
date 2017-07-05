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
import android.support.v7.widget.RecyclerView.Adapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OnCalculateDiffResultTest {

    @Mock
    Adapter adapter;
    @Mock
    Callback callback;
    @Mock
    Consumer producer;
    @Mock
    DiffUtil.Callback cb;
    @Mock
    BiConsumer empty;

    OnCalculateDiffResult<?, ?> result;

    @Before
    public void setup() {
        doReturn(cb).when(callback).diffUtilCallback(any(Adapter.class), any());
        result = new OnCalculateDiffResult<>(new WeakReference<>(adapter), null, callback, false, producer);
    }

    @Test
    public void applyDiff() throws Exception {
        result.applyDiff(empty);

        verify(producer).accept(anyLong());
    }

    @Test(expected = ConcurrentModificationException.class)
    public void applyDiff_concurrently() throws Exception {
        result.onChanged();

        result.applyDiff(empty);
    }

    @Test
    public void onChanged() throws Exception {
        result.onChanged();

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void onItemRangeChanged() throws Exception {
        result.onItemRangeChanged(0, 1);

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void onItemRangeChanged_payload() throws Exception {
        result.onItemRangeChanged(0, 1, null);

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void onItemRangeInserted() throws Exception {
        result.onItemRangeInserted(0, 1);

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void onItemRangeRemoved() throws Exception {
        result.onItemRangeRemoved(0, 1);

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void onItemRangeMoved() throws Exception {
        result.onItemRangeMoved(0, 1, 2);

        assertThat(result.invalidated, is(true));
    }

    @Test
    public void nonLeaking() throws Exception {
        assertThat(OnCalculateDiffResult.nonLeaking(new WeakReference<>(adapter)), is(adapter));
    }

    @Test(expected = RxDiffUtil.SubscriptionLeak.class)
    public void nonLeaking_leaking() throws Exception {
        OnCalculateDiffResult.nonLeaking(new WeakReference(null));
    }

    @Test
    public void checkConcurrency() throws Exception {
        result.checkConcurrency(adapter);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void checkConcurrency_changed() throws Exception {
        result.onChanged();

        result.checkConcurrency(adapter);
    }
}
