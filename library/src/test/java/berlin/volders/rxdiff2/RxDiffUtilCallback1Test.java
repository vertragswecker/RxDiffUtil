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

import berlin.volders.rxdiff2.RxDiffUtil.Callback2;
import io.reactivex.functions.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RxDiffUtilCallback1Test {

    @Mock
    Callback2 callback2;
    @Mock
    Function function;

    @Test
    public void diffUtilCallback() throws Exception {
        RxDiffUtilCallback1 callback = new RxDiffUtilCallback1(function, callback2);

        callback.diffUtilCallback(null, null);

        verify(callback2).diffUtilCallback(any(), any());
    }
}
