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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.ref.WeakReference;

import berlin.volders.rxdiff2.RxDiffUtil.Callback;

import static android.support.v7.widget.RecyclerView.Adapter;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class OnCalculateDiffTest {

    @Mock
    Adapter adapter;
    @Mock
    Callback callback;

    @Test
    public void call() throws Exception {
        OnCalculateDiff calculateDiff
                = new OnCalculateDiff(new WeakReference(adapter), callback, false);

        OnCalculateDiffSubscriber subscriber = calculateDiff.call(null);

        assertThat(subscriber.adapter, is(calculateDiff.adapter));
        assertThat(subscriber.cb, is(calculateDiff.cb));
        assertThat(subscriber.dm, is(calculateDiff.dm));
    }
}
