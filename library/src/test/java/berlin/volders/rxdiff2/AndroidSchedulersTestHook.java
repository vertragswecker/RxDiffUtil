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

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

class AndroidSchedulersTestHook extends RxAndroidSchedulersHook {

    static {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new AndroidSchedulersTestHook());
    }

    @Override
    public Scheduler getMainThreadScheduler() {
        return Schedulers.immediate();
    }

    static void innit() {
    }

    private AndroidSchedulersTestHook() {
    }
}
