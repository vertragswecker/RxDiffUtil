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

import berlin.volders.rxdiff2.RxDiffUtil.Callback;
import berlin.volders.rxdiff2.RxDiffUtil.Callback2;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RxDiffUtilTest {

    @Mock
    Adapter adapter;
    @Mock
    Callback callback;
    @Mock
    Callback2 callback2;
    @Mock
    Function function;
    @Mock
    DiffUtil.Callback cb;

    PublishProcessor emitter;
    RxDiffUtil rxDiffUtil;

    @BeforeClass
    public static void init() {
        AndroidSchedulersTestHook.innit();
    }

    @Before
    public void setup() {
        doReturn(cb).when(callback).diffUtilCallback(any(Adapter.class), any());
        doReturn(cb).when(callback2).diffUtilCallback(any(), any());
        emitter = PublishProcessor.create();
        rxDiffUtil = new RxDiffUtil(new WeakReference(adapter), emitter);
    }

    @Test
    public void calculateDiff_callback() throws Exception {
        Object o = 1;

        RxDiffResult rxDiffResult = rxDiffUtil.calculateDiff(callback);
        TestSubscriber subscriber = rxDiffResult.o.test();
        emitter.onNext(o);

        OnCalculateDiffResult result = (OnCalculateDiffResult) subscriber.getOnNextEvents().get(0);
        assertThat(result.adapter.get(), is((Object) adapter));
        assertThat(result.o, is(o));
        assertThat(result.o, is(o));
        verify(callback).diffUtilCallback(any(Adapter.class), any());
        subscriber.assertNoErrors();
        subscriber.assertNotComplete();
        subscriber.assertValueCount(1);
    }

    @Test
    public void calculateDiff_callback_detectMoves() throws Exception {
        Object o = 1;

        RxDiffResult rxDiffResult = rxDiffUtil.calculateDiff(callback, false);
        TestSubscriber subscriber = rxDiffResult.o.test();
        emitter.onNext(o);

        OnCalculateDiffResult result = (OnCalculateDiffResult) subscriber.getOnNextEvents().get(0);
        assertThat(result.adapter.get(), is((Object) adapter));
        assertThat(result.o, is(o));
        assertThat(result.o, is(o));
        verify(callback).diffUtilCallback(any(Adapter.class), any());
        subscriber.assertNoErrors();
        subscriber.assertNotComplete();
        subscriber.assertValueCount(1);
    }

    @Test
    public void calculateDiff_callback2() throws Exception {
        Object o = 1;

        RxDiffResult rxDiffResult = rxDiffUtil.calculateDiff(function, callback2);
        TestSubscriber subscriber = rxDiffResult.o.test();
        emitter.onNext(o);

        OnCalculateDiffResult result = (OnCalculateDiffResult) subscriber.getOnNextEvents().get(0);
        assertThat(result.adapter.get(), is((Object) adapter));
        assertThat(result.o, is(o));
        assertThat(result.o, is(o));
        verify(callback2).diffUtilCallback(any(), any());
        verify(function).apply(any());
        subscriber.assertNoErrors();
        subscriber.assertNotComplete();
        subscriber.assertValueCount(1);
    }

    @Test
    public void calculateDiff_callback2_detectMoves() throws Exception {
        Object o = 1;

        RxDiffResult rxDiffResult = rxDiffUtil.calculateDiff(function, callback2, false);
        TestSubscriber subscriber = rxDiffResult.o.test();
        emitter.onNext(o);

        OnCalculateDiffResult result = (OnCalculateDiffResult) subscriber.getOnNextEvents().get(0);
        assertThat(result.adapter.get(), is((Object) adapter));
        assertThat(result.o, is(o));
        assertThat(result.o, is(o));
        verify(callback2).diffUtilCallback(any(), any());
        verify(function).apply(any());
        subscriber.assertNoErrors();
        subscriber.assertNotComplete();
        subscriber.assertValueCount(1);
    }

    @Test
    public void with() throws Exception {
        assertThat(RxDiffUtil.with(adapter), instanceOf(ToRxDiffUtil.class));
    }
}
