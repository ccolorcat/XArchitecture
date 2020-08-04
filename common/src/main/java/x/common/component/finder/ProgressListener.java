/*
 * Copyright 2018 cxx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package x.common.component.finder;

/**
 * Author: cxx
 * Date: 2020-4-15
 */
public interface ProgressListener {
    /**
     * @param finished 已完成的进度
     * @param total    总大小
     * @param percent  已完成 / 总大小，取值范围 [1, 100]
     */
    void onChanged(long finished, long total, int percent);
}
