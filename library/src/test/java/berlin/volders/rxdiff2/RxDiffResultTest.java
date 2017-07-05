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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RxDiffResultTest {

    @Mock
    BiConsumer action;
    @Mock
    Adapter adapter;
    @Mock
    Callback callback;
    @Mock
    Consumer producer;
    @Mock
    DiffUtil.Callback cb;

    RxDiffResult rxDiffResult;
    PublishProcessor emitter;

    @BeforeClass
    public static void init() {
        AndroidSchedulersTestHook.innit();
    }

    @Before
    public void setup() {
        doReturn(cb).when(callback).diffUtilCallback(any(Adapter.class), any());
        emitter = PublishProcessor.create();
        rxDiffResult = new RxDiffResult(emitter);
    }

    @Test
    public void applyDiff() throws Exception {
        rxDiffResult.applyDiff(action).test();

        emitResult(1);
        emitResult(2);

        verify(action).accept(adapter, 1);
        verify(action).accept(adapter, 2);
        subscriber.assertNoErrors();
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    @Test
    public void applyDiff_concurrently() throws Exception {
        rxDiffResult.applyDiff(action).test();
        rxDiffResult.applyDiff(action).test();
        emitResult(1);

        verify(action).accept(adapter, 1);
        subscriber.assertError(ConcurrentModificationException.class);
        subscriber.assertNotCompleted();
        subscriber.assertNoValues();
    }

    void emitResult(int i) {
        emitter.onNext(new OnCalculateDiffResult(new WeakReference(adapter), i, callback, true, producer));
    }
}
